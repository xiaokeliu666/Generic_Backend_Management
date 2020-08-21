package com.xliu.qch.baseadmin.sys.sysmenu.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_menu")
@Data
public class SysMenu implements Serializable {
    @Id
    private String menuId;

    private String menuName;

    private String menuPath;

    private String menuParentId;

    private Date createTime;

    private Date updateTime;

}
