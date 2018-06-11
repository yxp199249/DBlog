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
import com.zyd.blog.business.entity.Notice;
import com.zyd.blog.business.entity.User;
import com.zyd.blog.business.enums.NoticeStatusEnum;
import com.zyd.blog.business.enums.ResponseStatus;
import com.zyd.blog.business.service.SysNoticeService;
import com.zyd.blog.business.service.SysUserService;
import com.zyd.blog.business.vo.NoticeConditionVO;
import com.zyd.blog.core.shiro.filter.JWTUtil;
import com.zyd.blog.framework.object.PageResult;
import com.zyd.blog.framework.object.ResponseVO;
import com.zyd.blog.util.CookieUtil;
import com.zyd.blog.util.ResultUtil;
import com.zyd.blog.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统通知-- 首页菜单下方滚动显示
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@RestController
@RequestMapping("/notice")
public class RestNoticeController {
    @Autowired
    private SysNoticeService noticeService;
    @Autowired
    private SysUserService userService;

    @PostMapping("/list")
    public PageResult list(NoticeConditionVO vo) {
        PageInfo<Notice> pageInfo = noticeService.findPageBreakByCondition(vo);
        return ResultUtil.tablePage(pageInfo);
    }

    @PostMapping(value = "/add")
    public ResponseVO add(Notice notice,HttpServletRequest request) {
//        User user = SessionUtil.getUser();
        String inspur_token = CookieUtil.getValue(request,"inspur_token","");
        String userName = JWTUtil.getUsername(inspur_token);
        User user = userService.getByUserName(userName);
        if (null != user) {
            notice.setUserId(user.getId());
        }
        noticeService.insert(notice);
        return ResultUtil.success("系统通知添加成功");
    }

    @PostMapping(value = "/remove")
    public ResponseVO remove(Long[] ids) {
        if (null == ids) {
            return ResultUtil.error(500, "请至少选择一条记录");
        }
        for (Long id : ids) {
            noticeService.removeByPrimaryKey(id);
        }
        return ResultUtil.success("成功删除 [" + ids.length + "] 个系统通知");
    }

    @PostMapping("/get/{id}")
    public ResponseVO get(@PathVariable Long id) {
        return ResultUtil.success(null, this.noticeService.getByPrimaryKey(id));
    }

    @PostMapping("/edit")
    public ResponseVO edit(Notice notice) {
        try {
            noticeService.updateSelective(notice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统通知修改失败！");
        }
        return ResultUtil.success(ResponseStatus.SUCCESS);
    }

    @PostMapping("/release/{id}")
    public ResponseVO release(@PathVariable Long id) {
        try {
            Notice notice = new Notice();
            notice.setId(id);
            notice.setStatus(NoticeStatusEnum.RELEASE.toString());
            noticeService.updateSelective(notice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("通知发布失败，状态不变！");
        }
        return ResultUtil.success("该通知已发布，可去前台页面查看效果！");
    }

    @PostMapping("/withdraw/{id}")
    public ResponseVO withdraw(@PathVariable Long id) {
        try {
            Notice notice = new Notice();
            notice.setId(id);
            notice.setStatus(NoticeStatusEnum.NOT_RELEASE.toString());
            noticeService.updateSelective(notice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("通知撤回失败，状态不变！");
        }
        return ResultUtil.success("该通知已撤回，可修改后重新发布！");
    }

}
