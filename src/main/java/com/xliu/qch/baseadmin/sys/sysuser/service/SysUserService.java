package com.xliu.qch.baseadmin.sys.sysuser.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonService;
import com.xliu.qch.baseadmin.sys.sysuser.pojo.SysUser;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public interface SysUserService extends CommonService<SysUserVo, SysUser, String> {
    Result<SysUserVo> findByLoginName(String username);
    Result<SysUserVo> resetPassword(String userId);
    PersistentTokenRepository getPersistentTokenRepository2();
}
