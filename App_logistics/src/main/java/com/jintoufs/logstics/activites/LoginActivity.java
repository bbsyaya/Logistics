package com.jintoufs.logstics.activites;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fntech.m10a.gpio.M10A_GPIO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppConfig;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.ApiHttpClient;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.db.UserDao;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.jintoufs.logstics.utils.Exit;
import com.jintoufs.logstics.utils.FingerUtil;
import com.jintoufs.logstics.utils.Result;
import com.jintoufs.logstics.utils.StringUtils;
import com.jintoufs.logstics.utils.TDevice;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

import static android.R.attr.button;
import static com.jintoufs.logstics.base.BaseRfidActivity.m_curInventoryBuffer;
import static com.jintoufs.logstics.base.BaseRfidActivity.m_curReaderSetting;

public class LoginActivity extends BaseActivity {
    private EditText mEtUserName;
    private EditText mEtPassword;

    private String mUserName;
    private String mPassword;
    private EditText ipText;
    private View ipView;
    private UserDao userDao;
    private String fea1 = "";//保存读取指纹特征

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEtUserName = (EditText) findViewById(R.id.login_user_edit);
        mEtPassword = (EditText) findViewById(R.id.login_passwd_edit);
        userDao = new UserDao(this);

        String wifi = AppContext.get(Constants.STORE_WIFI, "");
        if (!TextUtils.isEmpty(wifi)){
            Api.getUserList(fingerLoginHandler);
        }
        fingerLogin();
        openPDA();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(runnable, 1000);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 121:
                    showMainActivity();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Runnable runnable=new Runnable(){
        @Override
        public void run() {
            fingerLogin();
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            mHandler.postDelayed(this, 3000);
        }
    };

    /**
     * 密码登录
     *
     * @param v
     */
    public void login(View v) {
        handleLogin();
    }

    /**
     * 指纹登录
     */
    private void fingerLogin() {
        Result ret = FingerUtil.collectFingerTemplet();
        if (ret.code == 0) {
            fea1 = ret.value;
        }
        compareFingerPrint();
    }

    /**
     * 开启指纹扫描
     */
    private void openPDA() {
        M10A_GPIO.fingerPowerOn();
        M10A_GPIO.fingerSwitchSerialPort();
        new Thread(new Runnable(){

        @Override
        public void run() {
        	//SystemClock.sleep(1000);
        	runOnUiThread(new Runnable(){

        		@Override
        		public void run() {
        			FingerUtil.open("/dev/ttyHSL0",57600);

        		}});
        }}).start();
        //FingerUtil.open("/dev/ttyHSL0", 57600);
    }

    /**
     * 指纹验证
     */
    public void compareFingerPrint() {
        String fea2;
        List<User> userList = userDao.queryUser();
        for (User user : userList) {
            if (!TextUtils.isEmpty(user.getFingerprint())) {
                fea2 = user.getFingerprint();

                if (fea1.length() != fea2.length()) {
                    continue;
                }
                Result ret = FingerUtil.compareFinger(fea1, fea2);
                if (ret.code == 0) {
                    AppContext.getInstance().saveUserInfo(user);
                    Message message = Message.obtain();
                    message.what = 121;
                    mHandler.sendMessage(message);
                    break;
                }
            }
        }
    }

    private void handleLogin() {
        if (!prepareForLogin()) {
            return;
        }
        mUserName = mEtUserName.getText().toString();
        mPassword = mEtPassword.getText().toString();

        showWaitDialog(R.string.progress_login);
        Api.login(mUserName, mPassword, loginHandler);
    }

    /**
     * 跳转主界面
     */
    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private boolean prepareForLogin() {
        if (!isNetworkConnected(this)) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return false;
        }
        String uName = mEtUserName.getText().toString();
        if (StringUtils.isEmpty(uName)) {
            AppContext.showToastShort(R.string.tip_please_input_username);
            mEtUserName.requestFocus();
            return false;
        }
        String pwd = mEtPassword.getText().toString();
        if (StringUtils.isEmpty(pwd)) {
            AppContext.showToastShort(R.string.tip_please_input_password);
            mEtPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * 
		 * add()方法的四个参数，依次是：
		 * 
		 * 1、组别，如果不分组的话就写Menu.NONE,
		 * 
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
		 * 
		 * 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 
		 * 4、文本，菜单的显示文本
		 */
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "配置大库wifi名称").setIcon(android.R.drawable.ic_menu_set_as);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                buildTextDialog("请设置大库wifi名称");
                break;
        }

        return false;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Exit.exitBy2Click(this);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        FingerUtil.close();
        mHandler.removeCallbacks(runnable);
        super.onDestroy();
    }

    // 构建通用的文本输入对话框
    public void buildTextDialog(String title) {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        ipView = layoutInflater.inflate(R.layout.wifi_setting, null);
        ipText = (EditText) ipView.findViewById(R.id.ip_tv);
        String wifi = AppContext.get(Constants.STORE_WIFI, "");
        if (TextUtils.isEmpty(wifi)){
            Api.getUserList(fingerLoginHandler);
        }else {
            ipText.setText(wifi);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(ipView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AppContext.set(Constants.STORE_WIFI, ipText.getText().toString());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.show();// EditText 就可以显示在对话框中了
    }


    private final AsyncHttpResponseHandler loginHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                AsyncHttpClient client = ApiHttpClient.getHttpClient();
                HttpContext httpContext = client.getHttpContext();
                CookieStore cookies = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
                if (cookies != null) {
                    String tmpcookies = "";
                    for (Cookie c : cookies.getCookies()) {
                        tmpcookies += (c.getName() + "=" + c.getValue()) + ";";
                    }
                    AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE, tmpcookies);
                    ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext.getInstance()));
                }
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                hideWaitDialog();
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    User user = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), User.class);
                    if (user != null && user.getId() > 0) {
                        // 保存登录信息
                        AppContext.getInstance().saveUserInfo(user);
                        Message message = Message.obtain();

                        message.what = 121;
                        mHandler.sendMessage(message);
                        //showMainActivity();
                    } else {
                        AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
                        AppContext.getInstance().cleanLoginInfo();

                    }
                } else {
                    AppContext.getInstance().cleanLoginInfo();
                    AppContext.showToastShort(getResources().getString(R.string.tip_login_error_no_user));
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
            AppContext.showToastShort(getResources().getString(R.string.tip_login_error_for_network) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }

        @Override
        public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
            arg4.printStackTrace();
            hideWaitDialog();
            AppContext.showToastShort(getResources().getString(R.string.tip_login_error_for_network) + "\n原因:" + arg0 + ":" + arg4.getMessage());
        }
    };

    private final AsyncHttpResponseHandler fingerLoginHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
            try {
                userDao.deleteAll();
                Gson gson = new Gson();
                AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
                if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
                    List<User> userList = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<User>>() {
                    }.getType());

                    if (userList != null) {
                        userDao.add(userList);
                    }
                    hideWaitDialog();
                } else {
                    AppContext.showToastShort(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
                    hideWaitDialog();
                }
            } catch (Exception e) {
                hideWaitDialog();
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

}
