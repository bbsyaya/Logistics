package com.jintoufs.activites;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseRfidActivity;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.Constants;
import com.jintoufs.entity.User;
import com.jintoufs.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.reader.server.ReaderHelper;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 准备钞箱-钞箱界面
 * */
public class PrepareCashBoxActivity extends BaseRfidActivity {
	private AppContext appContext;
	private long lastPressTime = 0;
	private Button m_btnScan;
	private TextView mTypeTv;
	private TextView mCodeTv;
	private TextView mAreaTv;
	private TextView mShelfCellTv;
	private User user;
	private Cashbox cashBox;
	private long storeTaskId;
	private LinearLayout linearLayout;
	private TextView mPrepareTip;
	private TextView mheaderTitle;
	private TextView mPositionTv;
	private boolean isRead=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepare_cashbox);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		m_btnScan = BindControl(R.id.btn_scan);
		linearLayout = BindControl(R.id.id_state);
		mPrepareTip = BindControl(R.id.prepare_tip);
		mTypeTv = BindControl(R.id.type);
		mCodeTv = BindControl(R.id.cashboxCode);
		mAreaTv = BindControl(R.id.areaName);
		mPositionTv = BindControl(R.id.position);
		mShelfCellTv = BindControl(R.id.shelfCell);
		mheaderTitle = BindControl(R.id.header_title);
		Intent intent = getIntent();
		cashBox = (Cashbox) intent.getSerializableExtra("cashBox");
		storeTaskId = intent.getLongExtra("storeTaskId", 0);
		setTextByCashbox();
		mheaderTitle.setText("请准备：" + cashBox.getCashBoxCode());
	}

	private void setTextByCashbox() {
		mCodeTv.setText(cashBox.getCashBoxCode());
		mTypeTv.setText(cashBox.getType());
		mAreaTv.setText(cashBox.getPosition());
		mShelfCellTv.setText(cashBox.getCellCode());
		mPositionTv.setText(cashBox.getPreparePosition());
		if (cashBox.getStatus() == 1) {
			mPrepareTip.setText("已完成准备");
			linearLayout.setVisibility(View.VISIBLE);
		}
	}

	protected void onResume() {
		super.onResume();
	}

	public void scan(View v) {
		accompaniment.start();
		if (m_btnScan.getText().equals(getString(R.string.btn_read))) {
			m_btnScan.setText(R.string.btn_stop);
			read();
		} else if (m_btnScan.getText().equals(getString(R.string.btn_stop))) {
			stopInventory();
			m_btnScan.setText(R.string.btn_read);
		}
	}

	private Handler mRefreshHandler = new Handler();
	private Runnable mRefreshRunnable = new Runnable() {
		public void run() {

			if (mReader == null) {
				return;
			}

			byte btWorkAntenna = m_curInventoryBuffer.lAntenna.get(m_curInventoryBuffer.nIndexAntenna);
			if (btWorkAntenna < 0)
				btWorkAntenna = 1;

			mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);

			refreshList();
			mRefreshHandler.postDelayed(this, 1500);
		}
	};

	private void read() {
		byte outPower = Byte.parseByte(AppContext.get("singleReadPower", "15"));
		set(outPower);
		Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
		sendBroadcast(intent);// 传递过去
		mRefreshHandler.postDelayed(mRefreshRunnable, 500);
	}

	private void refreshList() {

		String str;
		String foundBoxCode = "";

		for (InventoryTagMap inventoryTagMap : m_curInventoryBuffer.lsTagList) {
			str = inventoryTagMap.strEPC;
			str = str.replace("f", "");
			str = str.replace("F", "");

			if (!str.startsWith(Constants.RFID_START_WITH_STR)) {
				continue;
			}
			if (str.length() < 10) {
				continue;
			}

			foundBoxCode = str.substring(4, 10);
			if (cashBox.getCashBoxCode().equals(foundBoxCode)) {
				scan(null);
				//accompaniment.start();
				isRead=true;
				mPrepareTip.setText("已扫描到，请提交");
				linearLayout.setVisibility(View.VISIBLE);
			}
			break;
		}


	}

	@Override
	public void stopInventory() {
		mRefreshHandler.removeCallbacks(mRefreshRunnable);
		super.stopInventory();
	}

	public void submit(View view) {
		if (isRead){
			showWaitDialog(R.string.loading);
			Api.prepareCashbox(user, storeTaskId, cashBox.getCashBoxCode(), mHandler);
		}else {
			 AppContext.showToastShort("请先完成读取操作");
		}

	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				hideWaitDialog();
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					mPrepareTip.setText("已完成准备");
					finish();
					AppContext.showToast(getResources().getString(R.string.operation_success));
				} else {
					AppContext.showToast(getResources().getString(R.string.operation_fail));
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
			AppContext.showToast(getResources().getString(R.string.tip_no_internet) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode){
			case KeyEvent.KEYCODE_BACK:
				this.finish();
				break;
			case KeyEvent.ACTION_DOWN:
				scan(m_btnScan);
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getDownTime() - lastPressTime < 300) {
			lastPressTime = event.getDownTime();
			return true;
		}

		lastPressTime = event.getDownTime();

		if (keyCode == KeyEvent.KEYCODE_FOCUS && event.getRepeatCount() == 0) {
			scan(null);
			return true;
		}

		return false;
	}
}
