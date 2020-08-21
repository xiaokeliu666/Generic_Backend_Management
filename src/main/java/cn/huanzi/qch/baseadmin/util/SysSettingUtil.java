package cn.huanzi.qch.baseadmin.util;

import cn.huanzi.qch.baseadmin.sys.syssetting.vo.SysSettingVo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * System Setting Util
 * Get the dataset data, set to SysSettingMap
 * After updating system setting, updating sysSettingMap
 */
public class SysSettingUtil {

    private static ConcurrentHashMap<String,SysSettingVo> sysSettingMap = new ConcurrentHashMap<>();

    public static SysSettingVo getSysSetting(){
        return sysSettingMap.get("sysSetting");
    }

    // Updating sysSettingMap
    public static void setSysSettingMap(SysSettingVo sysSetting){
        if(sysSettingMap.isEmpty()){
            sysSettingMap.put("sysSetting",sysSetting);
        }else{
            sysSettingMap.replace("sysSetting",sysSetting);
        }
    }
}
