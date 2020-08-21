package cn.huanzi.qch.baseadmin.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5
 */
@Slf4j
public class MD5Util {

    /**
     * Generate MD5 encrypted string
     */
    public static String getMD5(String message) {
        String md5 = "";
        try {
            // md5 object
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            // get MD5 byte[]
            byte[] md5Byte = md.digest(messageByte);
            // convert to hex
            md5 = ByteUtil.bytesToHex(md5Byte);
        } catch (Exception e) {
            log.error(ErrorUtil.errorInfoToString(e));
        }
        return md5;
    }

    /**
     * Verification
     * @param text Plaintext
     * @param md5 Ciphertext
     * @return result(boolean)
     */
    private static boolean verify(String text,String md5){
        return md5.equals(getMD5(text));
    }
}
