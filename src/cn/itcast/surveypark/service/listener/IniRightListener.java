package cn.itcast.surveypark.service.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.service.RightService;

/**
 * 事件监听器
 */
@SuppressWarnings("rawtypes")
@Component
public class IniRightListener implements ApplicationListener,ServletContextAware{
	
	@Resource
	private RightService rs ;
	
	//接收servletContext
	private ServletContext sc;
	
	public void onApplicationEvent(ApplicationEvent arg0) {
		if(arg0 instanceof ContextRefreshedEvent){
			List<Right> list = rs.findAllEntities();
			Map<String,Right> map = new HashMap<String, Right>();
			for(Right r : list){
				map.put(r.getRightUrl(), r);
			}
			if(sc != null){
				//将所有权限存放到application中
				sc.setAttribute("all_rights_map", map);
			}
			System.out.println("初始化权限完成");
		}
	}

	//注入servletContext
	public void setServletContext(ServletContext servletContext) {
		this.sc = servletContext ;
	}
}
