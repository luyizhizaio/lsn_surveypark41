package cn.itcast.surveypark.domain.security;

import java.util.HashSet;
import java.util.Set;

import cn.itcast.surveypark.domain.BaseEntity;

/**
 * Role
 */
public class Role extends BaseEntity{
	private static final long serialVersionUID = 7739845481442277750L;
	private Integer id;
	// 角色名称
	private String roleName;
	// 角色值
	private String roleValue;
	// 角色描述
	private String roleDesc;
	//权限集合,建立从Role到Right之间多对多关联关系
	private Set<Right> rights = new HashSet<Right>();

	public Set<Right> getRights() {
		return rights;
	}

	public void setRights(Set<Right> rights) {
		this.rights = rights;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleValue() {
		return roleValue;
	}

	public void setRoleValue(String roleValue) {
		this.roleValue = roleValue;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

}
