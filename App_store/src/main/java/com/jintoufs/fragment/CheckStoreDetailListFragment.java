package com.jintoufs.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jintoufs.R;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.Constants;
import com.jintoufs.entity.Terminal;
import com.jintoufs.utils.UIHelper;
import com.jintoufs.widget.dialog.CommonDialog;
import com.jintoufs.widget.dialog.DialogHelper;

@SuppressLint("NewApi")
public class CheckStoreDetailListFragment extends ListFragment {
	private List<Cashbox> presidents = new ArrayList<Cashbox>();
	public MyAdapter myadapter = null;
	private int curSelPosition = -1;
	private FragmentManager manager;
	private String m_TaskType = "";

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

		Intent intent = this.getActivity().getIntent();
		if (intent.hasExtra("taskType")) {
			m_TaskType = intent.getStringExtra("taskType");
		} else {
			m_TaskType = "";
		}
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
		// curSelPosition = position;
		//
		// final Cashbox box = presidents.get(position);
		// if (box.getFoundStatus() == 0) {
		// CommonDialog dialog =
		// DialogHelper.getPinterestDialogCancelable(this.getActivity());
		// dialog.setTitle("标记");
		// dialog.setMessage("是否标记为读取状态？");
		// dialog.setNegativeButton(R.string.cancle, null);
		// dialog.setPositiveButton("标记", new DialogInterface.OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// box.setFoundStatus(Constants.FOUND_STATUS_YES);
		// myadapter.notifyDataSetChanged();
		// dialog.dismiss();
		// }
		// });
		// dialog.show();
		// }
	}

	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
	 */
	public void addItem(Cashbox cashbox) {
		presidents.add(cashbox);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 增加列表显示项
	 * 
	 * @param tagEPC
	 *            要显示的EPC信息
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

		public void updateSpecialItem(ListFragment lv, String ecp) {
			int position = 0;
			for (Cashbox box : presidents) {
				if (box.getCashBoxCode().equals(ecp)) {
					presidents.remove(position);
					MyAdapter.this.notifyDataSetChanged();
 				}
				position++;
			}

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
			holder.mCell.setText(cashbox.getCellCode());
			
			if(cashbox.getFoundStatus()==Constants.FOUND_STATUS_YES){
				holder.mState.setVisibility(View.VISIBLE);
			}else if(cashbox.getFoundStatus()==Constants.FOUND_STATUS_NO){
				holder.mState.setVisibility(View.GONE);
			}
			
			holder.mCell.setVisibility(View.VISIBLE);
			return convertView;
		}

		class ViewHolder {
			public TextView mCode;
			public TextView mCell;
			public ImageView mState;
		}
	}

}
