package cn.itcast.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.surveypark.service.StatisticsService;

/**
 * TestUserService
 */
public class TestStatisticsService {
	private static ApplicationContext ac ;
	
	@BeforeClass
	public static void iniAC(){
		ac = new ClassPathXmlApplicationContext("beans.xml");
	}
	
	@Test
	public void statistics(){
		StatisticsService ss = (StatisticsService) ac.getBean("statisticsService");
		ss.statistics(1);
	}
}
