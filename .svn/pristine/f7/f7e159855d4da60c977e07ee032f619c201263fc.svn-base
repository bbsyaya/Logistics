package com.jintoufs.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.jintoufs.AppContext;

import android.app.Activity;
import android.widget.Toast;

public class Exit {
	public static Boolean isExit = false;

	public static void exitBy2Click(Activity activity) {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(activity.getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);

		} else {
			AppContext.getInstance().cleanLoginInfo();
			activity.finish();
			System.exit(0);
		}
	}
}
