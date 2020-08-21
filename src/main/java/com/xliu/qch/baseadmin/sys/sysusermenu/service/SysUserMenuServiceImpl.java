package com.xliu.qch.baseadmin.sys.sysusermenu.service;

import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonServiceImpl;
import com.xliu.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;
import com.xliu.qch.baseadmin.sys.sysusermenu.pojo.SysUserMenu;
import com.xliu.qch.baseadmin.sys.sysusermenu.repository.SysUserMenuRepository;
import com.xliu.qch.baseadmin.sys.sysusermenu.vo.SysUserMenuVo;
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
public class SysUserMenuServiceImpl extends CommonServiceImpl<SysUserMenuVo, SysUserMenu, String> implements SysUserMenuService{

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SysUserMenuRepository sysUserMenuRepository;

    @Override
    public Result<List<SysMenuVo>> findByUserId(String userId) {
        List<SysMenuVo> menuVoList = new ArrayList<>();
        List<SysUserMenuVo> sysUserMenuVoList = CopyUtil.copyList(sysUserMenuRepository.findByUserId(userId), SysUserMenuVo.class);
        sysUserMenuVoList.forEach((sysUserMenuVo) -> {
            SysMenuVo sysMenuVo = sysUserMenuVo.getSysMenu();
            if(StringUtils.isEmpty(sysMenuVo.getMenuParentId())){
                // parent node
                menuVoList.add(sysMenuVo);
            }
        });
        sysUserMenuVoList.forEach((sysUserMenuVo) -> {
            SysMenuVo sysMenuVo = sysUserMenuVo.getSysMenu();
            if(!StringUtils.isEmpty(sysMenuVo.getMenuParentId())){
                // child node
                menuVoList.forEach((sysMenuVoP) -> {
                    if(sysMenuVoP.getMenuId().equals(sysMenuVo.getMenuParentId())){
                        sysMenuVoP.getChildren().add(sysMenuVo);
                    }
                });
            }
        });

        return Result.of(menuVoList);
    }

    @Override
    public Result<Boolean> saveAllByUserId(String userId, String menuIdList) {
        // First delete the old one
        SysUserMenuVo sysUserMenuVo = new SysUserMenuVo();
        sysUserMenuVo.setUserId(userId);
        list(sysUserMenuVo).getData().forEach((userMenuVo)->{
            delete(userMenuVo.getUserMenuId());
        });

        // Then save the new one
        for (String menuId : menuIdList.split(",")) {
            sysUserMenuVo.setMenuId(menuId);
            save(sysUserMenuVo);
        }
        return Result.of(true);
    }
}
