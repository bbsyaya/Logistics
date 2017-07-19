package com.jintoufs.activites;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.entity.Task;
import com.jintoufs.entity.User;
import com.jintoufs.fragment.InStoreListFragment;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * 入库界面
 * 
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */

@SuppressLint("NewApi")
public class InStoreListActivity extends BaseActivity {

	private AppContext appContext;
	private static InStoreListFragment inStoreFragment = null;

	final FragmentManager fragmentManager = getFragmentManager();
	private TextView mHeaderTitle;
	private User user;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instore_site_list);

		appContext = AppContext.getInstance();
		user = appContext.getLoginUser();

		inStoreFragment = (InStoreListFragment) fragmentManager.findFragmentById(R.id.org_fragment_list);
		mHeaderTitle = (TextView) findViewById(R.id.header_title);

		mHeaderTitle.setText(getIntent().getStringExtra("caption"));

		//byte outPower = Byte.parseByte(AppContext.get(Constants.BATCH_READ_POWER, "28"));
		//set(outPower);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			showWaitDialog(R.string.loading);
			getSiteList();

			// deliverFragment.clearItem();
			// getDeliverTaskListTest();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getSiteList() {

		inStoreFragment.clearItem();

		Api.getInTaskRouteList(user, mHandler);
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
					inStoreFragment.addItems(list);
					inStoreFragment.myadapter.notifyDataSetChanged();
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

	private void GetReceiveTaskListTest() {
		Task t = new Task();
		// t.setRouteName("外滩一线");
		/*
		 * t.setKeyStation("阿波罗"); t.setPasswordStation("斯坦");
		 * inStoreFragment.addItem(t);
		 * 
		 * t=new Task(); //t.setRouteName("外滩二线"); t.setKeyStation("缪斯");
		 * t.setPasswordStation("波罗斯");
		 */
		inStoreFragment.addItem(t);

		inStoreFragment.myadapter.notifyDataSetChanged();
	}

}
