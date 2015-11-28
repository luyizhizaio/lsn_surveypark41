package cn.itcast.surveypark.service;

import cn.itcast.surveypark.domain.User;

public interface UserService extends BaseService<User> {

	/**
	 * 验证email的有效性
	 */
	public boolean isRegisted(String email);

	/**
	 * 校验登陆
	 */
	public User loginValidate(String email, String md5);

	/**
	 * 更新授权
	 */
	public void updateAuthorize(User model, Integer[] ownRoleIds);

	/**
	 * 清除授权
	 */
	public void clearAuthorize(Integer userId);
}
