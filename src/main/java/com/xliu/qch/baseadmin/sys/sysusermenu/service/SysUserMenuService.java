package com.xliu.qch.baseadmin.sys.sysusermenu.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonService;
import com.xliu.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;
import com.xliu.qch.baseadmin.sys.sysusermenu.pojo.SysUserMenu;
import com.xliu.qch.baseadmin.sys.sysusermenu.vo.SysUserMenuVo;

import java.util.List;

public interface SysUserMenuService extends CommonService<SysUserMenuVo, SysUserMenu, String> {
    Result<List<SysMenuVo>> findByUserId(String userId);

    Result<Boolean> saveAllByUserId(String userId, String menuIdList);
}
