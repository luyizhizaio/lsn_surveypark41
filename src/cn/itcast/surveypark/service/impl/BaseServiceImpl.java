package cn.itcast.surveypark.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.service.BaseService;

/**
 * 抽象service,专门用于继承
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	private BaseDao<T> dao ;
	
	private Class<T> clazz ;
	
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}


	@Resource
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}


	public void saveEntity(T t) {
		dao.saveEntity(t);
	}

	public void updateEntity(T t) {
		dao.updateEntity(t);
	}

	public void deleteEntity(T t) {
		dao.deleteEntity(t);
	}

	public void saveOrUpdateEntity(T t) {
		dao.saveOrUpdateEntity(t);
	}

	public void batchEntityByHQL(String hql, Object... objects) {
		dao.batchEntityByHQL(hql, objects);
	}

	public T getEntity(Integer id) {
		return (T) dao.getEntity(id);
	}

	public T loadEntity(Integer id) {
		return (T) dao.loadEntity(id);
	}

	public List<T> findEntityByHQL(String hql, Object... objects) {
		return dao.findEntityByHQL(hql, objects);
	}
	
	//单值检索,确保查询结果有且仅有一条记录
	public Object uniqueResult(String hql,Object...objects){
		return dao.uniqueResult(hql, objects);
	}
	
	/**
	 * 查询全部实体
	 */
	public List<T> findAllEntities(){
		String hql = "from " + clazz.getCanonicalName() ;
		return this.findEntityByHQL(hql);
	}
}