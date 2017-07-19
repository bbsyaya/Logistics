package com.jintoufs.api.remote;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.jintoufs.api.ApiHttpClient;
import com.jintoufs.entity.Cashbox;
import com.jintoufs.entity.EnumType.ErrorTypeOfCount;
import com.jintoufs.entity.EnumType.StockType;
import com.jintoufs.entity.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Api {

	/**
	 * 登陆
	 * 
	 * @param username
	 * @param password
	 * @param handler
	 */
	public static void login(String username, String password, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("userCode", username);
		params.put("password", password);
		String loginurl = "app/store/login.do";
		ApiHttpClient.post(loginurl, params, handler);
	}

	/**
	 * 获取任务概况
	 * 
	 * @param username
	 * 
	 * @param handler
	 */
	public static void getRouteTask(String username, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", username);
		ApiHttpClient.get("/app/store/task/getTask", params, handler);
	}

	public static void checkUpdate(AsyncHttpResponseHandler handler) {
		ApiHttpClient.get("app/sys/version", handler);
	}

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @param password
	 *@param mHandler
	 */
	public static void updatePwd(User user, String password, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("userName", user.getUserName());
		params.put("password", password);
		String url = "/app/store/update";
		ApiHttpClient.post(url, params, mHandler);

	}

	/**
	 * 获取出库任务类别的线路任务列表
	 * 
	 * @param user
	 * @param mHandler
	 */
	public static void getOutTaskRouteList(User user, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/task/getOutTaskRouteList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取入库任务类别的线路任务列表
	 * 
	 * @param user
	 * @param mHandler
	 */
	public static void getInTaskRouteList(User user, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/task/getInTaskRouteList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 保存出库结果
	 * 
	 * @param user
	 * @param keyId
	 * @param passwordId
	 * @param cashboxCodes
	 * @param mHandler
	 */
	public static void saveOutStore(User user, int keyId, int passwordId, String cashboxCodes, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("keyId", String.valueOf(keyId));
		params.put("passwordId", String.valueOf(passwordId));
		params.put("cashboxCodes", cashboxCodes);
		String url = "/app/store/task/saveOutStore";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 保存交接结果
	 * 
	 * @param user
	 * @param keyId
	 * @param passwordId
	 * @param cashboxCodes
	 * @param mHandler
	 */
	public static void saveInEscort(User user, int keyId, int passwordId, String cashboxCodes, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("keyId", String.valueOf(keyId));
		params.put("passwordId", String.valueOf(passwordId));
		params.put("cashboxCodes", cashboxCodes);
		String url = "/app/store/task/saveInEscort";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 保存入库结果
	 * 
	 * @param user
	 * @param keyId
	 * @param passwordId
	 * @param cashboxCodes
	 * @param mHandler
	 */
	public static void saveInStore(User user, int keyId, int passwordId, String cashboxCodes, AsyncHttpResponseHandler mHandler) {

		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("keyId", String.valueOf(keyId));
		params.put("passwordId", String.valueOf(passwordId));
		params.put("cashboxCodes", cashboxCodes);
		String url = "/app/store/task/saveInStore";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取入库交接任务钞箱列表
	 * 
	 * @param user
	 * @param mHandler
	 */
	public static void getInEscortCashboxList(User user, int keyId, int passwordId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyId", keyId);
		params.put("keyStr", user.getKeyStr());
		params.put("passwordId", passwordId);
		String url = "/app/store/task/getInEscortCashboxList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取入库钞箱列表
	 * 
	 * @param user
	 * @param mHandler
	 */
	public static void getInStoreCashboxList(User user, int keyId, int passwordId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyId", keyId);
		params.put("passwordId", passwordId);
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/task/getInStoreCashboxList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取出库钞箱列表
	 * 
	 * @param user
	 * @param keyPostId
	 * @param passwordPostId
	 * @param mHandler
	 */
	public static void getOutCashboxList(User user,int keyPostId, int passwordPostId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("keyId", keyPostId);
		params.put("passwordId", passwordPostId);
		String url = "/app/store/task/getOutCashboxList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取出库任务类别的任务数量
	 * 
	 * @param user
	 * @param mHandler
	 */
	public static void getOutStoreCount(User user, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/task/getOutTaskRouteCount";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取入库任务类别的任务数量
	 * 
	 * @param user
	 * @param mHandler
	 */
	public static void getInStoreCount(User user, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/task/getInTaskRouteCount";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 取得盘库数量
	 * 
	 * @param user
	 */
	public static void getCheckStoreCount(User user, AsyncHttpResponseHandler mCheckStoreHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/check_store/getCheckStoreCount";
		ApiHttpClient.post(url, params, mCheckStoreHandler);

	}

	/**
	 * 金库经理确认
	 * 
	 * @param userCode
	 * @param password
	 */
	public static void getStoreManager(String userCode, String password, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("userCode", userCode);
		params.put("password", password);
		String url = "/app/store/getStoreManager";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 取得 盘库列表
	 * 
	 * @param user
	 */
	public static void getCheckStoreList(User user, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/check_store/checkStore_list";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 取得 盘库钞箱列表
	 * 
	 * @param user
	 */
	public static void getCheckStoreDetailList(User user, Integer checkId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("checkId", checkId);
		String url = "/app/store/check_store/detail_list";
		ApiHttpClient.post(url, params, mHandler);
	}

	public static void saveCheckStore(User user, Integer checkId, List<String> founds, AsyncHttpResponseHandler mSaveHandler) {
		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.put("keyStr", user.getKeyStr());
		params.put("checkId", checkId);
		params.put("cashboxCodes", gson.toJson(founds));
		String url = "/app/store/check_store/saveCheckStore";
		ApiHttpClient.post(url, params, mSaveHandler);
	}

	/**
	 * 通过rfid取钞箱
	 * 
	 * @param rfid
	 * @param mHandler
	 */
	public static void readBox(String rfid, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("rfid", rfid);
		String url = "/app/tool/readBox";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 提交差错
	 * 
	 * @param user
	 * @param stockType
	 * @param errorTypeOfCount
	 * @param lstCashboxes
	 * @param mHandler
	 */
	public static void submitError(User user, StockType stockType, ErrorTypeOfCount errorTypeOfCount, List<Cashbox> lstCashboxes, AsyncHttpResponseHandler mHandler) {

		int nStockType = stockType.value();
		int nErrorType = errorTypeOfCount.value();

		List<String> lstBoxCodes = new ArrayList<String>();

		for (Cashbox x : lstCashboxes) {
			if (x != null) {
				lstBoxCodes.add(x.getCashBoxCode());
			}
		}

		RequestParams params = new RequestParams();
		params.put("keyStr", String.valueOf(user.getKeyStr()));
		params.put("storeType", String.valueOf(nStockType));
		params.put("errorType", String.valueOf(nErrorType));
		params.put("cashBoxCodes", new Gson().toJson(lstBoxCodes));
		String url = "/app/store/task/saveAppError";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取准备钞箱任务日期列表
	 * 
	 * @param keyStr
	 * @param mHandler
	 */
	public static void queryPrepareStoreTask(String keyStr, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		String url = "/app/store/task/queryPrepareStoreTask";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 取准备钞箱任务列表
	 * 
	 * @param keyStr
	 * @param mHandler
	 */
	public static void getTomorrowStoreTaskList(String keyStr, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		String url = "/app/store/task/getTomorrowStoreTaskList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 取准备钞箱任务中的钞箱列表
	 * 
	 * @param keyStr
	 * @param storeTaskId
	 * @param mHandler
	 */
	public static void queryPrepareCashBox(String keyStr, long storeTaskId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("storeTaskId", storeTaskId);
		String url = "/app/store/task/queryPrepareCashBox";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 取准备钞箱任务数量
	 * 
	 * @param user
	 * @param mHandler
	 */
	public static void queryPrepareStoreTaskCount(User user, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/task/queryPrepareStoreTaskCount";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 扫描确认后跟换钞箱位置（将当前位置放入准备位置）
	 * 
	 * @param user
	 * @param storeTaskId
	 *            任务ID
	 * @param cashBoxCode
	 *            钞箱编号
	 * 
	 * @param mHandler*/
	public static void prepareCashbox(User user, long storeTaskId, String cashBoxCode, AsyncHttpResponseHandler mHandler) {

		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("storeTaskId", storeTaskId);
		params.put("cashBoxCode", cashBoxCode);
		String url = "/app/store/task/prepareCashBox";
		ApiHttpClient.post(url, params, mHandler);
	}
	
	/**
	 * 获取当天出/入库扫描列表
	 * @param user
	 * @param type
	 */
	public static void getList(User user, Integer type, AsyncHttpResponseHandler mHandler){
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("type", type);
		String url = "/app/store/transport/getList";
		ApiHttpClient.post(url, params, mHandler);
	}
	
	/**
	 * 获取重新扫描钞箱列表
	 * @param user
	 * @param recordId
	 * @param mHandler
	 */
	public static void getCashBoxList(User user, long recordId, AsyncHttpResponseHandler mHandler){
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		params.put("recordId", recordId);
		String url = "/app/store/transport/getCashBoxList";
		ApiHttpClient.post(url, params, mHandler);
	}
	
	/**
	 * 提交扫描结果
	 * @param user
	 * @param recordId
	 * @param cashBoxCodes
	 * @param userA
	 * @param userB
	 * @param type
	 * @param mHandler
	 */
	public static void saveCashBox(User user, long recordId,String cashBoxCodes, Integer userA, Integer userB, Integer type, AsyncHttpResponseHandler mHandler){
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		if(recordId!=0){
			params.put("recordId", recordId);
		}
		params.put("cashBoxCodes", cashBoxCodes);
		params.put("userA", userA);
		params.put("userB", userB);
		params.put("type", type);
		String url = "/app/store/transport/saveCashBox";
		ApiHttpClient.post(url, params, mHandler);
	}
	
	/**
	 * 获取押运公司人员
	 * @param user
	 * @param mHandler
	 */
	public static void queryUserDocking(User user, AsyncHttpResponseHandler mHandler){
		RequestParams params = new RequestParams();
		params.put("keyStr", user.getKeyStr());
		String url = "/app/store/transport/queryUserDocking";
		ApiHttpClient.post(url, params, mHandler);
	}
}
