package com.jintoufs.logstics.utils;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.R;
import com.jintoufs.logstics.api.ApiHttpClient;
import com.jintoufs.logstics.api.remote.Api;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.Update;
import com.jintoufs.logstics.widget.dialog.CommonDialog;
import com.jintoufs.logstics.widget.dialog.DialogHelper;
import com.jintoufs.logstics.widget.dialog.WaitDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 更新管理类
 * 
 */

public class UpdateManager {

	private Update mUpdate;

	private Context mContext;

	private boolean isShow = false;

	private WaitDialog _waitDialog;

	private AsyncHttpResponseHandler mCheckUpdateHandle = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int arg0, Header[] arg1, org.json.JSONObject response) {
			hideCheckDialog();
			Gson gson = new Gson();
			AjaxMsg ajaxMsg = gson.fromJson(response.toString(), AjaxMsg.class);
			if (ajaxMsg.getCode().equals(AjaxMsg.SUCCESS)) {
				mUpdate = gson.fromJson(gson.toJson(ajaxMsg.getDatas()), Update.class);
			}
			onFinshCheck();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			super.onFailure(statusCode, headers, responseString, throwable);
			hideCheckDialog();
			if (isShow) {
				showFaileDialog();
			}
		}
	};

	public UpdateManager(Context context, boolean isShow) {
		this.mContext = context;
		this.isShow = isShow;
	}

	public boolean haveNew() {
		if (this.mUpdate == null) {
			return false;
		}
		boolean haveNew = false;
		int curVersionCode = TDevice.getVersionCode(AppContext.getInstance().getPackageName());
		if (curVersionCode < mUpdate.getVersionCode()) {
			haveNew = true;
		}
		return haveNew;
	}

	public void checkUpdate() {
		if (isShow) {
			showCheckDialog();
		}
		Api.checkUpdate(mCheckUpdateHandle);
	}

	private void onFinshCheck() {
		if (haveNew()) {
			showUpdateInfo();
		} else {
			if (isShow) {
				showLatestDialog();
			}
		}
	}

	private void showCheckDialog() {
		if (_waitDialog == null) {
			_waitDialog = DialogHelper.getWaitDialog((Activity) mContext, "正在获取新版本信息...");
		}
		_waitDialog.show();
	}

	private void hideCheckDialog() {
		if (_waitDialog != null) {
			_waitDialog.dismiss();
		}
	}

	private void showUpdateInfo() {
		if (mUpdate == null) {
			return;
		}
		CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(mContext);
		dialog.setTitle("发现新版本");
		dialog.setMessage(mUpdate.getUpdateLog());
		dialog.setNegativeButton(R.string.cancle, null);
		dialog.setPositiveButton("更新版本", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UIHelper.openDownLoadService(mContext,ApiHttpClient.getNetworkUrl() + mUpdate.getDownloadUrl(), mUpdate.getVersionName());
//				UIHelper.openDownLoadService(mContext,Constants.ftpUrl+mUpdate.getDownloadUrl(), mUpdate.getVersionName());
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showLatestDialog() {
		CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(mContext);
		dialog.setMessage("已经是最新版本了");
		dialog.setPositiveButton("", null);
		dialog.show();
	}

	private void showFaileDialog() {
		CommonDialog dialog = DialogHelper.getPinterestDialogCancelable(mContext);
		dialog.setMessage("网络异常，无法获取新版本信息");
		dialog.setPositiveButton("", null);
		dialog.show();
	}
}
