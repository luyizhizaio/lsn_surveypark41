package cn.itcast.surveypark.service;

import java.util.List;
import java.util.Set;

import cn.itcast.surveypark.domain.security.Right;

public interface RightService extends BaseService<Right> {

	/**
	 * �������Ȩ��
	 */
	public void saveOrUpdateRight(Right model);

	/**
	 * ׷��Ȩ��
	 */
	public void appendRight(String url);

	/**
	 * ��������Ȩ��
	 */
	public void batchUpdateRights(List<Right> allRights);

	/**
	 * ��ѯָ����Χ��Ȩ�޼���
	 */
	public List<Right> findRightsInRange(Integer[] ownRightIds);

	/**
	 * ��ѯ��ָ����Χ��Ȩ�޼���
	 */
	public List<Right> findRightNotInRange(Set<Right> rights);

	/**
	 * ��ѯȨ��λ�����ֵ
	 */
	public int findMaxRightPos();
	
}
