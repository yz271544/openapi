package com.teradata.portal.admin.right.service.impl;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.right.dao.RightApplyDetlMapper;
import com.teradata.portal.admin.right.dao.RightApplyInfoMapper;
import com.teradata.portal.admin.right.entity.Apply2RightDetl;
import com.teradata.portal.admin.right.entity.RightApplyDetl;
import com.teradata.portal.admin.right.entity.RightApplyInfo;
import com.teradata.portal.admin.right.service.RightApplyInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
@Transactional
@Service("rightApplyInfoService")
public class RightApplyInfoServiceImpl implements RightApplyInfoService {

    @Autowired
    private RightApplyInfoMapper rightApplyInfoMapper;

    @Autowired
    private RightApplyDetlMapper rightApplyDetlMapper;

    JSONObject json = new JSONObject();

    @Override
    public PageView query(PageView pageView, RightApplyInfo rightApplyInfo) {
        return null;
    }

    @Override
    public long count(RightApplyInfo rightApplyInfo) {
        return 0;
    }

    @Override
    public List<RightApplyInfo> queryAll(RightApplyInfo rightApplyInfo) {
        return null;
    }

    @Override
    public Integer delete(Integer id) throws Exception {
        return null;
    }

    @Override
    public Integer update(RightApplyInfo rightApplyInfo) throws Exception {
        return null;
    }

    @Override
    public RightApplyInfo getById(Integer id) {
        return null;
    }

    @Override
    public Integer add(RightApplyInfo rightApplyInfo) throws Exception {
        return null;
    }

    /**
     * 注册api申请单信息
     *
     * @param rightApplyInfo
     * @return
     */
    @Override
    public Integer registApiAuthInfo(RightApplyInfo rightApplyInfo) throws Exception {
        RightApplyInfo rightApplyInfo1 = rightApplyInfoMapper.isExist(rightApplyInfo.getApplyId());
        Integer result ;
        if (rightApplyInfo1 == null){
            result = rightApplyInfoMapper.add(rightApplyInfo);
        }else{
            result = rightApplyInfoMapper.updateAuditStat(rightApplyInfo);
        }
        return result;
    }

    @Override
    public JSONObject deleteApply(RightApplyInfo rightApplyInfo) throws Exception {
        rightApplyInfoMapper.deleteByApplyId(rightApplyInfo.getApplyId());
        rightApplyDetlMapper.deleteByApplyId(rightApplyInfo.getApplyId());
        json.put("data", 1);
        json.put("success", true);
        json.put("msg", "操作成功");
        return json;
    }

    /*
     * 获取rightApplyInfo审批状态信息
     */
    @Override
    public List<Map<String, Object>> getAuthAuditStat() {
        return rightApplyInfoMapper.queryAuthAuditStat();
    }

    /*
     * 获取rightApplyInfo列表信息
     */
    @Override
    public List<?> getRightApprInfo(Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("pageIndex").toString()) + 1;
        Integer limit = Integer.parseInt(param.get("pageSize").toString());
        String sortString = "apply_time.desc";
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sortString), true);
        System.out.println("------------service[loginAcct]:" + param.get("loginAcct"));
        System.out.println("------------service[auditStat]:" + param.get("auditStat"));
        System.out.println("------------offset:" + pageBounds.getOffset());
        System.out.println("------------limit:" + pageBounds.getLimit());
        return rightApplyInfoMapper.queryRightApprInfo(param, pageBounds);
    }

    /*
     * 获取rightApplyInfo列表信息
     */
    @Override
    public List<?> getRightApplyInfo(Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("pageIndex").toString()) + 1;
        Integer limit = Integer.parseInt(param.get("pageSize").toString());
        String sortString = "apply_time.desc";
        PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sortString), true);
        System.out.println("------------service[loginAcct]:" + param.get("loginAcct"));
        System.out.println("------------service[auditStat]:" + param.get("auditStat"));
        System.out.println("------------offset:" + pageBounds.getOffset());
        System.out.println("------------limit:" + pageBounds.getLimit());
        List<?> a = rightApplyInfoMapper.queryRightApplyInfo(param, pageBounds);
        return rightApplyInfoMapper.queryRightApplyInfo(param, pageBounds);
    }

    @Override
    public JSONObject doAuthApprove(RightApplyInfo rightApplyInfo) {
        // 把right_apply_detl明细信息添加到right_info表
        if (rightApplyInfo.getAuditStat() == 1) {
            int[] in = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            Integer applyId = rightApplyInfo.getApplyId();
            System.out.println(applyId);
            List<RightApplyDetl> rightApplyDetlList = rightApplyDetlMapper.queryRightApplyDetl(applyId);
            for (RightApplyDetl rightApplyDetl : rightApplyDetlList) {
                Apply2RightDetl apply2RightDetl = new Apply2RightDetl();
                apply2RightDetl.setRightId(UUIDUtils.getNotSimple(in, 10));
                apply2RightDetl.setApplyId(rightApplyDetl.getApplyId());
                apply2RightDetl.setRightMain(rightApplyDetl.getRightMain());
                apply2RightDetl.setRightMainVal(rightApplyDetl.getRightMainVal());
                apply2RightDetl.setRightField(rightApplyDetl.getRightField());
                apply2RightDetl.setRightFieldVal(rightApplyDetl.getRightFieldVal());
                apply2RightDetl.setEff_stat(rightApplyDetl.getEff_stat());
                rightApplyDetlMapper.updateApplyDetlInfo(apply2RightDetl);
            }
        }
        // 修改right_apply_info主表状态信息
        rightApplyInfo.setAuditTime(System.currentTimeMillis());
        rightApplyInfoMapper.updateAuditStat(rightApplyInfo);
        json.put("data", 1);
        json.put("success", true);
        json.put("msg", "成功");
        return json;
    }

    @Override
    public JSONObject doAuthRelease(RightApplyInfo rightApplyInfo) {
        // 修改right_apply_info主表状态信息
        System.out.println("Service doAuthRelease applyId:" + rightApplyInfo.getApplyId());
        System.out.println("Service doAuthRelease auditStat:" + rightApplyInfo.getAuditStat());
        rightApplyInfoMapper.updateAuditStat(rightApplyInfo);
        json.put("data", 1);
        json.put("success", true);
        json.put("msg", "成功");
        return json;
    }

    public static void main(String[] args) {

        for (int i = 0; i <= 10; i++) {
            int[] in = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            System.out.println(UUIDUtils.getNotSimple(in, 10));
        }
    }
}
