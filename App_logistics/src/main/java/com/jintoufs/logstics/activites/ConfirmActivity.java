package com.jintoufs.logstics.activites;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.ApiHttpClient;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Terminal;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.entity.UserDocking;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.jintoufs.logstics.widget.AvatarView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 在终端弹出确认身份界面
 * 
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
@SuppressLint("NewApi")
public class ConfirmActivity extends BaseActivity {

	private AppContext appContext;
	private User user;
	private TextView mHeaderTitle;
	private AvatarView mUserKey;
	private AvatarView mPassword;
	private AvatarView mSiteUserKey;
	private TextView mKeyName;
	private TextView mPasswordName;
	private TextView mSiteUsername;
	private TextView mStatusCode;
	private LinearLayout siteUserLayout;
	private int id;
	private String name;
	private Terminal mSite;
	private long detailId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mUserKey = (AvatarView) findViewById(R.id.user_key);
		mPassword = (AvatarView) findViewById(R.id.user_password);
		mKeyName = (TextView) findViewById(R.id.user_key_name);
		mPasswordName = (TextView) findViewById(R.id.user_password_name);
		mSiteUserKey=(AvatarView) findViewById(R.id.site_user_key);
		mSiteUsername=(TextView) findViewById(R.id.site_user_name);
		mStatusCode=(TextView) findViewById(R.id.status_code);
		siteUserLayout=(LinearLayout) findViewById(R.id.site_user_layout);
		
		Intent intent = getIntent();
		mSite = (Terminal) intent.getSerializableExtra("site");
		id = mSite.getId();
		detailId = mSite.getDetailId();
		name = mSite.getName();
		mHeaderTitle.setText(name + "身份确认");
		if (TextUtils.isEmpty(mSite.getIdCode())) {
			mStatusCode.setVisibility(View.GONE);
		}else{
			mStatusCode.setVisibility(View.VISIBLE);
			mStatusCode.setText("身份码："+mSite.getIdCode());
		}
		
		Api.getEscortUserList(user.getKeyStr(),detailId, mHandler);
		Api.getSiteUserList(user.getKeyStr(), id, mSiteHandler);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					List<User> list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<User>>() {
					}.getType());
					if (list.size() > 0) {
						mKeyName.setText(list.get(0).getRealName());
						if (!"".equals(list.get(0).getUserPhoto())) {
//							mUserKey.setAvatarUrl(Constants.ftpUrl + list.get(0).getUserPhoto());
							mUserKey.setAvatarUrl(ApiHttpClient.getNetworkUrl() + list.get(0).getUserPhoto());
						}
					}
					if (list.size() > 1) {
						mPasswordName.setText(list.get(1).getRealName());
						if (!"".equals(list.get(1).getUserPhoto())) {
//							mPassword.setAvatarUrl(Constants.ftpUrl + list.get(1).getUserPhoto());
							mPassword.setAvatarUrl(ApiHttpClient.getNetworkUrl() + list.get(1).getUserPhoto());
						}
					}
					hideWaitDialog();
				} else {
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

	private final AsyncHttpResponseHandler mSiteHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					List<UserDocking> list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<UserDocking>>() {
					}.getType());
					if (list.size() > 0) {
						for (UserDocking user : list) {
							siteUserLayout.setVisibility(View.VISIBLE);
							mSiteUsername.setText(user.getRealName());
							mSiteUserKey.setAvatarUrl(user.getAvatar());
						}
					}else{
						siteUserLayout.setVisibility(View.GONE);
					}
					hideWaitDialog();
				} else {
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

	public void verify(View view) {
		
		int terminalId = mSite.getId();
		Double longitude = mSite.getLongitude();
		Double latitude = mSite.getLatitude();
		int deliverType = mSite.getDeliverType();
		Intent intent = null;
		if (Constants.TASK_TYPE_DEVELIVER==deliverType) {
			intent = new Intent(ConfirmActivity.this, DeliverSiteActivity.class);
		} else if (Constants.TASK_TYPE_RECEIVE==deliverType) {
			intent = new Intent(ConfirmActivity.this, ReceiveSiteActivity.class);
		}
		if (intent != null) {
			intent.putExtra("deliverType", deliverType);
			intent.putExtra("id", terminalId);
			intent.putExtra("detailId", mSite.getDetailId());
			intent.putExtra("code", mSite.getCode());
			intent.putExtra("name", mSite.getName());
			intent.putExtra("type", mSite.getType());
			intent.putExtra("longitude", longitude);
			intent.putExtra("latitude", latitude);
			if (mSite.getVerifyCode() != null) {
				intent.putExtra("verifyCode", mSite.getVerifyCode());
			}
			
			startActivity(intent);
			ConfirmActivity.this.finish();
		}
	}

}
