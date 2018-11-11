<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户组管理</title>
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
	<form class="layui-form">
		<div class="layui-row">
			用户组信息设置
			<hr class="layui-bg-red">
			<div class="layui-form-item  layui-col-xs12">
				<input type="hidden" name="action" value="${action}">
				<label class="layui-form-label" style="width: 100px;">
					用户组编码
					<span class="layui-badge-dot" style="position: relative; top: -3px; left: 5px;" title="必填项"></span>
				</label>
				<div class="layui-input-block">
					<c:if test="${not empty role.roleid }">
						<input type="text" name="roleid" class="layui-input layui-disabled" lay-verify="required" placeholder="请输入用户组编码" value="${role.roleid }" readonly>
					</c:if>
					<c:if test="${empty role.roleid }">
						<input type="text" name="roleid" class="layui-input" lay-verify="required" placeholder="请输入用户组编码" value="${role.roleid }">
					</c:if>
				</div>
			</div>
			<div class="layui-form-item layui-col-xs12">
				<label class="layui-form-label"  style="width: 100px;">
					用户组名称
					<span class="layui-badge-dot" style="position: relative; top: -3px; left: 5px;" title="必填项"></span>
				</label>
				<div class="layui-input-block">
					<input type="text" name="rolename" class="layui-input" lay-verify="required" placeholder="请输入名称" value="${role.rolename }">
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-form-item layui-col-xs12">
					<label class="layui-form-label"  style="width: 100px;">说明</label>
					<div class="layui-input-block">
						<textarea placeholder="请输入说明" name="roledesc" class="layui-textarea">${role.roledesc }</textarea>
					</div>
				</div>
			</div>
			用户组权限设置
			<hr class="layui-bg-red">
			<c:forEach items="${tmoduleTree}" var="item">
				<div class="layui-row">
					<div class="layui-form-item layui-col-xs12">
						<label class="layui-form-label">
							<b>${item.title }</b>
						</label>
						<div class="layui-input-block tmodule_level1" data-parentid="${item.moduleid}">
							<c:if test="${not empty item.children }">
								<c:forEach items="${item.children}" var="childitem">
									<input class="tmodule_level2" type="checkbox" value="${childitem.moduleid }" title="${childitem.title }" <c:if test="${childitem.hasRole }"> checked</c:if>>
								</c:forEach>
							</c:if>
						</div>
					</div>
				</div>
			</c:forEach>
			<div class="layui-input-block">
				<button class="layui-btn layui-btn-sm layui-btn-normal" lay-filter="saveRole" lay-submit>保存</button>
				<button type="reset" class="layui-btn layui-btn-sm layui-btn-danger">取消</button>
			</div>
		</div>
	</form>
	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
</body>
<script>
	layui.use([ 'form', 'layer', 'element' ], function() {
		var form = layui.form;
		var element = layui.element;
		var layer = parent.layer === undefined ? layui.layer : top.layer;
		var $ = layui.jquery;

		// 保存
		form.on('submit(saveRole)', function(data) {
			var moduleids = '';
			$(".tmodule_level1").each(function() {
				var node = $(this);
				var parentid = $(this).attr("data-parentid");
				var checkedbox = node.find(".tmodule_level2:checked");
				checkedbox.each(function() {
					moduleids+=($(this).val())+",";
				});
				var selected = checkedbox.length;
				if (selected > 0) {
					moduleids+=(parentid)+",";
				}
			});
			if(moduleids == ''){
				layer.msg("请勾选用户组权限");
				return false;
			}
			data.field.moduleid = moduleids;
			console.info("提交数据： " +JSON.stringify(data.field));
			$.post("/sys/tuser/roleSave",data.field,function(res){
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