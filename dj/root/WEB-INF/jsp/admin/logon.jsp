<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="loginHtml">
<head>
<meta charset="utf-8">
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
		<div class="login_face" style="width: 180px; height: 105px; border-radius: 10px;">
			<img src="/resource/img/logo.jpeg" class="userAvatar">
		</div>
		<div class="layui-form-item input-item">
			<label for="userName">登录名</label>
			<input type="text" placeholder="请输入登录名" autocomplete="off" id="userName" class="layui-input" lay-verify="required" name="userid">
		</div>
		<div class="layui-form-item input-item">
			<label for="password">密码</label>
			<input type="password" placeholder="请输入密码" autocomplete="off" id="passs" class="layui-input" lay-verify="required" name="pass">
		</div>
		<div class="layui-form-item input-item" id="imgCode">
			<label>验证码</label>
			<input type="text" placeholder="请输入验证码" autocomplete="off" class="layui-input" lay-verify="required" name="valicode">
			<img id="randomCode" src="/logon/valicode" onclick="this.src='/logon/valicode?_='+new Date().getTime();"  title="点击图片刷新验证码">
		</div>
		<div class="layui-form-item">
			<button class="layui-btn layui-block" lay-filter="loginBtn" lay-submit style="background-color: #333;">登录</button>
		</div>
		<div class="layui-form-item" style="font-size:12px; font-style: italic; color:#666; text-align:right;">
			<span>忘记密码请联系管理员重置</span>
		</div>
		<div class="layui-form-item layui-row" style="display:none;">
			<a href="javascript:;" class="seraph icon-qq layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>
			<a href="javascript:;" class="seraph icon-wechat layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>
			<a href="javascript:;" class="seraph icon-sina layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>
		</div>
	</form>
	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
	<script type="text/javascript">
		if(top.location != self.location){
			top.location = "/logon";
		}
		layui.use(['form','layer','jquery'],function(){
		    var form = layui.form,
		        layer = parent.layer === undefined ? layui.layer : top.layer;
		    var $ = layui.jquery;
		        
		    //登录
		    form.on("submit(loginBtn)",function(data){
		    	var btn = $(this);
		    	btn.text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
		    	var datas = $("#logonForm").serialize();
		    	$.post("/logon/check",data.field, function(e){
		    		layer.msg(e.message,{time: 1000},function(){
		    			if(200!= e.status){
			    	    	btn.text("登录").removeAttr("disabled","disabled").removeClass("layui-disabled");
		    				$("#randomCode").attr("src","/logon/valicode?_="+new Date().getTime());
		    				$("input[valicode]").val();
		    			}else{
		    				if(e.userinfo != undefined){
		    					window.sessionStorage.setItem("userInfo",JSON.stringify(e.userinfo));// 保存用户信息	
		    				}
		    				location.href="/main";
		    			}
		    		});
		    	},"json");
		        return false;
		    });

		    //表单输入效果
		    $(".loginBody .input-item").click(function(e){
		        e.stopPropagation();
		        $(this).addClass("layui-input-focus").find(".layui-input").focus();
		    });
		    $(".loginBody .layui-form-item .layui-input").focus(function(){
		        $(this).parent().addClass("layui-input-focus");
		    });
		    
		    $(".loginBody .layui-form-item .layui-input").blur(function(){
		        $(this).parent().removeClass("layui-input-focus");
		        if($(this).val() != ''){
		            $(this).parent().addClass("layui-input-active");
		        }else{
		            $(this).parent().removeClass("layui-input-active");
		        }
		    });
		});
	</script>
</body>
</html>