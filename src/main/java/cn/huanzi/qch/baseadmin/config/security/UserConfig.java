package cn.huanzi.qch.baseadmin.config.security;

import cn.huanzi.qch.baseadmin.sys.sysuser.service.SysUserService;
import cn.huanzi.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import cn.huanzi.qch.baseadmin.sys.sysuserauthority.service.SysUserAuthorityService;
import cn.huanzi.qch.baseadmin.sys.sysuserauthority.vo.SysUserAuthorityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class UserConfig implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserAuthorityService sysUserAuthorityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // find by login name
        SysUserVo sysUserVo = sysUserService.findByLoginName(username).getData();
        // get the authority list
        List<SysUserAuthorityVo> sysUserAuthorityVoList = sysUserAuthorityService.findByUserId(sysUserVo.getUserId()).getData();
        StringBuilder authorityList = new StringBuilder();
        for (int i = 0; i < sysUserAuthorityVoList.size(); i++) {
            SysUserAuthorityVo sysUserAuthorityVo = sysUserAuthorityVoList.get(i);
            authorityList.append(sysUserAuthorityVo.getSysAuthority().getAuthorityName());
            if (i != sysUserAuthorityVoList.size() - 1) {
                authorityList.append(",");
            }
        }

        // User not found
        if(StringUtils.isEmpty(sysUserVo.getUserId())){
            sysUserVo.setLoginName("User not found");
            sysUserVo.setPassword("User not found");
        }

        // return user with login name, password and authority list
        return new User(sysUserVo.getLoginName(), sysUserVo.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(authorityList.toString()));
    }
}
