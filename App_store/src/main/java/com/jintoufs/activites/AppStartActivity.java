package com.jintoufs.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jintoufs.AppContext;
import com.jintoufs.R;

public class AppStartActivity extends Activity {
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
