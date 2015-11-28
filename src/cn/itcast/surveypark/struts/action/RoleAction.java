package cn.itcast.surveypark.struts.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.domain.security.Role;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.service.RoleService;

/**
 * RoleAction
 */
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	private static final long serialVersionUID = -4655149643371620500L;

	private List<Role> allRoles ;
	//���������Ȩ��id����
	private Integer[] ownRightIds ;
	
	private Integer roleId ;
	
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer[] getOwnRightIds() {
		return ownRightIds;
	}
	public void setOwnRightIds(Integer[] ownRightIds) {
		this.ownRightIds = ownRightIds;
	}
	public List<Role> getAllRoles() {
		return allRoles;
	}
	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}
	@Resource
	private RoleService roleService ;
	
	@Resource
	private RightService rightService ;

	//��ɫû�е�Ȩ�޼���
	private List<Right> noOwnRights;
	
	public List<Right> getNoOwnRights() {
		return noOwnRights;
	}
	public void setNoOwnRights(List<Right> noOwnRights) {
		this.noOwnRights = noOwnRights;
	}
	/**
	 * ��ѯȫ����ɫ
	 */
	public String findAllRoles(){
		this.allRoles = roleService.findAllEntities();
		return "roleListPage" ;
	}
	
	/**
	 * ��ӽ�ɫ 
	 */
	public String toAddRolePage(){
		this.noOwnRights = rightService.findAllEntities();
		return "addRolePage" ;
	}
	
	/**
	 * ������½�ɫ
	 */
	public String saveOrUpdateRole(){
		roleService.saveOrUpdateRole(model,ownRightIds);
		return "findAllRolesAction";
	}
	
	/**
	 * �༭��ɫ
	 */
	public String editRole(){
		return "editRolePage" ;
	}

	public void prepareEditRole(){
		this.model = roleService.getEntity(roleId);
		this.noOwnRights = rightService.findRightNotInRange(model.getRights());
	}
	
	/**
	 * ɾ����ɫ
	 */
	public String deleteRole(){
		Role r = new Role();
		r.setId(roleId);
		roleService.deleteEntity(r);
		return "findAllRolesAction" ;
	}
}
