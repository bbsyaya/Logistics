package com.jintoufs.logstics.activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.base.BaseRfidActivity;
import com.jintoufs.logstics.db.CashBoxDao;
import com.jintoufs.logstics.db.ReplaceCashBoxDao;
import com.jintoufs.logstics.db.EquipmentDao;
import com.jintoufs.logstics.entity.Cashbox;
import com.jintoufs.logstics.entity.ReplaceCashBox;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Equipment;
import com.jintoufs.logstics.entity.Terminal;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.logstics.reader.server.ReaderHelper;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.jintoufs.logstics.utils.MapUtil;
import com.jintoufs.logstics.widget.OperationView;
import com.jintoufs.logstics.widget.dialog.OperationDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自助设备交接界面
 */
@SuppressLint("NewApi")
public class EquipmentActivity extends BaseRfidActivity {

    private AppContext appContext;
    private User user;
    private TextView mHeaderTitle;
    private Double longitude;
    private Double latitude;
    private OperationView operationView1;
    private OperationView operationView2;
    private OperationView operationView3;
    private OperationView operationView4;
    private OperationView operationView5;
    private OperationView operationView6;
    private OperationView operationView7;
    private OperationView operationView8;
    private OperationView operationView9;
    private OperationView operationView10;
    private OperationView operationView11;
    private OperationView operationView12;
    private OperationView operationView13;
    private List<OperationView> m_lstOperationView = new ArrayList<OperationView>();
    private OperationDialog dialog;

    final int DISARMING_REQ_CODE = 101;
    final int STOCKEDCARD_REQ_CODE = 102;
    final int CHECK_MACHINE_REQ_CODE = 104;
    final int CLOSE_DOOR_REQ_CODE = 106;
    final int SECURITY_DEFENCE_REQ_CODE = 109;
    final int TECH_DEFENCE_REQ_CODE = 110;
    final int ATM_CLEAN_REQ_CODE = 111;
    final int CHECK_AGAIN_CODE = 112;
    final int SELFSHOT_REQ_CODE = 113;

    public boolean stepOneStatus = false;
    public boolean stepTwoStatus = false;
    public boolean stepFourStatus = false;
    public static boolean stepFiveStatus = false;
    public static boolean stepFiveStatusOne = false;
    public boolean getCashException=false;
    public boolean putCashException=false;
    public boolean stepSixStatus = false;
    public boolean stepSevenStatus = false;
    public boolean stepEightStatus = false;
    public boolean stepNineStatus = false;
    public boolean stepTenStatus = false;
    public boolean stepElevenStatus = false;
    public boolean stepTwelveStatus = false;
    public boolean stepThirteenStatus = false;

    private int id;
    private long detailId;
    private String code;
    private int lockType;
    private String rfid;

    private Terminal m_Site;
    AlertDialog loginDialog;
    PopupMenu popupMenu;
    Menu menu;
    private EquipmentDao equipmentDao;
    private CashBoxDao cashBoxDao;
    private ReplaceCashBoxDao replaceCashBoxDao;
    private ArrayList<String> data;//存放cashBoxCode值
    private List<Cashbox> cashboxList;//存放cashBox数据
    private String tempData;//临时存放更换钞箱cashBoxCode值
    private Map<String, Integer> previousMap=new HashMap<String, Integer>();//存放钞箱id,便于修改数据库中数据
    private Map<String, Integer> nextMap=new HashMap<String, Integer>();//存放钞箱id,便于修改数据库中数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        mHeaderTitle = (TextView) findViewById(R.id.header_title);
        appContext = (AppContext) getApplication();
        user = appContext.getLoginUser();
        equipmentDao=new EquipmentDao(this);
        cashBoxDao=new CashBoxDao(this);
        replaceCashBoxDao =new ReplaceCashBoxDao(this);

