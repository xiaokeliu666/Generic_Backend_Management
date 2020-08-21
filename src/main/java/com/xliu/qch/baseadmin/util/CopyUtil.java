package com.xliu.qch.baseadmin.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity conversion
 */
@Slf4j
public class CopyUtil {

    /**
     * Class Conversion: EntityVo <-> Entity: UserVo <->User
     */
    public static <T> T copy(Object src, Class<T> targetType) {
        T target = null;
        try {
            // Create an empty target object and get a BeanWrapper proxy used to fill the attribute
            // BeanWrapperImpl use Spring's BeanUtils to set attribute by reflecting
            target = targetType.newInstance();
            BeanWrapper targetBean = new BeanWrapperImpl(target);

            // Get source object's BeanMap, convert attribute-value into key-value form
            BeanMap srcBean = new BeanMap(src);
            for (Object key : srcBean.keySet()) {
                String srcPropertyName = key + "";
                Object srcPropertyVal = srcBean.get(key);
                Class srcPropertyType = srcBean.getType(srcPropertyName);
                Class targetPropertyType = targetBean.getPropertyType(srcPropertyName);
                if ("class".equals(srcPropertyName) || targetPropertyType == null) {
                    continue;
                }

                // If types equal, value can be set directly
                if (srcPropertyType == targetPropertyType) {
                    targetBean.setPropertyValue(srcPropertyName, srcPropertyVal);
                }
                // If types not equal, e.g. User UserVo
                else {
                    if(srcPropertyVal == null){
                        continue;
                    }

                    Object targetPropertyVal = targetPropertyType.newInstance();
                    BeanWrapper targetPropertyBean = new BeanWrapperImpl(targetPropertyVal);

                    BeanMap srcPropertyBean = new BeanMap(srcPropertyVal);
                    for (Object srcPropertyBeanKey : srcPropertyBean.keySet()) {
                        String srcPropertyBeanPropertyName = srcPropertyBeanKey + "";
                        Object srcPropertyBeanPropertyVal = srcPropertyBean.get(srcPropertyBeanKey);
                        Class srcPropertyBeanPropertyType = srcPropertyBean.getType(srcPropertyBeanPropertyName);
                        Class targetPropertyBeanPropertyType = targetPropertyBean.getPropertyType(srcPropertyBeanPropertyName);

                        if ("class".equals(srcPropertyBeanPropertyName) || targetPropertyBeanPropertyType == null) {
                            continue;
                        }

                        if (srcPropertyBeanPropertyType == targetPropertyBeanPropertyType) {
                            targetPropertyBean.setPropertyValue(srcPropertyBeanPropertyName, srcPropertyBeanPropertyVal);
                        } else {
                            // Dont process complicated object
                        }
                    }
                    // Set target object value
                    targetBean.setPropertyValue(srcPropertyName, targetPropertyBean.getWrappedInstance());
                }
            }
        } catch (Exception e) {
            log.error(ErrorUtil.errorInfoToString(e));
        }
        return target;
    }

    /**
     * Type Conversion：Entity Vo <->Entity  e.g.:List<UserVo> <-> List<User>
     */

    public static <T> List<T> copyList(List srcList, Class<T> targetType) {
        List<T> newList = new ArrayList<>();
        for (Object entity : srcList) {
            newList.add(copy(entity, targetType));
        }
        return newList;
    }

}