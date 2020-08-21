package cn.huanzi.qch.baseadmin.sys.sysusermenu.pojo;

import cn.huanzi.qch.baseadmin.sys.sysmenu.pojo.SysMenu;
import cn.huanzi.qch.baseadmin.sys.sysuser.pojo.SysUser;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_user_menu")
@Data
public class SysUserMenu implements Serializable {
    @Id
    private String userMenuId;

    private String userId;

    private String menuId;

    private Date createTime;

    private Date updateTime;

    @OneToOne
    @JoinColumn(name = "userId",referencedColumnName = "userId", insertable = false, updatable = false)
    @NotFound(action= NotFoundAction.IGNORE)
    private SysUser sysUser;

    @OneToOne
    @JoinColumn(name = "menuId",referencedColumnName = "menuId", insertable = false, updatable = false)
    @NotFound(action= NotFoundAction.IGNORE)
    private SysMenu sysMenu;
}
