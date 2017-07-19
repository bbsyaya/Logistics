package com.jintoufs.logstics.entity;

public class Task extends Entity {
	private String caption;
	private String business_type;
	private String excute_time;
	private int terminal_count;
	private int cashbox_count;
	private int unfinished_terminal_count;
	private int finished_terminal_count;

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}

	public String getExcute_time() {
		return excute_time;
	}

	public void setExcute_time(String excute_time) {
		this.excute_time = excute_time;
	}

	public int getTerminal_count() {
		return terminal_count;
	}

	public void setTerminal_count(int terminal_count) {
		this.terminal_count = terminal_count;
	}

	public int getCashbox_count() {
		return cashbox_count;
	}

	public void setCashbox_count(int cashbox_count) {
		this.cashbox_count = cashbox_count;
	}

	public int getUnfinished_terminal_count() {
		return unfinished_terminal_count;
	}

	public void setUnfinished_terminal_count(int unfinished_terminal_count) {
		this.unfinished_terminal_count = unfinished_terminal_count;
	}

	public int getFinished_terminal_count() {
		return finished_terminal_count;
	}

	public void setFinished_terminal_count(int finished_terminal_count) {
		this.finished_terminal_count = finished_terminal_count;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}