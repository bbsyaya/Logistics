package com.jintoufs.logstics.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.entity.Constants;

public class SettingActivity extends BaseActivity {
	private EditText mReadPowerEt;
	private EditText mWritePowerEt;
	private EditText mStoreWifiEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mReadPowerEt = (EditText) findViewById(R.id.readPower_Et);
		mWritePowerEt = (EditText) findViewById(R.id.writePower_Et);
		mStoreWifiEt = (EditText) findViewById(R.id.store_wifi_et);
	}

	protected void onResume() {
		super.onResume();
		String readPower=AppContext.get(Constants.BATCH_READ_POWER,"27");
		mReadPowerEt.setText(readPower);
		String writePower=AppContext.get(Constants.SINGLE_READ_POWER,"15");
		mWritePowerEt.setText(writePower);
		String storeWifi=AppContext.get(Constants.STORE_WIFI,"");
		if(!"".endsWith(storeWifi)){
			mStoreWifiEt.setText(storeWifi);
		}
	}

	public void set(View view) {
		String readPower = mReadPowerEt.getText().toString();
		String writePower = mWritePowerEt.getText().toString();
		String storeWifi = mStoreWifiEt.getText().toString();
		AppContext.set(Constants.BATCH_READ_POWER, readPower);
		AppContext.set(Constants.SINGLE_READ_POWER, writePower);
		AppContext.set(Constants.STORE_WIFI, storeWifi);
		AppContext.showToast("保存成功");
	}
}
