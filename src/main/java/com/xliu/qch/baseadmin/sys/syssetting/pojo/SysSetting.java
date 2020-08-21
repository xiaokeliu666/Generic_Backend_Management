package com.xliu.qch.baseadmin.sys.syssetting.pojo;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_setting")
@Data
public class SysSetting implements Serializable {
    @Id
    private String id;// table id

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
