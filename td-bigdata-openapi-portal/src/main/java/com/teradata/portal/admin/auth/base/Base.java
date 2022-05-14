package com.teradata.portal.admin.auth.base;

import java.util.List;

/**
 * 服务层基本接口
 * Created by Evan on 2016/7/4.
 */
public interface Base<T> {

    /**
     * 返回所有数据
     * @param t
     * @return
     */
    public List<T> queryAll(T t);

    public Integer delete(Integer id) throws Exception;

    public Integer update(T t) throws Exception;

    public T getById(Integer id);

    public Integer add(T t) throws Exception;
}
