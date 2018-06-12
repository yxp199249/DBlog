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

/**
 * 页面渲染相关 -- 页面跳转
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */

import com.zyd.blog.business.annotation.BussinessLog;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.core.shiro.filter.JWTUtil;
import com.zyd.blog.util.CookieUtil;
import com.zyd.blog.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 页面跳转类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@Controller
public class RenderController {
    @Autowired
    private SysUserService userService;

    @BussinessLog("进入首页")
    @GetMapping("")
    public ModelAndView home() {
//        Subject currentUser= SecurityUtils.getSubject();
//        String userId = currentUser.getPrincipal().toString();
        ModelAndView res = ResultUtil.view("index");
//        System.out.println("userId=============="+userId);
//        res.addObject("time", new Date(System.currentTimeMillis()));
        res.addObject("userId","1");
        return res;
    }

    @BussinessLog("进入首页")
    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
//        request.setAttribute("userId","admin");
//        Subject currentUser= SecurityUtils.getSubject();
//        String userId = currentUser.getPrincipal().toString();
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("index");
//        System.out.println("userId=============="+userName);
//        res.addObject("time", new Date(System.currentTimeMillis()));
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入用户列表页")
    @GetMapping("/users")
    public ModelAndView user(HttpServletRequest request) {
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("user/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入资源列表页")
    @GetMapping("/resources")
    public ModelAndView resources(HttpServletRequest request) {
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("resources/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入角色列表页")
    @GetMapping("/roles")
    public ModelAndView roles(HttpServletRequest request) {
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("role/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入文章列表页")
    @GetMapping("/articles")
    public ModelAndView articles(HttpServletRequest request) {
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("article/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入发表文章页")
    @GetMapping("/article/publish")
    public ModelAndView publish(HttpServletRequest request) {
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("article/publish");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入发表文章页")
    @GetMapping("/article/update/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, Model model,HttpServletRequest request) {
        model.addAttribute("id", id);
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("article/publish");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入分类列表页")
    @GetMapping("/article/types")
    public ModelAndView types(HttpServletRequest request) {
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("article/types");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入标签列表页")
    @GetMapping("/article/tags")
    public ModelAndView tags(HttpServletRequest request) {
//        return ResultUtil.view("article/tags");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("article/tags");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入链接页")
    @GetMapping("/links")
    public ModelAndView links(HttpServletRequest request) {
//        return ResultUtil.view("link/list");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("link/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入评论页")
    @GetMapping("/comments")
    public ModelAndView comments(HttpServletRequest request) {
//        return ResultUtil.view("comment/list");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("comment/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }


    @BussinessLog("进入系统通知页")
    @GetMapping("/notices")
    public ModelAndView notices(HttpServletRequest request) {
//        return ResultUtil.view("notice/list");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("notice/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入系统配置页")
    @GetMapping("/config")
    public ModelAndView config(HttpServletRequest request ){
//        return ResultUtil.view("config");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("config");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入模板管理页")
    @GetMapping("/templates")
    public ModelAndView templates(HttpServletRequest request) {
//        return ResultUtil.view("template/list");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("template/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入更新记录管理页")
    @GetMapping("/updates")
    public ModelAndView updates(HttpServletRequest request) {
//        return ResultUtil.view("update/list");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("update/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入歌单管理页")
    @GetMapping("/plays")
    public ModelAndView plays(HttpServletRequest request) {
//        return ResultUtil.view("play/list");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("play/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @BussinessLog("进入静态页面管理页")
    @GetMapping("/sysWebpage")
    public ModelAndView sysWebpage(HttpServletRequest request) {
//        return ResultUtil.view("sysWebpage/list");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("sysWebpage/list");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @GetMapping("/icons")
    public ModelAndView icons(Model model,HttpServletRequest request) {
//        return ResultUtil.view("icons");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("icons");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }

    @GetMapping("/shiro")
    public ModelAndView shiro(Model model,HttpServletRequest request) {
//        return ResultUtil.view("shiro");
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        ModelAndView res = ResultUtil.view("shiro");
        res.addObject("inspur_token_userid",user.getId());
        return res;
    }
}
