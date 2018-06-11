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
package com.zyd.blog.core.aspects;

import com.alibaba.fastjson.JSON;
import com.zyd.blog.Bean.ResponseBean;
import com.zyd.blog.business.entity.ArticleLook;
import com.zyd.blog.business.service.BizArticleLookService;
import com.zyd.blog.business.service.BizArticleService;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.util.IpUtil;
import com.zyd.blog.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 登录验证aop操作
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @date 2018/4/18 11:48
 * @since 1.0
 */
@Slf4j
@Component
@Aspect
@Order(1)
public class LoginAspects {
    @Autowired
    private RedisTemplate redisTemplate;
    @Pointcut("execution(* com.zyd.blog.controller..*.*(..))")
    public void pointcut() {
        // 切面切入点
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse httpServletResponse = attributes.getResponse();
        String authorization = request.getHeader("Authorization");//获取header中的token并在redis中判断是否之前验证过
        Boolean flag = false;
        if(authorization!=null&&!"".equals(authorization)) {
            flag= redisTemplate.hasKey(authorization);//判断value缓存是否存在
        }
        if(flag==false){
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(401);
            PrintWriter out = httpServletResponse.getWriter();
            ResponseBean responseBean = new ResponseBean(0,"","");
            responseBean.setCode(401);
            responseBean.setData("");
            responseBean.setMsg("401未授权，请登录后再试");
            String jsonString = JSON.toJSONString(responseBean);
            out.print(jsonString);
            out.print(responseBean.toString());
            out.close();
        }
    }

    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("reponse=="+ret);
    }
}
