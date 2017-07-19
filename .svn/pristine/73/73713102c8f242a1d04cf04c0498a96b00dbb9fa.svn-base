package com.jintoufs.activites;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jintoufs.AppConfig;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.ApiHttpClient;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.entity.User;
import com.jintoufs.utils.AjaxMsg;
import com.jintoufs.utils.Exit;
import com.jintoufs.utils.StringUtils;
import com.jintoufs.utils.TDevice;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginActivity extends BaseActivity {
	private EditText mEtUserName;

	private EditText mEtPassword;

	private String mUserName;
	private String mPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mEtUserName = (EditText) findViewById(R.id.login_user_edit);
		mEtPassword = (EditText) findViewById(R.id.login_passwd_edit);
	}

	public void login(View v) {
		handleLogin();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
			Exit.exitBy2Click(this);
		}
		return false;
	}

	private void handleLogin() {

		if (!prepareForLogin()) {
			return;
		}

		mUserName = mEtUserName.getText().toString();
		mPassword = mEtPassword.getText().toString();

		showWaitDialog(R.string.progress_login);
		Api.login(mUserName, mPassword, mHandler);
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

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
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					} else {
						AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
						AppContext.getInstance().cleanLoginInfo();
						
					}
				} else {
					AppContext.getInstance().cleanLoginInfo();
					AppContext.showToast(getResources().getString(R.string.tip_login_error_no_user));
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
			AppContext.showToast(getResources().getString(R.string.tip_login_error_for_network) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_login_error_for_network) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

	private boolean prepareForLogin() {
		if (!TDevice.hasInternet()) {
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

}
