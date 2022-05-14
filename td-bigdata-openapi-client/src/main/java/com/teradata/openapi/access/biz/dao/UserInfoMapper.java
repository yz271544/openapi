package com.teradata.openapi.access.biz.dao;

import java.util.List;
import java.util.Map;

import com.teradata.openapi.access.biz.bean.UserInfo;

public interface UserInfoMapper {

	/**
	 * 查询所有有效的用户信息
	 * 
	 * @return
	 * @date 2016年4月22日
	 * @author houbl
	 */
	public List<UserInfo> queryAllUserInfo();

	/**
	 * 查询用户与api关系信息
	 * 
	 * @return
	 * @date 2016年4月25日
	 * @author houbl
	 */
	public List<Map<Object, Object>> queryApiAndUserRel();

	/**
	 * 查询用户组与api关系信息
	 * 
	 * @return
	 * @date 2016年4月25日
	 * @author houbl
	 */
	public List<Map<Object, Object>> queryGroupAndApiRel();

	/**
	 * 查询用户与用户组的关系
	 * 
	 * @return
	 * @date 2016年4月27日
	 * @author houbl
	 */
	public List<Map<Object, Object>> queryGroupAndUserRel();
}