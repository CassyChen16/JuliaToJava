package com.kalepso.util;

public class MwmException extends Exception{

	String str1;
	
	public MwmException(String str2) {
		str1=str2;
	   }
	
   public String toString(){ 
	return ("MwmException Occurred: "+str1) ;
   }
}
