package com.jintoufs.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.base.BaseActivity;

public class RecordListActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		TextView mHeaderTitle=(TextView) findViewById(R.id.header_title);
		Intent intent = getIntent();
		String caption = intent.getStringExtra("caption");
		mHeaderTitle.setText(caption);
	}
	
	public void addData(View view){
		Intent intent=new Intent(this, ScanDataActivity.class);
		startActivity(intent);
	}
}
