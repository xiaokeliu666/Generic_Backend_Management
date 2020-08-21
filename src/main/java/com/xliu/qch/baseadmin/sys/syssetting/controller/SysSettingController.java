package com.xliu.qch.baseadmin.sys.syssetting.controller;

import com.xliu.qch.baseadmin.common.controller.CommonController;
import com.xliu.qch.baseadmin.sys.syssetting.pojo.SysSetting;
import com.xliu.qch.baseadmin.sys.syssetting.service.SysSettingService;
import com.xliu.qch.baseadmin.sys.syssetting.vo.SysSettingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/sys/sysSetting/")
public class SysSettingController extends CommonController<SysSettingVo, SysSetting, String> {
    @Autowired
    private SysSettingService sysSettingService;

    @GetMapping("setting")
    public ModelAndView setting() {
        return new ModelAndView("sys/setting/setting", "sys", sysSettingService.get("1").getData());
    }
}