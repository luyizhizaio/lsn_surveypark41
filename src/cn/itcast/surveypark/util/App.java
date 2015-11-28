package cn.itcast.surveypark.util;


public class App {

	public static void main(String[] args) {
		String str = "11,1,1,1?id=123" ;
		System.out.println(str.substring(0,str.indexOf("?")) );
	}

}
