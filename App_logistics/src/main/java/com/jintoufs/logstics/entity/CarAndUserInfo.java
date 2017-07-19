package com.jintoufs.logstics.entity;


@SuppressWarnings("serial")
public class CarAndUserInfo extends Entity {

	private String license;
	
	private String keyName;
	
	private String passName;

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getPassName() {
		return passName;
	}

	public void setPassName(String passName) {
		this.passName = passName;
	}
}
