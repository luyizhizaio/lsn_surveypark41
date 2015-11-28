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
 * SessionAware:session��ע
 */
@Controller
@Scope("prototype")

public class LoginAction extends BaseAction<User> implements SessionAware {

	private static final long serialVersionUID = 4515108037010593788L;
	
	@Resource
	private UserService userService ;
	
	@Resource
	private RightService rightService ;

	//����session��map
	private Map<String, Object> sessionMap;
	
	public User getModel() {
		return model;
	}
	
	/**
	 * �ﵽ��½ҳ��
	 */
	public String toLoginPage(){
		return "loginPage";
	}
	
	/**
	 * ���е�½
	 */
	public String doLogin(){
		return "success" ;
	}

	/**
	 * У��
	 */
	public void validateDoLogin(){
		ServletActionContext.getServletContext();
		User u = userService.loginValidate(model.getEmail(),DataUtil.md5(model.getPassword()));
		//��֤ʧ��
		if(u == null){
			this.addActionError("email/password ����");
		}
		//��֤ͨ��
		else{
			//��ʼ��Ȩ���ܺͳ���
			int maxPos = rightService.findMaxRightPos();
			u.setRightSum(new long[maxPos + 1]);
			//�����û���Ȩ���ܺ�
			u.calculateRightSum();
			sessionMap.put("user",u);
		}
	}

	/**
	 * ע��Session��map
	 */
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0 ;
	}
}
