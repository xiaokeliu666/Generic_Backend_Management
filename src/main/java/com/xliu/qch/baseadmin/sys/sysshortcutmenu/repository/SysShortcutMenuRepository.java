package com.xliu.qch.baseadmin.sys.sysshortcutmenu.repository;

import com.xliu.qch.baseadmin.common.repository.CommonRepository;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.pojo.SysShortcutMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysShortcutMenuRepository extends CommonRepository<SysShortcutMenu, String> {
    List<SysShortcutMenu> findByUserId(String userId);
}
