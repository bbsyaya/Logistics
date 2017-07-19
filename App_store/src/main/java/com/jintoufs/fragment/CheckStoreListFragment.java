package com.jintoufs.fragment;

import java.text.SimpleDateFormat;
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
import com.jintoufs.activites.CheckStoreDetailActivity;
import com.jintoufs.entity.CheckStore;
import com.jintoufs.entity.Constants;

/**
 * 盘库列表
 * */
@SuppressLint("NewApi")
public class CheckStoreListFragment extends ListFragment {
	private List<CheckStore> presidents = new ArrayList<CheckStore>();
	public MyAdapter myadapter = null;
	private int curSelPosition = -1;
	private FragmentManager manager;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

	/**
	 * 弹出对话框，手动标记为已读取
	 * */
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		// 记录当前选中的标签位置
		curSelPosition = position;
		final CheckStore checkStore = presidents.get(position);
		Intent intent = new Intent(this.getActivity(), CheckStoreDetailActivity.class);
		intent.putExtra("checkId", checkStore.getId());
		intent.putExtra("createTime", checkStore.getCreateTime());
		startActivity(intent);
	}

	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
	 */
	public void addItem(CheckStore entity) {
		presidents.add(entity);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
	 */
	public void addAllItems(List<CheckStore> checkStores) {
		presidents.addAll(checkStores);
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
				convertView = mInflater.inflate(R.layout.check_store_list_item, null);
				holder.mCode = (TextView) convertView.findViewById(R.id.tv_code);
				holder.mState = (ImageView) convertView.findViewById(R.id.iv_state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CheckStore checkStore = presidents.get(position);
			holder.mCode.setText(checkStore.getCreateTime());
			if (checkStore.getStatus() == Constants.CHECK_STORE_0) {
				holder.mState.setVisibility(View.GONE);
			} else if (checkStore.getStatus() == Constants.CHECK_STORE_1) {
				holder.mState.setImageResource(R.drawable.ok);
				holder.mState.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			public TextView mCode;
			public ImageView mState;
		}
	}

}
