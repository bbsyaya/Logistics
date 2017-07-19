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
import com.jintoufs.activites.PrepareTomorrowDetailActivity;
import com.jintoufs.entity.Task;

@SuppressLint("NewApi")
public class PrepareTomorrowListFragment extends ListFragment {
	private List<Task> presidents = new ArrayList<Task>();
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
	 * 弹出对话框，手动标记为已读取
	 * */
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Task task = presidents.get(position);

		Intent intent = new Intent(this.getActivity(), PrepareTomorrowDetailActivity.class);
		intent.putExtra("taskId", task.getId());
		intent.putExtra("routeName", task.getRouteName());
		intent.putExtra("keyStation", task.getKeyStation());
		intent.putExtra("passwordStation", task.getPasswordStation());
		startActivity(intent);
	}

	/**
	 * 增加列表显示项
	 *
	 * @param task
	 */
	public void addItem(Task task) {
		presidents.add(task);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 *
	 * @param tasks
	 */
	public void addAllItems(List<Task> tasks) {
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
				convertView = mInflater.inflate(R.layout.route_list_item, null);
				holder.tvKeyStation = (TextView) convertView.findViewById(R.id.tv_key);
				holder.tvPasswordStation = (TextView) convertView.findViewById(R.id.tv_password);
				holder.tvRouteName = (TextView) convertView.findViewById(R.id.tv_route_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Task task = presidents.get(position);
			holder.tvKeyStation.setText(task.getKeyStation());
			holder.tvPasswordStation.setText(task.getPasswordStation());
			if (!"".equals(task.getRouteName())) {
				holder.tvRouteName.setText(task.getRouteName());
			} else {
				holder.tvRouteName.setText("其它任务");
			}
			return convertView;
		}

		class ViewHolder {
			public TextView tvKeyStation;
			public TextView tvPasswordStation;
			public TextView tvRouteName;
		}
	}

}
