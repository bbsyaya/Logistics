package com.jintoufs.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.activites.IdentifyCashboxActivity;

public class Tab4Fragment extends Fragment {
	private AppContext appContext;
	private Button btnIdentifyCashbox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getActivity().getApplicationContext();

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout view1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab4, container, false);

		btnIdentifyCashbox = (Button) view1.findViewById(R.id.btnIdentifyCashbox);

		btnIdentifyCashbox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				btnIdentifyCashbox_onClick(arg0);
			}
		});

		return view1;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void btnIdentifyCashbox_onClick(View v) {
		startActivity(new Intent(getActivity(), IdentifyCashboxActivity.class));
	}
}