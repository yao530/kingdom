package com.ltu.constant.enums;

public class FileBusibessdict {
	
	 public enum BusibessType{
		 IMAGE(0,"img"), //图片
		 JSON(1,"js");//json
		
		 private int index;
		 private String value;
		 
		 BusibessType(int index,String value){
			 this.index=index;
			 this.value=value;
		 }

		public int getIndex() {
			return index;
		}

		public String getValue() {
			return value;
		}

		
		 
	 }
	

}
