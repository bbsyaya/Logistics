package com.jintoufs.entity;

/**
 * 枚举量存储
 * @author Administrator
 *
 */
public class EnumType{
	
	/**
	 * 出库入库
	 * @author Administrator
	 *
	 */
	public enum StockType{
		OutStock(0), 	//出库
		InStock(1),  	//入库
		EscortStock(2); //交接
		
		private int m_Value;
		
		private StockType(int value){
			m_Value=value;
		}
		
		public int value(){
			return m_Value;
		}
		
		public static StockType valueOf(int value){
			if(value == 0){
				return OutStock;
			}
			else if(value == 1){
				return InStock;
			}
			else{
				return null;
			}
		}
	}
	
	/**
	 * 扫描到的钞箱的错误类型,保存差错的时候用
	 * @author Administrator
	 *
	 */
	public enum ErrorTypeOfCount{
		Less(0), //钞箱少扫描了
		More(1), //钞箱多扫描了
		Both(2); //即有少的又有多的
		
		private int m_Value;
		
		private ErrorTypeOfCount(int value){
			m_Value=value;
		}
		
		public int value(){
			return m_Value;
		}
		
		public static ErrorTypeOfCount valueOf(int value){
			if(value == 0){
				return Less;
			}else if(value == 1){
				return More;
			}else if(value == 2){
				return Both;
			}else{
				return null;
			}
		}
	}
}