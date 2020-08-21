package com.xliu.qch.baseadmin.sys.sysuserauthority.pojo;

import com.xliu.qch.baseadmin.sys.sysauthority.pojo.SysAuthority;
import com.xliu.qch.baseadmin.sys.sysuser.pojo.SysUser;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_user_authority")
@Data
public class SysUserAuthority implements Serializable {
    @Id
    private String userAuthorityId;

    private String userId;

    private String authorityId;

    private Date createTime;

    private Date updateTime;

    @OneToOne
    @JoinColumn(name = "userId",referencedColumnName = "userId", insertable = false, updatable = false)
    @NotFound(action= NotFoundAction.IGNORE)
    private SysUser sysUser;

    @OneToOne
    @JoinColumn(name = "authorityId",referencedColumnName = "authorityId", insertable = false, updatable = false)
    @NotFound(action= NotFoundAction.IGNORE)
    private SysAuthority sysAuthority;
}
