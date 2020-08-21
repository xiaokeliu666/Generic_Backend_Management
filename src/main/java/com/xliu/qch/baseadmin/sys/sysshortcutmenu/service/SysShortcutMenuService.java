package com.xliu.qch.baseadmin.sys.sysshortcutmenu.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonService;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.pojo.SysShortcutMenu;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.vo.SysShortcutMenuVo;

import java.util.List;

public interface SysShortcutMenuService extends CommonService<SysShortcutMenuVo, SysShortcutMenu, String> {
    Result<List<SysShortcutMenuVo>> findByUserId(String userId);
}
