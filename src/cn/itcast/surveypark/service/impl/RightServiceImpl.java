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
	
	//����ServletContext����
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
	 * �������Ȩ��
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
	 * ׷��Ȩ��
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
	 * ����Ȩ����
	 */
	private void processRightName(Right r) {
		String url = r.getRightUrl() ;
		String entity = "" ;
		String action ="" ;
		if(url.toLowerCase().contains("survey")){
			entity = "����";
		}
		else if(url.toLowerCase().contains("page")){
			entity = "ҳ��";
		}
		else if(url.toLowerCase().contains("question")){
			entity = "����";
		}
		else if(url.toLowerCase().contains("role")){
			entity = "��ɫ";
		}
		else if(url.toLowerCase().contains("right")){
			entity = "Ȩ��";
		}
		else if(url.toLowerCase().contains("answer")){
			entity = "��";
		}
		else if(url.toLowerCase().contains("user")){
			entity = "�û�";
		}
		else if(url.toLowerCase().contains("authorize")){
			entity = "��Ȩ";
		}
		
		//����������
		if(url.toLowerCase().contains("update")){
			action = "����";
		}
		else if(url.toLowerCase().contains("save")){
			action = "����";
		}
		else if(url.toLowerCase().contains("delete")){
			action = "ɾ��";
		}
		else if(url.toLowerCase().contains("batch")){
			action = "��������";
		}
		else if(url.toLowerCase().contains("move")){
			action = "�ƶ�/����";
		}
		else if(url.toLowerCase().contains("add")){
			action = "���";
		}
		else if(url.toLowerCase().contains("edit")){
			action = "�༭";
		}
		else if(url.toLowerCase().contains("all")){
			action = "����";
		}
		else if(url.toLowerCase().contains("engage")){
			action = "����";
		}
		else if(url.toLowerCase().contains("clear")){
			action = "���";
		}
		r.setRightName(action + entity) ;
	}


	/**
	 * ��������Ȩ��
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
	 * ��ѯָ����Χ��Ȩ�޼���
	 */
	public List<Right> findRightsInRange(Integer[] ownRightIds){
		String hql ="from Right r where r.id in ("+StringUtil.arr2Str(ownRightIds)+")" ;
		return this.findEntityByHQL(hql);
	}
	
	/**
	 * ��ѯ��ָ����Χ��Ȩ�޼���
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
	 * ��ѯȨ��λ�����ֵ
	 */
	public int findMaxRightPos(){
		String hql = "select max(r.rightPos) from Right r" ;
		Integer rightPos = (Integer) this.uniqueResult(hql);
		return rightPos == null ?0:rightPos ;
	}
	
	/**
	 * ˢ��application��Χ�е�Ȩ��
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
			//������Ȩ�޴�ŵ�application��
			sc.setAttribute("all_rights_map", map);
		}
	}
	
	/**
	 * ˢ��application��Χ�е�Ȩ��
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
	 * ˢ��application��Χ�е�Ȩ��
	 */
	private void removeRightInApplication(Right r){
		if(sc == null)
			return ;
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		if(map != null){
			map.remove(r.getRightUrl());
		}
	}
	
	//ע��ServletContext
	public void setServletContext(ServletContext servletContext) {
		this.sc = servletContext ;
	}
}
