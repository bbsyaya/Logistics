package com.jintoufs.logstics.api;

import java.util.Locale;

import org.apache.http.client.params.ClientPNames;

import android.content.Context;
import android.util.Log;

import com.jintoufs.logstics.AppContext;
import com.jintoufs.logstics.utils.TDevice;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiHttpClient {
	//public final static String HOST_WIFI = "172.18.200.208:8088";//卢敏捷
	//public final static String HOST_WIFI = "172.18.56.167:8080";//陈梦黎
//	public final static String HOST_WIFI = "172.18.57.37:8086";//不加项目名
//	public final static String HOST_WIFI = "172.18.56.157:8080";
//	public final static String HOST_WIFI = "192.168.1.120:8080";
	public final static String HOST_WIFI = "192.168.1.22:8088";
//	private static final String PROJECT_NAME = "/";
	public final static String HOST_MOBILE = "112.64.186.126:8443";
	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String PROJECT_NAME = "/hongtai-rest/";

	public static final String MOBILE_URL = HTTPS + HOST_MOBILE;
	public static final String WIFI_URL = HTTP + HOST_WIFI;

	public static final String DELETE = "DELETE";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static AsyncHttpClient client;

	public ApiHttpClient() {
	}

	public static AsyncHttpClient getHttpClient() {
		return client;
	}

	public static void cancelAll(Context context) {
		client.cancelRequests(context, true);
	}

	public static void clearUserCookies(Context context) {
		// (new HttpClientCookieStore(context)).a();
	}

	public static void delete(String partUrl, AsyncHttpResponseHandler handler) {
		client.delete(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("DELETE ").append(partUrl).toString());
	}

	public static void get(String partUrl, AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("GET ").append(partUrl).toString());
	}

	public static void get(String partUrl, RequestParams params, AsyncHttpResponseHandler handler) {
		client.get(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("GET ").append(partUrl).append("&").append(params).toString());
	}

	public static String getAbsoluteApiUrl(String partUrl) {
		String url = getWifiNetworkUrl() + PROJECT_NAME + partUrl;
		// String url=getNetworkUrl()+partUrl;
		Log.d("BASE_CLIENT", "request:" + url);
		return url;
	}

	/*
	 * 根据网络类型选择连接地址
	 */
	public static String getNetworkUrl() {
		int netType = TDevice.getNetworkType();
		String headUrl = "";
		switch (netType) {
		case TDevice.NETTYPE_CMNET:
		case TDevice.NETTYPE_CMWAP:
			headUrl = String.format(MOBILE_URL);
			break;
		case TDevice.NETTYPE_WIFI:
			headUrl = String.format(WIFI_URL);
			break;
		default:
			break;
		}
		return headUrl;
	}

	/*
	 * 根据连接的wifi名称选择连接地址
	 */
	public static String getWifiNetworkUrl() {
		int netType = TDevice.getNetworkType();
		String headUrl = "";
		switch (netType) {
		case TDevice.NETTYPE_CMNET:
		case TDevice.NETTYPE_CMWAP:
			headUrl = String.format(MOBILE_URL);
			break;
		case TDevice.NETTYPE_WIFI:
			// 如果是连接大库wifi，则连接内网，否则连接外网
			String storeWifi = AppContext.get("storeWifi", "");
			String currentWifi = TDevice.getWifiSSId();
			currentWifi = currentWifi.replace("\"", "");
			if (currentWifi.equalsIgnoreCase(storeWifi)) {
				headUrl = String.format(WIFI_URL);
			} else {
				headUrl = String.format(MOBILE_URL);
			}
			break;
		default:
			break;
		}
		return headUrl;
	}

	public static void getDirect(String url, AsyncHttpResponseHandler handler) {
		client.get(url, handler);
		log(new StringBuilder("GET ").append(url).toString());
	}

	public static void log(String log) {
		Log.d("BaseApi", log);
	}

	public static void post(String partUrl, AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("POST ").append(partUrl).toString());
	}

	public static void post(String partUrl, RequestParams params, AsyncHttpResponseHandler handler) {
		client.post(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("POST ").append(partUrl).append("?").append(params).toString());
	}

	public static void postDirect(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		client.post(url, params, handler);
		log(new StringBuilder("POST ").append(url).append("&").append(params).toString());
	}

	public static void put(String partUrl, AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), handler);
		log(new StringBuilder("PUT ").append(partUrl).toString());
	}

	public static void put(String partUrl, RequestParams params, AsyncHttpResponseHandler handler) {
		client.put(getAbsoluteApiUrl(partUrl), params, handler);
		log(new StringBuilder("PUT ").append(partUrl).append("&").append(params).toString());
	}

	// public static void setApiUrl(String apiUrl) {
	// API_URL = apiUrl;
	// }

	public static void setHttpClient(AsyncHttpClient c) {
		client = c;
		client.addHeader("Accept-Language", Locale.getDefault().toString());
		int netType = TDevice.getNetworkType();
		switch (netType) {
		case TDevice.NETTYPE_CMNET:
		case TDevice.NETTYPE_CMWAP:
			client.addHeader("Host", HOST_MOBILE);
			break;
		case TDevice.NETTYPE_WIFI:
			client.addHeader("Host", HOST_WIFI);
			break;
		}
		client.addHeader("Connection", "Keep-Alive");
		//client.setConnectTimeout(60000);
		//client.setResponseTimeout(60000);
		client.setTimeout(30000);
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
	}

	public static void setUserAgent(String userAgent) {
		client.setUserAgent(userAgent);
	}

	public static void setCookie(String cookie) {
		client.addHeader("Cookie", cookie);
	}

	private static String appCookie;

	public static void cleanCookie() {
		appCookie = "";
	}

	public static String getCookie(AppContext appContext) {
		if (appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}
}
