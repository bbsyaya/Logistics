package com.jintoufs.logstics.activites;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.AppException;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.base.BaseActivity;
import com.jintoufs.logstics.db.TerminalDao;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Terminal;
import com.jintoufs.logstics.entity.User;
import com.jintoufs.logstics.fragment.TerminalListFragment;
import com.jintoufs.logstics.utils.AjaxMsg;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 任务网点、ATM界面
 * 
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
@SuppressLint("NewApi")
public class TerminalListActivity extends BaseActivity {

	private AppContext appContext;
	private static TerminalListFragment terminalFragment = null;

	final FragmentManager fragmentManager = getFragmentManager();
	private TextView mHeaderTitle;
	private int deliverType;
	private int terminalType;
	private String caption;
	private User user;
	private TerminalDao terminalDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site_list);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		terminalDao=new TerminalDao(this);
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		terminalFragment = (TerminalListFragment) fragmentManager.findFragmentById(R.id.org_fragment_list);
		Intent intent = getIntent();
		caption = intent.getStringExtra("caption");
		deliverType = intent.getIntExtra("deliverType", 0);
		terminalType = intent.getIntExtra("terminalType", 0);
		mHeaderTitle.setText(caption);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			showWaitDialog(R.string.loading);
			getSiteList();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	// 线程取得数据列表
	private void getSiteList() throws AppException {

		if (terminalType == Constants.TERMINAL_TYPE_SITE) {
			Api.getSiteList(user.getKeyStr(), deliverType, mHandler);
		} else if (terminalType == Constants.TERMINAL_TYPE_ATM) {
			//Api.getAtmList(user.getKeyStr(), mHandler);
			getAllTerminal();
			hideWaitDialog();
		}
	}

	/**
	 * 离线状态时，从数据库 获取Terminal
	 */
	private void getAllTerminal(){
		if (terminalType == Constants.TERMINAL_TYPE_ATM){
			List<Terminal> terminalList=terminalDao.queryAllTerminal();
			terminalFragment.clearItem();
			terminalFragment.addItems(terminalList);
		}
	}

	private final AsyncHttpResponseHandler mHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					List<Terminal> list = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<List<Terminal>>() {
					}.getType());
					terminalFragment.clearItem();
					terminalFragment.addItems(list);
					terminalFragment.myadapter.notifyDataSetChanged();
					hideWaitDialog();
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
			//getAllTerminal();
			arg4.printStackTrace();
			hideWaitDialog();

		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			//getAllTerminal();
			arg4.printStackTrace();
			hideWaitDialog();
		}
	};
}
