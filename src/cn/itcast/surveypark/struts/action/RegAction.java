package cn.itcast.surveypark.struts.action;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.UserService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.ValidateUtil;

/**
 * RegAction
 */
@Controller
@Scope("prototype")
public class RegAction extends BaseAction<User> {

	private static final long serialVersionUID = -7197488056805166872L;
	//ע��UserService
	@Resource
	private UserService us ;
	
	//ȷ������
	private String confirmPassword ;
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * �ﵽע��ҳ��c
	 */
	@SkipValidation
	public String toRegPage(){
		return "regView" ;
	}
	
	/**
	 * ����ע��
	 */
	public String doReg(){
		//��������м���
		model.setPassword(DataUtil.md5(model.getPassword()));
		us.saveEntity(model);
		return "success" ;
	}
	
	public void validate() {
		//�ǿ�
		if(!ValidateUtil.isValid(model.getEmail())){
			addFieldError("email", "email�Ǳ�����");
		}
		if(!ValidateUtil.isValid(model.getPassword())){
			addFieldError("password", "password�Ǳ�����");
		}
		if(!ValidateUtil.isValid(model.getNickName())){
			addFieldError("nickName", "nickName�Ǳ�����");
		}
		if(this.hasErrors()){
			return ;
		}
		//һ����
		if(!model.getPassword().equals(confirmPassword)){
			addFieldError("password", "���벻һ��");
			return ;
		}
		//email��Ч��
		boolean b = us.isRegisted(model.getEmail());
		if(b){
			addFieldError("email", "�����Ѿ�ռ��");
		}
	}
}
