package com.jintoufs.logstics.activites;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.R.id;
import com.jintoufs.logstics.R.layout;
import com.jintoufs.logstics.R.string;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseRfidActivity;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Key;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.fragment.KeyListFragment;
import com.jintoufs.logstics.reader.model.InventoryBuffer.InventoryTagMap;
import com.jintoufs.logstics.reader.server.ReaderHelper;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

@SuppressLint("NewApi")
public class KeyListActivity extends BaseRfidActivity {

	private AppContext appContext;
	private User user;
	private static KeyListFragment keyListFragment = null;
	final FragmentManager fragmentManager = getFragmentManager();
	private TextView mHeaderTitle;
	private TextView mReadCount;
	private TextView mTotalCount;
	private Button mButtonRead;
	private long lastPressTime=0;
	private List<Key> list;
	private Map<String,Key> kvpCashbox=new HashMap<String,Key>();
	private int m_readCount=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_key_list);
		init();
		mHeaderTitle.setText(getIntent().getStringExtra("caption"));
	}

	//组件初始化
	private void init(){
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		mHeaderTitle = (TextView) findViewById(id.header_title);
		mReadCount = (TextView) findViewById(id.readCount);
		mTotalCount = (TextView) findViewById(id.totalCount);
		appContext = (AppContext) getApplication();
		keyListFragment = (KeyListFragment) fragmentManager.findFragmentById(id.key_list_fragment);
		mButtonRead = (Button) findViewById(id.btn_read);
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		showWaitDialog(string.loading);
		
		keyListFragment.clearItem();
		kvpCashbox.clear();
		mReadCount.setText("已读取:0");
		mTotalCount.setText("总数:0");
		m_readCount=0;
		
		Api.getKeyList(user.getKeyStr(), mHandler);
	}

	public void read(View view) {
		accompaniment.start();
		if (mButtonRead.getText().equals(getString(string.btn_read))) {
			mButtonRead.setText(string.btn_stop);
			read();
		} else if (mButtonRead.getText().equals(getString(string.btn_stop))) {
			stopInventory();
			mButtonRead.setText(string.btn_read);
		}
	}

	public void clear(View view) {
		accompaniment.start();
		// TODO clear Task
		stopInventory();
		mButtonRead.setText(string.btn_read);
		
		onResume();
	}

	public void save(View view) {
		accompaniment.start();
		
		stopInventory();
		mButtonRead.setText(string.btn_read);
		
		if(kvpCashbox.size()==0){
			return;
		}
		
		boolean bException=false;
		
		for(Key x : kvpCashbox.values()){
			if(x!=null ){
				if(x.getStatus().equals(Constants.FOUND_STATUS_YES)){
					bException=false;
				}else{
					bException=true;
					break; 
				}
			}
		}
		
		if(bException){
			AppContext.showToast("与计划不一致，无法提交！");
		}else{
			AppContext.showToast("钥匙确认成功");
			this.finish();
//			showWaitDialog(R.string.uploading);
//			Api.confirmEscortTask(user.getKeyStr(), lstFound, mSaveHandler);
		}
		
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Key>>() {
					}.getType());
					hideWaitDialog();
					if(list!=null){
						for (Key key : list) {
							if(key==null){
								continue;
							}
							key.setStatus(Constants.FOUND_STATUS_NO);
							kvpCashbox.put(key.getRfid(), key);
						}
					}
					
					mReadCount.setText("已读取:0");
					mTotalCount.setText("总数:"+String.valueOf(kvpCashbox.size()));
					keyListFragment.addAllItems(list);
					keyListFragment.myadapter.notifyDataSetChanged();
				} else {
					AppContext.showToast(getResources().getString(string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
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
			AppContext.showToast(getResources().getString(string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			hideWaitDialog();
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
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
					AppContext.showToast("钥匙确认成功");
					KeyListActivity.this.finish();

				} else {
					AppContext.showToast(getResources().getString(string.tip_loading_error) + "\n原因:" + arg0 + ":" + ajaxMsg.getMessage());
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
			AppContext.showToast(getResources().getString(string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			hideWaitDialog();
			arg4.printStackTrace();
			hideWaitDialog();
			AppContext.showToast(getResources().getString(string.tip_loading_error) + "\n原因:" + arg0 + ":" + arg4.getMessage());
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
			
			if(!str.startsWith(Constants.RFID_START_WITH_KEY)){
				continue;
			}
			if(str.length()<10){
				continue;
			}
			Log.d("-----------", str);
			if(kvpCashbox.containsKey(str)){
				Key cb=kvpCashbox.get(str);
				
				if(cb.getStatus()==Constants.FOUND_STATUS_NO){
					cb.setStatus(Constants.FOUND_STATUS_YES);
					++m_readCount;
				}
			}
//			else{
//				Key cb = new Key();
//				cb.setRfid(str);
//				cb.setCashBoxCode(foundKeyCode);
//				cb.setFoundStatus(Constants.FOUND_STATUS_NOTPLAN);
//				cashboxListFragment.addItem(cb);
//				kvpCashbox.put(foundBoxCode, cb);
//				++m_readCount;
//			}
		}
		keyListFragment.myadapter.notifyDataSetChanged();
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
