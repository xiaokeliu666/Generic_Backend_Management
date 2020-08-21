package cn.huanzi.qch.baseadmin.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * byte[] utils
 */
@Slf4j
public class ByteUtil {

    /**
     * Transform Binary to Hexadecimal
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        int num;
        for (byte aByte : bytes) {
            num = aByte;
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    /**
     * Transform Object to byte[]
     */
    public static byte[] objectToByte(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            // Start input stream
            oo.writeObject(obj);
            // Convert output stream to byte
            bytes = bo.toByteArray();
        } catch (Exception e) {
            // Output to log file
            log.error(ErrorUtil.errorInfoToString(e));
        } finally {
            // close the stream
            try {
                assert bo != null;
                bo.close();
                assert oo != null;
                oo.close();
            } catch (IOException e) {
                // Output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
        }
        return bytes;
    }

    /**
     * Convert byte[] to Object
     */
    public static Object byteToObject(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);
            // Read the input stream
            obj = oi.readObject();
        } catch (Exception e) {
            // output to log file
            log.error(ErrorUtil.errorInfoToString(e));
        } finally {
            // close the stream
            try {
                assert bi != null;
                bi.close();
                assert oi != null;
                oi.close();
            } catch (IOException e) {
                // output to log file
                log.error(ErrorUtil.errorInfoToString(e));
            }
        }
        return obj;
    }
}