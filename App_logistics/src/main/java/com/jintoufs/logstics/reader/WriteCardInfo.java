package com.jintoufs.logstics.reader;

public class WriteCardInfo{
	private int m_Status=0;//0未操作,-1失败,1成功
	private String m_Info="";
	
	public int getStatus(){
		return m_Status;
	}
	
	public void setStatus(int s){
		m_Status=s;
	}
	
	public String getInfo(){
		return m_Info;
	}
	
	public void setInfo(String s){
		m_Info=s;
	}
}