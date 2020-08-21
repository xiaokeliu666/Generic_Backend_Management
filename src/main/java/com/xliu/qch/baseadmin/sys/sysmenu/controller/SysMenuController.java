package com.xliu.qch.baseadmin.sys.sysmenu.controller;

import com.xliu.qch.baseadmin.annotation.Decrypt;
import com.xliu.qch.baseadmin.annotation.Encrypt;
import com.xliu.qch.baseadmin.common.controller.CommonController;
import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.sys.sysmenu.pojo.SysMenu;
import com.xliu.qch.baseadmin.sys.sysmenu.service.SysMenuService;
import com.xliu.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/sys/sysMenu/")
public class SysMenuController extends CommonController<SysMenuVo, SysMenu, String> {
    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("menu")
    public ModelAndView menu(){
        return new ModelAndView("sys/menu/menu");
    }

    /**
     * list by tier
     */
    @PostMapping("listByTier")
    @Decrypt
    @Encrypt
    public Result<List<SysMenuVo>> listByTier(SysMenuVo entityVo) {
        return sysMenuService.listByTier(entityVo);
    }
}
