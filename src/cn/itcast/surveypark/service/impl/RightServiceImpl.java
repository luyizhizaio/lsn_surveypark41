package cn.itcast.surveypark.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.StringUtil;
import cn.itcast.surveypark.util.ValidateUtil;

@Service("rightService")
public class RightServiceImpl extends BaseServiceImpl<Right> implements RightService,ServletContextAware {
	
	//接收ServletContext对象
	private ServletContext sc;

	@Resource(name="rightDao")
	public void setDao(BaseDao<Right> dao) {
		super.setDao(dao);
	}
	
	
	public void updateEntity(Right t) {
		super.updateEntity(t);
		this.resfreshRightInApplication(t);
	}



	public void deleteEntity(Right t) {
		super.deleteEntity(t);
		this.removeRightInApplication(t);
	}



	/**
	 * 保存更新权限
	 */
	public void saveOrUpdateRight(Right model){
		//insert
		if(model.getId() == null){
			int rightPos = 0 ;
			long rightCode = 1 ;
			String hql = "select max(r.rightPos),max(r.rightCode) from Right r where r.rightPos = (select max(rr.rightPos) from Right rr)" ;
			Object[] res = (Object[]) this.uniqueResult(hql);
			Integer topRightPos = (Integer) res[0];
			Long topRightCode = (Long) res[1];
			//null
			if(topRightPos == null){
				rightPos = 0 ;
				rightCode = 1 ;
			}
			else{
				if(topRightCode >= (1L << 60) ){
					rightPos = topRightPos + 1 ;
					rightCode = 1 ;
				}
				else{
					rightPos = topRightPos;
					rightCode =  topRightCode << 1;
				}
			}
			model.setRightPos(rightPos);
			model.setRightCode(rightCode);
		}
		this.saveOrUpdateEntity(model);
		this.resfreshRightInApplication(model);
	}
	
	/**
	 * 追加权限
	 */
	public void appendRight(String url){
		String hql = "select count(*) from Right r where r.rightUrl = ?" ;
		Long count = (Long) this.uniqueResult(hql, url);
		if(count == 0){
			Right r = new Right();
			r.setRightUrl(url);
			processRightName(r);
			this.saveOrUpdateRight(r);
			resfreshRightInApplication(r);
		}
	}
	
	/**
	 * 处理权限名
	 */
	private void processRightName(Right r) {
		String url = r.getRightUrl() ;
		String entity = "" ;
		String action ="" ;
		if(url.toLowerCase().contains("survey")){
			entity = "调查";
		}
		else if(url.toLowerCase().contains("page")){
			entity = "页面";
		}
		else if(url.toLowerCase().contains("question")){
			entity = "问题";
		}
		else if(url.toLowerCase().contains("role")){
			entity = "角色";
		}
		else if(url.toLowerCase().contains("right")){
			entity = "权限";
		}
		else if(url.toLowerCase().contains("answer")){
			entity = "答案";
		}
		else if(url.toLowerCase().contains("user")){
			entity = "用户";
		}
		else if(url.toLowerCase().contains("authorize")){
			entity = "授权";
		}
		
		//处理动作名称
		if(url.toLowerCase().contains("update")){
			action = "保存";
		}
		else if(url.toLowerCase().contains("save")){
			action = "更新";
		}
		else if(url.toLowerCase().contains("delete")){
			action = "删除";
		}
		else if(url.toLowerCase().contains("batch")){
			action = "批量处理";
		}
		else if(url.toLowerCase().contains("move")){
			action = "移动/复制";
		}
		else if(url.toLowerCase().contains("add")){
			action = "添加";
		}
		else if(url.toLowerCase().contains("edit")){
			action = "编辑";
		}
		else if(url.toLowerCase().contains("all")){
			action = "管理";
		}
		else if(url.toLowerCase().contains("engage")){
			action = "参与";
		}
		else if(url.toLowerCase().contains("clear")){
			action = "清除";
		}
		r.setRightName(action + entity) ;
	}


	/**
	 * 批量更新权限
	 */
	public void batchUpdateRights(List<Right> allRights){
		String hql = "update Right r set r.rightName = ?,r.common = ? where r.id = ?" ;
		for(Right r : allRights){
			this.batchEntityByHQL(hql,r.getRightName(),r.isCommon(),r.getId());
		}
		//
		this.resfreshAllRightsInApplication();
	}
	
	/**
	 * 查询指定范围的权限集合
	 */
	public List<Right> findRightsInRange(Integer[] ownRightIds){
		String hql ="from Right r where r.id in ("+StringUtil.arr2Str(ownRightIds)+")" ;
		return this.findEntityByHQL(hql);
	}
	
	/**
	 * 查询在指定范围的权限集合
	 */
	public List<Right> findRightNotInRange(Set<Right> rights){
		if(!ValidateUtil.isValid(rights)){
			return this.findAllEntities();
		}
		else{
			String hql = "from Right r where r.id not in (" + DataUtil.extractIds(rights) + ")" ;
			return this.findEntityByHQL(hql);
		}
	}
	
	/**
	 * 查询权限位的最大值
	 */
	public int findMaxRightPos(){
		String hql = "select max(r.rightPos) from Right r" ;
		Integer rightPos = (Integer) this.uniqueResult(hql);
		return rightPos == null ?0:rightPos ;
	}
	
	/**
	 * 刷新application范围中的权限
	 */
	private void resfreshAllRightsInApplication(){
		if(sc == null)
			return ;
		List<Right> list = this.findAllEntities();
		Map<String,Right> map = new HashMap<String, Right>();
		for(Right r : list){
			map.put(r.getRightUrl(), r);
		}
		if(sc != null){
			//将所有权限存放到application中
			sc.setAttribute("all_rights_map", map);
		}
	}
	
	/**
	 * 刷新application范围中的权限
	 */
	private void resfreshRightInApplication(Right r){
		if(sc == null)
			return ;
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		if(map != null){
			map.put(r.getRightUrl(), r);
		}
	}
	
	/**
	 * 刷新application范围中的权限
	 */
	private void removeRightInApplication(Right r){
		if(sc == null)
			return ;
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		if(map != null){
			map.remove(r.getRightUrl());
		}
	}
	
	//注入ServletContext
	public void setServletContext(ServletContext servletContext) {
		this.sc = servletContext ;
	}
}
