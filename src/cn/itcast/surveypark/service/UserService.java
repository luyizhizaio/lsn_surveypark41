package cn.itcast.surveypark.service;

import cn.itcast.surveypark.domain.User;

public interface UserService extends BaseService<User> {

	/**
	 * ��֤email����Ч��
	 */
	public boolean isRegisted(String email);

	/**
	 * У���½
	 */
	public User loginValidate(String email, String md5);

	/**
	 * ������Ȩ
	 */
	public void updateAuthorize(User model, Integer[] ownRoleIds);

	/**
	 * �����Ȩ
	 */
	public void clearAuthorize(Integer userId);
}
