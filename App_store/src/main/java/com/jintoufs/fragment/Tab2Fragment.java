package com.jintoufs.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jintoufs.R;
import com.jintoufs.widget.XListView;

public class Tab2Fragment extends Fragment {
	private XListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout view1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab2, container, false);
		mListView = (XListView) view1.findViewById(R.id.msg_xListView);
		mListView.setPullLoadEnable(true);//
		return view1;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}