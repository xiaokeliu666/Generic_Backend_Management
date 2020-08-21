package com.xliu.qch.baseadmin.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * LogoutHandlerConfig
 */
@Component
public class LogoutHandlerConfig implements LogoutHandler {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        // Remove user who logs out
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal !=null){
            List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
            if (allSessions != null) {
                for (SessionInformation sessionInformation : allSessions) {
                    sessionInformation.expireNow();
                    sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
                }
            }
        }
    }
}
