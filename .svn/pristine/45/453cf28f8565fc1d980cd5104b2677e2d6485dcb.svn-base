package com.jintoufs;

import static com.jintoufs.AppConfig.KEY_LOAD_IMAGE;

import java.util.Properties;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.HandlerThread;
import android.util.Log;

import com.jintoufs.api.ApiClientHelper;
import com.jintoufs.api.ApiHttpClient;
import com.jintoufs.base.BaseApplication;
import com.jintoufs.entity.Constants;
import com.jintoufs.entity.User;
import com.jintoufs.reader.server.ReaderHelper;
import com.jintoufs.utils.Configuration;
import com.jintoufs.utils.StringUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author 火蚁 (http://my.oschina.net/LittleDY)
 * @version 1.0
 * @created 2014-04-22
 */
public class AppContext extends BaseApplication {

	public static final int PAGE_SIZE = 20;// 默认分页大小

	private static AppContext instance;

	private int loginUid;

	private boolean login;

	private HandlerThread handlerThread;
	private static Configuration mAppConfiguration;

	public static final String TAG = "MainApp";

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		init();
		initLogin();
		// 注册App异常崩溃处理器
		Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
		handlerThread = new HandlerThread("handlerThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
		handlerThread.start();
		try {
			ReaderHelper.setContext(getApplicationContext());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() {
		// 初始化网络请求
		//AsyncHttpClient client =AsynHttpClientSslHelper.getAsyncHttpsClient(this);
		AsyncHttpClient client = ApiClientHelper.getAsyncHttpClient(this);
		//AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		ApiHttpClient.setHttpClient(client);
		ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
	}

	private void initLogin() {
		User user = getLoginUser();
		if (null != user && user.getId() > 0) {
			login = true;
			loginUid = user.getId();
		} else {
			this.cleanLoginInfo();
		}
	}

	/**
	 * 获得当前app运行的AppContext
	 * 
	 * @return
	 */
	public static AppContext getInstance() {
		return instance;
	}

	public boolean containsProperty(String key) {
		Properties props = getProperties();
		return props.containsKey(key);
	}

	public void setProperties(Properties ps) {
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties() {
		return AppConfig.getAppConfig(this).get();
	}

	public void setProperty(String key, String value) {
		AppConfig.getAppConfig(this).set(key, value);
	}

	/**
	 * 获取cookie时传AppConfig.CONF_COOKIE
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		String res = AppConfig.getAppConfig(this).get(key);
		return res;
	}

	public void removeProperty(String... key) {
		AppConfig.getAppConfig(this).remove(key);
	}

	/**
	 * 获取App唯一标识
	 * 
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 保存登录信息
	 *
	 * @param user
	 */
	@SuppressWarnings("serial")
	public void saveUserInfo(final User user) {
		this.loginUid = user.getId();
		this.login = true;
		setProperties(new Properties() {
			{
				setUserProperties(user);
			}

		});
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 */
	@SuppressWarnings("serial")
	public void updateUserInfo(final User user) {
		setProperties(new Properties() {
			{
				setUserProperties(user);
			}
		});
	}

	private void setUserProperties(final User user) {
		if (user.getId() != 0) {
			setProperty("user.uid", String.valueOf(user.getId()));
		} else {
			setProperty("user.uid", "");
		}
		if (user.getKeyStr() != null) {
			setProperty("user.keyStr", user.getKeyStr());
		} else {
			setProperty("user.keyStr", "");
		}
		if (user.getRealName() != null) {
			setProperty("user.realName", user.getRealName());
		} else {
			setProperty("user.realName", "");
		}
		if (user.getUserPhoto() != null) {
			setProperty("user.userPhoto", user.getUserPhoto());// 用户头像-文件名
		} else {
			setProperty("user.userPhoto", "");
		}
		if (user.getUserName() != null) {
			setProperty("user.username", user.getUserName());
		} else {
			setProperty("user.username", "");
		}
		if (user.getPassword() != null) {
			setProperty("user.password", user.getPassword());
		} else {
			setProperty("user.password", "");
		}
		if (user.getMobilePhone() != null) {
			setProperty("user.mobilePhone", String.valueOf(user.getMobilePhone()));
		} else {
			setProperty("user.mobilePhone", "");
		}
	}

	/**
	 * 获得登录用户的信息
	 * 
	 * @return
	 */
	public User getLoginUser() {
		User user = new User();
		user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
		user.setUserName(getProperty("user.username"));
		user.setKeyStr(getProperty("user.keyStr"));
		user.setUserPhoto(getProperty("user.userPhoto"));
		user.setPassword(getProperty("user.password"));
		user.setRealName(getProperty("user.realName"));
		user.setMobilePhone(getProperty("user.mobilePhone"));
		return user;
	}

	/**
	 * 清除登录信息
	 */
	public void cleanLoginInfo() {
		this.loginUid = 0;
		this.login = false;
		removeProperty("user.uid", "user.realName", "user.keyStr","user.userPhoto", "user.username", "user.password", "user.mobilePhone");
	}

	public int getLoginUid() {
		return loginUid;
	}

	public boolean isLogin() {
		return login;
	}

	/**
	 * 用户注销
	 */
	public void Logout() {
		cleanLoginInfo();
		ApiHttpClient.cleanCookie();
		this.cleanCookie();
		this.login = false;
		this.loginUid = 0;

		Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
		sendBroadcast(intent);
	}

	/**
	 * 清除保存的缓存
	 */
	public void cleanCookie() {
		removeProperty(AppConfig.CONF_COOKIE);
	}

	/**
	 * 清除app缓存
	 */
	public void clearAppCache() {
	}

	public static void setLoadImage(boolean flag) {
		set(KEY_LOAD_IMAGE, flag);
	}

	private static final Configuration getConfiguration() {
		if (mAppConfiguration == null) {
			mAppConfiguration = new Configuration(instance, "settings", Context.MODE_PRIVATE);
		}
		return mAppConfiguration;
	}
}
