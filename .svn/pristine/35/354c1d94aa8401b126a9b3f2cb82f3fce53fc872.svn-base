package com.jintoufs.logstics.db;

import android.content.Context;
import android.util.Log;


public class BeanFactory {

	/**
	 * @description : 单例模式，获取DBHelper
	 * @param context
	 * @return DBHelper
	 */
	private static DBHelper dbHelper = null;

	public synchronized static DBHelper getDBHelper(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
			// 执行本语句是为了自动调用建表语句
			try {
				dbHelper.getWritableDatabase();
			} catch (Exception e) {
				Log.e("BeanFactory", "创建表失败！", e);
			}
		}
		return dbHelper;
	}
}