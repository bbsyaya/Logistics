package com.jintoufs.logstics.utils;

/**
 * Ajax 消息处理类
 */
public class AjaxMsg {
	// -- 返回代码定义 --//

	// 200成功，500失败
	public static final String SUCCESS = "200";//提交成功
	public static final String SUCCESS_NOT_PIC = "201";//提交成功，但有照片上传失败
	public static final String FAILURE = "500";//提交失败，请联系管理员！
	public static final String FAILURE_EXIST = "501";//提交失败，该自助设备记录已存在！

	// --Ajax返回消息的基类--//
	private String code = SUCCESS;
	private String message;
	private Object datas;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getDatas() {
		return datas;
	}

	public void setDatas(Object datas) {
		this.datas = datas;
	}

}
