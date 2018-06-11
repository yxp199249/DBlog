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
package com.zyd.blog.core.shiro.credentials;

import com.zyd.blog.business.consts.SessionConst;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.core.shiro.filter.JWTToken;
import com.zyd.blog.core.shiro.filter.JWTUtil;
import com.zyd.blog.framework.holder.RequestHolder;
import com.zyd.blog.util.PasswordUtil;
import com.zyd.blog.util.SessionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import javax.annotation.Resource;

/**
 * Shiro-密码凭证匹配器（验证密码有效性）
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {
    @Resource
    private SysUserService userService;
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JWTToken utoken = (JWTToken) token;
        //String token, String username, String secret
        String tokenstring = utoken.getCredentials().toString();
        String username = JWTUtil.getUsername(tokenstring);
        //获得数据库中的密码
        User user = userService.getByUserName(username);
//        user.setUserToken(tokenstring);
        Boolean flag = false;
        String password = user.getPassword();
        try {
            password = PasswordUtil.decrypt(password, username);//使用加盐的密码生成
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
        try {
            flag = JWTUtil.verify(tokenstring,username,password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 当验证都通过后，把用户信息放在session里
        // 注：User必须实现序列化
//        SecurityUtils.getSubject().getSession().setAttribute("yxp", tokenstring);
        return flag;
    }
}
