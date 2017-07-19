package com.jintoufs.activites;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
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
import com.jintoufs.utils.StringUtils;
import com.jintoufs.utils.TDevice;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UpdatePwdActivity extends BaseActivity {
	private EditText mOldPassword;
	private EditText mNewPassword1;
	private EditText mNewPassword2;
	private AppContext appContext;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		setContentView(R.layout.update_pwd);
		mOldPassword = (EditText) findViewById(R.id.oldPassword);
		mNewPassword1 = (EditText) findViewById(R.id.newPassword);
		mNewPassword2 = (EditText) findViewById(R.id.confirmPassword);
	}

	protected void onResume() {
		super.onResume();
	}

	public void commitPwd(View view) {
		if (!prepareForLogin()) {
			return;
		}
		showWaitDialog();
		String password = mNewPassword1.getText().toString();
		Api.updatePwd(user, password, mHandler);
	}

	private boolean prepareForLogin() {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_no_internet);
			return false;
		}
		String oldPassword = mOldPassword.getText().toString();
		if (StringUtils.isEmpty(oldPassword)) {
			AppContext.showToastShort(R.string.tip_please_input_password);
			mOldPassword.requestFocus();
			return false;
		}
		String pwd = mNewPassword1.getText().toString();
		if (StringUtils.isEmpty(pwd)) {
			AppContext.showToastShort(R.string.tip_please_input_password);
			mNewPassword1.requestFocus();
			return false;
		} else if (pwd.length() < 6) {
			AppContext.showToastShort(R.string.tip_please_input_password_less6);
			mNewPassword1.requestFocus();
			return false;
		} else if (pwd.length() > 12) {
			AppContext.showToastShort(R.string.tip_please_input_password_less6);
			mNewPassword1.requestFocus();
			return false;
		}
		String pwd2 = mNewPassword2.getText().toString();
		if (StringUtils.isEmpty(pwd2)) {
			AppContext.showToastShort(R.string.tip_please_input_password);
			mNewPassword2.requestFocus();
			return false;
		} else if (pwd2.length() < 6) {
			AppContext.showToastShort(R.string.tip_please_input_password_less6);
			mNewPassword2.requestFocus();
			return false;
		} else if (pwd2.length() > 12) {
			AppContext.showToastShort(R.string.tip_please_input_password_less6);
			mNewPassword2.requestFocus();
			return false;
		}

		if (!pwd2.equals(pwd)) {
			AppContext.showToastShort(R.string.tip_password_invalid);
			mNewPassword2.requestFocus();
			return false;
		}
		return true;
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				hideWaitDialog();
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					User user = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), User.class);
					if (user != null && user.getId() > 0) {
						// 保存登录信息
						AppContext.getInstance().saveUserInfo(user);
						hideWaitDialog();
						Intent intent = new Intent(UpdatePwdActivity.this, MainActivity.class);
						startActivity(intent);
						AppContext.showToast("修改成功");
						UpdatePwdActivity.this.finish();
					}
				} else {
					AppContext.showToast(getResources().getString(R.string.tip_login_error_no_user));
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
			AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

}
