package com.xliu.qch.baseadmin.common.pojo;

import lombok.Data;

/**
 * Monitor the system
 */
@Data
public class MonitorVo {
    private String os;// Operating System
    private String runTime;
    private String jvmJavaVersion;

    //Info of JVM
    private String jvmHeapInit;
    private String jvmHeapMax;
    private String jvmHeapUsed;
    private String jvmHeapCommitted;
    private String jvmNonHeapInit;
    private String jvmNonHeapMax;
    private String jvmNonHeapUsed;
    private String jvmNonHeapCommitted;

    //Info of hardware
    private String cpuInfo;
    private String cpuUseRate;
    private String ramTotal;
    private String ramUsed;
    private String diskTotal;
    private String diskUsed;
}
