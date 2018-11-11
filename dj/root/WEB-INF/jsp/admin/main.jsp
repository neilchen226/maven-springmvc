<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>Dajie订单管理系统</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="icon" href="favicon.ico">
	<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
	<link rel="stylesheet" href="/resource/layuicms2.0/css/index.css" media="all" />
	<style type="text/css">
.nav_btn {
	float: left;
	width: 20px;
	height: 20px;
	margin-top: 15px;
	margin-left: 5px;
	font-size: 17px;
	line-height: 20px;
	text-align: center;
	padding: 5px 5px;
	color: #fff;
	background-color: #1AA094;
}
</style>
</head>
<body class="main_body">
	<div class="layui-layout layui-layout-admin">
		<!-- 顶部 -->
		<div class="layui-header header">
			<div class="layui-main mag0">
				<a href="javascript:void(0);" class="logo">
					<img src="/resource/zmh/img/head.jpg" style="height: 48px; line-height: 60px;">
					<span style="font-size: 24px;vertical-align: bottom; line-height: 60px;">Dajie</span>
				</a>
				<!-- 显示/隐藏左侧菜单 -->
				<a href="javascript:void(0);" class="seraph hideMenu icon-caidan"></a>
				<a href="javascript:;" class="nav_btn refresh refreshThis" title=" 刷新当前"><i class="layui-icon">&#x1002;</i></a>
				<!-- 顶部菜单 -->
			    <!-- 顶部右侧菜单 -->
			    <ul class="layui-nav top_menu">
					<li class="layui-nav-item" pc  style="display:none;">
						<a href="javascript:;" class="clearCache"><i class="layui-icon" data-icon="&#xe640;">&#xe640;</i><cite>清除缓存</cite></a>
					</li>
					<li class="layui-nav-item lockcms" pc style="display:none;">
						<a href="javascript:;"><i class="seraph icon-lock"></i><cite>锁屏</cite></a>
					</li>
					<li class="layui-nav-item" id="userInfo">
						<a href="javascript:;"><img src="/resource/zmh/img/logon.png" class="layui-nav-img userAvatar" width="35" height="35"><cite class="userName">未登录</cite></a>
						<dl class="layui-nav-child">
							<dd><a href="javascript:;" data-url="/changeUserInfo"><i class="seraph icon-ziliao" data-icon="icon-ziliao"></i><cite>个人资料</cite></a></dd>
							<dd><a href="javascript:;" data-url="/changePass"><i class="seraph icon-xiugai" data-icon="icon-xiugai"></i><cite>修改密码</cite></a></dd>
							<dd><a href="javascript:;" class="showNotice" style="display:none;"><i class="layui-icon">&#xe645;</i><cite>系统公告</cite><span class="layui-badge-dot"></span></a></dd>
							<dd pc><a href="javascript:;" class="functionSetting"><i class="layui-icon">&#xe620;</i><cite>功能设定</cite><span class="layui-badge-dot"></span></a></dd>
							<dd pc><a href="javascript:;" class="changeSkin"><i class="layui-icon">&#xe61b;</i><cite>更换皮肤</cite></a></dd>
							<dd><a href="/logon/out" class="signOut"><i class="seraph icon-tuichu"></i><cite>退出</cite></a></dd>
						</dl>
					</li>
				</ul>
			</div>
		</div>
		<!-- 左侧导航 -->
		<div class="layui-side layui-bg-black">
			<!-- 搜索 -->
			<div class="layui-form component" style="margin: 10px 5px;">
				<select name="searchMenus" id="searchMenus" lay-search lay-filter="searchPage">
					<option value="">搜索页面或功能</option>
				</select>
				<i class="layui-icon">&#xe615;</i>
			</div>
			<div class="navBar layui-side-scroll" id="navBar">
				<ul class="layui-nav layui-nav-tree"></ul>
			</div>
		</div>
		<!-- 右侧内容 -->
		<div class="layui-body layui-form" style="bottom:0px;">
			<div class="layui-tab mag0" lay-filter="bodyTab" id="top_tabs_box">
				<ul class="layui-tab-title top_tab" id="top_tabs">
					<li class="layui-this" lay-id=""><i class="layui-icon">&#xe68e;</i> <cite>首页</cite></li>
				</ul>
				<ul class="layui-nav closeBox">
				  <li class="layui-nav-item">
				    <a href="javascript:;"><i class="layui-icon caozuo">&#xe643;</i> 页面操作</a>
				    <dl class="layui-nav-child">
					  <dd><a href="javascript:;" class="refresh refreshThis"><i class="layui-icon">&#x1002;</i> 刷新当前</a></dd>
				      <dd><a href="javascript:;" class="closePageOther"><i class="seraph icon-prohibit"></i> 关闭其他</a></dd>
				      <dd><a href="javascript:;" class="closePageAll"><i class="seraph icon-guanbi"></i> 关闭全部</a></dd>
				    </dl>
				  </li>
				</ul>
				<div class="layui-tab-content clildFrame">
					<div class="layui-tab-item layui-show">
						<iframe src="/index"></iframe>
					</div>
				</div>
			</div>
		</div>
		<!-- 底部 -->
		<div class="layui-footer footer" style="display:none;">
			<p><span>&copy; Dajie 2018</span>　
		</div>
	</div>

	<!-- 移动导航 -->
	<div class="site-tree-mobile"><i class="layui-icon">&#xe602;</i></div>
	<div class="site-mobile-shade"></div>

	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
	<script type="text/javascript" src="/resource/zmh/js/main.js"></script>
</body>
</html>