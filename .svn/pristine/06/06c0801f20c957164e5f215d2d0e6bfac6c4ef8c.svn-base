package com.jintoufs.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.base.BaseActivity;

public class SettingActivity extends BaseActivity {
	private EditText m_txtBatchReadPower;
	private EditText m_txtSingleReadPower;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		m_txtBatchReadPower = (EditText) findViewById(R.id.txtBatchReadPower);
		m_txtSingleReadPower = (EditText) findViewById(R.id.txtSingleReadPower);
	}

	protected void onResume() {
		super.onResume();
		String readPower=AppContext.get("batchReadPower","27");
		m_txtBatchReadPower.setText(readPower);
		String writePower=AppContext.get("singleReadPower","15");
		m_txtSingleReadPower.setText(writePower);
	}

	public void set(View view) {
		String batchReadPower = m_txtBatchReadPower.getText().toString();
		String singleReadPower = m_txtSingleReadPower.getText().toString();
		
		int nBatchReadPower=0,nSingleReadPower=0;
		
		try
		{
			nBatchReadPower=Integer.parseInt(batchReadPower);
			nSingleReadPower=Integer.parseInt(singleReadPower);
		}catch(Exception e){
			
			AppContext.showToast("设置失败：请输入正确的数字");
			return;
		}
		
		if(nBatchReadPower<=0 || nBatchReadPower>33){
			AppContext.showToast("设置失败：功率必须在0到33之间");
			return;
		}
		
		if(nSingleReadPower<=0 || nSingleReadPower>33){
			AppContext.showToast("设置失败：功率必须在0到33之间");
			return;
		}
		
		AppContext.set("batchReadPower", batchReadPower);
		AppContext.set("singleReadPower", singleReadPower);
		
		finish();
	}
}
