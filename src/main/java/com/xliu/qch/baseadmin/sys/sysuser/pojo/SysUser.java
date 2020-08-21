package com.xliu.qch.baseadmin.sys.sysuser.pojo;

import com.xliu.qch.baseadmin.annotation.Like;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_user")
@Data
public class SysUser implements Serializable {
    @Id
    private String userId;

    @Like
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

}
