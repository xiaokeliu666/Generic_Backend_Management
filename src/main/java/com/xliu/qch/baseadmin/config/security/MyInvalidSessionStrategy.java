package com.xliu.qch.baseadmin.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Customized Session invalid strategy
 */
@Component
@Slf4j
public class MyInvalidSessionStrategy implements InvalidSessionStrategy {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        HttpSession session = httpServletRequest.getSession();
        String sessionId = httpServletRequest.getRequestedSessionId();

//      True if the client does not yet know about the session or if the client chooses not to join the session
        if(!session.isNew()){
            // Inner redirect
            httpServletResponse.sendRedirect("/loginPage");
        }else{
            //js script
            httpServletResponse.setContentType("text/html;charset=UTF-8");
            httpServletResponse.getWriter().print("<script type='text/javascript'>window.location.href = \"/loginPage\"</script>");
        }
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if(sessionInformation != null){
            User user = (User) sessionInformation.getPrincipal();
            sessionRegistry.removeSessionInformation(sessionId);
            log.info("Remove expired user:"+user.getUsername());
        }
        log.info("session expire handler " + sessionRegistry.getAllPrincipals().size()+"");
        httpServletResponse.flushBuffer();
    }
}
