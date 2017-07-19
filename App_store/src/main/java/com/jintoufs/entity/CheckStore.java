package com.jintoufs.entity;

import java.util.Date;

/**
 * 盘库记录
 */
public class CheckStore {
	private Integer id;
	private String checkTime;
	private String createTime;
	private Byte status; // 0:未盘库，1：已盘库
	private Integer sysNumber;
	private Integer realNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getSysNumber() {
		return sysNumber;
	}

	public void setSysNumber(Integer sysNumber) {
		this.sysNumber = sysNumber;
	}

	public Integer getRealNumber() {
		return realNumber;
	}

	public void setRealNumber(Integer realNumber) {
		this.realNumber = realNumber;
	}
}
