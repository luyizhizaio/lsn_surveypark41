package cn.itcast.surveypark.service;

import java.util.List;
import java.util.Set;

import cn.itcast.surveypark.domain.security.Role;

public interface RoleService extends BaseService<Role> {

	/**
	 * ����/���½�ɫ
	 */
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds);

	/**
	 * ��ѯ����ָ����Χ�Ľ�ɫ����
	 */
	public List<Role> findRolesNotInRange(Set<Role> roles);
	
}
