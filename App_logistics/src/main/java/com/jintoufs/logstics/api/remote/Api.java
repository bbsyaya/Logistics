package com.jintoufs.logstics.api.remote;

import com.google.gson.Gson;
import com.jintoufs.logstics.api.ApiHttpClient;
import com.jintoufs.logstics.entity.Cashbox;
import com.jintoufs.logstics.entity.Constants;
import com.jintoufs.logstics.entity.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
		String loginUrl = "app/login.do";
		ApiHttpClient.post(loginUrl, params, handler);
	}

	/**
	 * 指纹登陆,获取用户列表数据
	 *
	 * @param handler
	 */
	public static void getUserList(AsyncHttpResponseHandler handler) {
		String url = "app/getUserList";
		ApiHttpClient.post(url, handler);
	}

	/**
	 * 更新用户指纹
	 *
	 * @param userId
	 * @param fingerprint
	 * @param handler
	 */
	public static void updateFingerprint(int userId, String fingerprint, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("id", userId);
		params.put("fingerprint", fingerprint);
		String url = "app/updateFingerprint";
		ApiHttpClient.post(url,params, handler);
	}

	/**
	 * 获取车辆和业务员信息
	 * 
	 * @param keyStr
	 * @param handler
	 */
	public static void getCarAndUserInfo(String keyStr, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		ApiHttpClient.post("/app/task/getCarAndUserInfo", params, handler);
	}


	/**
	 * 获取任务钞箱列表
	 * 
	 * @param username
	 *            用户名
	 * @param detailId
	 *            网点任务ID
	 * 
	 * @param handler
	 */
	public static void getTaskCashboxList(String username,  long detailId, int deliverType, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", username);
		params.put("detailId", detailId);
		params.put("type", deliverType);
		ApiHttpClient.post("/app/task/getCashboxList", params, handler);
	}

	/**
	 * 获取任务网点列表
	 * 
	 * @param keyStr
	 * @param deliverType
	 *            收送箱类型 0：送箱，1：收箱
	 * 
	 * @param handler
	 */
	public static void getSiteList(String keyStr, int deliverType, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("deliverType", deliverType);
		ApiHttpClient.post("/app/task/getSiteList", params, handler);
	}

	/**
	 * 获取自助设备离线操作数据
	 * @param keyStr
	 * @param handler
	 */
	public static void getOfflineData(String keyStr, AsyncHttpResponseHandler handler){
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		ApiHttpClient.post("/app/task/atm/offlineDate", params, handler);
	}

	/**
	 * 离线数据提交
	 * @param datas
	 * @param files
	 * @param handler
	 */
	public static void subAtmOff(String datas,List<File> files, AsyncHttpResponseHandler handler) {
		try {
			RequestParams params = new RequestParams();
			params.put("datas", datas);
			File[] f = files.toArray(new File[files.size()]);
			params.put("files",f );
			ApiHttpClient.post("/app/task/atm/subAtmOff", params, handler);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取任务网点列表
	 * 
	 * @param keyStr
	 * @param handler
	 */
	public static void getAtmList(String keyStr, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		ApiHttpClient.post("/app/task/getAtmList", params, handler);
	}

	public static void checkUpdate(AsyncHttpResponseHandler handler) {
		ApiHttpClient.get("app/sys/version", handler);
	}

	/**
	 * 获取任务人员列表
	 * 
	 * @param keyStr
	 * @param detailId
	 * @param handler
	 */
	public static void getEscortUserList(String keyStr,long detailId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("detailId", detailId);
		ApiHttpClient.post("/app/task/getEscortUserList", params, handler);
	}

	/**
	 * 获取网点人员列表
	 * 
	 * @param keyStr
	 * @param siteId
	 * @param handler
	 */
	public static void getSiteUserList(String keyStr, Integer siteId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("siteId", siteId);
		ApiHttpClient.post("/app/task/getSiteUserList", params, handler);
	}

	/**
	 * 保存收箱结果到后台
	 * 
	 * @param handler
	 */
	public static void saveReceiveTask(String keyStr, long detailId, String code, List<Cashbox> foundList, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.put("keyStr", keyStr);
		params.put("detailId", detailId);
		params.put("verifyCode", code);
		params.put("json", gson.toJson(foundList));
		String url = "/app/task/saveReceiveTask";
		ApiHttpClient.post(url, params, handler);
	}

	/**
	 * 重发验证码短信
	 * 
	 * @param handler
	 */
	public static void reSendMsg(String keyStr, long detailId,int deliverType, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("detailId", detailId);
		params.put("deliverType", deliverType);
		String url = "/app/task/reSendMsg";
		ApiHttpClient.post(url, params, handler);
	}

	/**
	 * 保存交箱结果到后台
	 * 
	 * @param code
	 * 
	 * @param handler
	 */
	public static void saveDeliverTask(String keyStr, long detailId, String code, List<Cashbox> foundList, AsyncHttpResponseHandler handler) {

		List<String> cashboxCodes = new ArrayList<String>();

		for (Cashbox x : foundList) {
			if (x != null) {
				cashboxCodes.add(x.getCashBoxCode());
			}
		}

		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("detailId", detailId);
		params.put("verifyCode", code);
		params.put("cashboxCodes", new Gson().toJson(cashboxCodes));
		String url = "/app/task/saveDeliverTask";
		ApiHttpClient.post(url, params, handler);

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
		String url = "/app/update";
		ApiHttpClient.post(url, params, mHandler);

	}

	/**
	 * 确认任务下车
	 *
	 * @param keyStr
	 * @param toWhere
	 * @param foundList
	 * @param routeItemIds
	 * @param mHandler
	 */
	public static void confirmOutCarTask(String keyStr, String toWhere, List<Cashbox> foundList, String routeItemIds, AsyncHttpResponseHandler mHandler) {

		List<String> cashBoxCodes = new ArrayList<String>();
		for (Cashbox x : foundList) {
			if (x != null) {
				cashBoxCodes.add(x.getCashBoxCode());
			}
		}

		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.put("keyStr", keyStr);
		params.put("cashboxCodes", gson.toJson(cashBoxCodes));
		params.put("toWhere", toWhere);
		params.put("routeItemIds", routeItemIds);
		String url = "/app/task/confirmOutCarTask";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 确认任务上车
	 *
	 * @param keyStr
	 * @param foundList
	 * @param mHandler
	 */
	public static void confirmInCarTask(String keyStr, List<Cashbox> foundList, AsyncHttpResponseHandler mHandler) {

		List<String> cashBoxCodes = new ArrayList<String>();
		for (Cashbox x : foundList) {
			if (x != null) {
				cashBoxCodes.add(x.getCashBoxCode());
			}
		}

		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.put("keyStr", keyStr);
		params.put("cashboxCodes", gson.toJson(cashBoxCodes));
		String url = "/app/task/confirmInCarTask";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 确认大库交接任务
	 *
	 * @param keyStr
	 * @param foundList
	 * @param mHandler
	 */
	public static void confirmEscortTask(String keyStr, List<Cashbox> foundList, AsyncHttpResponseHandler mHandler) {

		List<String> cashBoxCodes = new ArrayList<String>();
		for (Cashbox x : foundList) {
			if (x != null) {
				cashBoxCodes.add(x.getCashBoxCode());
			}
		}

		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.put("keyStr", keyStr);
		params.put("cashboxCodes", gson.toJson(cashBoxCodes));
		String url = "/app/task/confirmEscortTask";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取与大库交接的钞箱列表
	 * 
	 * @param keyStr
	 */
	public static void getEscortCashboxList(String keyStr, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		String url = "/app/task/getEscortCashboxList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取要上车的的钞箱列表
	 * 
	 * @param keyStr
	 */
	public static void getInCarCashboxList(String keyStr, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		String url = "/app/task/getInCarCashboxList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取要下车的的钞箱列表
	 * 
	 * @param keyStr
	 * @param toWhere
	 *            类型 0-出库 1-入库，2-交接到清分间
	 */
	public static void getOutCarCashboxList(String keyStr, String toWhere, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("toWhere", toWhere);
		String url = "/app/task/getOutCarCashboxList";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取领用钥匙列表
	 * @param keyStr
	 * @param mHandler
	 */
	public static void getKeyList(String keyStr, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		String url = "/app/key/queryKeyList";
		ApiHttpClient.post(url, params, mHandler);
	}
	
	/**
	 * 查钞箱编号是否存在
	 *
	 * @param keyStr
	 * @param mHandler
	 */
	public static void isCashboxExists(String keyStr, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		String url = "/app/cashbox/getAvailableCode";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 增加钞箱编号
	 * 
	 * @param rfid
	 * @param cashboxCode
	 * @param siteId
	 * @param mHandler
	 */
	public static void AddCashbox(String keyStr, String rfid, String cashboxCode, int siteId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("rfid", rfid);
		params.put("keyStr", keyStr);
		params.put("cashBoxCode", cashboxCode);
		params.put("siteId", siteId);
		String url = "/app/cashbox/add";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 查询多个钞箱是否为上缴箱
	 * 
	 * @param keyStr
	 * @param lstCashBoxes
	 * @param mHandler
	 */
	public static void IsHandInCashBoxes(String keyStr, List<Cashbox> lstCashBoxes, AsyncHttpResponseHandler mHandler) {

		List<String> lstStr = new ArrayList<String>();

		for (Cashbox x : lstCashBoxes) {
			if (x != null) {
				lstStr.add(x.getCashBoxCode());
			}
		}

		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("cashBoxCodes", new Gson().toJson(lstStr));
		String url = "/app/cashbox/queryCashBoxes";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 查询单个钞箱是否为上缴箱
	 * 
	 * @param keyStr
	 * @param cashbox
	 * @param mHandler
	 */
	public static void IsHandInCashBox(String keyStr, Cashbox cashbox, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("keyStr", keyStr);
		params.put("cashBoxCode", cashbox.getCashBoxCode());
		String url = "/app/cashbox/querySingleCashBox";
		ApiHttpClient.post(url, params, mHandler);
	}

	public static void getStepStatus(String keyStr, long taskDetailId, AsyncHttpResponseHandler mHandler){
		RequestParams params=new RequestParams();
		params.put("keyStr", keyStr);
		params.put("taskDetailId", String.valueOf(taskDetailId));
		
		String url="app/task/atm/getStepStatus";
		ApiHttpClient.post(url, params, mHandler);
	}
	/**
	 * 撤防,异常拍照,正常只改状态
	 * 
	 * @param file
	 * @param status
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void DisArming(String keyStr, File file, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("status", String.valueOf(status));
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		if (status == Constants.ATM_TASK_RESULT_FAILURE) {
			params.put("file", file);
		}
		String url = "/app/task/atm/step1";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 吞没卡检测,异常拍照,正常只改状态
	 * 
	 * @param keyStr
	 * @param file
	 * @param status
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void StockedCardChecking(String keyStr, File file, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("status", String.valueOf(status));
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		if (status == Constants.ATM_TASK_RESULT_FAILURE) {
			params.put("file", file);
		} 

		String url = "/app/task/atm/step2";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 获取动态密码
	 * 
	 * @param keyStr
	 * @param taskDetailId
	 * @param mHandler
	 */
	public static void GetDynamicPassword(String keyStr, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);

		String url = "/app/task/atm/step3";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 检查机器
	 * 
	 * @param keyStr
	 * @param file
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void CheckAtmMachine(String keyStr, File file, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("file", file);

		String url = "/app/task/atm/step4";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 更换钞箱,获取需要取出的钞箱
	 * 
	 * @param keyStr
	 * @param taskDetailId
	 * @param mHandler
	 */
	public static void GetTakeOutCashboxList(String keyStr, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);

		String url = "/app/task/atm/step5_out_cashBox_list";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 提交取回的钞箱
	 * 
	 * @param keyStr
	 * @param cashboxCodes
	 * @param taskDetailId
	 * @param mHandler
	 */
	public static void SubmitTakeOutCashboxList(String keyStr, String cashboxCodes, String itemIds, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("itemIds", itemIds);

//		List<String> cashboxCodes = new ArrayList<String>();
//
//		for (Cashbox c : lstCashboxes) {
//			if (c != null) {
//				cashboxCodes.add(c.getCashBoxCode());
//			}
//		}

		params.put("cashboxCodes", cashboxCodes);

		String url = "/app/task/atm/step5_post_out_cashBox_list";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 更换钞箱,获取需要放入的钞箱
	 * 
	 * @param keyStr
	 * @param taskDetailId
	 * @param mHandler
	 */
	public static void GetPutInCashboxList(String keyStr, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);

		String url = "/app/task/atm/step5_in_cashBox_list";
		ApiHttpClient.post(url, params, mHandler);
	}
	
	/**
	 * 确认放入的钞箱
	 * 
	 * @param keyStr
	 * @param taskDetailId
	 * @param mHandler
	 */
	public static void SubmitPutInCashboxList(String keyStr, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);

//		List<String> cashboxCodes = new ArrayList<String>();
//
//		for (Cashbox c : lstCashboxes) {
//			if (c != null) {
//				cashboxCodes.add(c.getCashBoxCode());
//			}
//		}
//
//		params.put("cashboxCodes", cashboxCodes);

		String url = "/app/task/atm/step5_post_in_cashBox_list";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 更换钞箱异常，停止加钞并取回
	 *
	 * @param keyStr   用户身份
	 * @param status   状态
	 * @param taskDetailId   任务编号
	 * @param mHandler
	 */
	public static void stopAddCashException(String keyStr, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("status", status);

		String url = "/app/task/atm/step5_rollback_in_cashBox_list";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 关闭保险箱门
	 * 
	 * @param keyStr
	 * @param file
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void CloseAtmDoor(String keyStr, File file, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("file", file);

		String url = "/app/task/atm/step6";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 清机加钞
	 * 
	 * @param keyStr
	 * @param status
	 *            清机加钞（0-待执行，1-完成，2-异常）
	 * @param taskDetailId
	 */
	public static void CashWorking(String keyStr, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("status", status);

		String url = "/app/task/atm/step7";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 耗材检查
	 * 
	 * @param keyStr
	 * @param status
	 * @param taskDetailId
	 * @param mHandler
	 */
	public static void MaterialChecking(String keyStr, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("status", status);

		String url = "/app/task/atm/step8";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 安防
	 * @param keyStr
	 * @param file
	 * @param status
	 *            0-待执行，1-正常，2-异常
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void SecurityDefence(String keyStr, File file, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("status", status);
		if(status == Constants.ATM_TASK_RESULT_FAILURE){
			params.put("file", file);
		}
		String url = "/app/task/atm/step9";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 技防
	 * 
	 * @param keyStr
	 * @param file
	 * @param status
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void TechDefence(String keyStr, File file, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("status", status);
		if(status == Constants.ATM_TASK_RESULT_FAILURE){
			params.put("file", file);
		}
		String url = "/app/task/atm/step10";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 卫生
	 * 
	 * @param keyStr
	 * @param file
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void AtmClean(String keyStr, File file, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("file", file);

		String url = "/app/task/atm/step11";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 物业巡检 status为1，此时file为空；否则为2，为2时，file不为空；
	 * 
	 * @param keyStr
	 * @param file
	 * @param status
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void CheckAgain(String keyStr, File file, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("status", status);
		if(status == Constants.ATM_TASK_RESULT_FAILURE){
			params.put("file", file);
		}
		String url = "/app/task/atm/step12";
		ApiHttpClient.post(url, params, mHandler);
	}

	/**
	 * 自拍留存照
	 * 
	 * @param keyStr
	 * @param file
	 * @param taskDetailId
	 * @param mHandler
	 * @throws FileNotFoundException
	 */
	public static void SelfShot(String keyStr, File file, Integer status, long taskDetailId, AsyncHttpResponseHandler mHandler) throws FileNotFoundException {
		RequestParams params = new RequestParams();
		params.put("taskDetailId", String.valueOf(taskDetailId));
		params.put("keyStr", keyStr);
		params.put("file", file);
		if(status == Constants.ATM_TASK_RESULT_FAILURE){
			params.put("file", file);
		}
		String url = "/app/task/atm/step13";
		ApiHttpClient.post(url, params, mHandler);
	}
}
