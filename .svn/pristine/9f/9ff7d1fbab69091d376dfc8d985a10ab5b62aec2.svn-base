package com.jintoufs.activites;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.entity.StoreTask;
import com.jintoufs.entity.User;
import com.jintoufs.fragment.PrepareTomorrowListFragment;

/**
 * 准备钞箱界面
 * 
 * @author
 * @version 创建时间：2013-7-11 上午8:37:18
 */
@SuppressLint("NewApi")
public class PrepareTomorrowActivity extends BaseActivity {

	private AppContext appContext;
	private User user;
	private static PrepareTomorrowListFragment prepareTomorrowListFragment = null;

	final FragmentManager fragmentManager = getFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prepare_tomorrow);
		appContext = (AppContext) getApplication();
		user = appContext.getLoginUser();
		prepareTomorrowListFragment = (PrepareTomorrowListFragment) fragmentManager.findFragmentById(R.id.prepare_tomorrow_fragment_list);
		Intent intent = getIntent();
		StoreTask sTask = (StoreTask) intent.getSerializableExtra("storeTask");
		prepareTomorrowListFragment.addAllItems(sTask.getTasks());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
