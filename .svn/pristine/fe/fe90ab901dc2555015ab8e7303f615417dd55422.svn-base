package com.jintoufs.logstics.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.db.CashBoxDao;
import com.jintoufs.logstics.db.ReplaceCashBoxDao;
import com.jintoufs.logstics.db.EquipmentDao;
import com.jintoufs.logstics.db.TerminalDao;
import com.jintoufs.logstics.entity.Cashbox;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Equipment;
import com.jintoufs.logstics.entity.ReplaceCashBox;
import com.jintoufs.logstics.entity.Terminal;
import com.jintoufs.logstics.entity.UploadData;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.jintoufs.logstics.utils.FileUtil;
import com.jintoufs.logstics.widget.GuidCard;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 收箱、送箱任务选择
 *
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
public class TaskTypeActivity extends BaseActivity {
    private static final String TAG = "TaskTypeActivity";
    LinearLayout view;
    private User user;
    private CashBoxDao cashBoxDao;
    private TerminalDao terminalDao;
    private EquipmentDao equipmentDao;
    private ReplaceCashBoxDao replaceCashBoxDao;
    private Terminal terminal;
    private Map<Integer, String> itemIdMap = new HashMap<Integer, String>();
    private Map<String, String> cashBoxMap = new HashMap<String, String>();//存放cashBoxCode值
    private Map<String, String> resultCodeMap = new HashMap<String, String>();//存放返回码
    private static boolean isFirst;//判断是否第一次下载数据
    private List<ReplaceCashBox> replaceCashBoxes;//存放替换后钞箱数据

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_type);
        AppContext appContext = (AppContext) getApplication();
        user = appContext.getLoginUser();
        cashBoxDao = new CashBoxDao(this);
        terminalDao = new TerminalDao(this);
        equipmentDao = new EquipmentDao(this);
        replaceCashBoxDao = new ReplaceCashBoxDao(this);
        view = (LinearLayout) findViewById(R.id.card_scroll);

        Integer taskType = getIntent().getIntExtra("taskType", -1);

        if (taskType < 0) {
            AppContext.showToast("任务类型不合法");
            this.finish();
            return;
        }
        view.removeAllViews();
        GuidCard gc0 = new GuidCard(this);
        GuidCard gc1 = new GuidCard(this);
        GuidCard gc2 = new GuidCard(this);
        GuidCard gc3 = new GuidCard(this);

        if (taskType == Constants.TASK_TYPE_LOGISTICS) {
            gc0.setTitle("送箱");
            gc0.setTip("将箱包送到网点");
            gc0.setImg(R.drawable.quick_option_put_in_nor);
            gc0.setOnHeaderClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent intent = new Intent(TaskTypeActivity.this, TerminalListActivity.class);
                    intent.putExtra("deliverType", Constants.TASK_TYPE_DEVELIVER);
                    intent.putExtra("terminalType", Constants.TERMINAL_TYPE_SITE);
                    intent.putExtra("caption", "送箱");
                    startActivity(intent);
                }
            });
            gc1.setTitle("收箱");
            gc1.setTip("去网点收取箱包");
            gc1.setImg(R.drawable.quick_option_take_out_nor);
            gc1.setOnHeaderClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent intent = new Intent(TaskTypeActivity.this, TerminalListActivity.class);
                    intent.putExtra("deliverType", Constants.TASK_TYPE_RECEIVE);
                    intent.putExtra("terminalType", Constants.TERMINAL_TYPE_SITE);
                    intent.putExtra("caption", "收箱");
                    startActivity(intent);
                }
            });
            gc2.setTitle("自助设备");
            gc2.setTip("自助设备加钞、维保等");
            gc2.setImg(R.drawable.atm_2);
            gc2.setOnHeaderClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(TaskTypeActivity.this, TerminalListActivity.class);
                    intent.putExtra("deliverType", Constants.TASK_TYPE_DEVELIVER);
                    intent.putExtra("terminalType", Constants.TERMINAL_TYPE_ATM);
                    intent.putExtra("caption", "自助设备");
                    startActivity(intent);
                }
            });
            gc3.setTitle("同步自助设备数据");
            gc3.setTip("自助设备离线模式使用");
            gc3.setImg(R.drawable.logistics_refresh);
            gc3.setOnHeaderClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showWaitDialog(R.string.loading);
                    List<Terminal> terminalList = terminalDao.queryAllTerminal();
                    if (terminalList.size()>0) {
                        prepareUpload();
                    } else {
                        isFirst = true;
                        downData();
                    }
                }
            });

            view.addView(gc3);
            view.addView(gc0);
            view.addView(gc1);
            view.addView(gc2);
        } else if (taskType == Constants.TASK_TYPE_OUTCAR) {
            gc0.setTitle("交接到大库");
            gc0.setTip("将箱包送到大库");
            gc0.setImg(R.drawable.quick_option_put_in_nor);
            gc0.setOnHeaderClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(TaskTypeActivity.this, ConfirmOutCarTaskActivity.class);
                    intent.putExtra("outcarType", Constants.TASK_CASHBOX_TO_WHERE_IN_STORAGE);
                    startActivity(intent);
                }
            });

            gc1.setTitle("交接到清分间");
            gc1.setTip("将箱包送到清分间");
            gc1.setImg(R.drawable.quick_option_take_out_nor);
            gc1.setOnHeaderClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(TaskTypeActivity.this, ConfirmOutCarTaskActivity.class);
                    intent.putExtra("outcarType", Constants.TASK_CASHBOX_TO_WHERE_CLEAR);
                    startActivity(intent);
                }
            });
            view.addView(gc1);
            view.addView(gc0);
        }
    }

    /**
     * 下载数据
     */
    private void downData() {
        Api.getOfflineData(user.getKeyStr(), mHandler);
        List<Equipment> equipments = equipmentDao.queryAllEquipment();
        for (Equipment equipment : equipments) {
            if (!equipment.getWithdrawStatus().equals(Constants.UNFINISHED_STATUS)) {
                equipmentDao.updateFinishedStatus(Constants.FINISHED_STATUS, equipment.getTaskDetailId());
            }
        }
    }

    /**
     * 准备上传处理
     */
    private void prepareUpload() {
        //List<Equipment> equipments = equipmentDao.queryEquipment(Constants.UNFINISHED_STATUS);
        //if (equipments.size() > 0) {
        //    hideWaitDialog();
        //    AppContext.showToastShort("您还有自助设备任务未完成");
        //} else {
            uploadHandle();
            //downData();
            //isFirst=false;
            //showResultMessage();
        //}
    }

    /**
     * 上传数据处理
     */
    private void uploadHandle() {
        resultCodeMap.clear();
        List<Equipment> equipments = equipmentDao.queryEquipment(Constants.FINISHED_STATUS);

        Gson gson=new Gson();
        for (Equipment equipment : equipments) {
            UploadData uploadData = new UploadData();
            getCashBoxList(equipment.getTaskDetailId());
            getReplaceCashBoxList(equipment.getTaskDetailId());
            uploadData.setKeyStr(user.getKeyStr());
            uploadData.setOutCashboxCodes(getCashBoxCodes());
            uploadData.setOutItemIds(getItemIds());
            uploadData.setTaskDetailId(equipment.getTaskDetailId());
            uploadData.setWithdrawStatus(equipment.getWithdrawStatus());
            uploadData.setSwallowedCard(equipment.getSwallowedCard());
            uploadData.setReplaceCashBox(equipment.getReplaceCashBox());
            uploadData.setAddCash(equipment.getAddCash());
            uploadData.setCheckMaterial(equipment.getCheckMaterial());
            uploadData.setSafetyProtection(equipment.getSafetyProtection());
            uploadData.setTechnicalProtection(equipment.getTechnicalProtection());
            uploadData.setInspection(equipment.getInspection());
            uploadData.setCreateTime(equipment.getCreateTime());
            uploadData.setEndTime(equipment.getEndTime());
            uploadData.setReplaceCashBoxs(replaceCashBoxes);

            String datas = gson.toJson(uploadData);

            List<File> files = new ArrayList<File>();
            if (!TextUtils.isEmpty(equipment.getWithdrawImg())) {
                files.add(new File(equipment.getWithdrawImg()));
            }
            if (!TextUtils.isEmpty(equipment.getSwallowedCardImg())) {
                files.add(new File(equipment.getSwallowedCardImg()));
            }
            if (!TextUtils.isEmpty(equipment.getMachineImg())) {
                files.add(new File(equipment.getMachineImg()));
            }
            if (!TextUtils.isEmpty(equipment.getCloseDoorImg())) {
                files.add(new File(equipment.getCloseDoorImg()));
            }
            if (!TextUtils.isEmpty(equipment.getSafetyProtectionImg())) {
                files.add(new File(equipment.getSafetyProtectionImg()));
            }
            if (!TextUtils.isEmpty(equipment.getTechnicalProtectionImg())) {
                files.add(new File(equipment.getTechnicalProtectionImg()));
            }
            if (!TextUtils.isEmpty(equipment.getSanitationImg())) {
                files.add(new File(equipment.getSanitationImg()));
            }
            if (!TextUtils.isEmpty(equipment.getInspectionImg())) {
                files.add(new File(equipment.getInspectionImg()));
            }
            if (!TextUtils.isEmpty(equipment.getPhoto())) {
                files.add(new File(equipment.getPhoto()));
            }
            Api.subAtmOff(datas, files, uploadHandler);
        }
    }

    /**
     * 从数据库获取repalceCashbox列表
     */
    private void getReplaceCashBoxList(long taskDetailId) {
        replaceCashBoxes = replaceCashBoxDao.queryCashBox(taskDetailId);
    }

    /**
     * 统一显示上传数据后的提示
     */
    private void showResultMessage() {
        Iterator<Map.Entry<String, String>> it = resultCodeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (!entry.getKey().equals(AjaxMsg.FAILURE)){
                String detailId=entry.getValue();
                equipmentDao.deleteByDetailId(detailId);
            }
            Log.d(TAG,"key= " + entry.getKey() + " and value= " + entry.getValue());
        }

        String message = "";
        if (resultCodeMap.containsKey(AjaxMsg.SUCCESS)) {
            //message=resultCodeMap.get(AjaxMsg.SUCCESS);
            message = "同步数据成功";
        }
        if (resultCodeMap.containsKey(AjaxMsg.SUCCESS_NOT_PIC)) {
            //message = resultCodeMap.get(AjaxMsg.SUCCESS_NOT_PIC);
            message = "同步数据成功,但有照片上传失败";
        }
        if (resultCodeMap.containsKey(AjaxMsg.FAILURE)) {
            //message=resultCodeMap.get(AjaxMsg.FAILURE);
            message = "同步数据失败，请联系管理员";
        }
        if (resultCodeMap.containsKey(AjaxMsg.FAILURE_EXIST)) {
            //message = resultCodeMap.get(AjaxMsg.FAILURE_EXIST);
            message = "该自助设备记录已存在";
        }
        AppContext.showToastShort(message);
    }

    /**
     * 从数据库获取cashbox列表
     */
    private void getCashBoxList(long detailId) {
        cashBoxMap.clear();
        itemIdMap.clear();
        List<Cashbox> cashboxList = cashBoxDao.queryCashBox(Constants.USE_TYPE_TAKE_OUT, detailId, Constants.FOUND_STATUS_YES);
        if (cashboxList != null) {
            for (Cashbox x : cashboxList) {
                String boxCode = x.getCashBoxCode() + ",";
                cashBoxMap.put(x.getCashBoxCode(), boxCode);

                String itemId = String.valueOf(x.getId()) + ",";
                itemIdMap.put(x.getId(), itemId);
            }
        }
    }

    /**
     * 获取cashBoxCodes字符串
     */
    private String getCashBoxCodes() {
        String cashboxCodes = "";
        for (String cashboxCode : cashBoxMap.values()) {
            cashboxCodes += cashboxCode;
        }
        return cashboxCodes;
    }

    /**
     * 获取ItemIds字符串
     */
    private String getItemIds() {
        String itemIds = "";
        for (String id : itemIdMap.values()) {
            itemIds += id;
        }
        return itemIds;
    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                cashBoxDao.deleteAll();//清空cashbox表中数据
                terminalDao.deleteAll();//清空terminal表中数据
                equipmentDao.deleteAll();//清空equipment表中数据
                replaceCashBoxDao.deleteAll();//清空changebox表中数据
                FileUtil.DeleteFolder(Constants.STORE_URL);//清空该目录下的图片文件
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(response.toString()).getAsJsonObject();

                JsonArray datas = jsonObject.getAsJsonArray("datas");
                for (JsonElement jsonElement : datas) {

                    JsonObject atmInfo = jsonElement.getAsJsonObject().getAsJsonObject("atmInfo");
                    terminal = gson.fromJson(atmInfo, Terminal.class);
                    terminalDao.add(terminal);

                    String dynamicCode = jsonElement.getAsJsonObject().get("dynamicCode").getAsString();
                    AppContext.getInstance().setProperty(String.valueOf(terminal.getDetailId()), dynamicCode);

                    JsonObject stepStatus = jsonElement.getAsJsonObject().getAsJsonObject("stepStatus");
                    Equipment equipment = new Equipment();
                    equipment.setTaskDetailId(terminal.getDetailId());
                    equipment.setWithdrawStatus(String.valueOf(stepStatus.get("step1")));
                    equipment.setSwallowedCard(String.valueOf(stepStatus.get("step2")));
                    equipment.setCheckMachine(String.valueOf(stepStatus.get("step4")));
                    equipment.setReplaceCashBox(String.valueOf(stepStatus.get("step5")));
                    equipment.setCloseDoor(String.valueOf(stepStatus.get("step6")));
                    equipment.setAddCash(String.valueOf(stepStatus.get("step7")));
                    equipment.setCheckMaterial(String.valueOf(stepStatus.get("step8")));
                    equipment.setSafetyProtection(String.valueOf(stepStatus.get("step9")));
                    equipment.setTechnicalProtection(String.valueOf(stepStatus.get("step10")));
                    equipment.setSanitation(String.valueOf(stepStatus.get("step11")));
                    equipment.setInspection(String.valueOf(stepStatus.get("step12")));
                    equipment.setPhotoStatus(String.valueOf(stepStatus.get("step13")));
                    if (equipment.getPhotoStatus().equals(Constants.FINISHED_STATUS)) {
                        equipment.setFinishedStatus(Constants.FINISHED_STATUS);
                    } else if (!equipment.getReplaceCashBox().equals(Constants.UNFINISHED_STATUS)) {
                        equipment.setFinishedStatus(Constants.FINISHED_STATUS);
                    } else {
                        equipment.setFinishedStatus(Constants.UNFINISHED_STATUS);
                    }
                    equipmentDao.add(equipment);

                    JsonArray inCashBoxList = jsonElement.getAsJsonObject().getAsJsonArray("inCashBoxList");
                    List<Cashbox> inCashBox = gson.fromJson(inCashBoxList, new TypeToken<List<Cashbox>>() {
                    }.getType());
                    for (Cashbox cashBox : inCashBox) {
                        cashBox.setDetailId(terminal.getDetailId());
                        cashBox.setUseType(Constants.UES_TYPE_PUT_IN);
                    }
                    cashBoxDao.add(inCashBox);

                    JsonArray outCashBoxList = jsonElement.getAsJsonObject().getAsJsonArray("outCashBoxList");
                    List<Cashbox> outCashBox = gson.fromJson(outCashBoxList, new TypeToken<List<Cashbox>>() {
                    }.getType());
                    for (Cashbox cashBox : outCashBox) {
                        cashBox.setDetailId(terminal.getDetailId());
                        cashBox.setUseType(Constants.USE_TYPE_TAKE_OUT);
                    }
                    cashBoxDao.add(outCashBox);
                }
                if (isFirst) {
                    hideWaitDialog();
                    AppContext.showToastShort("同步数据成功");
                }else{
                    hideWaitDialog();
                    showResultMessage();
                }
            } catch (Exception e) {
                hideWaitDialog();
                if (isFirst) {
                    AppContext.showToastShort("同步数据失败");
                }
                //AppContext.showToastShort("同步数据失败");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }
    };

    private final AsyncHttpResponseHandler uploadHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    resultCodeMap.put(ajaxMsg.getCode(), String.valueOf(ajaxMsg.getDatas()));
                    //AppContext.showToast(ajaxMsg.getMessage());
                    hideWaitDialog();
                } else if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS_NOT_PIC)) {
                    resultCodeMap.put(ajaxMsg.getCode(), String.valueOf(ajaxMsg.getDatas()));
                    //AppContext.showToast(ajaxMsg.getMessage());
                    hideWaitDialog();
                } else if (ajaxMsg.getCode().equals(AjaxMsg.FAILURE)) {
                    resultCodeMap.put(ajaxMsg.getCode(), String.valueOf(ajaxMsg.getDatas()));
                    //AppContext.showToast(ajaxMsg.getMessage());
                    hideWaitDialog();
                } else if (ajaxMsg.getCode().equals(AjaxMsg.FAILURE_EXIST)) {
                    resultCodeMap.put(ajaxMsg.getCode(), String.valueOf(ajaxMsg.getDatas()));
                    //AppContext.showToast(ajaxMsg.getMessage());
                    hideWaitDialog();
                }
                downData();
                isFirst=false;
            } catch (Exception e) {
                hideWaitDialog();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            hideWaitDialog();
            arg4.printStackTrace();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            hideWaitDialog();
            arg4.printStackTrace();
            AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }
    };
}
