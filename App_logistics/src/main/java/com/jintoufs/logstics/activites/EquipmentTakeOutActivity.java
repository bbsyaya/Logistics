package com.jintoufs.logstics.activites;

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
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseRfidActivity;
import com.jintoufs.logstics.db.CashBoxDao;
import com.jintoufs.logstics.db.EquipmentDao;
import com.jintoufs.logstics.entity.Cashbox;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.fragment.CashboxListFragment;
import com.jintoufs.logstics.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.logstics.reader.server.ReaderHelper;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 自助设备，取出钞箱界面
 *
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
@SuppressLint("NewApi")
public class EquipmentTakeOutActivity extends BaseRfidActivity {

    private AppContext appContext;
    private User user;
    private static CashboxListFragment cashboxListFragment = null;
    private TextView mHeaderTitle;

    final FragmentManager fragmentManager = getFragmentManager();
    private Button mButtonRead;
    private long taskDetailId;
    private Map<String, Cashbox> kvpCashbox = new HashMap<String, Cashbox>();
    private Map<String, String> cashBoxMap = new HashMap<String, String>();//存放cashBoxCode值
    //private Map<Integer, String> itemIdMap = new HashMap<Integer, String>();//存放itemId值
    private SparseArray<String> itemIdMap=new SparseArray<String>();
    private List<Cashbox> list;
    private EquipmentDao equipmentDao;
    private CashBoxDao cashBoxDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_cashbox);
        mHeaderTitle = (TextView) findViewById(R.id.header_title);
        mButtonRead = (Button) findViewById(R.id.btn_read);
        appContext = (AppContext) getApplication();
        user = appContext.getLoginUser();
        equipmentDao=new EquipmentDao(this);
        cashBoxDao=new CashBoxDao(this);
        cashboxListFragment = (CashboxListFragment) fragmentManager.findFragmentById(R.id.cashbox_fragment_list);
        Intent intent = getIntent();
        taskDetailId = intent.getLongExtra("taskDetailId", 0);
        mHeaderTitle.setText(R.string.operation_put_out_cashbox);

    }

    @Override
    protected void onResume() {
        super.onResume();
        cashboxListFragment.clearItem();
        kvpCashbox.clear();
        cashBoxMap.clear();
        itemIdMap.clear();
        List<Cashbox> cashboxList=cashBoxDao.queryCashBoxByUseType(Constants.USE_TYPE_TAKE_OUT, taskDetailId);
        showCashBoxList(cashboxList);
        //Api.GetTakeOutCashboxList(user.getKeyStr(), taskDetailId, mHandler);
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

        if (kvpCashbox.size() == 0) {
            EquipmentActivity.stepFiveStatusOne = true;
            EquipmentActivity.stepFiveStatus = false;
            equipmentDao.updateStatus(taskDetailId, 5, "1");
            AppContext.showToast("保存成功");
            this.finish();
            //Api.SubmitTakeOutCashboxList(user.getKeyStr(), getCashBoxCodes(), getItemIds(),taskDetailId, mSaveHandler);
        }else{
            boolean bException = false;
            for (Cashbox x : kvpCashbox.values()) {
                if (!x.getFoundStatus().equals(Constants.FOUND_STATUS_YES)) {
                    bException = true;
                    break;
                }
            }

            if (bException) {
                AppContext.showToast("与计划不一致，无法保存！");
            } else {
                EquipmentActivity.stepFiveStatusOne = true;
                EquipmentActivity.stepFiveStatus = false;
                equipmentDao.updateStatus(taskDetailId, 5, "1");
                cashBoxDao.updateStatus(Constants.FOUND_STATUS_YES, taskDetailId, Constants.USE_TYPE_TAKE_OUT);
                AppContext.showToast("保存成功");
                this.finish();
                //Api.SubmitTakeOutCashboxList(user.getKeyStr(), getCashBoxCodes(),getItemIds(), taskDetailId, mSaveHandler);
            }
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

    //拼接ItemIds字符串
    private String getItemIds() {
        String itemIds = "";
        //for (String id : itemIdMap.values()) {
        //    itemIds += id;
        //}
        for (int i = 0, key = itemIdMap.keyAt(i); i < itemIdMap.size(); i++) {
            itemIds += itemIdMap.get(key);
        }
        return itemIds;
    }

    private void read() {
        Byte bPower = Byte.valueOf(AppContext.get(Constants.SINGLE_READ_POWER, "15"));
        set(bPower);
        Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
        sendBroadcast(intent);// 传递过去
        mRefreshHandler.postDelayed(mRefreshRunnable, 500);
    }

    public void stopInventory() {
        mRefreshHandler.removeCallbacks(mRefreshRunnable);
        super.stopInventory();
    }

    private Handler mRefreshHandler = new Handler();
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {

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

            if (kvpCashbox.containsKey(foundBoxCode)) {
                Cashbox cb = kvpCashbox.get(foundBoxCode);

                if (cb.getFoundStatus() == Constants.FOUND_STATUS_NO) {
                    cb.setFoundStatus(Constants.FOUND_STATUS_YES);
                }
            } else {
                Cashbox cb = new Cashbox();
                cb.setRfid(str);
                cb.setCashBoxCode(foundBoxCode);
                cb.setFoundStatus(Constants.FOUND_STATUS_NOTPLAN);
                cashboxListFragment.addItem(cb);
                kvpCashbox.put(foundBoxCode, cb);
            }
        }
        cashboxListFragment.myadapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                break;
            case KeyEvent.ACTION_DOWN://按键扫描
                read(mButtonRead);
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1,
                              JSONObject response) {
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Cashbox>>() {
                    }.getType());

                    showCashBoxList(list);

                    cashboxListFragment.myadapter.notifyDataSetChanged();

                    hideWaitDialog();
                } else {
                    AppContext.showToast(getResources().getString(
                            R.string.tip_loading_error)
                            + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
                    hideWaitDialog();
                }
            } catch (Exception e) {
                hideWaitDialog();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3,
                              Throwable arg4) {
            List<Cashbox> cashboxList=cashBoxDao.queryCashBoxByUseType(Constants.USE_TYPE_TAKE_OUT, taskDetailId);
            showCashBoxList(cashboxList);
            arg4.printStackTrace();
            hideWaitDialog();
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4,
                              JSONObject arg3) {
            List<Cashbox> cashboxList=cashBoxDao.queryCashBoxByUseType(Constants.USE_TYPE_TAKE_OUT, taskDetailId);
            showCashBoxList(cashboxList);
            arg4.printStackTrace();
            hideWaitDialog();
        }
    };

    /**
     * 显示cashbox列表
     * @param cashboxList
     */
    private void showCashBoxList(List<Cashbox> cashboxList) {
        if (cashboxList != null) {
            for (Cashbox x : cashboxList) {
                if (x == null) {
                    continue;
                }
                x.setType(null);
                String boxCode = "," + x.getCashBoxCode();
                cashBoxMap.put(x.getCashBoxCode(), boxCode);

                String itemId=","+String.valueOf(x.getId());
                itemIdMap.put(x.getId(), itemId);
                kvpCashbox.put(x.getCashBoxCode(), x);
                cashboxListFragment.addItem(x);
            }
        }
    }

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
                    hideWaitDialog();
                }
            } catch (Exception e) {
                hideWaitDialog();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            equipmentDao.updateStatus(taskDetailId, 5, "1");
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast("保存成功");
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            equipmentDao.updateStatus(taskDetailId, 5, "1");
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast("保存成功");
        }
    };
}
