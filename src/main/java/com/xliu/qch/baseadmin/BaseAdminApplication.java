package com.xliu.qch.baseadmin;

import com.xliu.qch.baseadmin.sys.sysmenu.vo.SysMenuVo;
import com.xliu.qch.baseadmin.sys.syssetting.service.SysSettingService;
import com.xliu.qch.baseadmin.sys.syssetting.vo.SysSettingVo;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.service.SysShortcutMenuService;
import com.xliu.qch.baseadmin.sys.sysshortcutmenu.vo.SysShortcutMenuVo;
import com.xliu.qch.baseadmin.sys.sysuser.service.SysUserService;
import com.xliu.qch.baseadmin.sys.sysuser.vo.SysUserVo;
import com.xliu.qch.baseadmin.sys.sysusermenu.service.SysUserMenuService;
import com.xliu.qch.baseadmin.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@EnableAsync
@SpringBootApplication
public class BaseAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseAdminApplication.class, args);
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}

@Slf4j
@Controller
@RequestMapping("/")
@Configuration
class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysSettingService sysSettingService;

    @Autowired
    private SysUserMenuService sysUserMenuService;

    @Autowired
    private SysShortcutMenuService sysShortcutMenuService;

    /**
     * port
     */
    @Value("${server.port}")
    private String port;

    /**
     * start successfully
     */
    @Bean
    public ApplicationRunner applicationRunner() {
        return applicationArguments -> {
            try {
                // When system starts, get the data from database and set to sysSettingMap
                SysSettingVo sysSettingVo = sysSettingService.get("1").getData();
                sysSettingVo.setUserInitPassword(null);// hide some attributes
                SysSettingUtil.setSysSettingMap(sysSettingVo);

                // get local IP
                log.info("Start Successfully " + "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/");
            } catch (UnknownHostException e) {
                log.error(ErrorUtil.errorInfoToString(e));
            }
        };
    }

    /**
     * 跳转登录页面
     */
    @GetMapping("loginPage")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("login");

        //系统信息
        modelAndView.addObject("sys", SysSettingUtil.getSysSetting());

        //后端公钥
        String publicKey = RsaUtil.getPublicKey();
        log.info("后端公钥：" + publicKey);
        modelAndView.addObject("publicKey", publicKey);

        return modelAndView;
    }

    /**
     * 跳转首页
     */
    @GetMapping("")
    public void index1(HttpServletResponse response){
        //内部重定向
        try {
            response.sendRedirect("/index");
        } catch (IOException e) {
            //输出到日志文件中
            log.error(ErrorUtil.errorInfoToString(e));
        }
    }
    @GetMapping("index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");

        //系统信息
        modelAndView.addObject("sys", SysSettingUtil.getSysSetting());

        //登录用户
        SysUserVo sysUserVo = sysUserService.findByLoginName(SecurityUtil.getLoginUser().getUsername()).getData();
        sysUserVo.setPassword(null);//隐藏部分属性
        modelAndView.addObject( "loginUser", sysUserVo);

        //登录用户系统菜单
        List<SysMenuVo> menuVoList = sysUserMenuService.findByUserId(sysUserVo.getUserId()).getData();
        modelAndView.addObject("menuList",menuVoList);

        //登录用户快捷菜单
        List<SysShortcutMenuVo> shortcutMenuVoList= sysShortcutMenuService.findByUserId(sysUserVo.getUserId()).getData();
        modelAndView.addObject("shortcutMenuList",shortcutMenuVoList);

        //后端公钥
        String publicKey = RsaUtil.getPublicKey();
        log.info("后端公钥：" + publicKey);
        modelAndView.addObject("publicKey", publicKey);
        return modelAndView;
    }

    /**
     * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
     */
    @RequestMapping("getVerifyCodeImage")
    public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置页面不缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.getOutputStream();
        String verifyCode = VerifyCodeImageUtil.generateTextCode(VerifyCodeImageUtil.TYPE_NUM_UPPER, 4, null);

        //将验证码放到HttpSession里面
        request.getSession().setAttribute("verifyCode", verifyCode);
         log.info("本次生成的验证码为：" + verifyCode + ",已存放到HttpSession中");

        //设置输出的内容的类型为JPEG图像
        response.setContentType("image/jpeg");
        BufferedImage bufferedImage = VerifyCodeImageUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);

        //写给浏览器
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
    }

    /**
     * 跳转实时系统硬件监控
     */
    @GetMapping("monitor")
    public ModelAndView monitor() {
        return new ModelAndView("monitor.html","port",port);
    }

    /**
     * 跳转实时日志
     */
    @GetMapping("logging")
    public ModelAndView logging() {
        return new ModelAndView("logging.html","port",port);
    }
}
