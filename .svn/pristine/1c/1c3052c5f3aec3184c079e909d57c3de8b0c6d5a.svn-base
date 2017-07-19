package com.jintoufs.logstics.entity;

import java.util.Date;

public class Cashbox extends Entity {

	private String cashBoxCode;

	private String caption;
	
	private String rfid;

	private String useType;// 1--放入钞箱,0--取回钞箱

	private long detailId;



	private Integer cashBoxId;

	private Integer type;      // 1--现金(上缴领现)箱

	private Integer taskType;  // 1--入库,0--出库

	private Integer status;// 状态：1--注册，2--运营，3--维护，4--报废

	private Integer positionType;

	private Date backTime;

	private Integer foundStatus=0;// 1:检测到；0：未扫描达到
	
	private String cellCode="";
	
	private String amount=""; //现金上缴箱中的金额
	
	private Long routeItemId;

	public String getCellCode() {
		return cellCode;
	}

	public void setCellCode(String cellCode) {
		this.cellCode = cellCode;
	}

	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid == null ? null : rfid.trim();
	}

	public Date getBackTime() {
		return backTime;
	}

	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer type) {
		this.taskType = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPositionType() {
		return positionType;
	}

	public void setPositionType(Integer positionType) {
		this.positionType = positionType;
	}

	public Integer getFoundStatus() {
		return foundStatus;
	}

	public void setFoundStatus(Integer foundStatus) {
		this.foundStatus = foundStatus;
	}

	public String getCashBoxCode() {
		return cashBoxCode;
	}

	public void setCashBoxCode(String cashBoxCode) {
		this.cashBoxCode = cashBoxCode;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String cash) {
		this.amount = cash;
	}
	
	public void setCash(double cash){
		this.amount=String.valueOf(cash);
	}
	
	public void setCash(float cash){
		this.amount=String.valueOf(cash);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getRouteItemId() {
		return routeItemId;
	}

	public void setRouteItemId(Long routeItemId) {
		this.routeItemId = routeItemId;
	}
	public String getUseType() {
		return useType;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}
	public Integer getCashBoxId() {
		return cashBoxId;
	}

	public void setCashBoxId(Integer cashBoxId) {
		this.cashBoxId = cashBoxId;
	}

	public long getDetailId() {
		return detailId;
	}

	public void setDetailId(long detailId) {
		this.detailId = detailId;
	}

}