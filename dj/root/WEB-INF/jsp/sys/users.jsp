<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>系统用户列表</title>
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
    line-height: 20px;
}
</style>
</head>
<body class="childrenBody">
	<blockquote class="layui-elem-quote quoteBox">
		<form class="layui-form" id="searchForm">
			<div class="layui-inline">
				<div class="layui-input-inline">
					<input type="text" name="username" class="layui-input search_val" placeholder="请输入用户名" />
				</div>
			</div>
			<div class="layui-inline">
				<a class="layui-btn  layui-btn-sm search_btn" data-type="reload">搜索</a>
			</div>
			<div class="layui-inline">
				<button type="reset" class="layui-btn layui-btn-sm layui-btn-danger" data-type="reload">重置</button>
			</div>
			<div class="layui-inline" style="float:right;">
				<a class="layui-btn  layui-btn-sm layui-btn-normal addNews_btn">添加用户</a>
			</div>
		</form>
	</blockquote>
	<table id="userList" lay-filter="userList"></table>
		<!--操作-->
	<script type="text/html" id="userListBar">
			<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
	</script>
</body>
<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<script>
layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;
	
    function getFormDataToJSON(node) {
    	var data = {};
    	var array = $(node).serializeArray();
    	console.info(array)
    	for ( var i = 0; i < array.length; i++) {
    		var item = array[i];
    		if(item.value != '' && item.value != undefined){
    			data[item.name] = item.value;
    		}
    	}
    	return data;
    }
    //用户列表
    var tableUsers = table.render({
        elem: '#userList',
        id : "userListTable",
        url : '/sys/users',
        method: 'post',
        cellMinWidth : 50,
        page : true,
        height : "full-135",
        limits : [20,50,100,200],
        limit : 20,
        initSort:{
            field: 'userid',
            type: 'asc'
        },
        size:"sm",
        cols : [[
            {type: "checkbox",  width:50},
            {field: 'userface', title: '头像', width:60, align:"center",
            	templet:function(d){
                return '<img src="'+d.userface+'" class="layui-nav-img" />';
            }},
            {field: 'userid', title: '登录名',sort:true, align:'center'},
            {field: 'username', title: '用户名',sort:true, align:'center'},
            {field: 'jobname', title: '职位',sort:true, align:'center'},
            {field: 'usernumber', title: '代号',sort:true, align:'center'},
            {title: '工作状态',sort:true, align:'center',templet:function(res){
            	if(res.iswork ==1){
            		return "<a class='layui-btn layui-btn-xs layui-btn-normal'>在职</a>";
            	}else{
            		return "<a class='layui-btn layui-btn-xs layui-btn-danger'>离职</a>";
            	}
            }},
            {title: '操作', width:280, templet:'#userListBar',align:"left"}
        ]]
    });
    // 排序
    table.on('sort(userList)', function(obj){
   	  var data = getFormDataToJSON("#searchForm");
   	  data.sortfield=obj.field;
   	  data.sorttype=obj.type;
   		tableUsers.reload({
   	    initSort: obj ,
   	    where: data
   	  });
    });
	// 选中行背景颜色切换
	table.on('checkbox(dataList)', function(obj){
		$('.layui-form-checkbox').closest("tr").removeClass('chooseLineClass');
		$(".layui-form-checked").closest("tr").addClass('chooseLineClass');
	});
    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit'){ //编辑
        	userInit(data);
        }
    });

    //搜索
    $(".search_btn").on("click",function(){
    	var data = getFormDataToJSON("#searchForm");
       	tableUsers.reload({
               page: {curr: 1},
               where:  data
           });
    });
    

    //添加用户
    function userInit(edit){
    	var url = "/sys/user/init";
    	var title= "添加用户";
    	if(edit){
    		title = "编辑用户";
    		url += "?userid="+edit.userid;
    	}
        var index = layui.layer.open({
            title : title,
            type : 2,
            content : url,
            area:['480px','550px'],
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                form.render();
                setTimeout(function(){
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500);
            }
        });
    }
    $(".addNews_btn").click(function(){
        userInit();
    });
})
</script>
</html>