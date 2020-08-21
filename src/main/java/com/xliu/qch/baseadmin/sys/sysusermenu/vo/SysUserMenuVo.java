package com.xliu.qch.baseadmin.sys.sysusermenu.vo;

import com.xliu.qch.baseadmin.common.pojo.PageCondition;
import com.xliu.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUserMenuVo extends PageCondition implements Serializable {
    private String userMenuId;

    private String userId;

    private String menuId;

    private SysUserVo sysUser;

    private SysMenuVo sysMenu;

    private Date createTime;

    private Date updateTime;

    private String menuIdList;
}
