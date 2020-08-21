package com.xliu.qch.baseadmin.sys.sysuser.vo;

import com.xliu.qch.baseadmin. common.pojo.PageCondition;import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysUserVo extends PageCondition implements Serializable {
    private String userId;

    private String loginName;

    private String userName;

    private String password;

    private String valid;

    private String limitedIp;

    private Date expiredTime;

    private Date lastChangePwdTime;

    private String limitMultiLogin;

    private Date createTime;

    private Date updateTime;

    private String oldPassword;

    private String newPassword;

}
