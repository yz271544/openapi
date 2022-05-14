package com.teradata.portal.admin.auth.base;

import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.util.Common;

/**
 * Created by Evan on 2016/7/6.
 */
public class BaseController {

    PageView pageView = null;
    public PageView getPageView(String pageNow,String pageSize){
        if(Common.isEmpty(pageNow)){
            pageView = new PageView(1);
        }else {
            pageView = new PageView(Integer.parseInt(pageNow));
        }
        if(Common.isEmpty(pageSize)){
            pageSize = "5";
        }
        pageView.setPageSize(Integer.parseInt(pageSize));
        return pageView;
    }
}
