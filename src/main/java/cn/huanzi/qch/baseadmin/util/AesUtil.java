package cn.huanzi.qch.baseadmin.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

/**
 * AES encryption & decryption
 */
public class AesUtil {
    /**
     * AES encryption
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * Length of key，Wrong key size: must be equal to 128, 192 or 256
     * For input :16、24、36
     */
    private static final Integer KEY_LENGTH = 16 * 8;

    /**
     * Name of Algorithm/ Scheme of encryption/ Scheme of data padding
     * Default：AES/ECB/PKCS5Padding
     */
    private static final String ALGORITHMS = "AES/ECB/PKCS5Padding";

    /**
     * Backend AES' key, using static code block
     */
    public static String key;

    private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();

    static {
        key = getKey();
    }

    public static String getKey() {
        StringBuilder uid = new StringBuilder();
        // Generate 16-byte secure random
        Random rd = new SecureRandom();
        for (int i = 0; i < KEY_LENGTH / 8; i++) {
            // Generate 3 random numbers with 0-2
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    // random number within 0-9
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    // ASCII: uppercase character values from 65 to 90
                    // Generate random uppercase character
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    // ASCII: lowercase character values from 97 to 112
                    // Generate random lowercase character
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * Encryption
     *
     * @param content    Encrypted String
     * @param encryptKey key value
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        // Cipher instance
        Cipher cipher = Cipher.getInstance(ALGORITHMS, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));

        // Convert to base64
        return Base64.encodeBase64String(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));

    }

    /**
     * Decryption
     *
     * @param encryptStr Decryption String
     * @param decryptKey Dectyption key
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        // Convert base64 to byte
        byte[] decodeBase64 = Base64.decodeBase64(encryptStr);

        Cipher cipher = Cipher.getInstance(ALGORITHMS,PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));

        return new String(cipher.doFinal(decodeBase64));
    }

}
