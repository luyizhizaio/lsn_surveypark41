package cn.itcast.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.UserService;

/**
 * TestUserService
 */
public class TestUserService {
	private static ApplicationContext ac ;
	
	@BeforeClass
	public static void iniAC(){
		ac = new ClassPathXmlApplicationContext("beans.xml");
	}
	
	@Test
	public void newUser(){
		UserService us = (UserService) ac.getBean("userService");
		User u = new User();
		u.setEmail("xpc@tom.com");
		u.setPassword("123456");
		u.setNickName("stone");
		us.saveEntity(u);
	}
}
