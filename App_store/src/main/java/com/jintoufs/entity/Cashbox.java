package com.jintoufs.entity;


public class Cashbox extends Entity {

	private String cashBoxCode;
	private String rfid;
	private String storeName;
	private String storeAreaName;
	private String shelfName;
	private int status;
	private int foundStatus;
	private String cellCode;
	private String vendor;
	private String type;
	private String holder;
	private String bankName;
	private String position;//钞箱当前位置
	private String preparePosition;//钞箱准备位置
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getBackTime() {
		return backTime;
	}
	public void setBackTime(String backTime) {
		this.backTime = backTime;
	}
	private String siteName;
	private String backTime;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getCellCode() {
		return cellCode;
	}
	public void setCellCode(String cellCode) {
		this.cellCode = cellCode;
	}
	public String getCashBoxCode() {
		return cashBoxCode;
	}
	public void setCashBoxCode(String cashboxCode) {
		this.cashBoxCode = cashboxCode;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public String getStoreAreaName() {
		return storeAreaName;
	}
	public void setStoreAreaName(String storeAreaName) {
		this.storeAreaName = storeAreaName;
	}
	public String getShelfName() {
		return shelfName;
	}
	public void setShelfName(String shelfName) {
		this.shelfName = shelfName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getFoundStatus() {
		return foundStatus;
	}
	public void setFoundStatus(int foundStatus) {
		this.foundStatus = foundStatus;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPreparePosition() {
		return preparePosition;
	}
	public void setPreparePosition(String preparePosition) {
		this.preparePosition = preparePosition;
	}
}