package com.xliu.qch.baseadmin.sys.sysshortcutmenu.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonServiceImpl;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.pojo.SysShortcutMenu;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.repository.SysShortcutMenuRepository;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.vo.SysShortcutMenuVo;
import com.xliu.qch.baseadmin.util.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysShortcutMenuServiceImpl extends CommonServiceImpl<SysShortcutMenuVo, SysShortcutMenu, String> implements SysShortcutMenuService{

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SysShortcutMenuRepository sysShortcutMenuRepository;

    @Override
    public Result<String> delete(String id) {
        // First, delete the children
        SysShortcutMenuVo sysShortcutMenuVo = new SysShortcutMenuVo();
        sysShortcutMenuVo.setShortcutMenuParentId(id);
        super.list(sysShortcutMenuVo).getData().forEach((menuVo)->{
            super.delete(menuVo.getShortcutMenuId());
        });
        // Then delete self
        return super.delete(id);
    }

    @Override
    public Result<List<SysShortcutMenuVo>> findByUserId(String userId) {
        List<SysShortcutMenuVo> shortcutMenuVoList = new ArrayList<>();
        List<SysShortcutMenuVo> sysShortcutMenuVoList = CopyUtil.copyList(sysShortcutMenuRepository.findByUserId(userId), SysShortcutMenuVo.class);
        sysShortcutMenuVoList.forEach((SysShortcutMenuVo) -> {
            if(StringUtils.isEmpty(SysShortcutMenuVo.getShortcutMenuParentId())){
                // Parent node
                shortcutMenuVoList.add(SysShortcutMenuVo);
            }
        });
        sysShortcutMenuVoList.forEach((SysShortcutMenuVo) -> {
            if(!StringUtils.isEmpty(SysShortcutMenuVo.getShortcutMenuParentId())){
                // Child node
                shortcutMenuVoList.forEach((sysShortcutMenuVoP) -> {
                    if(sysShortcutMenuVoP.getShortcutMenuId().equals(SysShortcutMenuVo.getShortcutMenuParentId())){
                        sysShortcutMenuVoP.getChildren().add(SysShortcutMenuVo);
                    }
                });
            }
        });
        return Result.of(shortcutMenuVoList);
    }
}
