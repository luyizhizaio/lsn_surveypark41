package cn.itcast.surveypark.service;

import java.util.List;
import java.util.Set;

import cn.itcast.surveypark.domain.security.Role;

public interface RoleService extends BaseService<Role> {

	/**
	 * 保存/更新角色
	 */
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds);

	/**
	 * 查询不在指定范围的角色集合
	 */
	public List<Role> findRolesNotInRange(Set<Role> roles);
	
}
