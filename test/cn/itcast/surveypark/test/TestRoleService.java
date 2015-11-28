package cn.itcast.surveypark.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.surveypark.domain.security.Role;
import cn.itcast.surveypark.service.RoleService;

/**
 * TestUserService
 */
public class TestRoleService {
	private static ApplicationContext ac ;
	
	@BeforeClass
	public static void iniAC(){
		ac = new ClassPathXmlApplicationContext("beans.xml");
	}
	
	@Test
	public void deleteRole(){
		RoleService rs = (RoleService) ac.getBean("roleService");
		Role r = new Role();
		r.setId(1);
		rs.deleteEntity(r);
	}
}
