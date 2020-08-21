package com.xliu.qch.baseadmin.sys.sysshortcutmenu.controller;

import com.xliu.qch.baseadmin.common.controller.CommonController;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.pojo.SysShortcutMenu;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.service.SysShortcutMenuService;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.vo.SysShortcutMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/sysShortcutMenu/")
public class SysShortcutMenuController extends CommonController<SysShortcutMenuVo, SysShortcutMenu, String> {
    @Autowired
    private SysShortcutMenuService sysShortcutMenuService;
}
