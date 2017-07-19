package com.jintoufs.logstics.activites;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.utils.Exit;

public class MainActivity extends FragmentActivity {
	Calendar calendar = Calendar.getInstance();
	private ViewPager mTabPager;
	private ImageView mTabImg;//
	private ImageView mTab1, mTab2, mTab3;
	private TextView mTabTxt1, mTabTxt2, mTabTxt3;
	private int zero = 0;//
	private int currIndex = 0;
	private int one;
	private int two;
	private View view1;
	private View view2;
	private View view3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppContext.getInstance().addActivity(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mTabPager = (ViewPager) findViewById(R.id.tabpager);

		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mTab1 = (ImageView) findViewById(R.id.Tab1);
		mTab2 = (ImageView) findViewById(R.id.Tab2);
		mTab3 = (ImageView) findViewById(R.id.Tab3);
		mTabTxt1 = (TextView) findViewById(R.id.Tab1_txt);
		mTabTxt2 = (TextView) findViewById(R.id.Tab2_txt);
		mTabTxt3 = (TextView) findViewById(R.id.Tab3_txt);

		mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));

		mTabTxt1.setOnClickListener(new MyOnClickListener(0));
		mTabTxt2.setOnClickListener(new MyOnClickListener(1));
		mTabTxt3.setOnClickListener(new MyOnClickListener(2));

		Display currDisplay = getWindowManager().getDefaultDisplay();//
		int displayWidth = currDisplay.getWidth();
		one = displayWidth / 3; //
		two = one * 2;

		// InitImageView();
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.main_tab_1, null);
		view2 = mLi.inflate(R.layout.main_tab_2, null);
		view3 = mLi.inflate(R.layout.main_tab_3, null);

		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);
		mTabPager.setCurrentItem(0);
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_16_ov));
				mTabTxt1.setTextColor(Color.rgb(92, 184, 47));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, zero, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_17));
					mTabTxt2.setTextColor(Color.GRAY);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, zero, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_18));
					mTabTxt3.setTextColor(Color.GRAY);
				}
				break;

			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_17_ov));
				mTabTxt2.setTextColor(Color.rgb(92, 184, 47));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_16));
					mTabTxt1.setTextColor(Color.GRAY);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_18));
					mTabTxt3.setTextColor(Color.GRAY);
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_18_ov));
				mTabTxt3.setTextColor(Color.rgb(92, 184, 47));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_16));
					mTabTxt1.setTextColor(Color.GRAY);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.pic_sy_17));
					mTabTxt2.setTextColor(Color.GRAY);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Exit.exitBy2Click(this);
		}
		return false;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 大库交接
	 * */
	public void comfrimEscortTask(View view) {
		Intent intent = new Intent(MainActivity.this, ConfirmEscortTaskActivity.class);
		startActivity(intent);
	}

	/**
	 * 扫描上车
	 * */
	public void confirmInCarTask(View view) {
		Intent intent = new Intent(MainActivity.this, ConfirmInCarTaskActivity.class);
		startActivity(intent);
	}
	/**
	 * 扫描上车
	 * */
	public void confirmOutCarTask(View view) {
		Intent intent = new Intent(MainActivity.this, ConfirmOutCarTaskActivity.class);
		startActivity(intent);
	}
}