        Intent intent = getIntent();
        m_Site = (Terminal) intent.getSerializableExtra("site");
        longitude = m_Site.getLongitude();
        latitude = m_Site.getLatitude();
        code = m_Site.getCode();
        id = m_Site.getId();
        lockType = m_Site.getLockType();
        detailId = m_Site.getDetailId();
        rfid = m_Site.getRfid();
        mHeaderTitle.setText(code);

        long createTime=System.currentTimeMillis();
        equipmentDao.updateCreateTime(createTime, detailId);

        initMenu();

        operationView1 = (OperationView) findViewById(R.id.operation_1);
        operationView2 = (OperationView) findViewById(R.id.operation_2);
        operationView3 = (OperationView) findViewById(R.id.operation_3);
        operationView4 = (OperationView) findViewById(R.id.operation_4);
        operationView5 = (OperationView) findViewById(R.id.operation_5);
        operationView6 = (OperationView) findViewById(R.id.operation_6);
        operationView7 = (OperationView) findViewById(R.id.operation_7);
        operationView8 = (OperationView) findViewById(R.id.operation_8);
        operationView9 = (OperationView) findViewById(R.id.operation_9);
        operationView10 = (OperationView) findViewById(R.id.operation_10);
        operationView11 = (OperationView) findViewById(R.id.operation_11);
        operationView12 = (OperationView) findViewById(R.id.operation_12);
        operationView13 = (OperationView) findViewById(R.id.operation_13);
        read();
        createDialog();
        m_lstOperationView.add(operationView1);
        m_lstOperationView.add(operationView2);
        m_lstOperationView.add(operationView3);
        if (lockType == Constants.LOCK_TYPE_ELECTRONIC) {
            operationView3.setVisibility(View.VISIBLE);
            showDynamicPassword(3, AppContext.getInstance().getProperty(String.valueOf(detailId)));
            operationView3.mButton.setVisibility(View.GONE);
            operationView3.setOnHeaderClickListener(listener);
        } else {
            operationView3.setVisibility(View.GONE);
        }
        m_lstOperationView.add(operationView4);
        m_lstOperationView.add(operationView5);
        m_lstOperationView.add(operationView6);
        m_lstOperationView.add(operationView7);
        m_lstOperationView.add(operationView8);
        m_lstOperationView.add(operationView9);
        m_lstOperationView.add(operationView10);
        m_lstOperationView.add(operationView11);
        m_lstOperationView.add(operationView12);
        m_lstOperationView.add(operationView13);

