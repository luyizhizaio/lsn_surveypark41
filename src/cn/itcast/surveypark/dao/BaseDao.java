package cn.itcast.surveypark.dao;

import java.util.List;

/**
 * BaseDao
 */
public interface BaseDao<T> {
	public void saveEntity(T t);
	public void updateEntity(T t);
	public void deleteEntity(T t);
	public void saveOrUpdateEntity(T t);
	public void batchEntityByHQL(String hql,Object...objects);
	
	public T getEntity(Integer id);
	public T loadEntity(Integer id);
	public List<T> findEntityByHQL(String hql,Object...objects);
	//��ֵ����,ȷ����ѯ������ҽ���һ����¼
	public Object uniqueResult(String hql,Object...objects);
}
