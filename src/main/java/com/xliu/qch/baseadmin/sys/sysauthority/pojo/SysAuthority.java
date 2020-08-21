package com.xliu.qch.baseadmin.sys.sysauthority.pojo;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_authority")
@Data
public class SysAuthority implements Serializable {
    @Id
    private String authorityId;

    private String authorityName;// Start with "ROLE_" and all UPPERCASE

    private String authorityContent;// Accessible URL, using comma to separate

    private String authorityRemark;// Description

    private Date createTime;

    private Date updateTime;

}
