package com.jintoufs.logstics.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.entity.Cashbox;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.widget.dialog.CommonDialog;
import com.jintoufs.logstics.widget.dialog.DialogHelper;

@SuppressLint("NewApi")
public class CashboxListFragment extends ListFragment {
	private List<Cashbox> presidents = new ArrayList<Cashbox>();
	public MyAdapter myadapter = null;
	private int year,month,day;
	
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
		final Cashbox box = presidents.get(position);
		/*if (box.getType().equals(Constants.TASK_TYPE_DEVELIVER)&&box.getFoundStatus() == 0) {
			CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(this.getActivity());
			dialog.setTitle("标记");
			dialog.setMessage("是否标记为读取状态？");
			dialog.setNegativeButton(R.string.cancle, null);
			dialog.setPositiveButton("标记", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					box.setFoundStatus(Constants.FOUND_STATUS_YES);
					myadapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			});
			dialog.show();			
		}
		else if(box.getType().equals(Constants.TASK_TYPE_RECEIVE))*/
		
		if(box.getType()!=null && box.getType().equals(1)){
			final View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.handin_cash_setting, null);
			
			AlertDialog setCashDialog = new AlertDialog.Builder(this.getActivity()).setIcon(android.R.drawable.btn_star).setTitle("设置上缴金额").setView(view)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int i) {
							EditText txtCash = (EditText) view.findViewById(R.id.txtCash);
							EditText txtReCash = (EditText) view.findViewById(R.id.txtReCash);
							
							String cashStr,reCashStr;
							
							cashStr = txtCash.getText().toString();
							reCashStr = txtReCash.getText().toString();
							
							if(cashStr==null || reCashStr==null || reCashStr.isEmpty() || cashStr.isEmpty()){
								AppContext.showToast("金额输入有误!");
								return;
							}
							
							Double cash=null;
							Double reCash=null;
							
							try
							{
								cash = Double.valueOf(cashStr);
								reCash = Double.valueOf(reCashStr);
							}catch(Exception ex){
								AppContext.showToast("金额输入有误!");
								return;
							}
							
							if(!IsDoubleEquals(cash,reCash)){
								AppContext.showToast("两次输入金额不同!");
								return;
							}
							
							box.setCash(cash);
							myadapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					}).setNegativeButton("取消", null).create();
			
			setCashDialog.show();
			
			return;
		}
		
		if(box.getTaskType()!=null && box.getTaskType().equals(Constants.TASK_TYPE_RECEIVE))
		{
			CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(this.getActivity());
			//final EditText txtTime = new EditText(this.getActivity());
			final DatePicker datePicker=new DatePicker(this.getActivity());
			
			
			
			OnDateChangedListener onDateChangedEvent=new OnDateChangedListener(){

				@Override
				public void onDateChanged(DatePicker arg0,
						int arg1, int arg2, int arg3) {
					// TODO 自动生成的方法存根
					year=arg1;
					month=arg2;
					day=arg3;
				}
		
			};
			
			new SimpleDateFormat("yyyy-MM-dd");
			if(box.getBackTime() != null)
			{
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(box.getBackTime());
				datePicker.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
						, onDateChangedEvent);
			}
			else
			{
				Calendar calendar=Calendar.getInstance();
				datePicker.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 
						onDateChangedEvent);
			}
			dialog.setTitle("设置送箱时间");
			dialog.setContent(datePicker);
			dialog.setNegativeButton(R.string.cancle, null);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar=Calendar.getInstance();
					calendar.set(year, month, day);
					Date date = calendar.getTime();
					//date = fmt.parse(St);
					
					box.setBackTime(date);
					myadapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			});
			dialog.show();
		}
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
	
	private boolean IsDoubleEquals(Double a , Double b){
		double x=a.doubleValue();
		double y=b.doubleValue();
		
		if( (x-y) > -0.0001 && (x-y)<0.0001){
			return true;
		}
		
		return false;
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
			return presidents.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void updateSpecialItem(ListFragment lv, String ecp) {
			int position = 0;
			for (Cashbox box : presidents) {
				if (box.getRfid().equals(ecp)) {
					presidents.get(position).setFoundStatus(Constants.FOUND_STATUS_YES);
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
				holder.mTips = (TextView) convertView.findViewById(R.id.tv_Tips);
				holder.mState = (ImageView) convertView.findViewById(R.id.iv_cashbox_state);
				holder.mTaskType=(TextView) convertView.findViewById(R.id.tv_task_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Cashbox cashbox = presidents.get(position);
			holder.mCode.setText(cashbox.getCashBoxCode());
			holder.mTaskType.setText(cashbox.getCaption());
			holder.mTips.setVisibility(View.GONE);
			if (cashbox.getFoundStatus() == Constants.FOUND_STATUS_NO) {
				holder.mState.setVisibility(View.GONE);
			} else if(cashbox.getFoundStatus() == Constants.FOUND_STATUS_YES){
				holder.mState.setImageResource(R.drawable.ok);
				holder.mState.setVisibility(View.VISIBLE);
			} else if(cashbox.getFoundStatus() == Constants.FOUND_STATUS_NOTPLAN){
				holder.mState.setImageResource(R.drawable.alert);
				holder.mState.setVisibility(View.VISIBLE);
			}
			
//			if(cashbox.getType()!=null && cashbox.getType().equals(1) && (cashbox.getAmount()==null || cashbox.getAmount().equals(""))){
//				holder.mTips.setVisibility(View.VISIBLE);
//			}else{
//				holder.mTips.setVisibility(View.GONE);
//			}

			return convertView;
		}

		class ViewHolder {
			public TextView mCode;
			public TextView mTips;
			public TextView mTaskType;
			public ImageView mState;
		}
	}

}
