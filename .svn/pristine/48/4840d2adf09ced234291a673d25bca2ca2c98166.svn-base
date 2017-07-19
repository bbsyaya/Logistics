package com.jintoufs.logstics.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jintoufs.logstics.R;

public class Card extends RelativeLayout {
	private TextView mTextView1;
	private TextView mTextView2;
	private TextView mTextView3;
	private TextView mTextView4;
	private ImageView mImageView;

	private String text1;
	private String text2;
	private String text3;
	private String text4;

	public Card(Context context) {
		super(context);
		// 加载视图的布局
		LayoutInflater.from(context).inflate(R.layout.card, this, true);
		// 获取子控件
		mTextView1 = (TextView) findViewById(R.id.textView1);
		mTextView2 = (TextView) findViewById(R.id.textView2);
		mTextView3 = (TextView) findViewById(R.id.textView3);
		mTextView4 = (TextView) findViewById(R.id.textView4);
		mImageView = (ImageView) findViewById(R.id.iv_state);
	}

	/**
	 * 此方法会在所有的控件都从xml文件中加载完成后调用
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (!TextUtils.isEmpty(text1))
			setText1(text1);
		if (!TextUtils.isEmpty(text2))
			setText2(text2);
		if (!TextUtils.isEmpty(text3))
			setText3(text3);
		if (!TextUtils.isEmpty(text4))
			setText4(text4);
	}

	public void setText1(String text) {
		mTextView1.setText(text);
	}
	public void setState(int status) {
		if(status==0){
			mImageView.setVisibility(View.VISIBLE);
		}else{
			mImageView.setVisibility(View.GONE);
		}
	}

	public void setText2(String text) {
		mTextView2.setText(text);
	}

	public void setText3(String text) {
		mTextView3.setText(text);
	}

	public void setText4(String text) {
		mTextView4.setText(text);
	}

	/**
	 * 设置按钮点击事件监听器
	 * 
	 * @param listener
	 */
	public void setOnHeaderClickListener(OnClickListener listener) {
		this.setOnClickListener(listener);
	}
}
