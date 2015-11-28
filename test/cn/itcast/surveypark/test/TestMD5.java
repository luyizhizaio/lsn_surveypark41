package cn.itcast.surveypark.test;

import org.junit.Test;

import cn.itcast.surveypark.util.DataUtil;

/**
 * TestUserService
 */
public class TestMD5 {
	
	@Test
	public void md5(){
		String str = "12eeeeee3";
		System.out.println(DataUtil.md5(str));
	}
	
	@Test
	public void kk(){
		System.out.println(Integer.parseInt("q7_0".substring(1, "q7_0".indexOf("_"))));
	}
}
