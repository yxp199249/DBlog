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

import com.github.pagehelper.PageInfo;
import com.zyd.blog.business.entity.Comment;
import com.zyd.blog.business.entity.Config;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.CommentStatusEnum;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.enums.TemplateKeyEnum;
import com.zyd.blog.business.service.BizCommentService;
import com.zyd.blog.business.service.MailService;
import com.zyd.blog.business.service.SysConfigService;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.business.vo.CommentConditionVO;
import com.zyd.blog.core.shiro.filter.JWTUtil;
import com.zyd.blog.framework.exception.ZhydCommentException;
import com.zyd.blog.framework.object.PageResult;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.CookieUtil;
import com.zyd.blog.util.ResultUtil;
import com.zyd.blog.util.SessionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论管理
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@RestController
@RequestMapping("/comment")
public class RestCommentController {
    @Autowired
    private BizCommentService commentService;
    @Autowired
    private SysConfigService configService;
    @Autowired
    private MailService mailService;
    @Autowired
    private SysUserService userService;
//    @RequiresPermissions("comments")
    @PostMapping("/list")
    public PageResult list(CommentConditionVO vo) {
        Subject currentUser= SecurityUtils.getSubject();
        System.out.println("是否验证成功t2======"+currentUser.isAuthenticated());
        PageInfo<Comment> pageInfo = commentService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }
//    @RequiresRoles("admin")
    @PostMapping(value = "/reply")
    public ResponseVO reply(Comment comment,HttpServletRequest request) {
        try {
            Config config = configService.get();
//            User user = SessionUtil.getUser();
            String inspur_token = CookieUtil.getValue(request,"inspur_token","");
            String userName = JWTUtil.getUsername(inspur_token);
            User user = userService.getByUserName(userName);
            comment.setQq(user.getQq());
            comment.setEmail(user.getEmail());
            comment.setNickname(user.getNickname());
            comment.setAvatar(user.getAvatar());
            comment.setUrl(config.getSiteUrl());
            comment.setUserId(user.getId());
            comment.setStatus(CommentStatusEnum.APPROVED.toString());
            commentService.comment(comment);
        } catch (ZhydCommentException e) {
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("成功");
    }

    @PostMapping(value = "/remove")
    public ResponseVO remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            commentService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 条评论");
    }

    @PostMapping("/get/{id}")
    public ResponseVO get(@PathVariable Long id) {
        return ResultUtil.success(null, this.commentService.getByPrimaryKey(id));
    }

    @PostMapping("/edit")
    public ResponseVO edit(Comment comment) {
        try {
            commentService.updateSelective(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("评论修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 审核
     *
     * @param comment
     * @return
     */
    @PostMapping("/audit")
    public ResponseVO audit(Comment comment, Boolean sendEmail) {
        try {
            commentService.updateSelective(comment);
            if (null != sendEmail && sendEmail) {
                Comment commentDB = commentService.getByPrimaryKey(comment.getId());
                mailService.send(commentDB, TemplateKeyEnum.TM_COMMENT_AUDIT, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("评论审核失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    /**
     * 正在审核的
     *
     * @param comment
     * @return
     */
    @PostMapping("/listVerifying")
    public ResponseVO listVerifying(Comment comment) {
        return ResultUtil.success(null, commentService.listVerifying(10));
    }

}
