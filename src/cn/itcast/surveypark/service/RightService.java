package cn.itcast.surveypark.service;

import java.util.List;
import java.util.Set;

import cn.itcast.surveypark.domain.security.Right;

public interface RightService extends BaseService<Right> {

	/**
	 * 保存更新权限
	 */
	public void saveOrUpdateRight(Right model);

	/**
	 * 追加权限
	 */
	public void appendRight(String url);

	/**
	 * 批量更新权限
	 */
	public void batchUpdateRights(List<Right> allRights);

	/**
	 * 查询指定范围的权限集合
	 */
	public List<Right> findRightsInRange(Integer[] ownRightIds);

	/**
	 * 查询在指定范围的权限集合
	 */
	public List<Right> findRightNotInRange(Set<Right> rights);

	/**
	 * 查询权限位的最大值
	 */
	public int findMaxRightPos();
	
}
