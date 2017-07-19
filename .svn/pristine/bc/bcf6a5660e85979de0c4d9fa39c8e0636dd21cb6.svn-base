package com.jintoufs.logstics.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jintoufs.logstics.R;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Key;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class KeyListFragment extends ListFragment {

	private List<Key> presidents = new ArrayList<Key>();
	public MyAdapter myadapter = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.terminal_list_view, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myadapter = new MyAdapter(getActivity());
		setListAdapter(myadapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getListView().setVerticalScrollBarEnabled(true);
	}
	
	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
	 */
	public void addItem(Key key) {
		presidents.add(key);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
	 */
	public void addAllItems(List<Key> keys) {
		presidents.addAll(keys);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 清除列表的显示内容
	 */
	public void clearItem() {
		presidents.clear();
		myadapter.notifyDataSetChanged();
	}
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return presidents.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "NewApi", "ResourceAsColor" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.cashbox_list_item, parent, false);
				holder.code = (TextView) convertView.findViewById(R.id.tv_code);
				holder.name = (TextView) convertView.findViewById(R.id.tv_Tips);
				holder.state = (ImageView) convertView.findViewById(R.id.iv_cashbox_state);
				holder.type=(TextView) convertView.findViewById(R.id.tv_task_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Key key = presidents.get(position);
			holder.code.setText(key.getCode());
			holder.name.setText(key.getName());
			holder.type.setText(key.getKeyType());
			if (key.getStatus() == Constants.FOUND_STATUS_NO) {
				holder.state.setVisibility(View.INVISIBLE);
			} else if(key.getStatus() == Constants.FOUND_STATUS_YES){
				holder.state.setImageResource(R.drawable.ok);
				holder.state.setVisibility(View.VISIBLE);
			} 
			return convertView;
		}

		class ViewHolder {
			public TextView code;
			public TextView name;
			public TextView type;
			public ImageView state;
		}
	}
}
