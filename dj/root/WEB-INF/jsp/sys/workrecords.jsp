<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>考勤</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="icon" href="favicon.ico">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css" media="all" />
<style type="text/css">
.layui-table-view .layui-table[lay-size=sm] .layui-table-cell {
   height: inherit;
}
.layui-table[lay-size=sm] td{
    font-size: 12px;
    padding: 2px 5px;
	min-height: 10px;
	line-height: 1.2em;
	min-width: 100px;
	max-width: 160px;
}
.layui-table[lay-size=sm] tr:nth-child(1){
    text-align:center;
}
.layui-table[lay-size=sm] tr td:first-child{ 
    text-align:center;
}
</style>
</head>
<body class="childrenBody" style="margin-bottom:38px;">
		<form class="layui-form" id="searchForm" action="/sys/workrecord" method="post">
			<div class="layui-inline">月份: </div>
			<div class="layui-inline">
				<div class="layui-input-inline">
					<input type="text" name="createdate" class="layui-input laydate-item" readonly style='height:28px;' autocomplete="off"
						value="${nowMonth}" />
				</div>
			</div>
			<div class="layui-inline">
				<button class="layui-btn layui-btn-sm search_btn" type="submit">查询</button>
			</div>
			<div class="layui-inline" style="float: right;">
				<a class="layui-btn layui-btn-sm layui-btn-normal export_btn">导出考勤记录</a>
			</div>

		</form>
	<c:if test="${not empty workDatas }">
		<table class="layui-table" lay-size="sm" lay-even >
			<c:forEach var="item" items="${workDatas }">
				<tr>
					<c:forEach var="i" items="${item }">
						<td style="
							<c:if test="${fn:contains(i,'迟到') || fn:contains(i,'早退') }"> color: #ff8200; </c:if>">
							${i}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<div style="display:none;">
		<iframe name="exportExcel"></iframe>
		<form target="exportExcel" id="exportExcel" action='/sys/workrecord/export' method="post"></form>
	</div>
</body>
<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<script>
layui.use(['form','layer','table','laytpl', 'laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        laydate = layui.laydate,
        table = layui.table;

	lay('.laydate-item').each(function() {
		laydate.render({
			elem : this,
			format: "yyyy-MM",
			type: 'month',
			theme: "#ff8200"
		});
	});

	$(".export_btn").click(function(){
		var createdate = $("#searchForm input[name=createdate]").val();
		var html = "";
		html +="<input name='createdate' type='hidden' value='"+createdate+"' />";
		$("#exportExcel").html(html);
		$("#exportExcel").submit();
		
	});
})
</script>
</html>