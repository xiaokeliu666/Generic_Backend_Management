package com.xliu.qch.baseadmin.sys.sysauthority.vo;

import com.xliu.qch.baseadmin. common.pojo.PageCondition;import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysAuthorityVo extends PageCondition implements Serializable {
    private String authorityId;

    private String authorityName;

    private String authorityContent;// Accessible URL, using comma to separate

    private String authorityRemark;

    private Date createTime;

    private Date updateTime;

}
