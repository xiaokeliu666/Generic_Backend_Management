package cn.huanzi.qch.baseadmin.aspect;

import cn.huanzi.qch.baseadmin.annotation.Decrypt;
import cn.huanzi.qch.baseadmin.annotation.Encrypt;
import cn.huanzi.qch.baseadmin.common.pojo.Result;
import cn.huanzi.qch.baseadmin.util.AesUtil;
import cn.huanzi.qch.baseadmin.util.ErrorUtil;
import cn.huanzi.qch.baseadmin.util.RsaUtil;
import cn.huanzi.qch.baseadmin.util.SysSettingUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

/**
 * Using AOP to do the AES + RSA Encryption and decryption
 */
@Slf4j
@Aspect
@Component
public class SafetyAspect {

    /**
     * Pointcut
     * match
     * cn.huanzi.qch.baseadmin.sys.*.controller、
     * cn.huanzi.qch.baseadmin.*.controller
     */
    @Pointcut(value = "execution(public * cn.huanzi.qch.baseadmin.sys.*.controller.*.*(..)) || " +
            "execution(public * cn.huanzi.qch.baseadmin.*.controller.*.*(..))")
    public void safetyAspect() {}

    /**
     * Around advice
     */
    @Around(value = "safetyAspect()")
    public Object around(ProceedingJoinPoint pjp) {
       try {

           // api encrypt ON or OFF
           if("N".equals(SysSettingUtil.getSysSetting().getSysApiEncrypt())){
               return pjp.proceed(pjp.getArgs());
           }

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            //request object
            HttpServletRequest request = attributes.getRequest();

            //httpMethod:  post get
            String httpMethod = request.getMethod().toLowerCase();

            //method
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();

            //Annotation above method
            Annotation[] annotations = method.getAnnotations();

            //The list of field
            Object[] args = pjp.getArgs();

            //@Decrypt?
            boolean hasDecrypt = false;
            //@Encrypt?
            boolean hasEncrypt = false;
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Decrypt.class) {
                    hasDecrypt = true;
                }
                if (annotation.annotationType() == Encrypt.class) {
                    hasEncrypt = true;
                }
            }

            //public Key from front end
            String publicKey = null;

            //jackson
            ObjectMapper mapper = new ObjectMapper();
            //jackson Serialization and Deserialization of the date format
            mapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

           // Decrypt before executing the method and only intercept POST method
            if ("post".equals(httpMethod) && hasDecrypt) {
                // data after AES encryption
                String data = request.getParameter("data");
                // RSA encrypt the AES' key
                String aesKey = request.getParameter("aesKey");
                // front end public key
                publicKey = request.getParameter("publicKey");

                log.info("front end publicKey" + publicKey);

                // Using the private key at the backend and get AES key
                byte[] plaintext = RsaUtil.decryptByPrivateKey(Base64.decodeBase64(aesKey), RsaUtil.getPrivateKey());
                aesKey = new String(plaintext);
                log.info("AES Key after decryption:" + aesKey);

                // The String decrypted by RSA will have a double quotation
                // Remove the double quotation("")
                aesKey = aesKey.substring(1, aesKey.length() - 1);

                // AES Decrypt, and get the plaintext data
                String decrypt = AesUtil.decrypt(data, aesKey);
                log.info("data after decryption" + decrypt);

                // This line of code can be replaced by annotation @JsonIgnoreProperties(ignoreUnknown = true)
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                if(args.length > 0){
                    args[0] = mapper.readValue(decrypt, args[0].getClass());
                }
            }

            // Using new formal parameter to execute
            Object o = pjp.proceed(args);

            //Encrypt before return
            if (hasEncrypt) {
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                String key = AesUtil.getKey();
                log.info("AES' key：" + key);
                String dataString = mapper.writeValueAsString(o);
                log.info("Data waited to be encrypted" + dataString);
                String data = AesUtil.encrypt(dataString, key);

                // Using front end public key to decrypt AES' key and convert into Base64
                String aesKey = Base64.encodeBase64String(RsaUtil.encryptByPublicKey(key.getBytes(), publicKey));

                // convert to json String, then convert to Object
                o = Result.of(mapper.readValue("{\"data\":\"" + data + "\",\"aesKey\":\"" + aesKey + "\"}", Object.class));
            }

            return o;

        } catch (Throwable e) {
           log.error(ErrorUtil.errorInfoToString(e));
            return Result.of(null, false, "Exception during encryption(decryption)\n\t" + e.getMessage());
        }
    }
}
