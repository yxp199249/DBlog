package com.zyd.blog.util;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



/**

 * cookie工具类

 * @author hanchao

 * 2013-04-23

 */

public class CookieUtil {



    /*******************

     * 返回用户访问的次数

     * hanchao

     * 2013-04-23

     * *****************

     * @param request

     * @param cName

     * @param value

     * @return

     */

    public static String getValue(HttpServletRequest request,String cName,String value) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if(cName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return value;
    }


    /**************************

     * get Cookie By cookieName

     * hanchao

     * 2013-04-23

     * ***********************

     * @param request

     * @param cName

     * @return

     */

    public static Cookie getCookie(HttpServletRequest request,String cName) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if(cookie != null && cName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }


    /**
     * 添加cookie
     * @param response
     * 2013-6-18
     * @author： 韩超
     */

    public static void addCookie(HttpServletResponse response,String name,String value,String path,int timeout) {
        Cookie cookie = new Cookie(name, value);
//        if(domain == null) {
//            domain = ".baidu.com";//Constant.PASSPORTDOMAIN;
//        }
        if(path == null) {
            path = "/";
        }
//        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(timeout);
        response.addCookie(cookie);
    }


    /**

     * 删除cookie

     * @param request

     * @param response



     * 2013-6-18

     * @author： 韩立伟

     */

    public static void delCookie(HttpServletRequest request,HttpServletResponse response,String name) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookies != null && (name).equals(cookie.getName())) {
                addCookie(response,name,null,null,0);
                return;
            }
        }
    }


    /**

     * 修改cookie的value值

     * @param request

     * @param response

     * @param name
     * @param value
     * 2013-6-18
     * @author： 韩超
     */
    public static void updateCookie(HttpServletRequest request,HttpServletResponse response,String name,String value) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookies != null && (name).equals(cookie.getName())) {
                addCookie(response,name,value,cookie.getPath(),cookie.getMaxAge());
                return;
            }
        }
    }

}