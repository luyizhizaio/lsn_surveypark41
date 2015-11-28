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
	 * 判断字符串的有效性 
	 */
	public static boolean isValid(String str){
		if(str == null || str.trim().equals("")){
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断集合的有效性 
	 */
	public static boolean isValid(Collection coll){
		if(coll == null || coll.isEmpty()){
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断集合的有效性 
	 */
	public static boolean isValid(Object[] arr){
		if(arr == null || arr.length == 0){
			return false ;
		}
		return true ;
	}
	
	/**
	 * 判断是否有权限
	 */
	public static boolean hasRight(String actionName,String ns,HttpServletRequest req,BaseAction action){
		String url = "" ;
		if(!ValidateUtil.isValid(ns) || ns.equals("/")){
			ns = "" ;
		}
		url = ns + "/" + actionName ;
		
		//得到ServletContext + Session
		ServletContext sc = req.getSession().getServletContext();
		HttpSession session = req.getSession();
		//得到权限
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		//判断是否登陆
		User u = (User) session.getAttribute("user");
		if(u != null && action != null && action instanceof UserAware){
			((UserAware)action).setUser(u);
		}
		
		Right r = map.get(url);
		if(r == null || r.isCommon()){
			return true ;
		}
		//不是
		else{
			//登陆?
			if(u != null){
				
				//超级管理员?
				if(u.isSuperAdmin()){
					return true;
				}
				//不是
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
