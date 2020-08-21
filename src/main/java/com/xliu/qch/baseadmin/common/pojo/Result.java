package com.xliu.qch.baseadmin.common.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * General return object
 */

@Data
public class Result<T> implements Serializable {
    /**
     * data
     */
    private T data;
    /**
     * state
     */
    private boolean flag = true;
    /**
     * description
     */
    private String msg = "Successful";

    /**
     * Using static method to get the instance
     */
    public static <T> Result<T> of(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> of(T data, boolean flag) {
        return new Result<>(data, flag);
    }

    public static <T> Result<T> of(T data, boolean flag, String msg) {
        return new Result<>(data, flag, msg);
    }

    @Deprecated
    public Result() {

    }

    private Result(T data) {
        this.data = data;
    }

    private Result(T data, boolean flag) {
        this.data = data;
        this.flag = flag;
    }

    private Result(T data, boolean flag, String msg) {
        this.data = data;
        this.flag = flag;
        this.msg = msg;
    }

}
