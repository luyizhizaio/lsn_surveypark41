package cn.itcast.surveypark.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.User;
import cn.itcast.surveypark.service.UserService;
import cn.itcast.surveypark.util.StringUtil;
import cn.itcast.surveypark.util.ValidateUtil;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements
		UserService {

	/**
	 * ����ע��,ע��ָ����dao
	 */
	@Resource(name="userDao")
	public void setDao(BaseDao<User> dao) {
		super.setDao(dao);
	}
	
	/**
	 * ��֤email����Ч��
	 */
	public boolean isRegisted(String email){
		String hql = "from User u where u.email = ?" ;
		List<User> list = this.findEntityByHQL(hql, email);
		return ValidateUtil.isValid(list);
	}
	
	/**
	 * У���½
	 */
	public User loginValidate(String email, String password){
		String hql = "from User u where u.email = ? and u.password = ?" ;
		List<User> list = this.findEntityByHQL(hql, email,password);
		return ValidateUtil.isValid(list)?list.get(0):null;
	}
	
	/**
	 * ������Ȩ
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateAuthorize(User model, Integer[] ownRoleIds){
		//һ��Ҫ�Ȳ��ԭ����model����,������User������Ϣ�޸�
		model = this.getEntity(model.getId());
		if(!ValidateUtil.isValid(ownRoleIds)){
			model.getRoles().clear();
		}
		else{
			String hql = "from Role r where r.id in ("+StringUtil.arr2Str(ownRoleIds)+")" ;
			model.setRoles(new HashSet(this.findEntityByHQL(hql)));
		}
	}
	
	/**
	 * �����Ȩ
	 */
	public void clearAuthorize(Integer userId){
		this.getEntity(userId).getRoles().clear();
	}
}
