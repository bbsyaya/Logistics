package com.jintoufs.logstics.activites;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fntech.m10a.gpio.M10A_GPIO;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.utils.FingerUtil;
import com.jintoufs.logstics.utils.Result;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class UpdateFingerActivity extends BaseActivity {
    private AppContext appContext;
    private User user;
    private TextView tv_finger_info;
    private Button btn_register, btn_save;
    private String fingerprint;//保存指纹信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_finger);

        init();
        openPDA();
    }

    private void init() {
        appContext = (AppContext) getApplication();
        user = appContext.getLoginUser();
        tv_finger_info = (TextView) findViewById(R.id.finger_register_info);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_register.setOnClickListener(listener);
        btn_save.setOnClickListener(listener);
    }

    /**
     * 开启指纹扫描
     */
    private void openPDA() {
        M10A_GPIO.fingerPowerOn();
        M10A_GPIO.fingerSwitchSerialPort();
        new Thread(new Runnable() {

            @Override
            public void run() {
                SystemClock.sleep(1000);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        btn_register.setEnabled(true);
                        btn_save.setEnabled(true);
                        FingerUtil.open("/dev/ttyHSL0", 57600);

                    }
                });

            }
        }).start();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (btn_register == v) {
                registerFinger();
            } else if (btn_save == v) {
                save();
            }
        }
    };

    /**
     * 上传指纹
     */
    private void save() {
        if(!TextUtils.isEmpty(fingerprint)){
            Api.updateFingerprint(user.getId(), fingerprint, mHandler);
        }else {
            AppContext.showToastShort("录入指纹不能为空");
        }
    }

    /**
     * 录入指纹
     */
    private void registerFinger() {
        Result ret = FingerUtil.collectFingerTemplet();
        if (ret.code == 0) {
            fingerprint = ret.value;
            tv_finger_info.setText(ret.value);
        }else {
            AppContext.showToastShort(ret.value);
        }

    }

    private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                hideWaitDialog();
                AppContext.showToastShort("指纹上传成功");
                UpdateFingerActivity.this.finish();
            } catch (Exception e) {
                hideWaitDialog();
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }
    };

    @Override
    protected void onDestroy() {
        FingerUtil.close();
        super.onDestroy();
    }
}
