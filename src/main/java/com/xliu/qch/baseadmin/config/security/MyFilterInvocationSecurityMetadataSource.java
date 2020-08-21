package com.xliu.qch.baseadmin.config.security;

import com.xliu.qch.baseadmin.sys.sysauthority.vo.SysAuthorityVo;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Config authentication data source to implement dynamically authorization load
 */
@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    // Authorization data
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    /**
     * Find the authorization data according to current url in the initial authorization datasource
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest request = fi.getRequest();

        // traverse the initialized authorization data and find authorization according to current url
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * Update authorization collection
     */
    public void setRequestMap(List<SysAuthorityVo> authorityVoList){
        Map<RequestMatcher, Collection<ConfigAttribute>> map = new ConcurrentHashMap<>();
        for (SysAuthorityVo sysAuthorityVo : authorityVoList) {
            String authorityName = sysAuthorityVo.getAuthorityName();
            if (StringUtils.isEmpty(sysAuthorityVo.getAuthorityContent())) continue;
            for (String url : sysAuthorityVo.getAuthorityContent().split(",")) {
                Collection<ConfigAttribute> value = map.get(new AntPathRequestMatcher(url));
                if (StringUtils.isEmpty(value)) {
                    ArrayList<ConfigAttribute> configs = new ArrayList<>();
                    configs.add(new SecurityConfig(authorityName));
                    map.put(new AntPathRequestMatcher(url), configs);
                } else {
                    value.add(new SecurityConfig(authorityName));
                }
            }
        }
        this.requestMap = map;
    }
}
