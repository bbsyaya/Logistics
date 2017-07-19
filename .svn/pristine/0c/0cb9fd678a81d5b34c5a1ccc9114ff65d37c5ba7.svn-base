package com.jintoufs.fragment;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jintoufs.AppContext;
import com.jintoufs.R;
import com.jintoufs.activites.CheckStoreActivity;
import com.jintoufs.activites.InStoreListActivity;
import com.jintoufs.activites.OutStoreListActivity;
import com.jintoufs.activites.PrepareStoreActivity;
import com.jintoufs.activites.SupercargoActivity;
import com.jintoufs.api.remote.Api;
import com.jintoufs.base.BaseFragment;
import com.jintoufs.entity.User;
import com.jintoufs.utils.AjaxMsg;
import com.jintoufs.widget.GuidCard;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class Tab1Fragment extends BaseFragment {
	private User user;
	private AppContext appContext;
	private LinearLayout view;

	private GuidCard card1, card2, card3, card4, card5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = getApplication();
		user = appContext.getLoginUser();
	}

	private void InitCard() {

		card1 = new GuidCard(getActivity());
		card1.setTitle("入库");
		card1.setTip("数量:0个");
		card1.setImg(R.drawable.quick_option_take_out_nor);
		card1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), InStoreListActivity.class);
				intent.putExtra("caption", "入库");
				startActivity(intent);
			}
		});

		card2 = new GuidCard(getActivity());
		card2.setTitle("出库");
		card2.setTip("数量:0个");
		card2.setImg(R.drawable.quick_option_put_in_nor);
		card2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), OutStoreListActivity.class);
				intent.putExtra("caption", "出库");
				startActivity(intent);
			}
		});

		card3 = new GuidCard(getActivity());
		card3.setTitle("盘库");
		card3.setTip("数量:0个");
		card3.setImg(R.drawable.quick_option_scan_over);
		card3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), CheckStoreActivity.class);
				startActivity(intent);
			}
		});

		card4 = new GuidCard(getActivity());
		card4.setTitle("准备钞箱");
		card4.setTip("数量:0个");
		card4.setImg(R.drawable.quick_option_note_nor);
		card4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Tab1Fragment.this.getActivity(), PrepareStoreActivity.class);
				startActivity(intent);
			}
		});

		card5=new GuidCard(getActivity());
		card5.setTitle("押运人员专用");
		card5.setTip("");
		card5.setImg(R.drawable.sss_nor);
		card5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Tab1Fragment.this.getActivity(), SupercargoActivity.class);
				startActivity(intent);
			}
		});
		
		view.addView(card1);
		view.addView(card2);
		view.addView(card3);
		view.addView(card4);
		view.addView(card5);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout view1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab1, container, false);
		view = (LinearLayout) view1.findViewById(R.id.tab1_scroll);
		InitCard();
		return view1;
	}

	private Handler mRefreshHandler = new Handler();
	private Runnable mRefreshRunnable = new Runnable() {
		public void run() {
			refreshList();
			mRefreshHandler.postDelayed(this, 30000);
		}
	};

	private void refreshList() {
		Api.getInStoreCount(user, mInTaskHandler);
		Api.getOutStoreCount(user, mOutTaskHandler);
		Api.getCheckStoreCount(user, mCheckStoreHandler);
		Api.queryPrepareStoreTaskCount(user, mTomorrowHandler);
	}

	private final AsyncHttpResponseHandler mInTaskHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {

					Double count = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<Double>() {
					}.getType());

					if (count < 0) {
						return;
					}
					card1.setTip("数量:" + count.intValue() + "个");

				} else {
					AppContext.getInstance().cleanLoginInfo();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
			arg4.printStackTrace();
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
		}
	};

	private final AsyncHttpResponseHandler mOutTaskHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					Double count = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<Double>() {
					}.getType());

					if (count < 0) {
						return;
					}
					card2.setTip("数量:" + count.intValue() + "个");
				} else {
					AppContext.getInstance().cleanLoginInfo();

				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
			arg4.printStackTrace();
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
		}
	};

	private final AsyncHttpResponseHandler mCheckStoreHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					Double count = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<Double>() {
					}.getType());

					if (count < 0) {
						return;
					}
					card3.setTip("数量:" + count.intValue() + "个");
				} else {
					AppContext.getInstance().cleanLoginInfo();

				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
			arg4.printStackTrace();
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
		}
	};

	private final AsyncHttpResponseHandler mTomorrowHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, JSONObject response) {
			try {
				Gson gson = new Gson();
				AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
				if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
					Double count = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), new TypeToken<Double>() {
					}.getType());

					if (count < 0) {
						return;
					}
					card4.setTip("数量:" + count.intValue() + "个");
				} else {
					AppContext.getInstance().cleanLoginInfo();

				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int arg0, Header[] headers, String arg3, Throwable arg4) {
			arg4.printStackTrace();
		}

		@Override
		public void onFailure(int arg0, Header[] headers, Throwable arg4, JSONObject arg3) {
			arg4.printStackTrace();
		}
	};

	@Override
	public void onPause() {
		super.onPause();

		mRefreshHandler.removeCallbacks(mRefreshRunnable);
	}

	@Override
	public void onResume() {
		super.onResume();

		mRefreshHandler.postDelayed(mRefreshRunnable, 500);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

}
