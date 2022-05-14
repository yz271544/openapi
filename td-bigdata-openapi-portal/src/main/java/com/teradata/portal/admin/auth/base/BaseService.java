package com.teradata.portal.admin.auth.base;

import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;

/**
 * 所有服务接口都要继承这个接口
 * Created by Evan on 2016/7/4.
 */
public interface BaseService<T> extends Base<T> {

    /**
     * 返回分页后的数据
     * @param pageView
     * @param t
     * @return
     */
    public PageView query(PageView pageView, T t);

    public long count(T t);


}
