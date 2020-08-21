package com.xliu.qch.baseadmin.user.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.sys.sysuser.service.SysUserService;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import com.xliu.qch.baseadmin.util.MD5Util;
import com.xliu.qch.baseadmin.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result<SysUserVo> updatePassword(String oldPassword, String newPassword) {
        SysUserVo sysUserVo = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
        Result<SysUserVo> result = Result.of(null,false,"Fail: Wrong original password");
        // First confirm the original password
        if(sysUserVo.getPassword().equals(MD5Util.getMD5(oldPassword))){
            sysUserVo.setPassword(MD5Util.getMD5(newPassword));
            result = sysUserService.save(sysUserVo);
            // Empty private data
            result.getData().setPassword(null);
        }
        return result;
    }

    @Override
    public Result<SysUserVo> updateUser(SysUserVo sysUserVo) {
        SysUserVo sysUserVo1 = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
        // Users are only allowed to update these following options
        sysUserVo1.setUserName(sysUserVo.getUserName());
        sysUserVo1.setUpdateTime(new Date());

        Result<SysUserVo> result = sysUserService.save(sysUserVo1);
        // Empty private data
        result.getData().setPassword(null);
        return result;
    }
}
