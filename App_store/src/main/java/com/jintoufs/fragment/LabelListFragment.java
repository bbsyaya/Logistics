package com.jintoufs.fragment;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.entity.Cashbox;

@SuppressLint("NewApi")
public class LabelListFragment extends ListFragment {

	private List<Cashbox> presidents = new ArrayList<Cashbox>();
	public MyAdapter myadapter = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.label_list_view, container, false);
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
	 * @param cashbox
	 */
	public void addItem(Cashbox cashbox) {
		presidents.add(cashbox);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 *
	 * @param cashboxs
	 */
	public void addAllItems(List<Cashbox> cashboxs) {
		presidents.addAll(cashboxs);
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
				convertView = mInflater.inflate(R.layout.label_list_item, null);
				holder.mName = (TextView) convertView.findViewById(R.id.tv_code);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Cashbox cashbox = presidents.get(position);
			holder.mName.setText(cashbox.getCashBoxCode());
			return convertView;
		}

		class ViewHolder {
			public TextView mName;
		}
	}
}
