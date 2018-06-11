<#include "include/macros.ftl">
<@header title="留言板 | ${config.siteName}"
    keywords="${config.siteName},留言板,原创博客留言,个人原创网站,个人技术博客"
    description="我的留言板,欢迎给我的个人原创博客留言 - ${config.siteName}"
    canonical="/guestbook">
    <style>
        ul {
            list-style: none;
            margin-left: 0;
            padding-left: 0;
        }
    </style>
</@header>

<div class="container custome-container">
    <nav class="breadcrumb">
        <a class="crumbs" title="返回首页" href="${config.siteUrl}" data-toggle="tooltip" data-placement="bottom"><i class="fa fa-home"></i>首页</a>
        <i class="fa fa-angle-right"></i>留言板
    </nav>
    <div class="row">
        <@blogHeader title="留言板"></@blogHeader>
        <div class="col-sm-12 blog-main">
            <div class="blog-body expansion">
                <div class="alert alert-default alert-dismissible" role="alert" style="padding: 0px;">
                    <ul>
                        <li><i class="fa fa-lightbulb-o fa-fw"></i> 随便留言，只要不是反人类、反国家的危险言论。<strong>相信我！删除真的只需要1秒！</strong></li>
                        <li><i class="fa fa-lightbulb-o fa-fw"></i> 欢迎各种花式留言！e.g.<code>System.out.println("突如其来的骚，闪了老子的腰！");</code></li>
                        <li><i class="fa fa-heartbeat fa-fw"></i> <strong>爱谁谁...</strong></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-sm-12 blog-main">
            <div class="blog-body expansion">
                <div id="comment-box" data-id="-1"></div>
            </div>
        </div>
    </div>
</div>

<@footer>
    <script src="https://v1.hitokoto.cn/?encode=js&c=d&select=%23hitokoto" defer></script>
</@footer>