        operationView1.setOnHeaderClickListener(listener);
        operationView2.setOnHeaderClickListener(listener);
        operationView4.setOnHeaderClickListener(listener);
        operationView5.setOnHeaderClickListener(listener);
        operationView6.setOnHeaderClickListener(listener);
        operationView7.setOnHeaderClickListener(listener);
        operationView8.setOnHeaderClickListener(listener);
        operationView9.setOnHeaderClickListener(listener);
        operationView10.setOnHeaderClickListener(listener);
        operationView11.setOnHeaderClickListener(listener);
        operationView12.setOnHeaderClickListener(listener);
        operationView13.setOnHeaderClickListener(listener);

    }

    /**
     * 初始化弹出菜单操作
     */
    private void initMenu() {
        popupMenu = new PopupMenu(this, findViewById(R.id.popupmenu_btn));
        menu = popupMenu.getMenu();

        // 通过代码添加菜单项
        menu.add(Menu.NONE, Menu.FIRST, 0, "替换钞箱");
        menu.add(Menu.NONE, Menu.FIRST + 1, 0, "停止并带回要加钞箱");
        menu.add(Menu.NONE, Menu.FIRST + 2, 0, "停止并带回所有钞箱");
        // 监听事件
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case Menu.FIRST:
                        if (stepFourStatus){
                            if (stepThirteenStatus){
                                AppContext.showToastShort("在此阶段，该操作不可执行");
                            }else{
                                showNeedChangeBox();
                                resetOperation();
                            }
                        }else{
                            AppContext.showToastShort("在此阶段，该操作不可执行");
                        }
                        break;
                    case Menu.FIRST + 1:
                        if (stepFourStatus){
                            if (stepFiveStatusOne){
                                AppContext.showToastShort("取回钞箱已完成，不可执行此操作");
                            }else {
                                if (getCashException){
                                    AppContext.showToastShort("操作已执行");
                                }else{
                                    getCashException=true;
                                    equipmentDao.updateStatus(detailId, 5,"3");
                                    equipmentDao.updateFinishedStatus(Constants.FINISHED_STATUS, detailId);
                                    operationView5.mButton.setBackgroundResource(R.drawable.error);
                                    AppContext.showToastShort("操作成功");
                                    //Api.stopAddCashException(user.getKeyStr(), Constants.ATM_TASK_RESULT_GET_EXCEPTION, detailId, exceptionHandler);
                                }
                            }
                        }else{
                            if (getCashException){
                                AppContext.showToastShort("操作已执行");
                            }else{
                                getCashException=true;
                                equipmentDao.updateStatus(detailId,  5,"3");
                                equipmentDao.updateFinishedStatus(Constants.FINISHED_STATUS, detailId);
                                operationView5.mButton.setBackgroundResource(R.drawable.error);
                                AppContext.showToastShort("操作成功");
                                //Api.stopAddCashException(user.getKeyStr(), Constants.ATM_TASK_RESULT_GET_EXCEPTION, detailId, exceptionHandler);
                            }
                        }
                        break;
                    case Menu.FIRST + 2:
                        if (stepFiveStatusOne){
                            if (stepFiveStatus){
                                AppContext.showToastShort("更换钞箱已完成，不可执行此操作");
                            }else{
                                if (putCashException){
                                    AppContext.showToastShort("操作已执行");
                                }else{
                                    putCashException=true;
                                    equipmentDao.updateStatus(detailId, 5,"4");
                                    equipmentDao.updateFinishedStatus(Constants.FINISHED_STATUS, detailId);
                                    operationView5.mButton.setBackgroundResource(R.drawable.error);
                                    AppContext.showToastShort("操作成功");
                                    //Api.stopAddCashException(user.getKeyStr(), Constants.ATM_TASK_RESULT_PUT_EXCEPTION, detailId, exceptionHandler);
                                }
                            }
                        }else{
                            AppContext.showToastShort("更换钞箱已完成，不可执行此操作");
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 替换钞箱后，需要重置更换钞箱以及之后的操作
     */
    private void resetOperation() {
        stepFiveStatusOne=false;
        stepFiveStatus=false;
        stepSixStatus=false;
        stepSevenStatus=false;
        stepNineStatus=false;
        stepTenStatus=false;
        stepElevenStatus=false;
        stepTwelveStatus=false;
        cashBoxDao.updateStatus(Constants.FOUND_STATUS_NO, detailId);
        for (int i=5; i<=12; i++){
            equipmentDao.updateStatus(detailId, i, "0");
        }
        equipmentDao.updateImageUrl("", detailId, 6);
        equipmentDao.updateImageUrl("", detailId, 9);
        equipmentDao.updateImageUrl("", detailId, 10);
        equipmentDao.updateImageUrl("", detailId, 11);
        equipmentDao.updateImageUrl("", detailId, 12);
        refreshUIFromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUIFromDB();
        //Api.getStepStatus(user.getKeyStr(), detailId, mHandler);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void createDialog() {

        loginDialog = new AlertDialog.Builder(EquipmentActivity.this)
                .setTitle("rfid验证中...")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        stopInventory();
                        EquipmentActivity.this.finish();
                    }
                }).create();
        loginDialog.show();

    }

    /**
     * 显示需要更换钞箱列表
     */
    private void showNeedChangeBox(){
        cashboxList = cashBoxDao.queryCashBoxByUseType(Constants.USE_TYPE_TAKE_OUT, detailId);
        data = new ArrayList<String>();
        for(Cashbox cashbox: cashboxList){
            data.add(cashbox.getCashBoxCode());
            previousMap.put(cashbox.getCashBoxCode(), cashbox.getId());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.btn_star);
        builder.setTitle("请选择需要更换的钞箱");
        builder.setItems(data.toArray(new String[data.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tempData = data.get(i);
                dialogInterface.dismiss();
                showChooseBox();
                //AppContext.showToastShort(data.get(i));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 显示替换钞箱列表
     */
    private void showChooseBox(){
        data.clear();
        cashboxList.clear();
        cashboxList = cashBoxDao.queryCashBoxByUseType(Constants.UES_TYPE_PUT_IN, detailId);
        for(Cashbox cashbox: cashboxList){
            data.add(cashbox.getCashBoxCode());
            nextMap.put(cashbox.getCashBoxCode(), cashbox.getId());
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.btn_star);
        builder.setTitle("请选择替换钞箱");
        builder.setItems(data.toArray(new String[data.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ReplaceCashBox changeBox=new ReplaceCashBox();
                changeBox.setDetailId(detailId);

                Cashbox previousCash=cashBoxDao.queryCashBox(Constants.USE_TYPE_TAKE_OUT, detailId, tempData);

                String chooseBoxCode=data.get(i);
                Cashbox nextCash=cashBoxDao.queryCashBox(Constants.UES_TYPE_PUT_IN, detailId, chooseBoxCode);
                if (previousMap.containsKey(tempData)){
                    Integer previousId=previousMap.get(tempData);
                    changeBox.setOutItemId(previousId);
                    cashBoxDao.updateCashBox(chooseBoxCode,nextCash.getRfid(),nextCash.getCashBoxId(), previousId, Constants.USE_TYPE_TAKE_OUT);
                }
                if (nextMap.containsKey(chooseBoxCode)) {
                    Integer nextId=nextMap.get(chooseBoxCode);
                    changeBox.setInItemId(nextId);
                    cashBoxDao.updateCashBox(tempData, previousCash.getRfid(), previousCash.getCashBoxId(),nextId, Constants.UES_TYPE_PUT_IN);
                }
                replaceCashBoxDao.add(changeBox);
                dialogInterface.dismiss();
                AppContext.showToastShort("钞箱已替换");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private Handler mRefreshHandler = new Handler();
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {
            refreshList();
            mRefreshHandler.postDelayed(this, 2000);
        }
    };

    private void read() {
        Byte bPower = Byte.valueOf(AppContext.get("batchReadPower", "27"));
        set(bPower);
        Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
        sendBroadcast(intent);// 传递过去
        mRefreshHandler.postDelayed(mRefreshRunnable, 500);
    }

    private void refreshList() {
        for (InventoryTagMap inventoryTagMap : m_curInventoryBuffer.lsTagList) {
            String str = inventoryTagMap.strEPC;
            str = str.replace("F", "");
            str = str.replace("f", "");

            if (!str.startsWith(Constants.RFID_START_WITH_ATM)) {
                continue;
            }
            if (str.length() < 10) {
                continue;
            }
            if (str.equals(rfid)) {
                stopInventory();
                loginDialog.dismiss();
                AppContext.showToastShort("rfid验证成功");
                break;
            }

        }
    }

    public void stopInventory() {
        mRefreshHandler.removeCallbacks(mRefreshRunnable);
        super.stopInventory();
    }

    OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            switch (arg0.getId()) {
                case R.id.operation_1:
                    if (stepOneStatus) {
                        AppContext.showToastShort("操作已完成，请继续下一步操作");
                    } else {
                        showQuickOptionForResult(1, DISARMING_REQ_CODE);
                    }
                    break;
                case R.id.operation_2:
                    if (stepOneStatus) {
                        if (stepTwoStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOptionForResult(2, STOCKEDCARD_REQ_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }
                    break;
                case R.id.operation_3:
                    showQuickOption(3);
                    break;
                case R.id.operation_4:
                    if (stepTwoStatus) {
                        if (stepFourStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOptionForResult(4, CHECK_MACHINE_REQ_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }
                    break;
                case R.id.operation_5:
                    if (stepFourStatus) {
                        if (stepFiveStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            if (stepFiveStatusOne) {
                                if (putCashException){
                                    showQuickOptionForResult(5, Constants.ATM_TASK_RESULT_PUT_EXCEPTION);
                                }else{
                                    showQuickOptionForResult(5, 2);
                                    AppContext.showToastShort("取回钞箱已完成，请完成放入钞箱");
                                }
                            } else {
                                if (getCashException){
                                    showQuickOptionForResult(5, Constants.ATM_TASK_RESULT_GET_EXCEPTION);
                                }else {
                                    showQuickOptionForResult(5, 1);
                                }
                            }
                        }
                    } else {
                        if (getCashException){
                            showQuickOptionForResult(5, Constants.ATM_TASK_RESULT_GET_EXCEPTION);
                        }else {
                            AppContext.showToastShort("请先完成上一步操作");
                        }
                    }
                    break;
                case R.id.operation_6:
                    if (stepFiveStatus) {
                        if (stepSixStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOptionForResult(6, CLOSE_DOOR_REQ_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                case R.id.operation_7:
                    if (stepSixStatus) {
                        if (stepSevenStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOption(7);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                case R.id.operation_8:
                    if (stepSevenStatus) {
                        if (stepEightStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOption(8);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                case R.id.operation_9:
                    if (stepEightStatus) {
                        if (stepNineStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOptionForResult(9, SECURITY_DEFENCE_REQ_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                case R.id.operation_10:
                    if (stepNineStatus) {
                        if (stepTenStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOptionForResult(10, TECH_DEFENCE_REQ_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                case R.id.operation_11:
                    if (stepTenStatus) {
                        if (stepElevenStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOptionForResult(11, ATM_CLEAN_REQ_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                case R.id.operation_12:
                    if (stepElevenStatus) {
                        if (stepTwelveStatus) {
                            AppContext.showToastShort("操作已完成，请继续下一步操作");
                        } else {
                            showQuickOptionForResult(12, CHECK_AGAIN_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                case R.id.operation_13:
                    if (stepTwelveStatus) {
                        if (stepThirteenStatus) {
                            AppContext.showToastShort("已完成所有操作");
                        } else {
                            showQuickOptionForResult(13, SELFSHOT_REQ_CODE);
                        }
                    } else {
                        AppContext.showToastShort("请先完成上一步操作");
                    }

                    break;
                default:
                    break;
            }
            accompaniment.start();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DISARMING_REQ_CODE:
                    setOperationResult(false, 1);
                    break;
                case STOCKEDCARD_REQ_CODE:
                    setOperationResult(false, 2);
                    break;
                case CHECK_MACHINE_REQ_CODE:
                    setOperationResult(true, 4);
                    break;
                case CLOSE_DOOR_REQ_CODE:
                    setOperationResult(true, 6);
                    break;
                case SECURITY_DEFENCE_REQ_CODE:
                    setOperationResult(false, 9);
                    break;
                case TECH_DEFENCE_REQ_CODE:
                    setOperationResult(false, 10);
                    break;
                case ATM_CLEAN_REQ_CODE:
                    setOperationResult(true, 11);
                    break;
                case CHECK_AGAIN_CODE:
                    setOperationResult(false, 12);
                    break;
                case SELFSHOT_REQ_CODE:
                    setOperationResult(true, 13);
                    break;
                default:
                    break;
            }
        }
    }

    public void popupMenu(View view) {
        accompaniment.start();
        popupMenu.show();

    }

    public void openMap(View view) {
        accompaniment.start();
        MapUtil.openAmap(longitude, latitude, appContext);
    }

    /**
     * @param step 当前步骤
     */
    private void showQuickOption(int step) {
        dialog = new OperationDialog(EquipmentActivity.this, step, id, code, detailId);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * @param step 当前步骤
     */
    private void showQuickOptionForResult(int step, int requestCode) {
        dialog = new OperationDialog(EquipmentActivity.this, step, id, code, detailId, requestCode);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void setOperationResult(boolean isSuccess, int step) {

        if (step < 1 || step > 13) {
            return;
        }

        OperationView op = m_lstOperationView.get(step - 1);
        if (isSuccess) {
            op.refreshStatus(Constants.ATM_TASK_RESULT_SUCCESS);
        } else {
            op.refreshStatus(Constants.ATM_TASK_RESULT_FAILURE);
        }

    }

    /**
     * 显示动态密码
     */
    public void showDynamicPassword(int step, String message) {
        OperationView op = m_lstOperationView.get(step - 1);
        op.setText2(message);
    }

    /**
     * 通过数据库数据刷新界面状态
     */
    private void refreshUIFromDB() {
        Equipment equipment=equipmentDao.queryStatus(detailId);
        int one= Integer.parseInt(equipment.getWithdrawStatus());
        int two= Integer.parseInt(equipment.getSwallowedCard());
        int four= Integer.parseInt(equipment.getCheckMachine());
        int five= Integer.parseInt(equipment.getReplaceCashBox());
        int six= Integer.parseInt(equipment.getCloseDoor());
        int seven= Integer.parseInt(equipment.getAddCash());
        int eight= Integer.parseInt(equipment.getCheckMaterial());
        int nine= Integer.parseInt(equipment.getSafetyProtection());
        int ten= Integer.parseInt(equipment.getTechnicalProtection());
        int eleven= Integer.parseInt(equipment.getSanitation());
        int twelve= Integer.parseInt(equipment.getInspection());
        int thirteen= Integer.parseInt(equipment.getPhotoStatus());
        refreshUI(one, two, four,five,six,seven,eight,nine,ten, eleven, twelve, thirteen);
    }

    /**
     * 根据状态值刷新界面显示状态
     */
    private void refreshUI(Integer one, Integer two, Integer four, Integer five, Integer six, Integer seven,
                           Integer eight, Integer nine, Integer ten, Integer eleven, Integer twelve, Integer thirteen){
        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(one)) {
            operationView1.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepOneStatus = true;
        } else if (Constants.ATM_TASK_RESULT_FAILURE.equals(one)) {
            operationView1.mButton.setBackgroundResource(R.drawable.error);
            stepOneStatus = true;
        } else {
            operationView1.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepOneStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(two)) {
            operationView2.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepTwoStatus = true;
        } else if (Constants.ATM_TASK_RESULT_FAILURE.equals(two)) {
            operationView2.mButton.setBackgroundResource(R.drawable.error);
            stepTwoStatus = true;
        } else {
            operationView2.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepTwoStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(four)) {
            operationView4.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepFourStatus = true;
        } else {
            operationView4.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepFourStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_FAILURE.equals(five)) {
            operationView5.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepFiveStatus = true;
        } else if (Constants.ATM_TASK_RESULT_SUCCESS.equals(five)) {
            operationView5.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepFiveStatus = false;
            stepFiveStatusOne = true;
        } else if (Constants.ATM_TASK_RESULT_GET_EXCEPTION.equals(five)) {
            operationView5.mButton.setBackgroundResource(R.drawable.error);
            stepFiveStatus = false;
            getCashException=true;
        } else if (Constants.ATM_TASK_RESULT_PUT_EXCEPTION.equals(five)) {
            operationView5.mButton.setBackgroundResource(R.drawable.error);
            stepFiveStatus = false;
            putCashException=true;
        } else {
            operationView5.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepFiveStatus = false;
            stepFiveStatusOne = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(six)) {
            operationView6.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepSixStatus = true;
        } else {
            operationView6.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepSixStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(seven)) {
            operationView7.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepSevenStatus = true;
        } else {
            operationView7.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepSevenStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(eight)) {
            operationView8.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepEightStatus = true;
        } else {
            operationView8.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepEightStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(nine)) {
            operationView9.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepNineStatus = true;
        } else if (Constants.ATM_TASK_RESULT_FAILURE.equals(nine)) {
            operationView9.mButton.setBackgroundResource(R.drawable.error);
            stepNineStatus = true;
        } else {
            operationView9.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepNineStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(ten)) {
            operationView10.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepTenStatus = true;
        } else if (Constants.ATM_TASK_RESULT_FAILURE.equals(ten)) {
            operationView10.mButton.setBackgroundResource(R.drawable.error);
            stepTenStatus = true;
        } else {
            operationView10.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepTenStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(eleven)) {
            operationView11.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepElevenStatus = true;
        } else if (Constants.ATM_TASK_RESULT_FAILURE.equals(eleven)) {
            operationView11.mButton.setBackgroundResource(R.drawable.error);
            stepElevenStatus = true;
        } else {
            operationView11.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepElevenStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(twelve)) {
            operationView12.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepTwelveStatus = true;
        } else if (Constants.ATM_TASK_RESULT_FAILURE.equals(twelve)) {
            operationView12.mButton.setBackgroundResource(R.drawable.error);
            stepTwelveStatus = true;
        } else {
            operationView12.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepTwelveStatus = false;
        }

        if (Constants.ATM_TASK_RESULT_SUCCESS.equals(thirteen)) {
            operationView13.mButton.setBackgroundResource(R.drawable.btn_radio_on);
            stepThirteenStatus = true;
        } else if (Constants.ATM_TASK_RESULT_FAILURE.equals(thirteen)) {
            operationView13.mButton.setBackgroundResource(R.drawable.error);
            stepThirteenStatus = true;
        } else {
            operationView13.mButton.setBackgroundResource(R.drawable.btn_radio_off);
            stepThirteenStatus = false;
        }
        
    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            // TODO Auto-generated method stub
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    Map<String, Integer> resultMap = gson.fromJson(
                            gson.toJson(ajaxMsg.getDatas()),
                            new TypeToken<Map<String, Integer>>() {
                            }.getType());

                    refreshUI(resultMap.get("step1"),
                            resultMap.get("step2"),
                            resultMap.get("step4"),
                            resultMap.get("step5"),
                            resultMap.get("step6"),
                            resultMap.get("step7"),
                            resultMap.get("step8"),
                            resultMap.get("step9"),
                            resultMap.get("step10"),
                            resultMap.get("step11"),
                            resultMap.get("step12"),
                            resultMap.get("step13"));

                } else {
                    AppContext
                            .showToast(getResources().getString(
                                    R.string.tip_loading_error)
                                    + "\n原因:"
                                    + statusCode
                                    + ":"
                                    + ajaxMsg.getMessage());
                    hideWaitDialog();
                }
            } catch (Exception e) {
                // TODO: handle exception
                hideWaitDialog();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3,
                              Throwable arg4) {
            //refreshUIFromDB();
            arg4.printStackTrace();
            hideWaitDialog();
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4,
                              JSONObject arg3) {
            //refreshUIFromDB();
            arg4.printStackTrace();
            hideWaitDialog();
        }
    };

    private final AsyncHttpResponseHandler exceptionHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            // TODO Auto-generated method stub
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    operationView5.mButton.setBackgroundResource(R.drawable.error);
                    AppContext.showToastShort(ajaxMsg.getMessage());
                } else {
                    AppContext
                            .showToastShort(getResources().getString(
                                    R.string.tip_loading_error)
                                    + "\n原因:"
                                    + statusCode
                                    + ":"
                                    + ajaxMsg.getMessage());
                    hideWaitDialog();
                }
            } catch (Exception e) {
                // TODO: handle exception
                hideWaitDialog();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3,
                              Throwable arg4) {
            AppContext.showToast("操作成功");
            arg4.printStackTrace();
            hideWaitDialog();
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4,
                              JSONObject arg3) {
            AppContext.showToast("操作成功");
            arg4.printStackTrace();
            hideWaitDialog();
        }
    };
}
