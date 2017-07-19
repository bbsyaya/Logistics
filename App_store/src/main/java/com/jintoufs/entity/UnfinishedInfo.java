package com.jintoufs.entity;

public class UnfinishedInfo extends Entity{
	private int taskType; //任务类型
	private int unfinishedCount; //未完成数量
	
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public int getUnfinishedCount() {
		return unfinishedCount;
	}
	public void setUnfinishedCount(int unfinishedCount) {
		this.unfinishedCount = unfinishedCount;
	}
	
	
}
