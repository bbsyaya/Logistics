package com.jintoufs.logstics.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.db.UserDao;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

public class AppStartActivity extends BaseActivity {
	AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplicationContext();
		setContentView(R.layout.appstart);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(appContext.isLogin()){
					Intent intent = new Intent(AppStartActivity.this, MainActivity .class);
					startActivity(intent);
					AppStartActivity.this.finish();
				}else{
					Intent intent = new Intent(AppStartActivity.this, LoginActivity.class);
					startActivity(intent);
					AppStartActivity.this.finish();
				}
			}
		}, 1000);
	}


}
