package cn.itcast.surveypark.util;

/**
 * �ַ���������
 */
public class StringUtil {
	
	/**
	 * ���ַ�����ֳ�����
	 */
	public static String[] str2Arr(String str,String tag){
		if(ValidateUtil.isValid(str)){
			return str.split(tag);
		}
		return null ;
	}
	
	/**
	 * ���������Ƿ����ָ�����ַ���
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
	 * ������ת�����ַ���,����","�ָ�
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
