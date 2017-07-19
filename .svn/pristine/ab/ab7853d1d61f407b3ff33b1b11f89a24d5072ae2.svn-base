package com.jintoufs.activites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
import com.jintoufs.fragment.CheckStoreDetailListFragment;
import com.jintoufs.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.reader.server.ReaderHelper;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 盘库详细界面
 *
 * @author YangHao
 */
@SuppressLint("NewApi")
public class CheckStoreDetailActivity extends BaseRfidActivity {

    private AppContext appContext;
    private User user;
    private static CheckStoreDetailListFragment cashboxListFragment = null;

    private int checkId;//
    private String createTime;//
    private Button mButtonRead;
    private Button mButtonClear;
    private Button mButtonSave;
    private TextView header_title;
    private FragmentManager fragmentManager;
    private List<Cashbox> list;
    private List<String> founds = new ArrayList<String>();
    private long lastPressTime = 0;
    private int readCount = 0;
    private TextView m_ReadCount;
    private TextView m_TotalCount;

    private Map<String, Cashbox> kvpCashbox = new HashMap<String, Cashbox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_store_detail);
        appContext = (AppContext) getApplication();
        user = appContext.getLoginUser();
        fragmentManager = getFragmentManager();
        cashboxListFragment = (CheckStoreDetailListFragment) fragmentManager.findFragmentById(R.id.cashbox_fragment_list);
        Intent intent = getIntent();
        checkId = intent.getIntExtra("checkId", 0);
        createTime = intent.getStringExtra("createTime");
        mButtonRead = (Button) findViewById(R.id.btn_read);
        mButtonClear = (Button) findViewById(R.id.btn_clear);
        mButtonSave = (Button) findViewById(R.id.btn_save);
        header_title = (TextView) findViewById(R.id.header_title);
        header_title.setText("盘库：" + createTime);

        m_ReadCount = (TextView) findViewById(R.id.readCount);
        m_TotalCount = (TextView) findViewById(R.id.totalCount);

        //		byte outPower = Byte.parseByte(AppContext.get(Constants.BATCH_READ_POWER, "28"));
        //		set(outPower);
    }

    @Override
    protected void onResume() {
        super.onResume();

        readCount = 0;
        founds.clear();
        showWaitDialog();

        Api.getCheckStoreDetailList(user, checkId, mHandler);
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
        onResume();
    }

    public void save(View view) {
        accompaniment.start();

        stopInventory();
        mButtonRead.setText(R.string.btn_read);

        showWaitDialog();
        Api.saveCheckStore(user, checkId, founds, mSaveHandler);
    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Cashbox>>() {
                    }.getType());
                    for (Cashbox cashbox : list) {
                        if (cashbox != null) {
                            kvpCashbox.put(cashbox.getCashBoxCode(), cashbox);
                        }
                    }
                    cashboxListFragment.clearItem();
                    cashboxListFragment.addAllItems(list);
                    cashboxListFragment.myadapter.notifyDataSetChanged();
                    hideWaitDialog();
                    m_ReadCount.setText("已读取:0");
                    m_TotalCount.setText("总数:" + String.valueOf(list.size()));
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

    private final AsyncHttpResponseHandler mSaveHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    AppContext.showToast("保存成功");
                    hideWaitDialog();
                    // 重新获取数据
                    CheckStoreDetailActivity.this.finish();
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

    private Handler mRefreshHandler = new Handler();
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {
            byte btWorkAntenna = m_curInventoryBuffer.lAntenna
                    .get(m_curInventoryBuffer.nIndexAntenna);
            if (btWorkAntenna < 0)
                btWorkAntenna = 1;

            mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);

            refreshList();
            mRefreshHandler.postDelayed(this, 1500);
        }
    };

    private void refreshList() {
        try {

            for (InventoryTagMap inventoryTagMap : m_curInventoryBuffer.lsTagList) {
                String str = inventoryTagMap.strEPC;
                str = str.replace("f", "");
                str = str.replace("F", "");

                if (!str.startsWith(Constants.RFID_START_WITH_STR)) {
                    continue;
                }
                if (str.length() < 10) {
                    continue;
                }

                String foundBoxCode = str.substring(4, 10);

                if (kvpCashbox.containsKey(foundBoxCode)) {
                    Cashbox cb = kvpCashbox.get(foundBoxCode);
                    if (cb.getFoundStatus() == Constants.FOUND_STATUS_NO) {
                        cb.setFoundStatus(Constants.FOUND_STATUS_YES);
                        ++readCount;
                        founds.add(foundBoxCode);
                    }
                }
            }

            cashboxListFragment.myadapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        m_ReadCount.setText("已读取:" + String.valueOf(readCount));
        cashboxListFragment.myadapter.notifyDataSetChanged();
    }

    private void read() {
        byte outPower = Byte.parseByte(AppContext.get("batchReadPower", "27"));
        set(outPower);
        Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
        sendBroadcast(intent);// 传递过去
        mRefreshHandler.postDelayed(mRefreshRunnable, 500);
    }

    public void stopInventory() {
        mRefreshHandler.removeCallbacks(mRefreshRunnable);

        super.stopInventory();
    }

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
        //if (keyCode == KeyEvent.KEYCODE_BACK) {
        //
        //    return false;
        //} else if (keyCode == KeyEvent.ACTION_DOWN) {
        //    read(mButtonRead);
        //}
        return super.onKeyDown(keyCode, event);
    }
}
