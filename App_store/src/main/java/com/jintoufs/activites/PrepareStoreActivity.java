package com.jintoufs.activites;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.entity.StoreTask;
import com.jintoufs.entity.User;
import com.jintoufs.fragment.PrepareStoreListFragment;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

@SuppressLint("NewApi")
public class PrepareStoreActivity extends BaseActivity {
	private AppContext appContext;
	private User user;
	private static PrepareStoreListFragment prepareStoreListFragment = null;
	final FragmentManager fragmentManager = getFragmentManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepare_store);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		prepareStoreListFragment = (PrepareStoreListFragment) fragmentManager.findFragmentById(R.id.prepare_store_list_fragment);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		showWaitDialog();
		Api.queryPrepareStoreTask(user.getKeyStr(), mHandler);
	}
	
	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					List<StoreTask> list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<StoreTask>>() {
					}.getType());
					prepareStoreListFragment.addAllItems(list);
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
