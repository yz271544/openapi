package com.teradata.openapi.access.biz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.teradata.openapi.access.biz.bean.ApiInfo;
import com.teradata.openapi.access.biz.bean.CalcPrincCode;
import com.teradata.openapi.access.biz.bean.UserInfo;
import com.teradata.openapi.access.biz.dao.ApiInfoMapper;
import com.teradata.openapi.access.biz.dao.CalcPrincCodeMapper;
import com.teradata.openapi.access.biz.dao.UserInfoMapper;
import com.teradata.openapi.access.biz.service.AccessBizOperate;

@Service
public class AccessBizOperateImpl implements AccessBizOperate {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private ApiInfoMapper apiInfoMapper;

	@Autowired
	private CalcPrincCodeMapper calcPrincCodeMapper;

	/**
	 * 查询所有有效的用户信息
	 * 
	 * @return
	 * @date 2016年4月22日
	 * @author houbl
	 */
	@Override
	public List<UserInfo> findAllUserInfo() {
		return userInfoMapper.queryAllUserInfo();
	}

	/**
	 * 查询所有有效的api信息(包括元字段信息)
	 * 
	 * @return
	 * @date 2016年4月22日
	 * @author houbl
	 */
	@Override
	public List<ApiInfo> findAllApiInfo() {
		return apiInfoMapper.queryAllApiInfo();
	}

	/**
	 * 查询用户下有权限的Api
	 * 
	 * @return
	 * @date 2016年4月25日
	 * @author houbl
	 */
	@Override
	public Map<String, List<String>> findUserIncludeApi() {
		Map<String, List<String>> validMap = new HashMap<String, List<String>>();

		MultiValueMap<String, String> groupUserMap = new LinkedMultiValueMap<String, String>();
		// 用户组与用户的关系
		List<Map<Object, Object>> groupAndUserRelList = userInfoMapper.queryGroupAndUserRel();
		for (Map<Object, Object> map : groupAndUserRelList) {
			groupUserMap.add(map.get("user_grp_id").toString(), map.get("login_acct").toString());
		}

		MultiValueMap<String, String> groupTempMap = new LinkedMultiValueMap<String, String>();
		// 所有用户组与api的关系值
		List<Map<Object, Object>> groupAndApiRelList = userInfoMapper.queryGroupAndApiRel();
		for (Map<Object, Object> groupMap : groupAndApiRelList) {
			groupTempMap.add(groupMap.get("user_grp_id").toString(), groupMap.get("api_name").toString());
		}

		MultiValueMap<String, String> userTempMap = new LinkedMultiValueMap<String, String>();
		// 所有用户与api的关系值
		List<Map<Object, Object>> apiAndUserRelList = userInfoMapper.queryApiAndUserRel();
		for (Map<Object, Object> userMap : apiAndUserRelList) {
			userTempMap.add(userMap.get("login_acct").toString(), userMap.get("api_name").toString());
		}

		for (String key : groupUserMap.keySet()) {
			// 一个组下所有用户
			List<String> userNameList = groupUserMap.get(key);
			for (String userName : userNameList) {
				// 一个组下所有api权限
				List<String> groupApiListTemp = groupTempMap.get(key);
				// 一个用户下的所有api权限
				List<String> userApiList = userTempMap.get(userName);
				// 如果用户下的没有api权限 则取组的权限
				if (userApiList != null) {
					// 无重复并集
					userApiList.removeAll(groupApiListTemp);
					groupApiListTemp.addAll(userApiList);
				}

				validMap.put(userName, groupApiListTemp);
			}
		}

		return validMap;
	}

	/**
	 * 查询计算法则
	 * 
	 * @return
	 * @date 2016年4月29日
	 * @author houbl
	 */
	@Override
	public List<CalcPrincCode> findCalcPrincInfo() {
		return calcPrincCodeMapper.queryCalcPrincInfo();
	}
}
