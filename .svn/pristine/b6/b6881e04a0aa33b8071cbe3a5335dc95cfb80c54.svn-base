package com.jintoufs.logstics.activites;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseRfidActivity;
import com.jintoufs.logstics.entity.Cashbox;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.fragment.CashboxListFragment;
import com.jintoufs.logstics.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.logstics.reader.server.ReaderHelper;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.jintoufs.logstics.utils.MapUtil;
import com.jintoufs.logstics.widget.SendValidateButton;
import com.jintoufs.logstics.widget.SendValidateButton.SendValidateButtonListener;
import com.jintoufs.logstics.widget.dialog.CommonDialog;
import com.jintoufs.logstics.widget.dialog.DialogHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 收尾箱/钞箱界面
 * 
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
@SuppressLint("NewApi")
public class ReceiveSiteActivity extends BaseRfidActivity {

	private AppContext appContext;
	private User user;
	private static CashboxListFragment cashboxListFragment = null;
	private TextView mHeaderTitle;
	final FragmentManager fragmentManager = getFragmentManager();
	private Double longitude;// 经度
	private Double latitude;// 纬度
	private int id;// 终端ID
	private long detailId;// 终端任务ID
	private int deliverType=1;//钞箱任务类型
	private String name;
	private Button mButtonRead;
	private TextView m_txtReadCount;
	private TextView m_txtTotalCount;

