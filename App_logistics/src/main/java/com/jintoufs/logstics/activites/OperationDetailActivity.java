package com.jintoufs.logstics.activites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.db.EquipmentDao;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 处理操作时候发送图片
 *
 * @author kymjs (https://github.com/kymjs)
 */
@SuppressLint("NewApi")
public class OperationDetailActivity extends BaseActivity {

    private AppContext appContext;
    private User user;
    private String m_ImgPath = null;
    private long m_TaskDetailId;
    private int m_Step;
    private EquipmentDao equipmentDao;
    private EquipmentActivity m_FatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_detail);

        appContext = (AppContext) getApplication();
        user = appContext.getLoginUser();
        equipmentDao = new EquipmentDao(this);
        m_FatherActivity=new EquipmentActivity();

        Bundle bundle = getIntent().getBundleExtra("BUNDLE_KEY_ARGS");
        m_TaskDetailId = bundle.getLong("TaskDetailId");
        m_Step = bundle.getInt("step");
    }

    public void send(View view) {
        equipmentDao.updateEndTime(System.currentTimeMillis(), m_TaskDetailId);
        equipmentDao.updateImageUrl(m_ImgPath, m_TaskDetailId, m_Step);
        switch (m_Step){
            case 1://撤防
                m_FatherActivity.stepOneStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "2");
                break;
            case 2://吞没卡检查
                m_FatherActivity.stepTwoStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "2");
                break;
            case 4://检查机器
                m_FatherActivity.stepFourStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "1");
                break;
            case 6://关闭保险箱
                m_FatherActivity.stepSixStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "1");
                break;
            case 9://安防
                m_FatherActivity.stepNineStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "2");
                break;
            case 10://技防
                m_FatherActivity.stepTenStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "2");
                break;
            case 11://卫生
                m_FatherActivity.stepElevenStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "1");
                break;
            case 12://物業巡檢
                m_FatherActivity.stepTwelveStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "2");
                break;
            case 13://自拍存留照
                m_FatherActivity.stepThirteenStatus = true;
                equipmentDao.updateStatus(m_TaskDetailId, m_Step, "1");
                equipmentDao.updateFinishedStatus(Constants.FINISHED_STATUS, m_TaskDetailId);
                break;
        }
        AppContext.showToastShort("图片保存成功");
        setResult(RESULT_OK);
        OperationDetailActivity.this.finish();
    }

    public void SetImgPath(String imgPath) {
        m_ImgPath = imgPath;
    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {

                    hideWaitDialog();
                    AppContext.showToast(ajaxMsg.getMessage());
                    setResult(RESULT_OK);
                    OperationDetailActivity.this.finish();

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
            AppContext.showToast("图片保存成功");
            arg4.printStackTrace();
            hideWaitDialog();
            OperationDetailActivity.this.finish();
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            AppContext.showToast("图片保存成功");
            arg4.printStackTrace();
            hideWaitDialog();
            OperationDetailActivity.this.finish();

        }
    };

}
