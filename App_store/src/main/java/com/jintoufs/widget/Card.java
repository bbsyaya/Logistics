package com.jintoufs.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jintoufs.R;

public class Card extends RelativeLayout {
	private TextView mTextView1;
	private TextView mTextView2;
	private TextView mTextView3;
	private TextView mTextView4;
	private TextView m_TextView2Title;
	private TextView m_TextView3Title;
	private TextView m_TextView4Title;
	
	
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
		
		m_TextView2Title=(TextView)findViewById(R.id.textView2Title);
		m_TextView3Title=(TextView)findViewById(R.id.textView3Title);
		m_TextView4Title=(TextView)findViewById(R.id.textView4Title);
		
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
			setText2(text2,true);
		if (!TextUtils.isEmpty(text3))
			setText3(text3,true);
		if (!TextUtils.isEmpty(text4))
			setText4(text4,true);
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

	public void setText2(String text,boolean bCanSee) {
		mTextView2.setText(text);
		
		if(bCanSee){
			mTextView2.setVisibility(View.VISIBLE);
		}else{
			mTextView2.setVisibility(View.GONE);
		}
	}

	public void setText3(String text,boolean bCanSee) {
		mTextView3.setText(text);
		
		if(bCanSee){
			mTextView3.setVisibility(View.VISIBLE);
		}else{
			mTextView3.setVisibility(View.GONE);
		}
	}

	public void setText4(String text,boolean bCanSee) {
		mTextView4.setText(text);
		
		if(bCanSee){
			mTextView4.setVisibility(View.VISIBLE);
		}else{
			mTextView4.setVisibility(View.GONE);
		}
	}
	
	public void setText2Title(String text,boolean bCanSee){
		m_TextView2Title.setText(text);
		
		if(bCanSee){
			m_TextView2Title.setVisibility(View.VISIBLE);
		}else{
			m_TextView2Title.setVisibility(View.GONE);
		}
	}
	
	public void setText3Title(String text,boolean bCanSee){
		m_TextView3Title.setText(text);
		
		if(bCanSee){
			m_TextView3Title.setVisibility(View.VISIBLE);
		}else{
			m_TextView3Title.setVisibility(View.GONE);
		}
	}
	
	public void setText4Title(String text,boolean bCanSee){
		m_TextView4Title.setText(text);
		
		if(bCanSee){
			m_TextView4Title.setVisibility(View.VISIBLE);
		}else{
			m_TextView4Title.setVisibility(View.GONE);
		}
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
