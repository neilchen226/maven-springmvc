<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>个人资料</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css" media="all" />
<link rel="stylesheet" href="/resource/zmh/css/zmh.css" media="all" />
</head>
<body class="childrenBody">

	<form id="userinfo" class="layui-form layui-row">
		<input type="hidden" name="userFace" value="${user.userFace}">
		<div class="layui-col-md3 layui-col-xs12 user_right">
			<div class="layui-upload-list">
				<c:if test="${not empty user.userFace }">
					<img class="layui-upload-img layui-circle userFaceBtn userAvatar" src="${user.userFace}" id="userFace">
				</c:if>
				<c:if test="${empty user.userFace }">
					<img class="layui-upload-img layui-circle userFaceBtn userAvatar" id="userFace">
				</c:if>
			</div>
			<button type="button" class="layui-btn  layui-btn-sm layui-btn-primary userFaceBtn">
				<i class="layui-icon">&#xe67c;</i>
				选择头像
			</button>
		</div>
		<div class="layui-col-md6 layui-col-xs12" style="margin-top: 40px;">
			<div class="layui-form-item">
				<label class="layui-form-label">用户名</label>
				<div class="layui-input-block">
					<input type="text" value="${user.userid }" disabled class="layui-input layui-disabled">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">用户组</label>
				<div class="layui-input-block">
					<input type="text" value="${role.rolename }" disabled class="layui-input layui-disabled">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">姓名</label>
				<div class="layui-input-block">
					<input type="text" name="username" value="${user.username }" placeholder="请输入姓名" lay-verify="required" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="saveUser">保存</button>
					<button type="reset" class="layui-btn layui-btn-primary layui-btn-sm">重置</button>
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
</body>
<script>
	layui.use([ 'form', 'layer', 'element', 'upload', 'laydate' ], function() {
		var form = layui.form;
		var upload = layui.upload;
		var laydate = layui.laydate;
		var element = layui.element;
		var layer = parent.layer === undefined ? layui.layer : top.layer;
		var $ = layui.jquery;

		upload.render({
			elem : '.userFaceBtn',
			url : '/sys/tuser/uploadUserFace',
			method : "post",
			field : 'userFaceField', // 指定该文件上传的字段名
			accept : "images",
			done : function(res,index,upload) {
				if (res.status == 200) {
					$('.layui-circle,.uploadUserFace').attr('src', res.userFaceUrl);
					$('#userinfo input[name=userFace]').val(res.userFaceUrl);
				} else {
					layer.msg(res.message);
				}
			},
			error : function(index,upload) {
				layer.msg("上传失败，请稍后再试!");
			}
		});

		// 保存
		form.on('submit(saveUser)', function(data) {
			console.info("提交数据： " + JSON.stringify(data.field));
			$.post("/changeUserInfo", data.field, function(res) {
				layer.msg(res.message, {
					time : 2000
				}, function() {
					if (res.status == 200) {

					}
				});
			}, "json");
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
	});
</script>
</html>