package com.xliu.qch.baseadmin.sys.sysuserauthority.repository;

import com.xliu.qch.baseadmin.common.repository.CommonRepository;
import com.xliu.qch.baseadmin.sys.sysuserauthority.pojo.SysUserAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserAuthorityRepository extends CommonRepository<SysUserAuthority, String> {
    List<SysUserAuthority> findByUserId(String userId);
}
