package com.jintoufs.logstics.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.jintoufs.logstics.activites.EquipmentPutInActivity;
import com.jintoufs.logstics.activites.EquipmentTakeOutActivity;
import com.jintoufs.logstics.activites.LoginActivity;
import com.jintoufs.logstics.activites.OperationDetailActivity;
import com.jintoufs.logstics.interf.ICallbackResult;
import com.jintoufs.logstics.service.DownloadService;
import com.jintoufs.logstics.service.DownloadService.DownloadBinder;

/**
 * 界面帮助类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月10日 下午3:33:36
 * 
 */
public class UIHelper {

	private static final String SHOWIMAGE = "ima-api:action=showImage&data=";

	public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";

	/**
	 * 显示登录界面
	 * 
	 * @param context
	 */
	public static void showLoginActivity(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}

	public static void openDownLoadService(Context context, String downurl, String tilte) {
		final ICallbackResult callback = new ICallbackResult() {

			@Override
			public void OnBackResult(Object s) {
			}
		};
		ServiceConnection conn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				DownloadBinder binder = (DownloadBinder) service;
				binder.addCallback(callback);
				binder.start();

			}
		};
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
		intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
		context.startService(intent);
		context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

//	public static void showOperationImageActivity(Context context, Bundle bundle) {
//		Intent intent = new Intent(context, OperationDetailActivity.class);
//		intent.putExtra(BUNDLE_KEY_ARGS, bundle);
//		context.startActivity(intent);
//	}
	
	public static void showOperationImageActivityForResult(Context context, Bundle bundle,int requestCode) {
		Intent intent = new Intent(context, OperationDetailActivity.class);
		intent.putExtra(BUNDLE_KEY_ARGS, bundle);
		((Activity) context).startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 显示取回ATM钞箱界面
	 * 
	 * @param context
	 */
	public static void showTakeOutCashBoxActivity(Context context,long taskDetailId) {
		Intent intent = new Intent(context, EquipmentTakeOutActivity.class);
		intent.putExtra("taskDetailId", taskDetailId);
//		intent.putExtra("code",code);
		context.startActivity(intent);
	}
	/**
	 * 显示放入ATM钞箱界面
	 * 
	 * @param context
	 */
	public static void showPutInCashBoxActivity(Context context,long taskDetailId) {
		Intent intent = new Intent(context, EquipmentPutInActivity.class);
		intent.putExtra("taskDetailId", taskDetailId);
//		intent.putExtra("code",code);
		context.startActivity(intent);
	}
}
