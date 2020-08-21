package cn.huanzi.qch.baseadmin.common.service;


import cn.huanzi.qch.baseadmin.common.pojo.PageInfo;
import cn.huanzi.qch.baseadmin.common.pojo.Result;

import java.util.List;

/**
 * Generic Service
 *
 * @param <V> EntityVo
 * @param <E> Entity
 * @param <T> PrimaryKeyType
 */
public interface CommonService<V, E,T> {

    Result<PageInfo<V>> page(V entityVo);

    Result<List<V>> list(V entityVo);

    Result<V> get(T id);

    Result<V> save(V entityVo);

    Result<T> delete(T id);
}
