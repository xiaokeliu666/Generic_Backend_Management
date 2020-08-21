package com.xliu.qch.baseadmin.common.controller;

import com.xliu.qch.baseadmin.annotation.Decrypt;
import com.xliu.qch.baseadmin.annotation.Encrypt;
import com.xliu.qch.baseadmin.common.pojo.PageInfo;
import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Common Controller
 *
 * @param <V> Entity Class Vo
 * @param <E> Entity Class
 * @param <T> id primary key type
 */
public class CommonController<V, E, T> {

    @Autowired
    private CommonService<V, E, T> commonService;

    /*
        CRUD, Pagination, Sort Testing
     */
    @PostMapping("page")
    @Decrypt
    @Encrypt
    public Result<PageInfo<V>> page(V entityVo) {
        return commonService.page(entityVo);
    }

    @PostMapping("list")
    @Decrypt
    @Encrypt
    public Result<List<V>> list(V entityVo) {
        return commonService.list(entityVo);
    }

    @GetMapping("get/{id}")
    public Result<V> get(@PathVariable("id") T id) {
        return commonService.get(id);
    }

    @PostMapping("save")
    @Decrypt
    @Encrypt
    public Result<V> save(V entityVo) {
        return commonService.save(entityVo);
    }

    @DeleteMapping("delete/{id}")
    public Result<T> delete( @PathVariable("id") T id) {
        /*
        Delete Batch
        @DeleteMapping("deleteBatch")
        public Result<T> deleteBatch(@RequestBody List<String> ids){}
        前端调用：
        $.ajax({
            url: ctx + "deleteBatch",
            type: "DELETE",
            data: JSON.stringify([id1,id2]),
            dataType: "JSON",
            contentType: 'application/json',
            success: function (data) {

            }
        });
         */
        return commonService.delete(id);
    }
}
