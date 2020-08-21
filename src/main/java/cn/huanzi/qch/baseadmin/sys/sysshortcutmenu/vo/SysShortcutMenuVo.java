package cn.huanzi.qch.baseadmin.sys.sysshortcutmenu.vo;

import cn.huanzi.qch.baseadmin.common.pojo.PageCondition;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SysShortcutMenuVo extends PageCondition implements Serializable {
    private String shortcutMenuId;

    private String shortcutMenuName;

    private String shortcutMenuPath;

    private String userId;

    private String shortcutMenuParentId;

    private Date createTime;

    private Date updateTime;

    private List<SysShortcutMenuVo> children = new ArrayList<>();
}
