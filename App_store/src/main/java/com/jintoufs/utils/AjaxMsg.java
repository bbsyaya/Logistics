package com.jintoufs.utils;

/**
 * Ajax 消息处理类
 */
public class AjaxMsg {
	// -- 返回代码定义 --//

	// 200成功，500失败
	public static final String SUCCESS = "200";
	public static final String FAILURE = "500";

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
