package com.teradata.portal.admin.right.service;

import com.teradata.portal.admin.auth.base.BaseService;
import com.teradata.portal.admin.right.entity.RightApplyInfo;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/9/5.
 */
public interface RightApplyInfoService extends BaseService<RightApplyInfo>{


    Integer registApiAuthInfo(RightApplyInfo rightApplyInfo) throws Exception;

    JSONObject deleteApply(RightApplyInfo rightApplyInfo) throws Exception;
    /*
	 * 获取rightApplyInfo审批状态信息
	 */
    List<Map<String, Object>> getAuthAuditStat();


    /*
     * 获取rightApprInfo列表信息
     */
    List<?> getRightApprInfo(Map<String, Object> param);

    /*
	 * 获取rightApplyInfo列表信息
	 */
    List<?> getRightApplyInfo(Map<String, Object> param);
    /*
	 * apply审批
	 */
    JSONObject doAuthApprove(RightApplyInfo rightApplyInfo);

    /*
	 * apply审批
	 */
    JSONObject doAuthRelease(RightApplyInfo rightApplyInfo);
}
