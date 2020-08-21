package com.xliu.qch.baseadmin.config.security;

import com.xliu.qch.baseadmin.sys.sysuser.service.SysUserService;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import com.xliu.qch.baseadmin.util.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Login successfully handler, then do verification check
 */
@Component
@Slf4j
public class LoginSuccessHandlerConfig implements AuthenticationSuccessHandler {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DataSource dataSource;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // Get all the online users to judge single login
        ArrayList<String> allSessionIdList = new ArrayList<>();
        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
        for (SessionInformation sessionInformation : allSessions) {
            allSessionIdList.add(sessionInformation.getSessionId());
        }

        // Query current users who are still interacting with system and store in context to authenticate the user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUserVo sysUserVo = sysUserService.findByLoginName(user.getUsername()).getData();

        // Login successfully
        String msg = "{\"code\":\"300\",\"msg\":\"Login Successfully\",\"url\":\"/index\"}";
        boolean flag = false;

        String ipAddr = IpUtil.getIpAddr(httpServletRequest);
        String limitedIp = sysUserVo.getLimitedIp();
        if(!StringUtils.isEmpty(limitedIp) && !Arrays.asList(limitedIp.split(",")).contains(ipAddr)){
            msg = "{\"code\":\"400\",\"msg\":\"Your IP has been banned\"}";
            flag = true;
        }

        // Multiple login denied
        if("N".equals(sysUserVo.getLimitMultiLogin()) &&  allSessionIdList.size() > 0){
            msg = "{\"code\":\"400\",\"msg\":\"Multiple login denied \"}";
            flag = true;
        }

        // Expired account
        if(!StringUtils.isEmpty(sysUserVo.getExpiredTime()) && new Date().getTime() > sysUserVo.getExpiredTime().getTime()){
            msg = "{\"code\":\"400\",\"msg\":\"This account is expired\"}";
            flag = true;
        }

        // BANNED
        if("N".equals(sysUserVo.getValid())){
            msg = "{\"code\":\"400\",\"msg\":\"This account has been banned\"}";
            flag = true;
        }

        // denied
        if(flag){
            // clear context
            SecurityContextHolder.clearContext();

            // clear remember-me persistent token
//            System.out.println("=========Start Test:persistentTokenRepository1().removeUserTokens(user.getUsername());==========");
//            System.out.println("=========Remove UserTokens:"+user.getUsername());
            persistentTokenRepository1().removeUserTokens(user.getUsername());
//            System.out.println("=========Finish:persistentTokenRepository1().removeUserTokens(user.getUsername());==========");
        }
        else{
            // Successful authentication, register new session
            sessionRegistry.registerNewSession(httpServletRequest.getSession().getId(),user);
        }

        // API ON/OFF
        if("Y".equals(SysSettingUtil.getSysSetting().getSysApiEncrypt())) {
            // Encrypt
            try {
                // Frontend public key
                String publicKey = httpServletRequest.getParameter("publicKey");

                log.info("Frontend public key:" + publicKey);

                //jackson
                ObjectMapper mapper = new ObjectMapper();
                // jackson serialize and deserialize date
                mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // Every time before response, get an AES key to encrypt data
                String key = AesUtil.getKey();
                log.info("AES key:" + key);
                log.info("Data waited to be encrypted:" + msg);
                String data = AesUtil.encrypt(msg, key);

                // Use frontend publicKey to decrypt AES key and convert to Base64
                String aesKey = Base64.encodeBase64String(RsaUtil.encryptByPublicKey(key.getBytes(), publicKey));

                msg = "{\"data\":{\"data\":\"" + data + "\",\"aesKey\":\"" + aesKey + "\"}}";
            } catch (Throwable e) {
                log.error(ErrorUtil.errorInfoToString(e));
            }
        }

        // Convert json String to Object object
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.print(msg);
        out.flush();
        out.close();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository1() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        return persistentTokenRepository;
    }
}
