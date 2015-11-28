package cn.itcast.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.SurveyService;

/**
 * TestUserService
 */
public class TestSurveyService {
	private static ApplicationContext ac ;
	
	@BeforeClass
	public static void iniAC(){
		ac = new ClassPathXmlApplicationContext("beans.xml");
	}
	
	@Test
	public void newSurvey(){
		SurveyService ss = (SurveyService) ac.getBean("surveyService");
		User u = new User();
		u.setId(3);
		ss.newSurvey(u);
	}
	
	@Test
	public void deleteSurvey(){
		SurveyService ss = (SurveyService) ac.getBean("surveyService");
		ss.deleteSurvey(7);
	}
	
	@Test
	public void getQuestion(){
		SurveyService ss = (SurveyService) ac.getBean("surveyService");
		ss.getQuestion(2);
	}
	@Test
	public void getSurvey(){
		SurveyService ss = (SurveyService) ac.getBean("surveyService");
		ss.getSurvey(1);
	}
}
