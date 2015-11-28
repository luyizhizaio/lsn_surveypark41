package cn.itcast.surveypark.struts.interceptor;

import java.util.Map;

import org.apache.struts2.ServletActionContext;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.struts.UserAware;
import cn.itcast.surveypark.struts.action.BaseAction;
import cn.itcast.surveypark.util.ValidateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * µÇÂ½À¹½ØÆ÷
 */
public class RightFilterInterceptor implements Interceptor {

	private static final long serialVersionUID = -5259259624406021055L;

	public void destroy() {
	}

	public void init() {
	}

	@SuppressWarnings("rawtypes")
	public String intercept(ActionInvocation arg0) throws Exception {
		BaseAction action = (BaseAction) arg0.getAction();
		ActionProxy proxy = arg0.getProxy();
		String ns = proxy.getNamespace();
		String actionName = proxy.getActionName();
		if(ValidateUtil.hasRight(actionName, ns, ServletActionContext.getRequest(), action)){
			return arg0.invoke();
		}
		else{
			return "login" ;
		}
	}
}
