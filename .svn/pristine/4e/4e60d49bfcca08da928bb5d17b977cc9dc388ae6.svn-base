package com.jintoufs.widget;

import com.jintoufs.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class GuidCard extends RelativeLayout {
	private TextView m_txtTitle;
	private TextView m_txtTip;
	private ImageView m_imgType;

	public GuidCard(Context context) {
		super(context);
		// 加载视图的布局
		LayoutInflater.from(context).inflate(R.layout.guid_card, this, true);
		// 获取子控件
		m_txtTitle = (TextView) findViewById(R.id.txtTitle);
		m_txtTip = (TextView) findViewById(R.id.txt_tip);
		m_imgType=(ImageView) findViewById(R.id.imgType);
	}

	public void setTitle(String text){
		m_txtTitle.setText(text);
	}
	public void setTip(String text){
		m_txtTip.setText(text);
	}
	
	public void setImg(int rid){
		m_imgType.setImageResource(rid);
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
