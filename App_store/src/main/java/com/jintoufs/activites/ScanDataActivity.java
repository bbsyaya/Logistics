package com.jintoufs.activites;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseRfidActivity;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.Constants;
import com.jintoufs.entity.User;
import com.jintoufs.entity.UserDocking;
import com.jintoufs.fragment.LabelListFragment;
import com.jintoufs.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.reader.server.ReaderHelper;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 押运人员专用-入库、出库界面
 */
@SuppressLint("NewApi")
public class ScanDataActivity extends BaseRfidActivity {

    private AppContext appContext;
    private User user;
    private static LabelListFragment labelListFragment = null;
    private TextView mHeaderTitle;

    final FragmentManager fragmentManager = getFragmentManager();
    private Button mButtonRead;
    private List<UserDocking> list;
    private List<String> dataList;
    private DataAdapter arrayAdapterA;
    private DataAdapter arrayAdapterB;
    private String boxCode;
    private Map<String, String> cashBoxMap = new HashMap<String, String>();//存放cashBoxCode值
    private Map<String, Integer> userIdMap = new HashMap<String, Integer>();//存放可选列表用户id
    private int m_readCount = 0;
    private TextView m_txtReadCount;
    private Spinner userA, userB;
    private Integer userAId, userBId;//用于传递用户id接口参数
    private String caption;
    private long lastPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_data);
        init();

        Intent intent = getIntent();
        caption = intent.getStringExtra("caption");
        mHeaderTitle.setText(caption);

    }

    private void init() {
        appContext = (AppContext) getApplication();
        user = appContext.getLoginUser();
        labelListFragment = (LabelListFragment) fragmentManager.findFragmentById(R.id.label_list_fragment);
        mHeaderTitle = (TextView) findViewById(R.id.header_title);
        mButtonRead = (Button) findViewById(R.id.btn_read);
        m_txtReadCount = (TextView) findViewById(R.id.readCount);
        userA = (Spinner) findViewById(R.id.user_one);
        userB = (Spinner) findViewById(R.id.user_two);

        arrayAdapterA = new DataAdapter();
        arrayAdapterB = new DataAdapter();

        userA.setAdapter(arrayAdapterA);
        userB.setAdapter(arrayAdapterB);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Api.queryUserDocking(user, mHandler);
    }

    public void read(View view) {
        accompaniment.start();
        if (mButtonRead.getText().equals(getString(R.string.btn_read))) {
            mButtonRead.setText(R.string.btn_stop);
            read();
        } else if (mButtonRead.getText().equals(getString(R.string.btn_stop))) {
            stopInventory();
            mButtonRead.setText(R.string.btn_read);
        }
    }

    public void clear(View view) {
        accompaniment.start();
        stopInventory();
        mButtonRead.setText(R.string.btn_read);
        cashBoxMap.clear();
        labelListFragment.clearItem();
        m_readCount = 0;
        m_txtReadCount.setText("已扫描:0");
    }

    public void save(View view) {
        accompaniment.start();

        stopInventory();
        mButtonRead.setText(R.string.btn_read);
        if (cashBoxMap.size() > 0) {
            createDialog();
        }else{
            AppContext.showToastShort("暂未读取到数据");
        }
    }

    //拼接cashBoxCodes字符串
    private String getCashBoxCodes() {
        String cashboxCodes = "";
        for (String cashboxCode : cashBoxMap.values()) {
            cashboxCodes += cashboxCode;
        }
        return cashboxCodes;
    }

    private void createDialog() {
        // 获得自定义对话框
        Dialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.btn_star)
                .setTitle("请确认已扫描个数")
                .setMessage("已扫到个数:" + String.valueOf(m_readCount))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        long recordId = 0;
                        if (caption.equals("入库")) {
                            Api.saveCashBox(user, recordId, getCashBoxCodes(), userAId, userBId, Constants.TASK_TYPE_RECEIVE, mSaveHandler);
                        } else if (caption.equals("出库")) {
                            Api.saveCashBox(user, recordId, getCashBoxCodes(), userAId, userBId, Constants.TASK_TYPE_DEVELIVER, mSaveHandler);
                        }
                        closeDialog(dialog);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        closeDialog(dialog);
                    }
                }).create();
        alertDialog.show();
    }

    private void closeDialog(DialogInterface dialog) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            // 将mShowing变量设为false，表示对话框已关闭
            field.set(dialog, true);
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler mRefreshHandler = new Handler();
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {

            byte btWorkAntenna = m_curInventoryBuffer.lAntenna.get(m_curInventoryBuffer.nIndexAntenna);
            if (btWorkAntenna < 0)
                btWorkAntenna = 1;

            mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);

            refreshList();
            mRefreshHandler.postDelayed(this, 2000);
        }
    };

    private void refreshList() {
        for (InventoryTagMap inventoryTagMap : m_curInventoryBuffer.lsTagList) {
            String str = inventoryTagMap.strEPC;
            str = str.replace("F", "");
            str = str.replace("f", "");

            if (!str.startsWith(Constants.RFID_START_WITH_STR)) {
                continue;
            }
            if (str.length() < 10) {
                continue;
            }

            String foundBoxCode = str.substring(4, 10);

            Cashbox cb = new Cashbox();
            cb.setRfid(str);
            cb.setCashBoxCode(foundBoxCode);
            labelListFragment.addItem(cb);
            boxCode = foundBoxCode + ",";
            cashBoxMap.put(cb.getCashBoxCode(), boxCode);
            ++m_readCount;


        }
        m_txtReadCount.setText("已扫描:" + String.valueOf(m_readCount));
        labelListFragment.myadapter.notifyDataSetChanged();
    }

    private void read() {
        Byte bPower = Byte.valueOf(AppContext.get(Constants.BATCH_READ_POWER, "27"));
        set(bPower);
        Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
        sendBroadcast(intent);// 传递过去
        mRefreshHandler.postDelayed(mRefreshRunnable, 500);
    }

    public void stopInventory() {
        mRefreshHandler.removeCallbacks(mRefreshRunnable);
        super.stopInventory();
    }

    private List<String> tempDataList = new ArrayList<String>();

    //为可选列表添加数据
    private void addUserData(List<UserDocking> list) {
        dataList = new ArrayList<String>();
        for (UserDocking userDocking : list) {
            dataList.add(userDocking.getRealName());
            userIdMap.put(userDocking.getRealName(), userDocking.getId());
        }
        arrayAdapterA.setDatas(dataList);
        arrayAdapterB.setDatas(dataList);

        userA.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                userAId = userIdMap.get(userA.getSelectedItem());
                tempDataList.clear();
                tempDataList.addAll(dataList);
                tempDataList.remove(position);
                arrayAdapterB.setDatas(tempDataList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        userB.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                userBId = userIdMap.get(userB.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }

    class DataAdapter extends BaseAdapter {

        private List<String> mDatas;

        public DataAdapter() {
            mDatas = new ArrayList<String>();
        }

        public void setDatas(List<String> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            convertView = LayoutInflater.from(ScanDataActivity.this).inflate(R.layout.spinner_item, null);

            TextView tv = (TextView) convertView.findViewById(R.id.spinner_item);
            tv.setText(mDatas.get(position));
            return convertView;
        }

    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<UserDocking>>() {
                    }.getType());
                    addUserData(list);
                } else {
                    AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
                    AppContext.getInstance().cleanLoginInfo();
                    hideWaitDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            hideWaitDialog();
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            hideWaitDialog();
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }
    };

    private final AsyncHttpResponseHandler mSaveHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    AppContext.showToast(ajaxMsg.getMessage());
                    hideWaitDialog();
                    finish();
                } else {
                    AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
                    AppContext.getInstance().cleanLoginInfo();
                    hideWaitDialog();
                }
            } catch (Exception e) {
                hideWaitDialog();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            hideWaitDialog();
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            hideWaitDialog();
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (event.getDownTime() - lastPressTime < 300) {
            lastPressTime = event.getDownTime();
            return true;
        }

        lastPressTime = event.getDownTime();

        if (keyCode == KeyEvent.KEYCODE_FOCUS && event.getRepeatCount() == 0) {
            read(null);
            return true;
        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                break;
            case KeyEvent.ACTION_DOWN:
                read(mButtonRead);
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
