package com.xliu.qch.baseadmin.user.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;

public interface UserService {
    Result<SysUserVo> updatePassword(String oldPassword, String newPassword);

    Result<SysUserVo> updateUser(SysUserVo sysUserVo);
}
