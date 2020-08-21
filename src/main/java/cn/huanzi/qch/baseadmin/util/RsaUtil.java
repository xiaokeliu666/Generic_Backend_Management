package cn.huanzi.qch.baseadmin.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * RSA Encryption & Decryption
 */
@Slf4j
public class RsaUtil {

    /**
     * RSA Encryption
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * Name of Algorithm/ Scheme of encryption/ Scheme of data padding
     * Default：RSA/ECB/PKCS1Padding
     */
    private static final String ALGORITHMS = "RSA/ECB/PKCS1Padding";

    private static final String PUBLIC_KEY = "publicKey";

    private static final String PRIVATE_KEY = "privateKey";

    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final int INITIALIZE_LENGTH = 1024;

    /**
     * Backend RSA key pair, using static code block to init the value
     */
    private static Map<String, Object> genKeyPair = new LinkedHashMap<>();

    static {
        try {
            genKeyPair.putAll(genKeyPair());
        } catch (Exception e) {
            // Log out to log file
            log.error(ErrorUtil.errorInfoToString(e));
        }
    }

    /**
     * Generate key pair(public and private)
     */
    private static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(INITIALIZE_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        // Public key
        keyMap.put(PUBLIC_KEY, publicKey);
        // Private key
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * Private key decryption
     *
     * @param encryptedData Encrypted data
     * @param privateKey    privateKey(BASE64)
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        // Convert key(base64) to Key object
        Key privateK = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));

        // Set encryption and padding schemE
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        // Decrypt by subsection
        return encryptAndDecryptOfSubsection(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * Public key encryption
     *
     * @param data      source data
     * @param publicKey public key(BASE64)
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        // Convert key(base64) to key Object
        Key publicK = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.ENCRYPT_MODE, publicK);

        // Encrypt by subsection
        return encryptAndDecryptOfSubsection(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    public static String getPrivateKey() {
        Key key = (Key) genKeyPair.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static String getPublicKey() {
        Key key = (Key) genKeyPair.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * Encrypt and decrypt by subsection
     */
    private static byte[] encryptAndDecryptOfSubsection(byte[] data, Cipher cipher, int encryptBlock) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // Encrypt by subsection
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > encryptBlock) {
                cache = cipher.doFinal(data, offSet, encryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * encryptBlock;
        }
        out.close();
        return out.toByteArray();
    }
}
