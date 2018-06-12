/**
 * MIT License
 * Copyright (c) 2018 yadong.zhang
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zyd.blog.controller;

import com.zyd.blog.bean.ResponseBean;
import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.core.config.StatelessSessionManager;
import com.zyd.blog.core.shiro.filter.JWTToken;
import com.zyd.blog.core.shiro.filter.JWTUtil;
import com.zyd.blog.exception.UnauthorizedException;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.framework.property.AppProperties;
import com.zyd.blog.util.CookieUtil;
import com.zyd.blog.util.ResultUtil;
import com.zyd.blog.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@Slf4j
@Controller
@RequestMapping(value = "/passport")
public class PassportController {

    @Autowired
    private AppProperties config;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${myconfig.redis.expire}")
    private String expire;
    /**
     * 用户token存储  redisKey 前缀
     */
    private static final String SHIRO_LOGIN_TOKEN = "shiro_login_token_";
    @BussinessLog("进入登录页面")
    @GetMapping("/login")
    public ModelAndView login(Model model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()||subject.isRemembered()){
            return ResultUtil.redirect("/index");
        }
        model.addAttribute("enableKaptcha", config .getEnableKaptcha());
        return ResultUtil.view("/login");
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/signin")
    @ResponseBody
    public ResponseBean submitLogin(String username, String password, boolean rememberMe, String kaptcha, ServletRequest servletRequest, ServletResponse servletResponse) {
        //获取当前的Subject
        Subject  currentUser= SecurityUtils.getSubject();
        //从request中获取inspur_token
        String JwtToken = (String) servletRequest.getAttribute("inspur_token");
        System.out.println("JwtToken============="+JwtToken);
        if(currentUser.isAuthenticated()){
            //这里代码着已经登陆成功，所以自然不用再次认证，直接重新生成之后返回
            redisTemplate.opsForValue().set(JwtToken,SHIRO_LOGIN_TOKEN+username , Long.parseLong(expire), TimeUnit.MINUTES);
            CookieUtil.addCookie((HttpServletResponse) servletResponse,"inspur_token",JwtToken,null,50000);
            return new ResponseBean(200, "Login success", JwtToken);
        }
        JWTToken jwttoken = new JWTToken(JwtToken);
        //UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到xxRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            currentUser.login(jwttoken);
            //验证通过后将token存入redis
            Subject test2 = SecurityUtils.getSubject();
            System.out.println("是否验证成功t2======"+test2.isAuthenticated());
            redisTemplate.opsForValue().set(JwtToken,SHIRO_LOGIN_TOKEN+username , Long.parseLong(expire), TimeUnit.MINUTES);
            CookieUtil.addCookie((HttpServletResponse) servletResponse,"inspur_token",JwtToken,null,50000);
            return new ResponseBean(200, "Login success", JwtToken);
        } catch (Exception e) {
            log.error("登录失败，用户名[{}]", username, e);
          //  jwttoken.clear();
            throw new UnauthorizedException();
        }
    }

    /**
     * 使用权限管理工具进行用户的退出，跳出登录，给出提示信息
     *
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/logout")
    public ModelAndView logout(RedirectAttributes redirectAttributes) {
        // http://www.oschina.net/question/99751_91561
        // 此处有坑： 退出登录，其实不用实现任何东西，只需要保留这个接口即可，也不可能通过下方的代码进行退出
        // SecurityUtils.getSubject().logout();
        // 因为退出操作是由Shiro控制的
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return ResultUtil.redirect("index");
    }
}
