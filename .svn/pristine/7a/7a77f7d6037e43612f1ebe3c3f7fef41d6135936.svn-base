package com.jintoufs.activites;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.User;
import com.jintoufs.fragment.PrepareTomorrowDetailListFragment;
import com.jintoufs.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 准备钞箱-钞箱列表详细界面
 * 
 * @author YangHao
 */
@SuppressLint("NewApi")
public class PrepareTomorrowDetailActivity extends BaseActivity {

	private AppContext appContext;
	private User user;
	private static PrepareTomorrowDetailListFragment cashboxListFragment = null;

	private long taskId;//
	private TextView header_title;
	private FragmentManager fragmentManager;

	private Map<String, Cashbox> kvpCashbox = new HashMap<String, Cashbox>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepare_tomorrow_detail);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		fragmentManager = getFragmentManager();
		cashboxListFragment = (PrepareTomorrowDetailListFragment) fragmentManager.findFragmentById(R.id.cashbox_fragment_list);
		Intent intent = getIntent();
		taskId = intent.getLongExtra("taskId", 0);
		cashboxListFragment.taskId = taskId;
		String routeName = intent.getStringExtra("routeName");
		String key = intent.getStringExtra("keyStation");
		String password = intent.getStringExtra("passwordStation");
		header_title = (TextView) findViewById(R.id.header_title);
		header_title.setText(routeName + "（" + key + "," + password + "）");
	}

	@Override
	protected void onResume() {
		super.onResume();
		showWaitDialog();
		Api.queryPrepareCashBox(user.getKeyStr(), taskId, mHandler);
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					List<Cashbox> list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Cashbox>>() {
					}.getType());
					kvpCashbox.clear();
					for (Cashbox cashbox : list) {
						if (cashbox != null) {
							kvpCashbox.put(cashbox.getCashBoxCode(), cashbox);
						}
					}
					cashboxListFragment.addAllItems(list);
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
