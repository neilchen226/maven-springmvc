<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>日志记录</title>
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
<link rel="stylesheet" href="/resource/zmh/css/zmh.css" media="all" />
</head>
 <body class="childrenBody">
	<div>
		<div class="layui-row">
			<form id="searchForm" class="layui-form">
				<div class="layui-row">
					<div class="layui-col-xs12 layui-col-sm4 layui-col-md3  layui-col-lg2">
						<div class="layui-form-item">
							<label class="layui-form-label">用户</label>
							<div class="layui-input-block">
								<input type="text" name="userid" class="layui-input" placeholder="请输入用户" />
							</div>
						</div>
					</div>
					<div class="layui-col-xs12 layui-col-sm4 layui-col-md3  layui-col-lg2">
						<div class="layui-form-item">
							<label class="layui-form-label">日志类型</label>
							<div class="layui-input-block">
								<select name="logtype" class="layui-input" layui-search>
								<option value="">选择类型</option>
								<c:forEach items="${logtypes }" var="item">
									<option value="${item.code }">${item.name }</option>
								</c:forEach>
							</select>
							</div>
						</div>
					</div>
					<div class="layui-col-xs12 layui-col-sm4 layui-col-md3  layui-col-lg2">
						<div class="layui-form-item">
							<label class="layui-form-label">开始时间</label>
							<div class="layui-input-block">
								<input type="text" name="starttime" autocomplete="off" class="layui-input laydate-item" placeholder="开始时间">
							</div>
						</div>
					</div>
					<div class="layui-col-xs12 layui-col-sm4 layui-col-md3  layui-col-lg2">
						<div class="layui-form-item">
							<label class="layui-form-label">结束</label>
							<div class="layui-input-block">
								<input type="text" name="endtime" autocomplete="off" class="layui-input laydate-item" placeholder="结束时间">
							</div>
						</div>
					</div>
					<div class="layui-col-xs12 layui-col-sm4 layui-col-md3  layui-col-lg2">
						<div class="layui-form-item" style="padding-left: 20px;">
							<a class="layui-btn layui-btn-sm layui-btn-normal search_btn" data-type="reload">
								<i class="layui-icon">&#xe615;</i>
								查询
							</a>
							<button type="reset" class="layui-btn layui-btn-sm layui-btn-danger" data-type="reload">
								<i class="layui-icon">&#xe666;</i>
								重置
							</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<table id="dataList" lay-filter="dataList"></table>
	</div>
</body>
<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<script>
layui.use(['form', 'layer', 'table', 'laytpl', 'element','laydate' ],function() {
	var form = layui.form;
	var layer = parent.layer === undefined ? layui.layer : top.layer;
	var $ = layui.jquery;
	var laytpl = layui.laytpl;
	var table = layui.table;
	var element = layui.element;
	var laydate = layui.laydate;

	var date = new Date();
	var max_date = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date
			.getDate();
	lay('.laydate-item').each(function() {
		laydate.render({
			elem : this,
			trigger : 'click',
			max : max_date,
		});
	});

	
    function getFormDataToJSON(node) {
    	var data = {};
    	var array = $(node).serializeArray();
    	for ( var i = 0; i < array.length; i++) {
    		var item = array[i];
    		if(item.value != '' && item.value != undefined){
    			data[item.name] = item.value;
    		}
    	}
    	return data;
    }
    
    var dataTable = table.render({
        elem: '#dataList',
        id : "dataTable",
        url : location.href,
        method: 'post',
        cellMinWidth : 50,
        page : true,
        height : "full-175",
        limits : [20,50,100,200],
        limit : 20,
        initSort:{
            field: 'editdate',
            type: 'desc'
        },
        size:"sm",
        cols : [[
            {fixed:"left",type: "checkbox", width:50},
            {field: 'userid', title: '用户名',sort:true, align:'center', width:200},
            {field: 'logtype', title: '日志类型', align:'center', width:200},
            {field: 'logip', title: 'ip', align:'center', width:200},
            {field: 'editdate', title: '操作日期',sort:true, align:'center', width:200},
            {field: 'note', title: '操作说明',   align:'center', minWidth: 250},
        ]]
    });
    // 排序
    table.on('sort(dataList)', function(obj){
   	  var data = getFormDataToJSON("#searchForm");
   	  data.sortfield=obj.field;
   	  data.sorttype=obj.type;
   		dataTable.reload({
   	    initSort: obj ,
   	    where: data
   	  });
    });
    
    
    //搜索
    $(".search_btn").on("click",function(){
       	dataTable.reload({
               page: {curr: 1},
               where:  getFormDataToJSON("#searchForm")
           });
    });
    

});
</script>
</html>