package cn.huanzi.qch.baseadmin.sys.syssetting.service;

import cn.huanzi.qch.baseadmin.common.pojo.Result;
import cn.huanzi.qch.baseadmin.common.service.CommonServiceImpl;
import cn.huanzi.qch.baseadmin.sys.syssetting.pojo.SysSetting;
import cn.huanzi.qch.baseadmin.sys.syssetting.repository.SysSettingRepository;
import cn.huanzi.qch.baseadmin.sys.syssetting.vo.SysSettingVo;
import cn.huanzi.qch.baseadmin.util.SysSettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class SysSettingServiceImpl extends CommonServiceImpl<SysSettingVo, SysSetting, String> implements SysSettingService{

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SysSettingRepository sysSettingRepository;

    @Override
    public Result<SysSettingVo> save(SysSettingVo entityVo) {
        // call the super class
        Result<SysSettingVo> result = super.save(entityVo);

        // Update SysSettingMap while updating the system setting
        SysSettingUtil.setSysSettingMap(result.getData());

        return result;
    }
}
