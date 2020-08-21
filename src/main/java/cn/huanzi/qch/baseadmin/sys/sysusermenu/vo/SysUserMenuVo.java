package cn.huanzi.qch.baseadmin.sys.sysusermenu.vo;

import cn.huanzi.qch.baseadmin.common.pojo.PageCondition;
import cn.huanzi.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;
import cn.huanzi.qch.baseadmin.sys.sysuser.vo.SysUserVo;
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
