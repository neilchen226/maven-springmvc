<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css"
	media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css"
	media="all" />

<style type="text/css">
.layui-table[lay-size=sm] td, .layui-table[lay-size=sm] th {
	padding: 0px 2px;
}
.layui-table tr th{
	font-weight: bold;
}
.layui-table-cell {
	height: inherit;
}

.layui-form-label {
	padding: 0px;
}
</style>
</head>
<body class="childrenBody" style="padding:0;">
	<div class="layui-container" style="padding:0;">
		<div class="layui-collapse" style="padding:0;">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title layui-bg-green">订单详情</h2>
				<div class="layui-colla-content layui-show" style="padding:0">
					<table class="layui-table" lay-size='md'  style="margin:0">
						<tr>
							<th>客户名称</th>
							<td>${order.client }</td>
							<th>项目名称</th>
							<td>${order.projectname }</td>
							<th>当前状态</th>
							<td>
								<c:if test="${order.status==0 }">
								<span class="layui-badge layui-bg-orange">未完成</span>
								</c:if>
								<c:if test="${order.status!=0 }">
								<span class="layui-badge layui-bg-blue">已完成</span>
								</c:if>
							</td>
						</tr>
						<tr>
							<th>制图员</th>
							<td>${order.userid }</td>
							<th>图数</th>
							<td>
								<span title='视频图${order.vmap }张'>${order.vmap }+</span>
								<span title='静态图${order.smap }张'>${order.smap }</span>
							</td>
							<th>总费用</th>
							<td>${order.cost }</td>
						</tr>
						<tr>
							<th>定金</th>
							<td>${order.precost }</td>
							<th>已付费用</th>
							<td>${order.paycost }</td>
							<th>支付方式</th>
							<td>${order.payway }</td>
						</tr>
						<tr>
							<th>对图时间</th>
							<td>${order.checkdate }</td>
							<th>创建人</th>
							<td>${order.editor }</td>
							<th>创建时间</th>
							<td>${order.editdate }</td>
						</tr>
						<c:if test="${order.isdelete != 0 }">
							<tr>
								<th>作废时间</th>
								<td>${order.deletedate }</td>
								<th colspan="4"  style="color:red; font-weight: bold;">该订单已被删除</th>
							</tr>
						</c:if>
					</table>
				</div>
			</div>
			<div class="layui-colla-item">
				<h2 class="layui-colla-title layui-bg-green">操作历史</h2>
				<div class="layui-colla-content"  style="padding:0;">
					<table class="layui-table" lay-size="sm" style="margin:0">
						<tr>
							<th style="text-align: center; width:140px;">处理时间</th>
							<th style="text-align: center; width: 60px;">处理人</th>
							<th style="text-align: center;">处理信息</th>
						</tr>
						<c:forEach var="item" items="${records }">
							<tr>
								<td style="text-align: center;">${fn:substring(item.editdate,0,19) }</td>
								<td style="text-align: center;">${item.editor}</td>
								<td>${item.note }</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
</body>
<script>
	layui.use([ 'element' ], function() {
		var element = layui.element;

	});
</script>
</html>