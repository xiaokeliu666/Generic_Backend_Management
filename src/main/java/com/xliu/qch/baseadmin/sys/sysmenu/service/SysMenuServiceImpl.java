package com.xliu.qch.baseadmin.sys.sysmenu.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonServiceImpl;
import com.xliu.qch.baseadmin.sys.sysmenu.pojo.SysMenu;
import com.xliu.qch.baseadmin.sys.sysmenu.repository.SysMenuRepository;
import com.xliu.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;
import com.xliu.qch.baseadmin.sys.sysusermenu.service.SysUserMenuService;
import com.xliu.qch.baseadmin.sys.sysusermenu.vo.SysUserMenuVo;
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
public class SysMenuServiceImpl extends CommonServiceImpl<SysMenuVo, SysMenu, String> implements SysMenuService{

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private SysUserMenuService sysUserMenuService;

    @Override
    public Result<String> delete(String id) {
        // First, delete the child nodes
        SysMenuVo sysMenuVo = new SysMenuVo();
        sysMenuVo.setMenuParentId(id);
        super.list(sysMenuVo).getData().forEach((menuVo)->{
            super.delete(menuVo.getMenuId());
        });

        // Delete all the relative information
        SysUserMenuVo sysUserMenuVo = new SysUserMenuVo();
        sysUserMenuVo.setMenuId(id);
        sysUserMenuService.list(sysUserMenuVo).getData().forEach((vo)->{
            sysUserMenuService.delete(vo.getUserMenuId());
        });

        // Delete self
        return super.delete(id);
    }

    @Override
    public Result<List<SysMenuVo>> listByTier(SysMenuVo entityVo) {
        List<SysMenuVo> menuVoList = new ArrayList<>();
        List<SysMenuVo> sysMenuVoList = super.list(entityVo).getData();
        sysMenuVoList.forEach((sysMenuVo) -> {
            if(StringUtils.isEmpty(sysMenuVo.getMenuParentId())){
                // Parent node
                menuVoList.add(sysMenuVo);
            }
        });
        sysMenuVoList.forEach((sysMenuVo) -> {
            if(!StringUtils.isEmpty(sysMenuVo.getMenuParentId())){
                // Child node
                menuVoList.forEach((sysMenuVoP) -> {
                    if(sysMenuVoP.getMenuId().equals(sysMenuVo.getMenuParentId())){
                        sysMenuVoP.getChildren().add(sysMenuVo);
                    }
                });
            }
        });

        return Result.of(menuVoList);
    }

}
