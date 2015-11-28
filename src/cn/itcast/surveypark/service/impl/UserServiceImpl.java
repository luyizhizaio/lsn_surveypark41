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
	 * 覆盖注解,注入指定的dao
	 */
	@Resource(name="userDao")
	public void setDao(BaseDao<User> dao) {
		super.setDao(dao);
	}
	
	/**
	 * 验证email的有效性
	 */
	public boolean isRegisted(String email){
		String hql = "from User u where u.email = ?" ;
		List<User> list = this.findEntityByHQL(hql, email);
		return ValidateUtil.isValid(list);
	}
	
	/**
	 * 校验登陆
	 */
	public User loginValidate(String email, String password){
		String hql = "from User u where u.email = ? and u.password = ?" ;
		List<User> list = this.findEntityByHQL(hql, email,password);
		return ValidateUtil.isValid(list)?list.get(0):null;
	}
	
	/**
	 * 更新授权
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateAuthorize(User model, Integer[] ownRoleIds){
		//一定要先查出原来的model对象,否则会对User自身信息修改
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
	 * 清除授权
	 */
	public void clearAuthorize(Integer userId){
		this.getEntity(userId).getRoles().clear();
	}
}
