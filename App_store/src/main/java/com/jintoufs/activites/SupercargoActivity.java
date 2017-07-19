package com.jintoufs.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jintoufs.R;
import com.jintoufs.base.BaseActivity;
import com.jintoufs.widget.GuidCard;

public class SupercargoActivity extends BaseActivity{

	LinearLayout view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supercargo);
		view = (LinearLayout) findViewById(R.id.card_scroll);
		view.removeAllViews();
		
		GuidCard gc0 = new GuidCard(this);
		GuidCard gc1 = new GuidCard(this);
		gc0.setTitle("入库");
		gc0.setTip("将物品送回库房");
		gc0.setImg(R.drawable.quick_option_take_out_nor);
		gc0.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(SupercargoActivity.this, ScanDataActivity.class);
				intent.putExtra("caption", "入库");
				startActivity(intent);
			}
		});
		
		gc1.setTitle("出库");
		gc1.setTip("从库房提取物品");
		gc1.setImg(R.drawable.quick_option_put_in_nor);
		gc1.setOnHeaderClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(SupercargoActivity.this, ScanDataActivity.class);
				intent.putExtra("caption", "出库");
				startActivity(intent);
			}
		});
		view.addView(gc0);
		view.addView(gc1);
	}
}
