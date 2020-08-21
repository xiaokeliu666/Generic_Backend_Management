package cn.huanzi.qch.baseadmin.sys.sysuserauthority.vo;

import cn.huanzi.qch.baseadmin.common.pojo.PageCondition;
import cn.huanzi.qch.baseadmin.sys.sysauthority.vo.SysAuthorityVo;
import cn.huanzi.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUserAuthorityVo extends PageCondition implements Serializable {
    private String userAuthorityId;

    private String userId;

    private String authorityId;

    private SysUserVo sysUser;

    private SysAuthorityVo sysAuthority;

    private Date createTime;

    private Date updateTime;

    private String authorityIdList;
}
