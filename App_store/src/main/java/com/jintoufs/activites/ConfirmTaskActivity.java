package com.jintoufs.activites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseRfidActivity;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.Constants;
import com.jintoufs.entity.EnumType.ErrorTypeOfCount;
import com.jintoufs.entity.EnumType.StockType;
import com.jintoufs.entity.User;
import com.jintoufs.fragment.CashboxListFragment;
import com.jintoufs.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.reader.server.ReaderHelper;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 上车前确认所有的钞箱界面
 * 
 * @author YangHao
 */
@SuppressLint("NewApi")
public class ConfirmTaskActivity extends BaseRfidActivity {

	private AppContext appContext;
	private User user;
	private static CashboxListFragment cashboxListFragment = null;
	final FragmentManager fragmentManager = getFragmentManager();
	private TextView mHeaderTitle;
	private Button mButtonRead;
	private List<Cashbox> list;
	private Map<String, Cashbox> kvpCashbox = new HashMap<String, Cashbox>();
	private String taskType;
	private String caption;
	private List<String> cashBoxCodes = new ArrayList<String>();

	private long lastPressTime = 0;
	private int readCount = 0;
	private TextView m_ReadCount;
	private TextView m_TotalCount;

	private int m_KeyPostId;
	private int m_PasswordPostId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		setContentView(R.layout.activity_confirm_task);
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		appContext = (AppContext) getApplication();
		cashboxListFragment = (CashboxListFragment) fragmentManager.findFragmentById(R.id.cashbox_fragment_list);

		mButtonRead = (Button) findViewById(R.id.btn_read);

		m_ReadCount = (TextView) findViewById(R.id.readCount);
		m_TotalCount = (TextView) findViewById(R.id.totalCount);

		taskType = getIntent().getStringExtra("taskType");
		caption = getIntent().getStringExtra("caption");

		m_KeyPostId = getIntent().getIntExtra("keyId", -1);
		m_PasswordPostId = getIntent().getIntExtra("passwordId", -1);

