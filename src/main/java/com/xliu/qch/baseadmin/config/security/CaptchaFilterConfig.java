package com.xliu.qch.baseadmin.config.security;

import com.xliu.qch.baseadmin.common.pojo.ParameterRequestWrapper;
import com.xliu.qch.baseadmin.util.AesUtil;
import com.xliu.qch.baseadmin.util.ErrorUtil;
import com.xliu.qch.baseadmin.util.RsaUtil;
import com.xliu.qch.baseadmin.util.SysSettingUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * CaptchaFilterConfig
 */
@Component
@Slf4j
public class CaptchaFilterConfig implements Filter {

    @Value("${captcha.enable}")
    private Boolean captchaEnable;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        /*
            Note: source code at org.springframework.security.web.session.SessionManagementFilter
            Spring Security stores log info in session's attribute.
            1.session.getAttribute(this.springSecurityContextKey) to retrieve log info, if null then step 2;
            2.SecurityContextHolder.getContext().getAuthentication() to retrieve log info according to the context;
            So, in order to force user to be offline we have to do as following:
         */
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
        if(sessionInformation == null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null){
            // print js and logout the user
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print("<script type='text/javascript'>window.location.href = '/logout'</script>");
        }

        // Intercept log in request only unless under development environment
        if ("POST".equals(request.getMethod()) && "/login".equals(request.getRequestURI())) {
            // front end public key
            String publicKey = null;

            //jackson
            ObjectMapper mapper = new ObjectMapper();
            //jackson serialization and deserialization date
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            // API Encryption ON or OFF
            if("Y".equals(SysSettingUtil.getSysSetting().getSysApiEncrypt())){
                //decryption
//                System.out.println("=========Start to decrypt========");
                try {
                    // data after AES encryption
                    String data = request.getParameter("data");
                    // AES key after back end RSA public key encryption
                    String aesKey = request.getParameter("aesKey");
                    // front end public key
                    publicKey = request.getParameter("publicKey");

                    log.info("front end public key：" + publicKey);

                    // using backend private key to decrypt AES and get the ky
                    byte[] plaintext = RsaUtil.decryptByPrivateKey(Base64.decodeBase64(aesKey), RsaUtil.getPrivateKey());
                    aesKey = new String(plaintext);
                    log.info("AES Key：" + aesKey);

                    // delete the quote (the result of decryption has quotes)
                    aesKey = aesKey.substring(1, aesKey.length() - 1);

                    // AES decrypts and get plaintext data
                    String decrypt = AesUtil.decrypt(data, aesKey);
                    log.info("Data after decryption：" + decrypt);

                    // Allow if there is a new field added to json which doesn't exist in entity.
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    // Customized RequestWrapper
                    // Method to deserialize JSON content from given JSON content String.
                    HashMap hashMap = mapper.readValue(decrypt, HashMap.class);
                    ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(request);

                    for (Object key : hashMap.keySet()) {
                        parameterRequestWrapper.addParameter((String) key,  hashMap.get(key));
                    }

                    servletRequest = parameterRequestWrapper;
                    request = (HttpServletRequest) servletRequest;
                } catch (Throwable e) {
                    log.error(ErrorUtil.errorInfoToString(e));
                }
            }

            // Get captcha from session
            String verifyCode = session.getAttribute("verifyCode").toString();

            if (captchaEnable && !verifyCode.toLowerCase().equals(request.getParameter("captcha").toLowerCase())) {
                String dataString = "{\"code\":\"400\",\"msg\":\"Wrong Captcha\"}";

                // API Encryption ON/OFF
                if("Y".equals(SysSettingUtil.getSysSetting().getSysApiEncrypt())){
                    // Encrypt
                    try {
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    // get AES key to encrypt the data
                    String key = AesUtil.getKey();
                    log.info("AES key：" + key);
                    log.info("Data waits to be encrypted：" + dataString);
                    String data = AesUtil.encrypt(dataString, key);

                    // Using front end publicKey to decrypt and convert to Base64
                    String aesKey = Base64.encodeBase64String(RsaUtil.encryptByPublicKey(key.getBytes(), publicKey));
                    dataString = "{\"data\":{\"data\":\"" + data + "\",\"aesKey\":\"" + aesKey + "\"}}";
                } catch (Throwable e) {
                        log.error(ErrorUtil.errorInfoToString(e));
                    }
                }

                // Convert Json String to Object
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                out.print(dataString);
                out.flush();
                out.close();
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
