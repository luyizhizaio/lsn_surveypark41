package cn.itcast.surveypark.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.struts.UserAware;
import cn.itcast.surveypark.struts.action.BaseAction;

/**
 * ValidateUtil
 */
@SuppressWarnings("rawtypes")
public class ValidateUtil {
	
	/**
	 * �ж��ַ�������Ч�� 
	 */
	public static boolean isValid(String str){
		if(str == null || str.trim().equals("")){
			return false ;
		}
		return true ;
	}
	
	/**
	 * �жϼ��ϵ���Ч�� 
	 */
	public static boolean isValid(Collection coll){
		if(coll == null || coll.isEmpty()){
			return false ;
		}
		return true ;
	}
	
	/**
	 * �жϼ��ϵ���Ч�� 
	 */
	public static boolean isValid(Object[] arr){
		if(arr == null || arr.length == 0){
			return false ;
		}
		return true ;
	}
	
	/**
	 * �ж��Ƿ���Ȩ��
	 */
	public static boolean hasRight(String actionName,String ns,HttpServletRequest req,BaseAction action){
		String url = "" ;
		if(!ValidateUtil.isValid(ns) || ns.equals("/")){
			ns = "" ;
		}
		url = ns + "/" + actionName ;
		
		//�õ�ServletContext + Session
		ServletContext sc = req.getSession().getServletContext();
		HttpSession session = req.getSession();
		//�õ�Ȩ��
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		//�ж��Ƿ��½
		User u = (User) session.getAttribute("user");
		if(u != null && action != null && action instanceof UserAware){
			((UserAware)action).setUser(u);
		}
		
		Right r = map.get(url);
		if(r == null || r.isCommon()){
			return true ;
		}
		//����
		else{
			//��½?
			if(u != null){
				
				//��������Ա?
				if(u.isSuperAdmin()){
					return true;
				}
				//����
				else{
					if(u.hasRight(r)){
						return true ;
					}
					else{
						return false ;
					}
				}
			}
			else{
				return false ;
			}
		}
	}
}
