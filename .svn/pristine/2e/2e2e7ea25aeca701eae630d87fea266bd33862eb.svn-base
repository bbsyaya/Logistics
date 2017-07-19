package com.jintoufs.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.activites.ConfirmTaskActivity;
import com.jintoufs.entity.Constants;
import com.jintoufs.entity.Task;

@SuppressLint("NewApi")
public class InStoreListFragment extends ListFragment {
	private List<Task> presidents = new ArrayList<Task>();
	public MyAdapter myadapter = null;
	private int curSelPosition = -1;
	private FragmentManager manager;
	
	private int taskChoose=-1;
	
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
		final Task inStoreTask = presidents.get(position);
		
		Intent intent=new Intent(this.getActivity(),ConfirmTaskActivity.class);
		intent.putExtra("caption", inStoreTask.getKeyStation()+"&"+inStoreTask.getPasswordStation());
		if(inStoreTask.getStatus()==Constants.ROUTE_TASK_STATUS_0){
			intent.putExtra("taskType", "交接");
		}else if(inStoreTask.getStatus()==Constants.ROUTE_TASK_STATUS_2){
			intent.putExtra("taskType", "入库");
		}else{
			return;
		}
		
		intent.putExtra("keyId", inStoreTask.getKeyId());
		intent.putExtra("passwordId", inStoreTask.getPasswordId());
		
		startActivity(intent);
	}
	
	private void showChooseAlertDialog(final Task task){
		
		taskChoose=0;
		
		Builder builder=new Builder(this.getActivity());
		builder.setTitle("选择任务");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setSingleChoiceItems(new String[]{"入库","交接"},0,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO 自动生成的方法存根
				taskChoose=arg1;
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO 自动生成的方法存根
				showChooseAlertDialogCallback(task);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	
	private void showChooseAlertDialogCallback(Task task){
		
		Intent intent=new Intent(this.getActivity(),ConfirmTaskActivity.class);
		
		if(taskChoose==0){
			intent.putExtra("taskType", "入库");
			//intent.putExtra("caption", task.getRouteName());
		}else if(taskChoose==1){
			intent.putExtra("taskType", "交接");
			//intent.putExtra("caption", task.getRouteName());
		}else{
			return;
		}
		
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

		@SuppressLint({ "NewApi", "ResourceAsColor" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.instore_list_item, null);
				holder.mName = (TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Task task = presidents.get(position);
			holder.mName.setText("业务员："+task.getKeyStation()+" "+task.getPasswordStation());
			return convertView;
		}

		class ViewHolder {
			public TextView mName;
		}
	}

}
