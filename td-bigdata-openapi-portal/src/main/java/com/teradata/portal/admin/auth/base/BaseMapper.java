package com.teradata.portal.admin.auth.base;

import java.util.List;
import java.util.Map;

/**
 * 所有的Mapper继承这个接口
 * 已经实现民基本的 增,删,改,查接口,不需要重复写
 * Created by Evan on 2016/7/4.
 */
public interface BaseMapper<T> extends Base<T> {

    /**
     * 返回分页后的数据
     * @param map
     * @return
     */
    public List<T> query(Map<String,Object> map);


}
