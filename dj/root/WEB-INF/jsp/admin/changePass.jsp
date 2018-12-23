<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>修改密码</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css" media="all" />
</head>
<body class="childrenBody">
	<form class="layui-form layui-row changePwd">
		<div class="layui-col-xs12 layui-col-sm6 layui-col-md6" style="margin-top:40px;">
			<div class="layui-form-item">
				<label class="layui-form-label">用户名</label>
				<div class="layui-input-block">
					<input type="text" value="${user.username}" disabled class="layui-input layui-disabled">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">旧密码</label>
				<div class="layui-input-block">
					<input type="password" name="oldpass" value="" placeholder="请输入旧密码" autocomplete="off" lay-verify="required|oldPwd" class="layui-input pwd">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">新密码</label>
				<div class="layui-input-block">
					<input type="password" name="newpass" value="" placeholder="请输入新密码" autocomplete="off" lay-verify="required|newPwd" id="newPwd" class="layui-input pwd">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">确认密码</label>
				<div class="layui-input-block">
					<input type="password" value="" placeholder="请确认密码" autocomplete="off" lay-verify="required|confirmPwd" class="layui-input pwd">
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="changePwd">立即修改</button>
					<button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">重置</button>
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
	<script>
	layui.use(['form','layer','laydate','table','laytpl'],function(){
	    var form = layui.form,
	        layer = parent.layer === undefined ? layui.layer : top.layer,
	        $ = layui.jquery,
	        laydate = layui.laydate,
	        laytpl = layui.laytpl,
	        table = layui.table;

	    //添加验证规则
	    form.verify({
	        newPwd : function(value, item){
	            if(value.length < 6){
	                return "密码长度不能小于6位";
	            }
	        },
	        confirmPwd : function(value, item){
	            if(!new RegExp($("#newPwd").val()).test(value)){
	                return "两次输入密码不一致，请重新输入！";
	            }
	        }
	    });
	    form.on('submit(changePwd)', function(data) {
			console.info("提交数据： " +JSON.stringify(data.field));
			$.post("/sys/user/changePass",data.field,function(res){
					layer.msg(res.message,{time:2000},function(){
					if(res.status==200){
			           
					}
				});
			},"json");
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
	    
	    
	});
	
	
	</script>
</body>
</html>