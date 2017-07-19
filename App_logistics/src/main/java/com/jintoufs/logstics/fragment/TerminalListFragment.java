package com.jintoufs.logstics.fragment;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jintoufs.logstics.R;
import com.jintoufs.logstics.activites.ConfirmActivity;
import com.jintoufs.logstics.activites.EquipmentActivity;
import com.jintoufs.logstics.db.EquipmentDao;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Equipment;
import com.jintoufs.logstics.entity.Terminal;

@SuppressLint("NewApi")
public class TerminalListFragment extends ListFragment {
	
	private List<Terminal> presidents = new ArrayList<Terminal>();
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

	@SuppressLint("NewApi")
	public void onListItemClick(ListView parent, View v, int position, long id) {
		// 记录当前选中的标签位置
		final Terminal site = presidents.get(position);
		String siteType = String.valueOf(site.getType());
		Intent intent = new Intent(getActivity(), ConfirmActivity.class);
		if (siteType.equals(Constants.BUSINESS_TYPE_2)) {

		} else if (siteType.equals(Constants.BUSINESS_TYPE_6)) {
			intent = new Intent(getActivity(), EquipmentActivity.class);
		} else if (siteType.equals(Constants.BUSINESS_TYPE_8)) {

		} else {
			intent = new Intent(getActivity(), ConfirmActivity.class);
			intent.putExtra("deliverType", site.getDeliverType());
		}

		// 新建Bundle对象
		Bundle mBundle = new Bundle();
		// 放入site对象
		mBundle.putSerializable("site", site);
		intent.putExtras(mBundle);
		startActivity(intent);

	}

	/**
	 * 增加列表显示项
	 *
	 * @param site
	 */
	public void addItem(Terminal site) {
		presidents.add(site);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 *
	 * @param sites
	 */
	public void addItems(List<Terminal> sites) {
		presidents.addAll(sites);
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
				convertView = mInflater.inflate(R.layout.terminal_list_item, null);
				holder.mName = (TextView) convertView.findViewById(R.id.tv_name);
				holder.mAddr = (TextView) convertView.findViewById(R.id.tv_addr);
				holder.mSiteName=(TextView) convertView.findViewById(R.id.site_name);
				holder.mState = (ImageView) convertView.findViewById(R.id.iv_site_state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Terminal site = presidents.get(position);
			String typeStr = String.valueOf(site.getType());
			if (Constants.BUSINESS_TYPE_6.equals(typeStr) || Constants.BUSINESS_TYPE_8.equals(typeStr)) {
				holder.mName.setText(site.getCode());
				holder.mSiteName.setVisibility(View.VISIBLE);
				holder.mSiteName.setText("("+site.getSiteShort()+")");
				EquipmentDao equipmentDao=new EquipmentDao(getActivity());
				Equipment equipment=equipmentDao.queryStatus(site.getDetailId());
				if (equipment.getFinishedStatus().equals(Constants.FINISHED_STATUS)){
					holder.mState.setVisibility(View.VISIBLE);
					holder.mState.setImageResource(R.drawable.ok);
				}
			} else {
				holder.mName.setText(site.getName());
				holder.mSiteName.setVisibility(View.GONE);
			}

			if(site.getStatus()==Constants.TERMINAL_STATUS_FINISHED){
				holder.mState.setVisibility(View.VISIBLE);
				holder.mState.setImageResource(R.drawable.ok);
			}

			holder.mAddr.setText(site.getAddress());
			return convertView;
		}

		class ViewHolder {
			public TextView mName;
			public TextView mAddr;
			public TextView mSiteName;
			public ImageView mState;
		}
	}

}
