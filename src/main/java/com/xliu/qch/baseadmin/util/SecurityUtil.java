package com.xliu.qch.baseadmin.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security Util
 */
public class SecurityUtil {
    /**
     * Get SecurityContext from ThreadLocal to get Security context cached login user
     */
    public static User getLoginUser() {
        User user = null;
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) user = (User) auth.getPrincipal();
        assert user != null;
        return user;
    }
}
