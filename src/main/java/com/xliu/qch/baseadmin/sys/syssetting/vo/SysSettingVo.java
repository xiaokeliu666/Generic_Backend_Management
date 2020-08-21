package com.xliu.qch.baseadmin.sys.syssetting.vo;

import com.xliu.qch.baseadmin. common.pojo.PageCondition;import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysSettingVo extends PageCondition implements Serializable {
    private String id;

    private String sysName;

    private String sysLogo;

    private String sysBottomText;

    private String sysColor;

    private String sysNoticeText;

    private String sysApiEncrypt;

    private Date createTime;

    private Date updateTime;

    private String userInitPassword;

}
