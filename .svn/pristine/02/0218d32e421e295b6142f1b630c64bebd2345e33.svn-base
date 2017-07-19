package com.jintoufs.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.entity.Constants;

public class WightUser extends RelativeLayout {
	private TextView mTextView1;
	private TextView mTextView2;
	private AvatarView mAvatarView;

	private String text1;
	private String text2;
	private String url;

	public WightUser(Context context) {
		super(context);
		// 加载视图的布局
		LayoutInflater.from(context).inflate(R.layout.wight_user, this, true);
		// 获取子控件
		mTextView1 = (TextView) findViewById(R.id.textView1);
		mTextView2 = (TextView) findViewById(R.id.textView2);
		mAvatarView = (AvatarView) findViewById(R.id.user_face);
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
		mAvatarView.setAvatarUrl(Constants.ftpUrl + url);
	}

	public void setText1(String text) {
		mTextView1.setText(text);
	}

	public void setText2(String text) {
		mTextView2.setText(text);
	}

	public void setAvatarUrl(String url) {
		mAvatarView.setAvatarUrl(Constants.ftpUrl + url);
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
