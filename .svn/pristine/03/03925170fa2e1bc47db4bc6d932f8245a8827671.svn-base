package com.jintoufs.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.activites.PrepareTomorrowActivity;
import com.jintoufs.entity.StoreTask;

@SuppressLint("NewApi")
public class PrepareStoreListFragment extends ListFragment {
	private List<StoreTask> presidents = new ArrayList<StoreTask>();
	public MyAdapter myadapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.terminal_list_view, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
	 *
	 * */
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		StoreTask storeTask = presidents.get(position);

		Intent intent = new Intent(this.getActivity(), PrepareTomorrowActivity.class);
		intent.putExtra("storeTask", storeTask);
		startActivity(intent);
	}

	/**
	 * 增加列表显示项
	 *
	 * @param storeTask
	 */
	public void addItem(StoreTask storeTask) {
		presidents.add(storeTask);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 *
	 * @param tasks
	 */
	public void addAllItems(List<StoreTask> tasks) {
		presidents.clear();
		presidents.addAll(tasks);
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
				convertView = mInflater.inflate(R.layout.prepare_tomorrow_list_item, null);
				holder.mCode = (TextView) convertView.findViewById(R.id.tv_date);
				holder.mNum = (TextView) convertView.findViewById(R.id.tv_num);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			StoreTask storeTask = presidents.get(position);
			holder.mCode.setText(storeTask.getDate());
			if (storeTask.getTasks() == null) {
				holder.mNum.setText("（0个任务）");
			} else {
				holder.mNum.setText("（" + storeTask.getTasks().size() + "个 任务）");
			}

			return convertView;
		}

		class ViewHolder {
			public TextView mCode;
			public TextView mNum;
		}
	}
}
