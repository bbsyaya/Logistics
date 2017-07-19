package com.jintoufs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jintoufs.R;

//加钞操作
public class OperationView extends RelativeLayout {
	private TextView mTextView1;
	private TextView mTextView2;
	private ImageButton mButton;

	private CharSequence title;
	private CharSequence tip;
	private CharSequence status;
	private int bg;

	public OperationView(Context context) {
		super(context);
	}

	public OperationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 加载视图的布局
		View view = LayoutInflater.from(context).inflate(R.layout.operation, this, true);
		// 获取子控件
		mTextView1 = (TextView) view.findViewById(R.id.operation_title);
		mTextView2 = (TextView) view.findViewById(R.id.operation_tip);
		mButton = (ImageButton) view.findViewById(R.id.operation_action);

		// 将属性值设置到控件中
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OperationView);
		title = a.getText(R.styleable.OperationView_opetation_title);
		tip = a.getText(R.styleable.OperationView_opetation_tip);
		status = a.getText(R.styleable.OperationView_opetation_status);
		bg = a.getColor(R.styleable.OperationView_bg, 0xFFFFFFFF);
		if (!TextUtils.isEmpty(status) && status.toString().equals("1")) {
			mButton.setBackgroundResource(R.drawable.btn_radio_on);
		} else {
			mButton.setBackgroundResource(R.drawable.btn_radio_off);
		}
		view.setBackgroundColor(bg);
		a.recycle();
	}

	/**
	 * 此方法会在所有的控件都从xml文件中加载完成后调用
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (!TextUtils.isEmpty(title))
			setText1(title);
		if (!TextUtils.isEmpty(tip))
			setText2(tip);
	}

	public void setText1(CharSequence text) {
		mTextView1.setText(text);
	}

	public void setText2(CharSequence text) {
		mTextView2.setText(text);
	}
	public void refreshStatus(String state){
		if (!TextUtils.isEmpty(state) && state.toString().equals("1")) {
			mButton.setBackgroundResource(R.drawable.btn_radio_on);
		} else {
			mButton.setBackgroundResource(R.drawable.btn_radio_off);
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
