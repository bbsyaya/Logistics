package com.jintoufs.logstics.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jintoufs.logstics.R;
import com.jintoufs.logstics.activites.ConfirmEscortTaskActivity;
import com.jintoufs.logstics.activites.ConfirmInCarTaskActivity;
import com.jintoufs.logstics.activites.KeyListActivity;
import com.jintoufs.logstics.activites.TaskTypeActivity;
import com.jintoufs.logstics.base.BaseFragment;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.widget.GuidCard;

public class Tab1Fragment extends BaseFragment {
	LinearLayout view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout view1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab1, container, false);
		view = (LinearLayout) view1.findViewById(R.id.tab1_scroll);
		return view1;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		GuidCard gc1 = new GuidCard(this.getActivity());
		GuidCard gc2 = new GuidCard(this.getActivity());
		GuidCard gc3 = new GuidCard(this.getActivity());
		GuidCard gc0 = new GuidCard(this.getActivity());
		GuidCard gc4 = new GuidCard(this.getActivity());
		GuidCard gc5 = new GuidCard(this.getActivity());

		gc0.setTitle("大库交接");
		gc0.setTip("在大库验证身份，取得钞箱");
		gc0.setImg(R.drawable.hand_over);
		gc0.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), ConfirmEscortTaskActivity.class);
				startActivity(intent);
			}
		});

		gc1.setTitle("出库上车");
		gc1.setTip("在月台，扫描钞箱并装车");
		gc1.setImg(R.drawable.in_car);
		gc1.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), ConfirmInCarTaskActivity.class);
				startActivity(intent);
			}
		});

		gc2.setTitle("物流任务");
		gc2.setTip("去网点执行物流任务");
		gc2.setImg(R.drawable.task);
		gc2.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), TaskTypeActivity.class);
				intent.putExtra("taskType", Constants.TASK_TYPE_LOGISTICS);
				startActivity(intent);
			}
		});

		gc3.setTitle("下车回库");
		gc3.setTip("在月台，确认卸载全部钞箱");
		gc3.setImg(R.drawable.store);
		gc3.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), TaskTypeActivity.class);
				intent.putExtra("taskType", Constants.TASK_TYPE_OUTCAR);
				startActivity(intent);
			}
		});
		
		gc4.setTitle("钥匙领用");
		gc4.setTip("在月台，确认卸载全部钞箱");
		gc4.setImg(R.drawable.quick_option_key_nor);
		gc4.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), KeyListActivity.class);
				intent.putExtra("caption", "钥匙领用");
				startActivity(intent);
			}
		});
		
		gc5.setTitle("钥匙归还");
		gc5.setTip("在月台，确认卸载全部钞箱");
		gc5.setImg(R.drawable.quick_option_key_nor);
		gc5.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), KeyListActivity.class);
				intent.putExtra("caption", "钥匙归还");
				startActivity(intent);
			}
		});

		view.removeAllViews();
		view.addView(gc4);
		view.addView(gc0);
		view.addView(gc1);
		view.addView(gc2);
		view.addView(gc3);
		view.addView(gc5);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}