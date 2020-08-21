package com.xliu.qch.baseadmin.user.controller;

import com.xliu.qch.baseadmin.annotation.Decrypt;
import com.xliu.qch.baseadmin.annotation.Encrypt;
import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.service.SysShortcutMenuService;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.vo.SysShortcutMenuVo;
import com.xliu.qch.baseadmin.sys.sysuser.service.SysUserService;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import com.xliu.qch.baseadmin.user.service.UserService;
import com.xliu.qch.baseadmin.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * user login access
 */
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysShortcutMenuService sysShortcutMenuService;

    @GetMapping("userinfo")
    public ModelAndView userinfo() {
        SysUserVo sysUserVo = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
        sysUserVo.setPassword(null);
        return new ModelAndView("user/userinfo", "user", sysUserVo);
    }

    @GetMapping("shortcMenu")
    public ModelAndView shortcMenu() {
        return new ModelAndView("user/shortcMenu");
    }

    /**
     * update password
     */
    @PostMapping("updatePassword")
    @Decrypt
    @Encrypt
    public Result<SysUserVo> updatePassword(SysUserVo sysUserVo) {
        return userService.updatePassword(sysUserVo.getOldPassword(), sysUserVo.getNewPassword());
    }

    /**
     * update user info
     */
    @PostMapping("updateUser")
    @Decrypt
    @Encrypt
    public Result<SysUserVo> updateUser(SysUserVo sysUserVo) {
        return userService.updateUser(sysUserVo);
    }

    /**
     * Hierarchical
     */
    @PostMapping("shortcutMenuListByTier")
    @Decrypt
    @Encrypt
    public Result<List<SysShortcutMenuVo>> shortcutMenuListByTier() {
        SysUserVo sysUserVo = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
        return sysShortcutMenuService.findByUserId(sysUserVo.getUserId());
    }

    /**
     * save
     */
    @PostMapping("shortcutMenuSave")
    @Decrypt
    @Encrypt
    public Result<SysShortcutMenuVo> shortcutMenuSave(SysShortcutMenuVo sysShortcutMenuVo) {
        SysUserVo sysUserVo = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
        sysShortcutMenuVo.setUserId(sysUserVo.getUserId());
        return sysShortcutMenuService.save(sysShortcutMenuVo);
    }

    @DeleteMapping("shortcutMenuDelete/{id}")
    public Result<String> shortcutMenuDelete(@PathVariable("id") String id) {
        SysUserVo sysUserVo = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
        SysShortcutMenuVo sysShortcutMenuVo = new SysShortcutMenuVo();
        sysShortcutMenuVo.setUserId(sysUserVo.getUserId());
        List<SysShortcutMenuVo> sysShortcutMenuVoList = sysShortcutMenuService.list(sysShortcutMenuVo).getData();

        // First determine whether the shortcutMenu belongs to current user
        boolean flag = false;
        for (SysShortcutMenuVo shortcutMenuVo : sysShortcutMenuVoList) {
            if (shortcutMenuVo.getShortcutMenuId().equals(id)) {
                flag = true;
                break;
            }
        }

        if(flag){
            return sysShortcutMenuService.delete(id);
        }else{
            return Result.of(null,false,"Please don't delete others menu!");
        }
    }
}
