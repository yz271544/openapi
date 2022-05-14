package com.teradata.portal.web.registAppr.service.impl;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.teradata.portal.admin.auth.dao.UserInfoMapper;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.web.registAppr.service.RegistApprService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 注册审批业务接口层. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-29 上午6:39:41
 * <p>
 * Company: TERADATA
 * <p>
 *
 * @author Lyndon.Hu@Teradata.com
 * @version 1.0.0
 */
@Service
public class RegistApprServiceImpl implements RegistApprService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    JSONObject json = new JSONObject();

    public List<UserInfo> getRegUserInfo(){
        return userInfoMapper.getAuditUsers('0');
    }

    /*
	 * 获取注册审批状态信息
	 */
    @Override
    public List<Map<String, Object>> getRegExamStat() {
        return userInfoMapper.queryRegExamStat();
    }

    /*
     * 获取注册审批列表
     */
    @Override
    public List<?> getRegInfo(Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("pageIndex").toString()) + 1;
        Integer limit = Integer.parseInt(param.get("pageSize").toString());
        String sortString = "regist_time.desc";
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sortString), true);
        return userInfoMapper.queryRegInfo(param, pageBounds);
    }

    @Override
    public JSONObject doRegApprove(UserInfo user) {
        // 修改user_info主表状态信息
        userInfoMapper.updateUserStatInfo(user);
        json.put("data", 1);
        json.put("success", true);
        json.put("msg", "成功");
        return json;
    }
}
