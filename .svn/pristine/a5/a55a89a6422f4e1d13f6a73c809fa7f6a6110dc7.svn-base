package com.jintoufs.logstics.activites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 上车前确认所有的钞箱界面
 * 
 * @author YangHao
 */
@SuppressLint("NewApi")
public class ConfirmEscortTaskActivity extends BaseRfidActivity {

	private AppContext appContext;
	private User user;
	private static CashboxListFragment cashboxListFragment = null;
	final FragmentManager fragmentManager = getFragmentManager();
	private TextView mHeaderTitle;
	private TextView mReadCount;
	private TextView mTotalCount;
	private Button mButtonRead;
	private long lastPressTime=0;
	private List<Cashbox> list;
	private Map<String,Cashbox> kvpCashbox=new HashMap<String,Cashbox>();
	private int m_readCount=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		setContentView(R.layout.activity_confirm_task);
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mReadCount = (TextView) findViewById(R.id.readCount);
		mTotalCount = (TextView) findViewById(R.id.totalCount);
		appContext = (AppContext) getApplication();
		cashboxListFragment = (CashboxListFragment) fragmentManager.findFragmentById(R.id.cashbox_fragment_list);
		mHeaderTitle.setText("任务开始确认");
		mButtonRead = (Button) findViewById(R.id.btn_read);

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
					hideWaitDialog();
					if(list!=null){
						for (Cashbox cashbox : list) {
							if(cashbox==null){
								continue;
							}
							cashbox.setType(null);
							cashboxListFragment.addItem(cashbox);
							kvpCashbox.put(cashbox.getCashBoxCode(), cashbox);
						}
					}
					
					mReadCount.setText("已读取:0");
					mTotalCount.setText("总数:"+kvpCashbox.size()+"");
					cashboxListFragment.myadapter.notifyDataSetChanged();
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

	@Override
	protected void onResume() {
		super.onResume();
		showWaitDialog(R.string.loading);
		
		cashboxListFragment.clearItem();
		kvpCashbox.clear();
		mReadCount.setText("已读取:0");
		mTotalCount.setText("总数:0");
		m_readCount=0;
		
		Api.getEscortCashboxList(user.getKeyStr(), mHandler);
	}

	public void read(View view) {
		onRead();
	}

	private void onRead() {
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
		// TODO clear Task
		stopInventory();
		mButtonRead.setText(R.string.btn_read);
		
		onResume();
	}

	public void save(View view) {
		accompaniment.start();
		
		stopInventory();
		mButtonRead.setText(R.string.btn_read);
		
		if(kvpCashbox.size()==0){
			return;
		}
		
		List<Cashbox> lstFound=new ArrayList<Cashbox>();
		
		boolean bException=false;
		
		for(Cashbox x : kvpCashbox.values()){
			if(x!=null ){
				if(x.getFoundStatus().equals(Constants.FOUND_STATUS_YES)){
					lstFound.add(x);
				}else{
					bException=true;
					break; 
				}
			}
		}
		
		if(bException){
			AppContext.showToast("与计划不一致，无法提交！");
		}else{
			showWaitDialog(R.string.uploading);
			Api.confirmEscortTask(user.getKeyStr(), lstFound, mSaveHandler);
		}
		
	}

	private final AsyncHttpResponseHandler mSaveHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					hideWaitDialog();
					AppContext.showToast("大库交接任务确认成功");
					ConfirmEscortTaskActivity.this.finish();

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

	private Handler mRefreshHandler = new Handler();
	private Runnable mRefreshRunnable = new Runnable() {
		public void run() {
			
			byte btWorkAntenna = m_curInventoryBuffer.lAntenna
					.get(m_curInventoryBuffer.nIndexAntenna);
			if (btWorkAntenna < 0)
				btWorkAntenna = 1;
			 
			mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);
			//mReader.setOutputPower((byte)0xff, (byte)12);
			refreshList();
			mRefreshHandler.postDelayed(this, 2000);
		}
	};

	private void read() {
		Byte bPower=Byte.valueOf(AppContext.get("batchReadPower","27"));
		set(bPower);
		Intent intent = new Intent(ReaderHelper.BROADCAST_REFRESH_INVENTORY_REAL);
		sendBroadcast(intent);// 传递过去
		mRefreshHandler.postDelayed(mRefreshRunnable, 500);
	}

	private void refreshList() {
		for (InventoryTagMap inventoryTagMap : m_curInventoryBuffer.lsTagList) {
			String str = inventoryTagMap.strEPC;
			str = str.replace("F", "");
			str = str.replace("f", "");
			
			if(!str.startsWith(Constants.RFID_START_WITH_STR)){
				continue;
			}
			if(str.length()<10){
				continue;
			}
			
			String foundBoxCode=str.substring(4,10);
			
			if(kvpCashbox.containsKey(foundBoxCode)){
				Cashbox cb=kvpCashbox.get(foundBoxCode);
				
				if(cb.getFoundStatus()==Constants.FOUND_STATUS_NO){
					cb.setFoundStatus(Constants.FOUND_STATUS_YES);
					++m_readCount;
				}
			}
			else{
				Cashbox cb = new Cashbox();
				cb.setRfid(str);
				cb.setCashBoxCode(foundBoxCode);
				cb.setFoundStatus(Constants.FOUND_STATUS_NOTPLAN);
				cashboxListFragment.addItem(cb);
				kvpCashbox.put(foundBoxCode, cb);
				++m_readCount;
			}
		}
		cashboxListFragment.myadapter.notifyDataSetChanged();
		mReadCount.setText("已读取:" + String.valueOf(m_readCount));
	}

	@Override
	public void stopInventory() {
		mRefreshHandler.removeCallbacks(mRefreshRunnable);
		
		super.stopInventory();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if(event.getDownTime()- lastPressTime < 300){
			lastPressTime=event.getDownTime();
			return true;
		}
		lastPressTime=event.getDownTime();
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
			case KeyEvent.ACTION_DOWN://按键扫描
				read(mButtonRead);
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
