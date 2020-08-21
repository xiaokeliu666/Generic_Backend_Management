package com.xliu.qch.baseadmin.sys.sysmenu.vo;

import com.xliu.qch.baseadmin.common.pojo.PageCondition;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SysMenuVo extends PageCondition implements Serializable {
    private String menuId;

    private String menuName;

    private String menuPath;

    private String menuParentId;

    private Date createTime;

    private Date updateTime;

    private List<SysMenuVo> children = new ArrayList<>();// If a parent class, here store the child node
}
