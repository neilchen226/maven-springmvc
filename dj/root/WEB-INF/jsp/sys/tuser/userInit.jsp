<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户管理</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css" media="all" />
<link rel="stylesheet" href="/resource/zmh/css/zmh.css" media="all" />

<style type="text/css">
.layui-table[lay-size=sm] td, .layui-table[lay-size=sm] th {
   padding: 0px 2px;
}
.layui-table-cell{
height: inherit;
}
</style>
</head>
<body class="childrenBody">
	
<form id="userinfo" class="layui-form layui-row">
	<input type="hidden" name="action" value="${action}">
	<input type="hidden" name="userFace" value="${user.userFace}">
	<div class="layui-col-md3 layui-col-xs12 user_right" style="margin-bottom:20px;">
		<div class="layui-upload-list">
			<c:if test="${not empty user.userFace }">
				<img class="layui-upload-img layui-circle userFaceBtn userAvatar" src="${user.userFace}" id="userFace" style="width:60px; height:60px;">
			</c:if>
			<c:if test="${empty user.userFace }">
				<img class="layui-upload-img layui-circle userFaceBtn userAvatar" id="userFace" style="width:60px; height:60px;">
			</c:if>
		</div>
		<button type="button" class="layui-btn layui-btn-sm layui-btn-primary userFaceBtn">
			<i class="layui-icon">&#xe67c;</i> 选择头像
		</button>
	</div>
	<div class="layui-col-md6 layui-col-xs12">
		<div class="layui-form-item">
			<label class="layui-form-label">
					登录名
					<span class="layui-badge-dot" style="position: relative; top: -3px; left: 5px;" title="必填项"></span>
				</label>
			<div class="layui-input-block">
				<c:if test="${not empty user.userid }">
				<input type="text" name="userid" value="${user.userid }" disabled class="layui-input layui-disabled" lay-verify="username|required">
				</c:if>
				<c:if test="${empty user.userid }">
				<input type="text" name="userid" value="${user.userid }" class="layui-input" lay-verify="username|required">
				</c:if>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">
					用户组
					<span class="layui-badge-dot" style="position: relative; top: -3px; left: 5px;" title="必填项"></span>
				</label>
			<div class="layui-input-block"  style="width:300px">
				<select name="roleid"  lay-verify="required" class="layui-input" style="width:300px">
				<option value="">请选择用户组</option>
				<c:forEach items="${roles }" var="i">
				<option value="${i.roleid }" <c:if test="${i.roleid == user.roleid }"> selected</c:if>>${i.rolename}</option>
				</c:forEach>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">用户名</label>
			<div class="layui-input-block">
				<input type="text" name="username" value="${user.username }" placeholder="请输入姓名" lay-verify="required" class="layui-input">
			</div>
		</div>
		<c:if test="${not empty user.userid }">
		<div class="layui-form-item">
			<label class="layui-form-label">重置密码</label>
			<div class="layui-input-block"  id="changePassSwitch">
				<input type="checkbox" name="changePassFlag" lay-filter="changePass" lay-skin="switch" value="1" lay-text="是|否">
			</div>
		</div>
		</c:if>
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="saveUser">保存</button>
				<button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">重置</button>
			</div>
		</div>
	</div>
</form>
	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
</body>
<script>
	layui.use([ 'form', 'layer', 'element','upload','laydate' ], function() {
		var form = layui.form;
		var upload = layui.upload;
		var laydate = layui.laydate;
		var element = layui.element;
		var layer = parent.layer === undefined ? layui.layer : top.layer;
		var $ = layui.jquery;

	    upload.render({
	        elem: '.userFaceBtn',
	        url: '/sys/tuser/uploadUserFace',
	        method : "post",  
	        field:'userFaceField', // 指定该文件上传的字段名
	        accept: "images",
	        done: function(res, index, upload){
	        	if(res.status == 200){
	           		$('.layui-circle,.uploadUserFace').attr('src',res.userFaceUrl);
	           		$('#userinfo input[name=userFace]').val(res.userFaceUrl);
	        	}else{
	        		layer.msg(res.message);
	        	}
	        },
	        error:function(index, upload){
	        	layer.msg("上传失败，请稍后再试!");
	        }
	    });
	    form.verify({
	    	  username: function(value, item){ //value：表单的值、item：表单的DOM对象
    		  if(value.length<3 || value.length>16){
	    	    	return "请输入3~16个字符之间的字符,不可包含中文";
	    	    }
	    	    if(!new RegExp("^[a-zA-Z0-9\s·]+$").test(value)){
	    	      return '登录名不能有中文和特殊字符';
	    	    }
	    	    if(/(^\_)|(\__)|(\_+$)/.test(value)){
	    	      return '登录名首尾不能出现下划线\'_\'';
	    	    }
	    	    if(/^\d+\d+\d$/.test(value)){
	    	      return '登录名不能全为数字';
	    	    }
	    	   
	    	  }
	    	});  
		// 保存
		form.on('submit(saveUser)', function(data) {
			if(data.field.ispda ==undefined){
				data.field.ispda = 0;
			}
			console.info("提交数据： " +JSON.stringify(data.field));
			$.post("/sys/tuser/userSave",data.field,function(res){
					layer.msg(res.message,{time:2000},function(){
					if(res.status==200){
			            layer.closeAll("iframe");
			            parent.location.reload();  //刷新父页面
					}
				});
			},"json");
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
	});
</script>
</html>