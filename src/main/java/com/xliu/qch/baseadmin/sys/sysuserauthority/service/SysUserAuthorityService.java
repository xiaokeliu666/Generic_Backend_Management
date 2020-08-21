package com.xliu.qch.baseadmin.sys.sysuserauthority.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonService;
import com.xliu.qch.baseadmin.sys.sysuserauthority.pojo.SysUserAuthority;
import com.xliu.qch.baseadmin.sys.sysuserauthority.vo.SysUserAuthorityVo;

import java.util.List;

public interface SysUserAuthorityService extends CommonService<SysUserAuthorityVo, SysUserAuthority, String> {
    Result<List<SysUserAuthorityVo>> findByUserId(String userId);

    Result<Boolean> saveAllByUserId(String userId, String authorityIdList);
}
