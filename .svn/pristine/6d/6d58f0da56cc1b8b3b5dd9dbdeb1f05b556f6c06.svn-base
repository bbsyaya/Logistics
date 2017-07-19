package com.jintoufs.logstics.activites;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.utils.preDefiniation.AlignType;

//瑞工蓝牙打印机
public class ConnectActivity extends BaseActivity {

	public int state;
	public String PrintName;
	public Button con;
	public Button printBtn;
	public Spinner btName;
	public EditText Adress;
	private ArrayAdapter<String> mAdapter;
	public AppContext context;
	public String error;
	public boolean mBconnect = false;
	ArrayList<String> getbtName = new ArrayList<String>();
	ArrayList<String> getbtNM = new ArrayList<String>();
	ArrayList<String> getbtMax = new ArrayList<String>();
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		// 多个页面之间数据共享
		context = (AppContext) getApplicationContext();
		context.setObject();
		Adress = (EditText) findViewById(R.id.edit_btmax);
		btName = (Spinner) findViewById(R.id.spinner_btname);
		getbtName.clear();
		mAdapter = new ArrayAdapter<String>(ConnectActivity.this, android.R.layout.simple_spinner_item, getbtName);
		btName.setAdapter(mAdapter);

		getbtNM = (ArrayList<String>) context.getObject().CON_GetWirelessDevices(0);
		// 对获得的蓝牙地址和名称进行拆分以逗号进行拆分
		for (int i = 0; i < getbtNM.size(); i++) {
			getbtName.add(getbtNM.get(i).split(",")[0]);
			getbtMax.add(getbtNM.get(i).split(",")[1].substring(0, 17));
		}
		mAdapter = new ArrayAdapter<String>(ConnectActivity.this, android.R.layout.simple_spinner_item, getbtName);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		btName.setAdapter(mAdapter);
		btName.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Adress.setText(getbtMax.get(arg2));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// btName后期要添加监听事件用于连接
		con = (Button) findViewById(R.id.button_btcon);
		printBtn = (Button) findViewById(R.id.print);
		con.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connect(Adress.getText().toString());
			}
		});

		// 自动打印
		for (int j = 0; j < getbtName.size(); j++) {
			if ("RG-MTP58B".equals(getbtName.get(j))) {
				Adress.setText(getbtMax.get(j));
			}
		}
		connect(Adress.getText().toString());
		print(null);
	}

	public void connect(String port) {
		PrintName = "RG-MTP58B";
		try {
			if (mBconnect) {
				context.getObject().CON_CloseDevices(context.getState());
				con.setText(R.string.button_btcon);// "连接"
				mBconnect = false;
			} else {
				state = context.getObject().CON_ConnectDevices(PrintName, port, 200);
				if (state > 0) {
					Toast.makeText(ConnectActivity.this, R.string.mes_consuccess, Toast.LENGTH_SHORT).show();
					mBconnect = true;
					context.setState(state);
					context.setName(PrintName);
					con.setText(R.string.TextView_close);// "关闭"
					printBtn.setEnabled(true);
				} else {
					Toast.makeText(ConnectActivity.this, R.string.mes_confail, Toast.LENGTH_SHORT).show();
					mBconnect = false;
					con.setText(R.string.button_btcon);// "连接"
				}
				printBtn.setEnabled(mBconnect);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	public void print(View view) {
		try {
			// 先取得数据
			Intent intent = this.getIntent();
			ArrayList<String> list = intent.getExtras().getStringArrayList("list");
			String siteName = intent.getExtras().getString("siteName");
			String userA_name = intent.getExtras().getString("userA_name");
			String userB_name = intent.getExtras().getString("userB_name");
			Date date = new Date();
			for(int count=0;count<2;++count)
			{
				context.getObject().CON_PageStart(context.getState(), false, 0, 0);
				context.getObject().ASCII_CtrlAlignType(context.getState(), AlignType.AT_CENTER.getValue());
				context.getObject().ASCII_PrintString(context.getState(), 0, 1, 0, 0, 0, "欢迎使用上海弘泰现金外包服务平台  ", "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlAlignType(context.getState(), AlignType.AT_LEFT.getValue());
				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "网点名称：" + siteName, "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);

				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "交接人：" + userA_name + "、" + userB_name, "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);

				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "交接时间：" + sdf.format(date), "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);

				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "尾箱编号", "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);

				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "----------------------------", "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);

				for (int i = 0; i < list.size(); i++) {
					context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, list.get(i), "gb2312");
					context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				}

				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "----------------------------", "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);

				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "合计：" + list.size() + "个钞箱", "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);

				// @param type 0交箱 1接箱
				context.getObject().ASCII_PrintString(context.getState(), 0, 0, 0, 0, 0, "网点签字：", "gb2312");
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlPrintCRLF(context.getState(), 1);
				context.getObject().ASCII_CtrlReset(context.getState());
				context.getObject().CON_PageEnd(context.getState(), context.getPrintway());
			}
			
			Intent intent2 = null;
			connect(Adress.getText().toString());// 关闭
			this.finish();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("print Error", e.getMessage());
		}
	}
}
