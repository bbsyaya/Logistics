package com.jintoufs.activites;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.base.BaseRfidActivity;
import com.jintoufs.entity.CheckStore;
import com.jintoufs.entity.User;
import com.jintoufs.fragment.CheckStoreListFragment;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 盘库界面
 * 
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
@SuppressLint("NewApi")
public class CheckStoreActivity extends BaseActivity {

	private AppContext appContext;
	private List<CheckStore> list;
	private User user;
	private static CheckStoreListFragment checkStoreListFragment = null;

	final FragmentManager fragmentManager = getFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_store);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		checkStoreListFragment = (CheckStoreListFragment) fragmentManager.findFragmentById(R.id.check_store_fragment_list);
	}

	@Override
	protected void onResume() {
		super.onResume();
		showWaitDialog();
		Api.getCheckStoreList(user, mHandler);
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<CheckStore>>() {
					}.getType());
					checkStoreListFragment.clearItem();
					checkStoreListFragment.addAllItems(list);
					checkStoreListFragment.myadapter.notifyDataSetChanged();
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