	private Map<String, Cashbox> kvpCashbox = new HashMap<String, Cashbox>();
	private List<Cashbox> lstFound = new ArrayList<Cashbox>();;
	private long lastPressTime = 0;
	private String verifyCode;
	private SendValidateButton mSendValidateButton;
	private TextView codeTipTv;
	private int m_readCount = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_site);
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		cashboxListFragment = (CashboxListFragment) fragmentManager.findFragmentById(R.id.cashbox_fragment_list);
		Intent intent = getIntent();
		longitude = intent.getDoubleExtra("longitude", Constants.DEFAULT_LONGITUDE);
		latitude = intent.getDoubleExtra("latitude", Constants.DEFAULT_LATITUDE);
		name = intent.getStringExtra("name");
		id = intent.getIntExtra("id", 0);
		detailId = intent.getLongExtra("detailId", 0);
		verifyCode = intent.getStringExtra("verifyCode");
		mHeaderTitle.setText(name);
		mButtonRead = (Button) findViewById(R.id.btn_read);
		m_txtTotalCount = (TextView) findViewById(R.id.totalCount);
		m_txtReadCount = (TextView) findViewById(R.id.readCount);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		cashboxListFragment.clearItem();
		cashboxListFragment.myadapter.notifyDataSetChanged();

		m_readCount = 0;
		kvpCashbox.clear();
		lstFound.clear();
		m_txtTotalCount.setText("总数:0");
		m_txtReadCount.setText("已扫描:0");
		//LabelChanged();
		
		showWaitDialog();
		Api.getTaskCashboxList(user.getKeyStr(), detailId, deliverType, mHandler);
	}

	public void openMap(View view) {
		accompaniment.start();

		stopInventory();
		mButtonRead.setText(R.string.btn_read);

		MapUtil.openAmap(longitude, latitude, getApplicationContext());
	}

	public void btnWriteCard_OnClick(View view) {
		accompaniment.start();

		stopInventory();
		mButtonRead.setText(R.string.btn_read);

		Intent intent = new Intent(this, WriteCardActivity.class);
		intent.putExtra("siteId", id);
		startActivity(intent);
	}

	public void addCashbox(View view) {
		/*
		 * accompaniment.start(); Intent intent = new Intent(this,
		 * ConfirmActivity.class); intent.putExtra("id", id);
		 * intent.putExtra("name", name); startActivity(intent);
		 */
		CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(this);
		final EditText txtCode = new EditText(this);

		dialog.setTitle("输入钞箱编号");
		dialog.setContent(txtCode);
		dialog.setNegativeButton(R.string.cancle, null);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String code = txtCode.getText().toString();
				Cashbox cashbox = new Cashbox();
				cashbox.setCashBoxCode(code);
				cashbox.setFoundStatus(Constants.FOUND_STATUS_YES);
				kvpCashbox.put(cashbox.getCashBoxCode(), cashbox);
				cashboxListFragment.addItem(cashbox);
				//LabelChanged();
				dialog.dismiss();
			}
		});
		dialog.show();
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

		showWaitDialog("等待验证上缴款箱");
		
		List<Cashbox> cashBoxes=new ArrayList<Cashbox>();
		for(Cashbox x : kvpCashbox.values()){
			if(x!=null){
				cashBoxes.add(x);
			}
		}
		Api.IsHandInCashBoxes(user.getKeyStr(), cashBoxes, mGetHandInHandler);
	}

	private void createDialog() {
		LayoutInflater factory = LayoutInflater.from(ReceiveSiteActivity.this);
		// 获得自定义对话框
		final View view = factory.inflate(R.layout.validate_login, null);
		mSendValidateButton = (SendValidateButton) view.findViewById(R.id.btn_sendCode);
		codeTipTv = (TextView) view.findViewById(R.id.validate_code_tip_tv);
		codeTipTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				codeTipTv.setVisibility(View.GONE);
				mSendValidateButton.setVisibility(View.VISIBLE);
			}
		});
		mSendValidateButton.setmListener(mSendValidateListener);
		AlertDialog loginDialog = new AlertDialog.Builder(ReceiveSiteActivity.this).setIcon(android.R.drawable.btn_star).setTitle("请输入验证码").setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int i) {
						EditText passwordEt = (EditText) view.findViewById(R.id.login_passwd_edit);
						passwordEt.requestFocus(); 
						String password = passwordEt.getText().toString();
						showDialog(dialog);
						if (password == null || "".equals(password)) {
							passwordEt.setError(getResources().getText(R.string.nullError));
						} else if (!verifyCode.equals(password)) {
							passwordEt.setError("验证码有误");
						} else {
							closeDialog(dialog);
							send(password);
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						closeDialog(dialog);
					}
				}).create();
		loginDialog.show();
	}

	private void closeDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			// 将mShowing变量设为false，表示对话框已关闭
			field.set(dialog, true);
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			// 将mShowing变量设为false，表示对话框已关闭
			field.set(dialog, false);
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 保存
	private void send(final String code) {

		if (kvpCashbox.size() == 0) {
			return;
		}

		
		boolean bException = false;

		for (Cashbox x : kvpCashbox.values()) {
			if (x != null) {		
					
				if (x.getFoundStatus().equals(Constants.FOUND_STATUS_YES)) {
					lstFound.add(x);
					bException = false;
				}else{
					bException = true;
				}
			}
		}
		submit(lstFound, code, bException);

	}

	private void submit(final List<Cashbox> lstFound, final String code, boolean bException) {
		if (bException) {
			final CommonDialog saveDialog = new CommonDialog(ReceiveSiteActivity.this);
			saveDialog.setTitle("与预期钞箱不一致，确定保存？");
			saveDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					showWaitDialog();
					Api.saveReceiveTask(user.getKeyStr(), detailId, code, lstFound, mSaveHandler);
				}
			});
			saveDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					saveDialog.dismiss();
				}
			});
			saveDialog.show();
		} else {
			showWaitDialog();
			Api.saveReceiveTask(user.getKeyStr(), detailId, code, lstFound, mSaveHandler);
		}
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				hideWaitDialog();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					List<Cashbox> list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Cashbox>>() {
					}.getType());

					if (list != null) {
						for (Cashbox x : list) {
							if (x == null) {
								continue;
							}
							x.setType(null);
							kvpCashbox.put(x.getCashBoxCode(), x);
							cashboxListFragment.addItem(x);
						}
					}
					m_txtTotalCount.setText("总数:" + String.valueOf(kvpCashbox.size()));
					//LabelChanged();
					cashboxListFragment.myadapter.notifyDataSetChanged();

				} else {
					AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
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
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};
	private final AsyncHttpResponseHandler mSaveHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			hideWaitDialog();
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					AppContext.showToast("保存成功");
					hideWaitDialog();

					finish();
				} else {
					AppContext.showToast("\n原因:" + ajaxMsg.getMessage());
					hideWaitDialog();
				}
			} catch (Exception e) {
			
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

	private final AsyncHttpResponseHandler mGetHandInHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					hideWaitDialog();

 					List<Cashbox> list = new ArrayList<Cashbox>();
					JsonParser parser = new JsonParser(); // 创建JSON解析器
					JsonArray array = (JsonArray) parser.parse(gson.toJson(ajaxMsg.getDatas())); // 创建JsonObject对象
					for (int i = 0; i < array.size(); i++) {
						JsonObject subObject = array.get(i).getAsJsonObject();
						if (subObject == null) {
							continue;
						}
						Cashbox cashbox = new Cashbox();
						cashbox.setCashBoxCode(subObject.get("cashBoxCode").getAsString());
						cashbox.setType(subObject.get("type").getAsInt());
						list.add(cashbox);
					}

					if (list != null && list.size() != 0) {

						for (Cashbox x : list) {
							if (x == null || x.getType() == null) {
								continue;
							}

							if(kvpCashbox.containsKey(x.getCashBoxCode())){
								kvpCashbox.get(x.getCashBoxCode()).setType(x.getType());
							}
						}

//						for (Cashbox x : kvpCashbox.values()) {
//							if (x!=null && x.getType()!=null && x.getType().equals(1) && (x.getAmount() == null || x.getAmount().isEmpty())) {
//								AppContext.showToast("有部分上缴箱尚未设置上缴金额!");
//								cashboxListFragment.myadapter.notifyDataSetChanged();
//								return;
//							}
//						}
					}

					createDialog();

				} else {
					AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
					hideWaitDialog();
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
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

	private Handler mRefreshHandler = new Handler();
	private Runnable mRefreshRunnable = new Runnable() {
		public void run() {

			byte btWorkAntenna = m_curInventoryBuffer.lAntenna.get(m_curInventoryBuffer.nIndexAntenna);
			if (btWorkAntenna < 0)
				btWorkAntenna = 1;

			mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);

			refreshList();
			mRefreshHandler.postDelayed(this, 2000);
		}
	};

	private void refreshList() {
		for (InventoryTagMap inventoryTagMap : m_curInventoryBuffer.lsTagList) {
			String str = inventoryTagMap.strEPC;
			str = str.replace("F", "");
			str = str.replace("f", "");
			if (str.startsWith(Constants.RFID_START_WITH_STR)) {
				
				if(str.length()<10){
					continue;
				}
				
				String code = str.substring(4, 10);
				if(kvpCashbox.containsKey(code)){
					Cashbox cb=kvpCashbox.get(code);
					if(cb.getFoundStatus()==Constants.FOUND_STATUS_NO){
						cb.setFoundStatus(Constants.FOUND_STATUS_YES);
						++m_readCount;
					}
				}
				else{
					Cashbox cashbox = new Cashbox();
					cashbox.setRfid(str);
					cashbox.setCashBoxCode(code);
					cashbox.setTaskType(Constants.TASK_TYPE_RECEIVE);
					Calendar ca = Calendar.getInstance();
					ca.add(Calendar.DATE, 1);
					cashbox.setBackTime(ca.getTime());
					cashbox.setFoundStatus(Constants.FOUND_STATUS_NOTPLAN);
					kvpCashbox.put(cashbox.getCashBoxCode(), cashbox);
					lstFound.add(cashbox);
					cashboxListFragment.addItem(cashbox);
					
					++m_readCount;
				}

			}
		}
		//LabelChanged();
		m_txtReadCount.setText("已扫描:" + String.valueOf(m_readCount));
		cashboxListFragment.myadapter.notifyDataSetChanged();
	}

	private void read() {
		Byte bPower = Byte.valueOf(AppContext.get(Constants.SINGLE_READ_POWER, "15"));
		set(bPower);
		Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
		sendBroadcast(intent);// 传递过去
		mRefreshHandler.postDelayed(mRefreshRunnable, 500);
	}

	public void stopInventory() {
		mRefreshHandler.removeCallbacks(mRefreshRunnable);

		super.stopInventory();
	}

	private final SendValidateButtonListener mSendValidateListener = new SendValidateButtonListener() {

		@Override
		public void onClickSendValidateButton() {
			Api.reSendMsg(user.getKeyStr(), detailId,deliverType, mSendMsgHandler);
		}

		@Override
		public void onTick() {

		}

	};

	private final AsyncHttpResponseHandler mSendMsgHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			hideWaitDialog();
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					verifyCode = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<String>() {
					}.getType());
					if (verifyCode == null) {
						verifyCode = "";
					}
				} else {
					AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
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
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(R.string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}
	};

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

	private void LabelChanged(){
		m_txtTotalCount.setText("总数:"+String.valueOf(kvpCashbox.size()));
		m_txtReadCount.setText("已扫描:"+String.valueOf(m_readCount));
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                break;
            case KeyEvent.ACTION_DOWN://按键扫描
                read(mButtonRead);
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
