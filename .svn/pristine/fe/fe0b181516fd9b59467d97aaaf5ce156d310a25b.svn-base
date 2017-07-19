package com.jintoufs.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.activites.ConfirmTaskActivity;
import com.jintoufs.entity.Task;

@SuppressLint("NewApi")
public class OutStoreListFragment extends ListFragment {
	private List<Task> presidents = new ArrayList<Task>();
	public MyAdapter myadapter = null;
	private int curSelPosition = -1;
	private FragmentManager manager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		manager = getFragmentManager();
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

	@SuppressLint("NewApi")
	public void onListItemClick(ListView parent, View v, int position, long id) {
		// 记录当前选中的标签位置
		curSelPosition = position;
		final Task outStoreTask = presidents.get(position);
		
		Intent intent=new Intent(this.getActivity(),ConfirmTaskActivity.class);
		intent.putExtra("caption", outStoreTask.getKeyStation()+"&"+outStoreTask.getPasswordStation());
		intent.putExtra("taskType", "出库");
		
		intent.putExtra("keyId", outStoreTask.getKeyId());
		intent.putExtra("passwordId",outStoreTask.getPasswordId());
		
		startActivity(intent);
	}

	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
	 */
	public void addItem(Task task) {
		presidents.add(task);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
	 */
	public void addItems(List<Task> tasks) {
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

		@SuppressLint({ "NewApi", "ResourceAsColor", "UseValueOf" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.outstore_list_item, null);
				holder.mName = (TextView) convertView.findViewById(R.id.tv_name);
				//holder.mState = (ImageView) convertView.findViewById(R.id.iv_site_state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Task task = presidents.get(position);
			
			holder.mName.setText("业务员:"+task.getKeyStation()+" "+task.getPasswordStation());
			//holder.mStation.setText("月台："+task.getPlatform());	
			//holder.mBatch.setText("批次号："+task.getBatchNum()+"("+new Double(task.getSequence()).intValue()+")");
			//holder.mBatch.setText("批次号："+task.getBatchNum());
			//holder.mCount.setText("数量："+String.valueOf(task.getCashboxAmount()));
			//holder.mOperator.setText("业务员："+task.getKeyStation()+" "+task.getPasswordStation());
			return convertView;
		}

		class ViewHolder {
			public TextView mName;
		}
	}

}
