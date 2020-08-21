package com.xliu.qch.baseadmin.sys.sysuser.service;

import com.xliu.qch.baseadmin.common.pojo.PageInfo;
import com.xliu.qch.baseadmin.common.pojo.Result;
import com.xliu.qch.baseadmin.common.service.CommonServiceImpl;
import com.xliu.qch.baseadmin.sys.syssetting.service.SysSettingService;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.service.SysShortcutMenuService;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.vo.SysShortcutMenuVo;
import com.xliu.qch.baseadmin.sys.sysuser.pojo.SysUser;
import com.xliu.qch.baseadmin.sys.sysuser.repository.SysUserRepository;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import com.xliu.qch.baseadmin.sys.sysuserauthority.service.SysUserAuthorityService;
import com.xliu.qch.baseadmin.sys.sysusermenu.service.SysUserMenuService;
import com.xliu.qch.baseadmin.sys.sysusermenu.vo.SysUserMenuVo;
import com.xliu.qch.baseadmin.util.CopyUtil;
import com.xliu.qch.baseadmin.util.MD5Util;
import com.xliu.qch.baseadmin.util.SqlUtil;
import org.hibernate.query.internal.NativeQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

@Service
@Transactional
public class SysUserServiceImpl extends CommonServiceImpl<SysUserVo, SysUser, String> implements SysUserService {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysSettingService sysSettingService;

    @Autowired
    private SysUserAuthorityService sysUserAuthorityService;

    @Autowired
    private SysUserMenuService sysUserMenuService;

    @Autowired
    private SysShortcutMenuService sysShortcutMenuService;

    @Autowired
    private DataSource dataSource;

    @Override
    public Result<String> delete(String id) {
        // Delete relative authority table, menu table, personal menu table
        sysUserAuthorityService.findByUserId(id).getData().forEach((vo -> {
            sysUserAuthorityService.delete(vo.getUserAuthorityId());
        }));
        SysUserMenuVo sysUserMenuVo = new SysUserMenuVo();
        sysUserMenuVo.setUserId(id);
        sysUserMenuService.list(sysUserMenuVo).getData().forEach((vo -> {
            sysUserMenuService.delete(vo.getUserMenuId());
        }));
        SysShortcutMenuVo sysShortcutMenuVo = new SysShortcutMenuVo();
        sysShortcutMenuVo.setUserId(id);
        sysShortcutMenuService.list(sysShortcutMenuVo).getData().forEach((vo -> {
            sysShortcutMenuService.delete(vo.getShortcutMenuId());
        }));

        return super.delete(id);
    }

    @Override
    public Result<PageInfo<SysUserVo>> page(SysUserVo entityVo) {
        //SQL
        SysUser entity = CopyUtil.copy(entityVo,SysUser.class);
        StringBuilder sql = SqlUtil.appendFields(entity);
        SqlUtil.appendQueryColumns(entity,sql);

        // Set SQL, mapping entity, value and return Query object
        Query query = em.createNativeQuery(sql.toString(), SysUser.class);

        // Pagination, sort information, and set the page starts from 0
        PageRequest pageRequest = PageRequest.of(entityVo.getPage() - 1, entityVo.getRows(), new Sort(Sort.Direction.ASC, "id"));
        query.setFirstResult((int) pageRequest.getOffset());
        query.setMaxResults(pageRequest.getPageSize());

        // The result of pagination
        Page page = PageableExecutionUtils.getPage(query.getResultList(), pageRequest, () -> {
            // Set countQuerySQL query
            Query countQuery = em.createNativeQuery("select count(1) from ( " + ((NativeQueryImpl) query).getQueryString() + " ) count_table");
            // Set countQuerySQL parameter
            query.getParameters().forEach(parameter -> countQuery.setParameter(parameter.getName(), query.getParameterValue(parameter.getName())));
            // Return the amount
            return Long.valueOf(countQuery.getResultList().get(0).toString());
        });

        Result<PageInfo<SysUserVo>> result = Result.of(PageInfo.of(page, SysUserVo.class));

        // Set the password to null
        result.getData().getRows().forEach((sysUserVo) -> {
            sysUserVo.setPassword(null);
        });
        return result;
    }


    @Override
    public Result<SysUserVo> save(SysUserVo entityVo) {
        // Add a new user and use the initial default password
        if (StringUtils.isEmpty(entityVo.getUserId())) {
            entityVo.setPassword(MD5Util.getMD5(sysSettingService.get("1").getData().getUserInitPassword()));
        }
        return super.save(entityVo);
    }

    /**
     * Reset password
     */
    @Override
    public Result<SysUserVo> resetPassword(String userId) {
        SysUserVo entityVo = new SysUserVo();
        entityVo.setUserId(userId);
        entityVo.setPassword(MD5Util.getMD5(sysSettingService.get("1").getData().getUserInitPassword()));
        Result<SysUserVo> result = super.save(entityVo);
        result.getData().setPassword(null);
        return result;
    }

    @Override
    public PersistentTokenRepository getPersistentTokenRepository2() {
        return persistentTokenRepository2();
    }

    @Override
    public Result<SysUserVo> findByLoginName(String username) {
        return Result.of(CopyUtil.copy(sysUserRepository.findByLoginName(username), SysUserVo.class));
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository2() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        return persistentTokenRepository;
    }
}
