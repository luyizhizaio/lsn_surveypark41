package cn.itcast.surveypark.struts.interceptor;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.util.ValidateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 *  ²¶»ñURLÀ¹½ØÆ÷
 */
public class CatchUrlInterceptor implements Interceptor {

	private static final long serialVersionUID = -8835829514257764285L;
	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		String ns = proxy.getNamespace();
		String actionName = proxy.getActionName();
		String url = "" ;
		if(!ValidateUtil.isValid(ns) || ns.equals("/")){
			ns = "" ;
		}
		url = ns + "/" + actionName ;
		ServletContext sc = ServletActionContext.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		RightService rs = (RightService) ac.getBean("rightService");
		rs.appendRight(url);
		return invocation.invoke();
	}
}
