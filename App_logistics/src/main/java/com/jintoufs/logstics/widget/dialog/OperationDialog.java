package com.jintoufs.logstics.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.activites.EquipmentActivity;
import com.jintoufs.logstics.db.EquipmentDao;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.jintoufs.logstics.utils.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class OperationDialog extends Dialog {

    AjaxMsg ajaxMsg;
    private ImageView mClose;
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    public static final String ACTION_TYPE = "action_type";
    public static final String STEP = "step";
    private User user;
    private LinearLayout mLayout1;
    private LinearLayout mLayout2;
    private TextView mTextView1;
    private TextView mTextView2;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private long m_TaskDetailId;
    static long lastTime;
    private EquipmentDao equipmentDao;

    private EquipmentActivity m_FatherActivity;

    private OperationDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public OperationDialog(Context context, int step, int equipmentId, String code, long taskDetailId) {
        this(context, R.style.quick_option_dialog, step, equipmentId, code, taskDetailId, 0);
    }

    public OperationDialog(Context context, int step, int equipmentId, String code, long taskDetailId, int requestCode) {
        this(context, R.style.quick_option_dialog, step, equipmentId, code, taskDetailId, requestCode);
    }

    @SuppressLint("InflateParams")
    private OperationDialog(final Context context, int defStyle, final int step, final int equipmentId,
                            final String code, long taskDetailId, final int requestCode) {
        super(context, defStyle);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_operation, null);
        mLayout1 = (LinearLayout) contentView.findViewById(R.id.ly_operation_1);

        mLayout2 = (LinearLayout) contentView.findViewById(R.id.ly_operation_2);

        mTextView1 = (TextView) mLayout1.findViewById(R.id.operation_1_text);
        mImageView1 = (ImageView) mLayout1.findViewById(R.id.operation_1_img);

        mTextView2 = (TextView) mLayout2.findViewById(R.id.operation_2_text);
        mImageView2 = (ImageView) mLayout2.findViewById(R.id.operation_2_img);

        m_TaskDetailId = taskDetailId;

        m_FatherActivity = (EquipmentActivity) context;
        user = ((AppContext) m_FatherActivity.getApplication()).getLoginUser();
        equipmentDao=new EquipmentDao(context);

        switch (step) {
            case 1:// 撤防:正常，拍照
                mTextView1.setText(R.string.operation_success);
                mTextView2.setText(R.string.operation_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_ok_selector);
                mImageView2.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);

                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        OperationDialog.this.dismiss();
                        m_FatherActivity.stepOneStatus = true;
                        equipmentDao.updateStatus(m_TaskDetailId, step, "1");
                        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
                        m_FatherActivity.setOperationResult(true, step);
                        AppContext.showToastShort("撤防成功");
                        //try {
                        //    Api.DisArming(user.getKeyStr(), null, Constants.ATM_TASK_RESULT_SUCCESS, m_TaskDetailId, mHandler);
                        //} catch (FileNotFoundException e) {
                        //    // TODO Auto-generated catch block
                        //    e.printStackTrace();
                        //}
                    }
                });
                mLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;
            case 2: // 吞没卡:正常，拍照
                mTextView1.setText(R.string.operation_success);
                mTextView2.setText(R.string.operation_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_ok_selector);
                mImageView2.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        OperationDialog.this.dismiss();
                        m_FatherActivity.stepTwoStatus = true;
                        equipmentDao.updateStatus(m_TaskDetailId, step, "1");
                        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
                        m_FatherActivity.setOperationResult(true, step);
                        AppContext.showToastShort("吞没卡成功");
                        //try {
                        //    Api.StockedCardChecking(user.getKeyStr(), null, Constants.ATM_TASK_RESULT_SUCCESS, m_TaskDetailId, mHandler);
                        //} catch (FileNotFoundException e) {
                        //    // TODO Auto-generated catch block
                        //    e.printStackTrace();
                        //}
                    }
                });
                mLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;
            case 3: // 动态密码
                mLayout2.setVisibility(View.GONE);
                mTextView1.setText(R.string.operation_password);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_lock_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (System.currentTimeMillis() - lastTime < 1000 * 60) {
                            AppContext.showToastShort("重复获取动态密码时间不能短于一分钟哦");
                            return;
                        }
                        String dynamicCode=AppContext.getInstance().getProperty(String.valueOf(m_TaskDetailId));
                        m_FatherActivity.showDynamicPassword(3, dynamicCode);
                        //Api.GetDynamicPassword(user.getKeyStr(), m_TaskDetailId, mPasswordHandler);
                        dismiss();
                    }
                });

                break;

            case 4: // 检查封包、卡钞、废钞箱工作
                mTextView1.setText(R.string.operation_photo);
                mLayout2.setVisibility(View.GONE);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;

            case 5: // 更换钞箱:取回，放入
                mTextView1.setText(R.string.operation_put_out_cashbox);
                mTextView2.setText(R.string.operation_put_in_cashbox);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_take_out_selector);
                mImageView2.setBackgroundResource(R.drawable.day_quickoption_icon_put_in_selector);
                switch (requestCode){
                    case 1:
                        mLayout1.setVisibility(View.VISIBLE);
                        mLayout2.setVisibility(View.INVISIBLE);
                        mLayout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                UIHelper.showTakeOutCashBoxActivity(context, m_TaskDetailId);
                            }
                        });
                        break;
                    case 2:
                        mLayout1.setVisibility(View.INVISIBLE);
                        mLayout2.setVisibility(View.VISIBLE);
                        mLayout2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                UIHelper.showPutInCashBoxActivity(context, m_TaskDetailId);
                            }
                        });
                        break;
                    case 3:
                    case 4:
                        mLayout1.setVisibility(View.INVISIBLE);
                        mLayout2.setVisibility(View.INVISIBLE);
                        break;
                }
                break;
            case 6: // 关闭保险箱门
                mLayout2.setVisibility(View.GONE);
                mTextView1.setText(R.string.operation_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;
            case 7: // 清机加钞
                mLayout2.setVisibility(View.GONE);
                mTextView1.setText(R.string.operation_success);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_ok_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        OperationDialog.this.dismiss();
                        m_FatherActivity.stepSevenStatus = true;
                        equipmentDao.updateStatus(m_TaskDetailId, step, "1");
                        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
                        m_FatherActivity.setOperationResult(true, step);
                        AppContext.showToastShort("清机加钞成功");
                        //Api.CashWorking(user.getKeyStr(), Constants.ATM_TASK_RESULT_SUCCESS, m_TaskDetailId, mHandler);
                    }
                });
                break;
            case 8: // 耗材检查及更换工作
                mLayout2.setVisibility(View.GONE);
                mTextView1.setText(R.string.operation_success);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_ok_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        OperationDialog.this.dismiss();
                        m_FatherActivity.stepEightStatus = true;
                        equipmentDao.updateStatus(m_TaskDetailId, step, "1");
                        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
                        m_FatherActivity.setOperationResult(true, step);
                        AppContext.showToastShort("耗材检查成功");
                        //Api.MaterialChecking(user.getKeyStr(), Constants.ATM_TASK_RESULT_SUCCESS, m_TaskDetailId, mHandler);
                    }
                });
                break;
            case 9: // 布防
                mTextView1.setText(R.string.operation_success);
                mTextView2.setText(R.string.operation_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_ok_selector);
                mImageView2.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        OperationDialog.this.dismiss();
                        m_FatherActivity.stepNineStatus = true;
                        equipmentDao.updateStatus(m_TaskDetailId, step, "1");
                        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
                        m_FatherActivity.setOperationResult(true, step);
                        AppContext.showToastShort("布防操作完成");
                        //try {
                        //    Api.SecurityDefence(user.getKeyStr(), null, Constants.ATM_TASK_RESULT_SUCCESS, m_TaskDetailId, mHandler);
                        //} catch (FileNotFoundException e) {
                        //    // TODO Auto-generated catch block
                        //    e.printStackTrace();
                        //}

                    }
                });
                mLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;
            case 10:// 重控物品检查
                mLayout2.setVisibility(View.GONE);
                mTextView1.setText(R.string.operation_success);
                //mTextView2.setText(R.string.operation_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_ok_selector);
                //mImageView2.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        OperationDialog.this.dismiss();
                        m_FatherActivity.stepTenStatus = true;
                        equipmentDao.updateStatus(m_TaskDetailId, step, "1");
                        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
                        m_FatherActivity.setOperationResult(true, step);
                        AppContext.showToastShort("重控物品检查完成");
                        //try {
                        //    Api.TechDefence(user.getKeyStr(), null, Constants.ATM_TASK_RESULT_SUCCESS, m_TaskDetailId, mHandler);
                        //} catch (FileNotFoundException e) {
                        //    // TODO Auto-generated catch block
                        //    e.printStackTrace();
                        //}
                    }
                });
                //mLayout2.setOnClickListener(new View.OnClickListener() {
                //    @Override
                //    public void onClick(View arg0) {
                //        opnDetailImageForResult(context, step, requestCode);
                //    }
                //});
                break;
            case 11: // 卫生
                mLayout2.setVisibility(View.GONE);
                mTextView1.setText(R.string.quick_option_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;
            case 12: // 物业巡检
                mTextView1.setText(R.string.operation_success);
                mTextView2.setText(R.string.operation_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_ok_selector);
                mImageView2.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        OperationDialog.this.dismiss();
                        m_FatherActivity.stepTwelveStatus = true;
                        equipmentDao.updateStatus(m_TaskDetailId, step, "1");
                        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
                        m_FatherActivity.setOperationResult(true, step);
                        AppContext.showToastShort("物业巡检完成");
                        //try {
                        //    Api.CheckAgain(user.getKeyStr(), null, Constants.ATM_TASK_RESULT_SUCCESS, m_TaskDetailId, mHandler);
                        //} catch (FileNotFoundException e) {
                        //    // TODO Auto-generated catch block
                        //    e.printStackTrace();
                        //}
                    }
                });
                mLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;
            case 13: // 自拍
                mLayout2.setVisibility(View.GONE);
                mTextView1.setText(R.string.quick_option_photo);
                mImageView1.setBackgroundResource(R.drawable.day_quickoption_icon_photo_selector);
                mLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        opnDetailImageForResult(context, step, requestCode);
                    }
                });
                break;
            default:
                break;
        }

        mClose = (ImageView) contentView.findViewById(R.id.iv_close);

        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.quick_option_close);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        mClose.startAnimation(operatingAnim);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                OperationDialog.this.dismiss();
                return true;
            }
        });
        super.setContentView(contentView);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    //	private void opnDetailImage(final int step) {
    //		Bundle bundle = new Bundle();
    //		bundle.putInt(ACTION_TYPE, ACTION_TYPE_PHOTO);
    //		bundle.putInt(STEP, step);
    //		bundle.putLong("TaskDetailId", m_TaskDetailId);
    //		UIHelper.showOperationImageActivity(getContext(), bundle);
    //	}

    private void opnDetailImageForResult(Context context, final int step, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putInt(ACTION_TYPE, ACTION_TYPE_PHOTO);
        bundle.putInt(STEP, step);
        bundle.putLong("TaskDetailId", m_TaskDetailId);
        UIHelper.showOperationImageActivityForResult(context, bundle, requestCode);
    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    AppContext.showToast(ajaxMsg.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            arg4.printStackTrace();
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            arg4.printStackTrace();
        }
    };

    private final AsyncHttpResponseHandler mPasswordHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    lastTime = System.currentTimeMillis();
                    String data=String.valueOf(ajaxMsg.getDatas());
                    m_FatherActivity.showDynamicPassword(3, data);
                    AppContext.getInstance().setProperty(String.valueOf(m_TaskDetailId), data);
                } else {
                    AppContext.showToastShort(ajaxMsg.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            arg4.printStackTrace();
            String dynamicCode=AppContext.getInstance().getProperty(String.valueOf(m_TaskDetailId));
            m_FatherActivity.showDynamicPassword(3, dynamicCode);
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            arg4.printStackTrace();
            String dynamicCode=AppContext.getInstance().getProperty(String.valueOf(m_TaskDetailId));
            m_FatherActivity.showDynamicPassword(3, dynamicCode);
        }
    };
}