package com.xliu.qch.baseadmin.util;


import java.util.UUID;

/**
 * UUID Util
 */
public class UUIDUtil{

	/** 
     * Generate UUID
     */
    public static String getUUID(){
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}
