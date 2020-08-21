package com.xliu.qch.baseadmin.sys.sysshortcutmenu.pojo;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_shortcut_menu")
@Data
public class SysShortcutMenu implements Serializable {
    @Id
    private String shortcutMenuId;

    private String shortcutMenuName;

    private String shortcutMenuPath;

    private String userId;

    private String shortcutMenuParentId;

    private Date createTime;

    private Date updateTime;

}
