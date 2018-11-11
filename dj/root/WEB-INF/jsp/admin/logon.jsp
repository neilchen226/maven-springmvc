<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="loginHtml">
<head>
<meta charset="utf-8">
<title>登录--中马航订单管理系统</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="icon" href="/favicon.ico">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css" media="all" />
</head>
<body class="loginBody">
	<form id="logonForm" class="layui-form">
		<div class="login_face">
			<img src="/resource/zmh/img/logon.png" class="userAvatar">
		</div>
		<div class="layui-form-item input-item">
			<label for="userName">用户名</label>
			<input type="text" placeholder="请输入用户名" autocomplete="off" id="userName" class="layui-input" lay-verify="required" name="username">
		</div>
		<div class="layui-form-item input-item">
			<label for="password">密码</label>
			<input type="password" placeholder="请输入密码" autocomplete="off" id="password" class="layui-input" lay-verify="required" name="password">
		</div>
		<div class="layui-form-item input-item" id="imgCode">
			<label>验证码</label>
			<input type="text" placeholder="请输入验证码" autocomplete="off" class="layui-input" lay-verify="required" name="valicode">
			<img id="randomCode" src="/logon/valicode" onclick="this.src='/logon/valicode?_='+new Date().getTime();"  title="点击图片刷新验证码">
		</div>
		<div class="layui-form-item">
			<button class="layui-btn layui-block" lay-filter="loginBtn" lay-submit>登录</button>
		</div>
		<div class="layui-form-item layui-row" style="display:none;">
			<a href="javascript:;" class="seraph icon-qq layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>
			<a href="javascript:;" class="seraph icon-wechat layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>
			<a href="javascript:;" class="seraph icon-sina layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>
		</div>
	</form>
	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
	<script type="text/javascript" src="/resource/zmh/js/logon.js"></script>
	<script type="text/javascript">
		if(top.location != self.location){
			top.location = "/logon";
		}
	</script>
</body>
</html>