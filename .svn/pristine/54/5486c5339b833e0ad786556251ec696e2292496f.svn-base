package com.jintoufs.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jintoufs.R;
import com.jintoufs.interf.BaseViewInterface;
import com.jintoufs.widget.Accompaniment;
import com.jintoufs.widget.dialog.CommonToast;
import com.jintoufs.widget.dialog.DialogControl;
import com.jintoufs.widget.dialog.DialogHelper;
import com.jintoufs.widget.dialog.WaitDialog;

/**
 * baseActionBar Activity
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 上午11:30:15 引用自：tonlin
 */
public abstract class BaseActivity extends Activity implements DialogControl{
	public static final String INTENT_ACTION_EXIT_APP = "INTENT_ACTION_EXIT_APP";
	public Accompaniment accompaniment = new Accompaniment(this, R.raw.tag_inventoried);
	private boolean _isVisible;
	private WaitDialog _waitDialog;

	protected LayoutInflater mInflater;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onBeforeSetContentLayout();
		if (getLayoutId() != 0) {
			setContentView(getLayoutId());
		}
		mInflater = getLayoutInflater();

		init(savedInstanceState);
		_isVisible = true;
		accompaniment.init();
	}

	protected void onBeforeSetContentLayout() {
	}

	protected boolean hasActionBar() {
		return true;
	}

	protected int getLayoutId() {
		return 0;
	}

	protected View inflateView(int resId) {
		return mInflater.inflate(resId, null);
	}

	protected int getActionBarTitle() {
		return R.string.app_name;
	}

	protected boolean hasBackButton() {
		return false;
	}

	protected int getActionBarCustomView() {
		return 0;
	}

	protected boolean haveSpinner() {
		return false;
	}

	protected void init(Bundle savedInstanceState) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void showToast(int msgResid, int icon, int gravity) {
		showToast(getString(msgResid), icon, gravity);
	}

	public void showToast(String message, int icon, int gravity) {
		CommonToast toast = new CommonToast(this);
		toast.setMessage(message);
		toast.setMessageIc(icon);
		toast.setLayoutGravity(gravity);
		toast.show();
	}

	@Override
	public WaitDialog showWaitDialog() {
		return showWaitDialog(R.string.loading);
	}

	@Override
	public WaitDialog showWaitDialog(int resid) {
		return showWaitDialog(getString(resid));
	}

	@Override
	public WaitDialog showWaitDialog(String message) {
		if (_isVisible) {
			if (_waitDialog == null) {
				_waitDialog = DialogHelper.getWaitDialog(this, message);
			}
			if (_waitDialog != null) {
				_waitDialog.setMessage(message);
				_waitDialog.show();
			}
			return _waitDialog;
		}
		return null;
	}

	@Override
	public void hideWaitDialog() {
		if (_isVisible && _waitDialog != null) {
			try {
				_waitDialog.dismiss();
				_waitDialog = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {

		// setOverflowIconVisible(featureId, menu);
		return super.onMenuOpened(featureId, menu);
	}

	public void back(View view) {
		this.finish();
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T BindControl(int id){
		return (T)findViewById(id);
	}
}
