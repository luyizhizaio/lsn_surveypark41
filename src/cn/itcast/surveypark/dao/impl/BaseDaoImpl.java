package cn.itcast.surveypark.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cn.itcast.surveypark.dao.BaseDao;

/**
 * 抽象dao实现,专门用于继承的
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	
	@SuppressWarnings("rawtypes")
	private Class clazz ;
	
	@SuppressWarnings("rawtypes")
	public BaseDaoImpl(){
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class) type.getActualTypeArguments()[0];
	}
	//注入sessionFactory
	@Resource
	private SessionFactory sf ;
	
	public void saveEntity(T t) {
		sf.getCurrentSession().save(t);
	}

	public void updateEntity(T t) {
		sf.getCurrentSession().update(t);
	}

	public void deleteEntity(T t) {
		sf.getCurrentSession().delete(t);
	}

	public void saveOrUpdateEntity(T t) {
		sf.getCurrentSession().saveOrUpdate(t);
	}

	public void batchEntityByHQL(String hql, Object... objects) {
		Session s = sf.getCurrentSession();
		Query q = s.createQuery(hql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		q.executeUpdate();
	}

	public T getEntity(Integer id) {
		return (T) sf.getCurrentSession().get(clazz, id);
	}

	public T loadEntity(Integer id) {
		return (T) sf.getCurrentSession().load(clazz, id);
	}

	public List<T> findEntityByHQL(String hql, Object... objects) {
		Session s = sf.getCurrentSession();
		Query q = s.createQuery(hql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		return q.list();
	}
	
	//单值检索,确保查询结果有且仅有一条记录
	public Object uniqueResult(String hql,Object...objects){
		Session s = sf.getCurrentSession();
		Query q = s.createQuery(hql);
		for(int i = 0 ; i < objects.length ; i ++){
			q.setParameter(i, objects[i]);
		}
		return q.uniqueResult();
	}
}
