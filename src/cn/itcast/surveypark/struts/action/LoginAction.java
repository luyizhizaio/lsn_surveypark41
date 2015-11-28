package cn.itcast.surveypark.struts.action;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.service.UserService;
import cn.itcast.surveypark.util.DataUtil;

/**
 * LoginAction
 * SessionAware:session关注
 */
@Controller
@Scope("prototype")

public class LoginAction extends BaseAction<User> implements SessionAware {

	private static final long serialVersionUID = 4515108037010593788L;
	
	@Resource
	private UserService userService ;
	
	@Resource
	private RightService rightService ;

	//接收session的map
	private Map<String, Object> sessionMap;
	
	public User getModel() {
		return model;
	}
	
	/**
	 * 达到登陆页面
	 */
	public String toLoginPage(){
		return "loginPage";
	}
	
	/**
	 * 进行登陆
	 */
	public String doLogin(){
		return "success" ;
	}

	/**
	 * 校验
	 */
	public void validateDoLogin(){
		ServletActionContext.getServletContext();
		User u = userService.loginValidate(model.getEmail(),DataUtil.md5(model.getPassword()));
		//验证失败
		if(u == null){
			this.addActionError("email/password 错误");
		}
		//验证通过
		else{
			//初始化权限总和长度
			int maxPos = rightService.findMaxRightPos();
			u.setRightSum(new long[maxPos + 1]);
			//计算用户的权限总和
			u.calculateRightSum();
			sessionMap.put("user",u);
		}
	}

	/**
	 * 注入Session的map
	 */
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0 ;
	}
}
