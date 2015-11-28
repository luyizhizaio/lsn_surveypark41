package cn.itcast.surveypark.struts.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.service.RightService;

/**
 * RightAction
 */
@Controller
@Scope("prototype")
public class RightAction extends BaseAction<Right> {

	private static final long serialVersionUID = -3425851108801531064L;

	private List<Right> allRights ;
	
	@Resource
	private RightService rs ;
	
	private Integer rightId ;
	
	public Integer getRightId() {
		return rightId;
	}
	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}
	public List<Right> getAllRights() {
		return allRights;
	}
	public void setAllRights(List<Right> allRights) {
		this.allRights = allRights;
	}
	
	/**
	 * ��ѯ����Ȩ��
	 */
	public String findAllRights(){
		this.allRights = rs.findAllEntities();
		return "rightListPage" ;
	}
	
	/**
	 * ���Ȩ��
	 */
	public String toAddRightPage(){
		return "addRightPage" ;
	}
	
	/**
	 * ����/����Ȩ��
	 */
	public String saveOrUpdateRight(){
		rs.saveOrUpdateRight(model);
		return "findAllRightsAction" ;
	}
	
	/**
	 * �༭Ȩ�� 
	 */
	public String editRight(){
		return "editRightPage" ;
	}

	public void prepareEditRight(){
		this.model = rs.getEntity(rightId);
	}
	
	/**
	 * ɾ��Ȩ��
	 */
	public String deleteRight(){
		Right r = new Right();
		r.setId(rightId);
		rs.deleteEntity(r);
		return "findAllRightsAction";
	}
	
	/**
	 * ��������Ȩ��
	 */
	public String batchUpdateRights(){
		rs.batchUpdateRights(allRights);
		return "findAllRightsAction" ;
	}
}
