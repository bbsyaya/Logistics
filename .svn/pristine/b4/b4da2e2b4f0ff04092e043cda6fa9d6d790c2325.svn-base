package com.jintoufs.activites;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseRfidActivity;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.Constants;
import com.jintoufs.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.reader.server.ReaderHelper;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class IdentifyCashboxActivity extends BaseRfidActivity {
	private AppContext appContext;
	private long lastPressTime = 0;
	private Button m_btnScan;
	private TextView m_lblCashboxCode;
	private TextView m_lblCashboxType;
	private TextView m_lblHolder;
	private TextView m_lblBankName;
	private TextView m_lblSiteName;
	private TextView m_lblStoreName;
	private TextView m_lblAreaName;
	private TextView m_lblShelfName;
	private TextView m_lblCellName;
	private TextView m_lblVendor;
	private TextView m_lblBackTime;

	private boolean m_bFirstSetPower = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identify_cashbox);
		appContext = (AppContext) getApplication();

		m_btnScan = BindControl(R.id.btnScan);
		m_lblCashboxCode = BindControl(R.id.lblCashboxCode);
		m_lblCashboxType = BindControl(R.id.lblCashboxType);
		m_lblHolder = BindControl(R.id.lblHolder);
		m_lblBankName = BindControl(R.id.lblBankName);
		m_lblSiteName = BindControl(R.id.lblSiteName);
		m_lblStoreName = BindControl(R.id.lblStoreName);
		m_lblAreaName = BindControl(R.id.lblAreaName);
		m_lblShelfName = BindControl(R.id.lblShelfName);
		m_lblCellName = BindControl(R.id.lblCellName);
		m_lblVendor = BindControl(R.id.lblVendor);
		m_lblBackTime = BindControl(R.id.lblBackTime);

		ClearText(m_lblCashboxCode, m_lblCashboxType, m_lblHolder, m_lblBankName, m_lblSiteName, m_lblStoreName, m_lblAreaName, m_lblShelfName, m_lblCellName, m_lblVendor, m_lblBackTime);
		byte outPower = Byte.parseByte(AppContext.get(Constants.BATCH_READ_POWER, "28"));
		set(outPower);
	}

	private void ClearText(TextView... lbl) {
		for (TextView tv : lbl) {
			if (tv != null) {
				tv.setText("");
			}
		}
	}

	private void SetTextByCashbox(Cashbox cashbox) {
		m_lblCashboxCode.setText(cashbox.getCashBoxCode());
		m_lblCashboxType.setText(cashbox.getType());
		m_lblHolder.setText(cashbox.getHolder());
		m_lblBankName.setText(cashbox.getBankName());
		m_lblSiteName.setText(cashbox.getSiteName());
		m_lblStoreName.setText(cashbox.getStoreName());
		m_lblAreaName.setText(cashbox.getStoreAreaName());
		m_lblShelfName.setText(cashbox.getShelfName());
		m_lblCellName.setText(cashbox.getCellCode());
		m_lblVendor.setText(cashbox.getVendor());
		m_lblBackTime.setText(cashbox.getBackTime());
	}

	protected void onResume() {
		super.onResume();
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				hideWaitDialog();
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					Cashbox cashbox = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), Cashbox.class);

					if (cashbox != null) {
						SetTextByCashbox(cashbox);
					} else {
						AppContext.showToast("读取失败！");
					}
				} else {
					AppContext.showToast(ajaxMsg.getMessage());
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

	public void btnScan_onClick(View v) {

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

		String str = "";
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

			break;
		}

		if (!(foundBoxCode == null || foundBoxCode.equals(""))) {
			btnScan_onClick(null);

			m_lblCashboxCode.setText(foundBoxCode);

			Api.readBox(str, mHandler);
		}
	}

	@Override
	public void stopInventory() {
		mRefreshHandler.removeCallbacks(mRefreshRunnable);
		super.stopInventory();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (event.getDownTime() - lastPressTime < 300) {
			lastPressTime = event.getDownTime();
			return true;
		}

		lastPressTime = event.getDownTime();

		if (keyCode == KeyEvent.KEYCODE_FOCUS && event.getRepeatCount() == 0) {
			btnScan_onClick(null);
			return true;
		}

		return false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode){
			case KeyEvent.KEYCODE_BACK:
				this.finish();
				break;
			case KeyEvent.ACTION_DOWN:
				btnScan_onClick(m_btnScan);
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
