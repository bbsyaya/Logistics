package com.jintoufs.activites;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.AppException;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.entity.Task;
import com.jintoufs.entity.User;
import com.jintoufs.fragment.OutStoreListFragment;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * 出库界面
 * 
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
@SuppressLint("NewApi")
public class OutStoreListActivity extends BaseActivity {

	private AppContext appContext;
	private static OutStoreListFragment outStoreFragment = null;

	final FragmentManager fragmentManager = getFragmentManager();
	private TextView mHeaderTitle;
	private String type;
	private String typeStr;
	private String caption;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outstore_site_list);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		outStoreFragment = (OutStoreListFragment) fragmentManager.findFragmentById(R.id.org_fragment_list);
		Intent intent = getIntent();
		//type = intent.getStringExtra("taskType");
		caption = intent.getStringExtra("caption");
		mHeaderTitle.setText(caption);
		//byte outPower = Byte.parseByte(AppContext.get(Constants.BATCH_READ_POWER, "28"));
		//set(outPower);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			showWaitDialog(R.string.loading);
			getSiteList();
			
			//deliverFragment.clearItem();
			//getDeliverTaskListTest();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getDeliverTaskListTest(){
		Task dt=new Task();
		//dt.setBatchNum("1");
		/*dt.setCashboxAmount(45);
		dt.setExecuteTime("ff");
		dt.setKeyStation("张三");
		dt.setPasswordStation("李四");
		//dt.setPlatform("四");
		//dt.setRouteName("黄埔一线");
		
		outStoreFragment.addItem(dt);
		
		dt=new Task();
		//dt.setBatchNum("1");
		dt.setCashboxAmount(39);
		dt.setExecuteTime("ff");
		dt.setKeyStation("王五");
		dt.setPasswordStation("赵六");*/
		//dt.setPlatform("三");
		//dt.setRouteName("黄埔二线");
		outStoreFragment.addItem(dt);
	}

	// 线程取得数据列表
	private void getSiteList() throws AppException {
		outStoreFragment.clearItem();
		
		Api.getOutTaskRouteList(user, mHandler);
	}
	

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					List<Task> list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Task>>() {
					}.getType());
					outStoreFragment.addItems(list);
					outStoreFragment.myadapter.notifyDataSetChanged();
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
