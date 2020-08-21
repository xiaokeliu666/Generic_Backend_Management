package com.xliu.qch.baseadmin.sys.sysuserauthority.vo;

import com.xliu.qch.baseadmin.common.pojo.PageCondition;
import com.xliu.qch.baseadmin.sys.sysauthority.vo.SysAuthorityVo;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUserAuthorityVo extends PageCondition implements Serializable {
    private String userAuthorityId;

    private String userId;

    private String authorityId;

    private SysUserVo sysUser;

    private SysAuthorityVo sysAuthority;

    private Date createTime;

    private Date updateTime;

    private String authorityIdList;
}
