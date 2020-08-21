package com.xliu.qch.baseadmin.sys.sysmenu.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.*;
import com.xliu.qch.baseadmin.sys.sysmenu.pojo.SysMenu;
import com.xliu.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;

import java.util.List;

public interface SysMenuService extends CommonService<SysMenuVo, SysMenu, String> {
    Result<List<SysMenuVo>> listByTier(SysMenuVo entityVo);
}
