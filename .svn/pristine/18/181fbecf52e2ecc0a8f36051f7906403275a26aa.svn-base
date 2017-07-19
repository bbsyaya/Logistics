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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.activites.PrepareCashBoxActivity;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.Constants;

@SuppressLint("NewApi")
public class PrepareTomorrowDetailListFragment extends ListFragment {
	private List<Cashbox> presidents = new ArrayList<Cashbox>();
	public MyAdapter myadapter = null;
	public Long taskId;

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

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {

		final Cashbox cashbox = presidents.get(position);
		Intent intent = new Intent(this.getActivity(), PrepareCashBoxActivity.class);
		intent.putExtra("cashBox", cashbox);
		intent.putExtra("storeTaskId", taskId);
		startActivity(intent);
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
		presidents.clear();
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
				convertView = mInflater.inflate(R.layout.cashbox_list_item, null);
				holder.mCode = (TextView) convertView.findViewById(R.id.tv_code);
				holder.mCell = (TextView) convertView.findViewById(R.id.tv_cell);
				holder.mState = (ImageView) convertView.findViewById(R.id.iv_cashbox_state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Cashbox cashbox = presidents.get(position);
			holder.mCode.setText(cashbox.getCashBoxCode());
			holder.mCell.setText(cashbox.getType());
			holder.mCell.setVisibility(View.VISIBLE);
			if (cashbox.getStatus() == Constants.PREPARE_STATUS_YES) {
				holder.mState.setVisibility(View.VISIBLE);
			} else if (cashbox.getStatus() == Constants.PREPARE_STATUS_NO) {
				holder.mState.setVisibility(View.GONE);
			}
			return convertView;
		}

		class ViewHolder {
			public TextView mCode;
			public TextView mCell;
			public ImageView mState;
		}
	}

}
