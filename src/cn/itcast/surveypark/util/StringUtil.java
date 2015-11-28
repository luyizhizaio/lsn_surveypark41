package cn.itcast.surveypark.util;

/**
 * 字符串工具类
 */
public class StringUtil {
	
	/**
	 * 将字符串拆分成数组
	 */
	public static String[] str2Arr(String str,String tag){
		if(ValidateUtil.isValid(str)){
			return str.split(tag);
		}
		return null ;
	}
	
	/**
	 * 在数组中是否包括指定的字符串
	 */
	public static boolean contains(String[] arr,String tag){
		if(ValidateUtil.isValid(arr)){
			for(String str : arr){
				if(str.equals(tag)){
					return true ;
				}
			}
		}
		return false ;
	}
	
	
	/**
	 * 将数组转化成字符串,按照","分割
	 */
	public static String arr2Str(Object[] arr){
		String temp = "" ;
		if(ValidateUtil.isValid(arr)){
			for(int i = 0 ; i < arr.length ; i ++){
				if(i ==(arr.length - 1)){
					temp = temp + arr[i] ; 
				}
				else{
					temp = temp + arr[i] + "," ; 
				}
			}
		}
		return temp ;
		
	}
}
