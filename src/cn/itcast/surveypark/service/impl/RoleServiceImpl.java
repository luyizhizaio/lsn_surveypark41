package cn.itcast.surveypark.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.surveypark.dao.BaseDao;
import cn.itcast.surveypark.domain.security.Right;
import cn.itcast.surveypark.domain.security.Role;
import cn.itcast.surveypark.service.RightService;
import cn.itcast.surveypark.service.RoleService;
import cn.itcast.surveypark.util.DataUtil;
import cn.itcast.surveypark.util.ValidateUtil;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
		RoleService {

	@Resource
	private RightService rightService ;
	
	@Resource(name="roleDao")
	public void setDao(BaseDao<Role> dao) {
		super.setDao(dao);
	}
	
	
	/**
	 * 保存/更新角色
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveOrUpdateRole(Role model, Integer[] ownRightIds){
		if(!ValidateUtil.isValid(ownRightIds)){
			model.getRights().clear();
		}
		else{
			List<Right> rights = rightService.findRightsInRange(ownRightIds);
			model.setRights(new HashSet(rights));
		}
		this.saveOrUpdateEntity(model);
	}
	
	/**
	 * 查询不在指定范围的角色集合
	 */
	public List<Role> findRolesNotInRange(Set<Role> roles){
		if(!ValidateUtil.isValid(roles)){
			return this.findAllEntities();
		}
		else{
			String hql = "from Role r where r.id not in ("+DataUtil.extractIds(roles)+")" ;
			return this.findEntityByHQL(hql);
		}
	}
}
