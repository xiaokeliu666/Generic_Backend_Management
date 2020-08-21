package com.xliu.qch.baseadmin.config.security;

import com.xliu.qch.baseadmin.util.AesUtil;
import com.xliu.qch.baseadmin.util.ErrorUtil;
import com.xliu.qch.baseadmin.util.RsaUtil;
import com.xliu.qch.baseadmin.util.SysSettingUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * LoginFailureHandler
 */
@Component
@Slf4j
public class LoginFailureHandlerConfig implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        String msg = "{\"code\":\"400\",\"msg\":\"User name or password invalid\"}";

        // API ON/OFF
        if("Y".equals(SysSettingUtil.getSysSetting().getSysApiEncrypt())){
            // Encrypt
            try {
                // Front end public key
                String publicKey = httpServletRequest.getParameter("publicKey");

                log.info("Front end public key:" + publicKey);

                //jackson
                ObjectMapper mapper = new ObjectMapper();
                //jackson serialization and deserialization date
                mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // Get AES key randomly every time before response, to do encryption
                String key = AesUtil.getKey();
                log.info("AES KEY" + key);
                log.info("Data waited to be encrypted" + msg);
                String data = AesUtil.encrypt(msg, key);

                // Use front end public key to decrypt AES key and convert to Base64
                String aesKey = Base64.encodeBase64String(RsaUtil.encryptByPublicKey(key.getBytes(), publicKey));
                msg = "{\"data\":{\"data\":\"" + data + "\",\"aesKey\":\"" + aesKey + "\"}}";
            } catch (Throwable ee) {
                log.error(ErrorUtil.errorInfoToString(ee));
            }
        }

        // COnvert json String to Object
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.print(msg);
        out.flush();
        out.close();
    }
}
