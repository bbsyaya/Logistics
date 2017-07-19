package com.jintoufs.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.activites.SettingActivity;
import com.jintoufs.activites.UpdatePwdActivity;
import com.jintoufs.entity.Constants;
import com.jintoufs.entity.User;
import com.jintoufs.utils.TDevice;
import com.jintoufs.utils.UpdateManager;
import com.jintoufs.widget.AvatarView;

public class Tab3Fragment extends Fragment {
	private AvatarView mIvAvatarView;
	private TextView userNameTv;
	private TextView nickNameTv;
	private TextView mSettingVersionTv;
	private RelativeLayout mLay_checkUpdate;
	private RelativeLayout mLay_UpdatePwd;
	private RelativeLayout mLay_Setting;
	private Button mExitBtn;
	private User user;
	AppContext appContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getActivity().getApplicationContext();
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout view1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab3, container, false);
		mIvAvatarView = (AvatarView) view1.findViewById(R.id.iv_avatar);
		userNameTv = (TextView) view1.findViewById(R.id.tabUserName);
		nickNameTv = (TextView) view1.findViewById(R.id.tabNickName);
		mSettingVersionTv = (TextView) view1.findViewById(R.id.setting_version);
		mLay_checkUpdate = (RelativeLayout) view1.findViewById(R.id.lay_checkUpdate);
		mLay_UpdatePwd = (RelativeLayout) view1.findViewById(R.id.lay_updatePwd);
		mLay_Setting = (RelativeLayout) view1.findViewById(R.id.lay_setting);
		mExitBtn = (Button) view1.findViewById(R.id.btn_exit);
		String version = TDevice.getVersionName();
		mSettingVersionTv.setText(version);
		mLay_checkUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkUpdate();
			}
		});
		mLay_UpdatePwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updatePwd();
			}
		});
		mLay_Setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				startActivity(intent);
			}
		});
		mExitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AppContext.getInstance().cleanLoginInfo();
				getActivity().finish();
				System.exit(0);
			}
		});
		return view1;
	}

	public void getUserInfo() {
		user = appContext.getLoginUser();
		if (user != null) {
			userNameTv.setText(user.getUserName());
			nickNameTv.setText(user.getRealName());
			mIvAvatarView.setAvatarUrl(Constants.ftpUrl + user.getUserPhoto());
		}
	}

	public void updatePwd() {
		Intent intent = new Intent(getActivity(), UpdatePwdActivity.class);
		startActivity(intent);
	}

	public void checkUpdate() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				new UpdateManager(Tab3Fragment.this.getActivity(), false).checkUpdate();
			}
		}, 0);
	}

	@Override
	public void onViewCreated(View view,  Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getUserInfo();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}