<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>夏印设计表现订单管理系统</title>
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
</style>
</head>
<body class="main_body">
	<div class="layui-layout layui-layout-admin">
		<!-- 顶部 -->
		<div class="layui-header header">
			<div class="layui-main mag0">
				<a href="javascript:void(0);" class="logo">
					<img src="/resource/img/logo.jpeg" style="height: 48px; line-height: 60px;">
				</a>
				<!-- 顶部菜单 -->
				<ul class="layui-nav topLevelMenus">
					<li class="layui-nav-item layui-this">
						<a href="javascript:;" data-url="/orders"><i class="layui-icon" data-icon="&#xe63c;">&#xe63c;</i><cite>订单列表</cite></a>
					</li>
					<c:if test="${user.jobname == 'admin'}">
					<li class="layui-nav-item">
						<a  href="javascript:;" data-url="/sys/users"><i class="seraph icon-icon10" data-icon="icon-icon10"></i><cite>用户中心</cite></a>
					</li>
					<li class="layui-nav-item">
						<a href="javascript:;" data-url="/sys/workrecord"><i class="layui-icon" data-icon="&#xe620;">&#xe620;</i><cite>考勤</cite></a>
					</li>
					</c:if>
				</ul>
			    <!-- 顶部右侧菜单 -->
			    <ul class="layui-nav top_menu">
					<li class="layui-nav-item" style="width:100px;" id="playFirstStatus">
						<c:if test="${firstStatus == '1'}">
							<a href='javascript:;' style="padding: 0 5px;" class="layui-badge layui-bg-gray">已上班打卡</a>
						</c:if> 
						<c:if test="${firstStatus == '0'}">
							<a href='javascript:;' style="padding: 0 5px;"  class="layui-badge" >上班打卡</a>
						</c:if>
					</li>
					<li class="layui-nav-item" style="width:100px;" id="playLastStatus">
						<c:if test="${lastStatus == '1'}">
							<a href='javascript:;' style="padding: 0 5px;" class="layui-badge layui-bg-gray" >已下班打卡</a>
						</c:if>
						<c:if test="${lastStatus == '0'}">
							<a href='javascript:;' style="padding: 0 5px;"  class="layui-badge">打卡下班</a>
						</c:if>
					</li>
					<li class="layui-nav-item">
						<a href="javascript:;" class="nav_btn refresh refreshThis" title="刷新当前"><i class="layui-icon">&#x1002;</i>刷新</a>
					</li>
					<li class="layui-nav-item" id="userInfo">
						<a href="javascript:;">
							<img src="${user.userface }" class="layui-nav-img userAvatar" width="35" height="35">
							<cite class="userName">${user.username }</cite>
						</a>
						<dl class="layui-nav-child">
							<c:if test="${deleteOrderCount>0 }">
								<dd id="deleteOrderCount">
									<a href="javascript:;"><i class="layui-icon">&#xe640;</i><cite>有新废弃订单</cite><span class="layui-badge-dot"></span></a>
								</dd>
							</c:if>
							<dd><a href="javascript:;" data-url="/sys/user/changeUserInfo"><i class="seraph icon-ziliao" data-icon="icon-ziliao"></i><cite>个人资料</cite></a></dd>
							<dd><a href="javascript:;" data-url="/sys/user/changePass"><i class="seraph icon-xiugai" data-icon="icon-xiugai"></i><cite>修改密码</cite></a></dd>
							<dd style="display:none;"><a href="javascript:;" class="changeSkin"><i class="layui-icon">&#xe61b;</i><cite>更换皮肤</cite></a></dd>
							<dd><a href="/logon/out" class="signOut"><i class="seraph icon-tuichu"></i><cite>退出</cite></a></dd>
						</dl>
					</li>
				</ul>
			</div>
		</div>
		<!-- 右侧内容 -->
		<div class="layui-body layui-form" style="bottom:0px;left:0px;">
			<div class="layui-tab mag0" lay-filter="bodyTab" id="top_tabs_box">
				<ul class="layui-tab-title top_tab" id="top_tabs">
					<li class="layui-this" lay-id=""><i class="layui-icon">&#xe63c;</i> <cite>订单列表</cite></li>
				</ul>
				<div class="layui-tab-content clildFrame">
					<div class="layui-tab-item layui-show">
						<iframe src="/sys/orders"></iframe>
					</div>
				</div>
			</div>
		</div>
		<!-- 底部 -->
		<div class="layui-footer footer" style="left:0px;">
			<p><span>&copy; 夏印设计表现 2018</span>　
		</div>
	</div>


	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
	<script type="text/javascript" src="/resource/js/main.js"></script>
	<script type="text/javascript">

	layui.use(['form','element','layer','jquery'],function(){
		var form = layui.form,
			element = layui.element,
	        layer = parent.layer === undefined ? layui.layer : top.layer,
			$ = layui.$;
		
		$("#playFirstStatus").click(function(){
			var node = $(this);
			$.get("/sys/user/signFirst",function(res){
				layer.msg(res.message)
				if(res.status == 200 || res.status == 300){
					node.html("<a href='javascript:;' style='padding: 0 5px;' class='layui-badge layui-bg-gray'>已上班打卡</a>");
				}else{
					setTimeOut(function(){window.href = '/logon';},2000);
				}
			},'json');
		});	
		
		$("#playLastStatus").click(function(){
			var node = $(this);
			$.get("/sys/user/signLast",function(res){
				layer.msg(res.message)
				if(res.status == 200 || res.status == 300){
					node.html("<a href='javascript:;' style='padding: 0 5px;' class='layui-badge layui-bg-gray'>已下班打卡</span>");
				}else{
					setTimeOut(function(){window.location.reload();},2000);
				}
			},'json');
		});	
		$("#deleteOrderCount").click(function(){
			var node = $(this);
			node.remove();
			var html = "<a href='javascript:;' data-url='/sys/deleteOrders'><i class='lyui-icon'></i><cite>废弃订单</cite></a>";
			addTab($(html));
		});	
			
	});
	
	</script>
</body>
</html>