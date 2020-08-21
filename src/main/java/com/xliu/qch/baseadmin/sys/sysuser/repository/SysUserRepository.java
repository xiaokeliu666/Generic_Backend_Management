package com.xliu.qch.baseadmin.sys.sysuser.repository;

import com.xliu.qch.baseadmin.common.repository.*;
import com.xliu.qch.baseadmin.sys.sysuser.pojo.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends CommonRepository<SysUser, String> {
    SysUser findByLoginName(String username);
}