		mHeaderTitle.setText(taskType + "-" + caption);
	}

	
	@Override
	protected void onResume() {
		super.onResume();

		cashboxListFragment.clearItem();
		cashboxListFragment.myadapter.notifyDataSetChanged();
		kvpCashbox.clear();
		readCount = 0;
		m_ReadCount.setText("已读取:0");
		m_TotalCount.setText("总数:0");

		showWaitDialog(R.string.loading);

		if (taskType.equals("出库")) {
			Api.getOutCashboxList(user,m_KeyPostId, m_PasswordPostId, mHandler);
		} else if (taskType.equals("入库")) {
			Api.getInStoreCashboxList(user, m_KeyPostId, m_PasswordPostId, mHandler);
		} else if (taskType.equals("交接")) {
			Api.getInEscortCashboxList(user, m_KeyPostId, m_PasswordPostId, mHandler);
		} else {
			hideWaitDialog();
			return;
		}
	}

	public void read(View view) {
		accompaniment.start();
		if (mButtonRead.getText().equals(getString(R.string.btn_read))) {
			mButtonRead.setText(R.string.btn_stop);
			read();
		} else if (mButtonRead.getText().equals(getString(R.string.btn_stop))) {
			stopInventory();
			mButtonRead.setText(R.string.btn_read);
		}
	}

	public void clear(View view) {
		accompaniment.start();
		stopInventory();
		mButtonRead.setText(R.string.btn_read);

		onResume();
	}

	public void save(View view) {
		accompaniment.start();
		stopInventory();
		mButtonRead.setText(R.string.btn_read);

		cashBoxCodes.clear();

		// boolean bNeedManagerVerify=false;

		boolean bMore = false;
		boolean bLess = false;
		List<Cashbox> lstMoreBoxes = new ArrayList<Cashbox>();
		List<Cashbox> lstLessBoxes = new ArrayList<Cashbox>();

		for (Cashbox x : kvpCashbox.values()) {
			if (x != null) {
				if (x.getFoundStatus() == Constants.FOUND_STATUS_YES) {
					cashBoxCodes.add(x.getCashBoxCode());
				} else if (x.getFoundStatus() == Constants.FOUND_STATUS_NOTPLAN) {
					bMore = true;
					lstMoreBoxes.add(x);
				} else if (x.getFoundStatus() == Constants.FOUND_STATUS_NO) {
					bLess = true;
					lstLessBoxes.add(x);
				}
			}
		}

		if (bMore || bLess) {
			AppContext.showToast("与计划不一致，无法提交！");

			List<Cashbox> lstExceptionBoxes = new ArrayList<Cashbox>();
			ErrorTypeOfCount errorTypeOfCount = ErrorTypeOfCount.Both;
			StockType stockType = StockType.InStock;

			if (bMore && bLess) {

				for (Cashbox x : lstMoreBoxes) {
					lstExceptionBoxes.add(x);
				}
				for (Cashbox x : lstLessBoxes) {
					lstExceptionBoxes.add(x);
				}

				errorTypeOfCount = ErrorTypeOfCount.Both;

			} else if (bMore && !bLess) {
				for (Cashbox x : lstMoreBoxes) {
					lstExceptionBoxes.add(x);
				}

				errorTypeOfCount = ErrorTypeOfCount.More;

			} else if (!bMore && bLess) {
				for (Cashbox x : lstLessBoxes) {
					lstExceptionBoxes.add(x);
				}

				errorTypeOfCount = ErrorTypeOfCount.Less;
			}

			if (taskType.equals("出库")) {
				stockType = StockType.OutStock;
			} else if (taskType.equals("入库")) {
				stockType = StockType.InStock;
			} else if (taskType.equals("交接")) {
				stockType = StockType.EscortStock;
			}

			Api.submitError(user, stockType, errorTypeOfCount, lstExceptionBoxes, mExceptionHandler);

		} else {
			submitTask();
		}

		// if(bNeedManagerVerify){
		// showManagerVerify();
		// }else{
		// submitTask();
		// }

	}

	private void showManagerVerify() {

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View view = layoutInflater.inflate(R.layout.manager_verify, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("金库经理确认");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO 自动生成的方法存根

				TextView txtUserName = (TextView) view.findViewById(R.id.login_user_edit);
				TextView txtPassword = (TextView) view.findViewById(R.id.login_passwd_edit);

				String userCode = txtUserName.getText().toString();
				String password = txtPassword.getText().toString();

				if (userCode == null || password == null || userCode.isEmpty() || password.isEmpty()) {
					AppContext.showToast("用户或密码不能为空");
					return;
				}

				showManagerVerifyCallback(userCode, password);
			}

		});

		builder.setNegativeButton("取消", null);

		builder.create().show();
	}

	private void showManagerVerifyCallback(String userCode, String password) {
		if (cashBoxCodes == null) {
			return;
		}

		showWaitDialog(R.string.loading);
		Api.getStoreManager(userCode, password, mManagerVerifyHandler);
	}

	private void submitTask() {

		showWaitDialog(R.string.loading);

		if (taskType.equals("出库")) {
			Api.saveOutStore(user, m_KeyPostId, m_PasswordPostId, new Gson().toJson(cashBoxCodes), mSaveHandler);
		} else if (taskType.equals("入库")) {
			Api.saveInStore(user, m_KeyPostId, m_PasswordPostId, new Gson().toJson(cashBoxCodes), mSaveHandler);
		} else if (taskType.equals("交接")) {
			Api.saveInEscort(user, m_KeyPostId, m_PasswordPostId, new Gson().toJson(cashBoxCodes), mSaveHandler);
		} else {
			hideWaitDialog();
			return;
		}
	}

	private Handler mRefreshHandler = new Handler();
	private Runnable mRefreshRunnable = new Runnable() {
		public void run() {

			byte btWorkAntenna = m_curInventoryBuffer.lAntenna.get(m_curInventoryBuffer.nIndexAntenna);
			if (btWorkAntenna < 0)
				btWorkAntenna = 1;

			mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);

			refreshList();
			mRefreshHandler.postDelayed(this, 1500);
		}
	};

	private void read() {
		byte outPower = Byte.parseByte(AppContext.get("batchReadPower", "27"));
		set(outPower);
		Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
		sendBroadcast(intent);// 传递过去
		mRefreshHandler.postDelayed(mRefreshRunnable, 500);
	}

	private void refreshList() {

		for (InventoryTagMap inventoryTagMap : m_curInventoryBuffer.lsTagList) {
			String str = inventoryTagMap.strEPC;
			str = str.replace("f", "");
			str = str.replace("F", "");

			if (!str.startsWith(Constants.RFID_START_WITH_STR)) {
				continue;
			}
			if (str.length() < 10) {
				continue;
			}

			String foundBoxCode = str.substring(4, 10);
			if (kvpCashbox.containsKey(foundBoxCode)) {
				Cashbox cb = kvpCashbox.get(foundBoxCode);

				if (cb.getFoundStatus() == Constants.FOUND_STATUS_NO) {
					cb.setFoundStatus(Constants.FOUND_STATUS_YES);
					++readCount;
				}
			} else {
				Cashbox cb = new Cashbox();
				cb.setRfid(str);
				cb.setCashBoxCode(foundBoxCode);
				cb.setFoundStatus(Constants.FOUND_STATUS_NOTPLAN);
				cashboxListFragment.addItem(cb);
				kvpCashbox.put(foundBoxCode, cb);
				++readCount;
			}
		}

		m_ReadCount.setText("已读取:" + String.valueOf(readCount));
		cashboxListFragment.myadapter.notifyDataSetChanged();
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
			read(null);
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
				read(mButtonRead);
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Cashbox>>() {
					}.getType());

					if (list != null) {
						for (Cashbox cashbox : list) {
							if (cashbox == null) {
								continue;
							}
							cashbox.setFoundStatus(Constants.FOUND_STATUS_NO);
							cashboxListFragment.addItem(cashbox);
							kvpCashbox.put(cashbox.getCashBoxCode().trim(), cashbox);
						}
					}
					m_ReadCount.setText("已读取:0");
					m_TotalCount.setText("总数:" + String.valueOf(kvpCashbox.size()));
					cashboxListFragment.myadapter.notifyDataSetChanged();
					hideWaitDialog();
				} else {
					AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
					AppContext.getInstance().cleanLoginInfo();
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

	private final AsyncHttpResponseHandler mSaveHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					hideWaitDialog();
					AppContext.showToast("提交成功");
					finish();
				} else {
					AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
					AppContext.getInstance().cleanLoginInfo();
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

	private final AsyncHttpResponseHandler mManagerVerifyHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					hideWaitDialog();
					submitTask();
				} else {
					AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
					AppContext.getInstance().cleanLoginInfo();
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

	private final AsyncHttpResponseHandler mExceptionHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					hideWaitDialog();
				} else {
					AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
					AppContext.getInstance().cleanLoginInfo();
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

}