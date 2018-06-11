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
package com.zyd.blog.core.shiro.realm;

import com.auth0.jwt.JWT;
import com.zyd.blog.business.entity.Resources;
import com.zyd.blog.business.entity.Role;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.UserStatusEnum;
import com.zyd.blog.business.service.SysResourcesService;
import com.zyd.blog.business.service.SysRoleService;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.core.shiro.filter.JWTToken;
import com.zyd.blog.core.shiro.filter.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Shiro-密码输入错误的状态下重试次数的匹配管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
public class ShiroRealm extends AuthorizingRealm {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Resource
    private SysUserService userService;
    @Resource
    private SysResourcesService resourcesService;
    @Resource
    private SysRoleService roleService;
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
//        String password = new String ((char[])auth.getCredentials());
//        //获取用户的输入的账号.
//        String username = (String) auth.getPrincipal();
        String ss = (String)auth.getCredentials();
        String username = "";
        try{
            username= JWTUtil.getUsername(ss).toString();
        }catch (Exception e){
            throw new UnknownAccountException("TOKEN内不含用户名");
        }

        // 解密获得username，用于和数据库进行对比
        User user = userService.getByUserName(username);
        if (user == null) {
            throw new UnknownAccountException("账号不存在！");
        }
        if (user.getStatus() != null && UserStatusEnum.DISABLE.getCode().equals(user.getStatus())) {
            throw new LockedAccountException("帐号已被锁定，禁止登录！");
        }
        return new SimpleAuthenticationInfo(
                user.getId(),
                user.getPassword(),
                ByteSource.Util.bytes(username),
                getName()
        );
    }
    /**
     * 权限认证，为当前登录的Subject授予角色和权限（角色的权限信息集合）
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("=============================进入");
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
      //  Long userId = JWTUtil.getUsername(principalCollection.toString());
       Long userId = (Long) SecurityUtils.getSubject().getPrincipal();
        // 赋予角色
        List<Role> roleList = roleService.listRolesByUserId(userId);
        for (Role role : roleList) {
            info.addRole(role.getName());
        }
        // 赋予权限
        List<Resources> resourcesList = resourcesService.listByUserId(userId);
        if (!CollectionUtils.isEmpty(resourcesList)) {
            for (Resources resources : resourcesList) {
                String permission = null;
                if (!StringUtils.isEmpty(permission = resources.getPermission())) {
                    info.addStringPermission(permission);
                }
            }
        }
        return info;
    }

}
