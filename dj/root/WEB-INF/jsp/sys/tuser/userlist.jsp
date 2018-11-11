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
<link rel="stylesheet" href="/resource/zmh/css/zmh.css" media="all" />
<style type="text/css">
.layui-table-view .layui-table[lay-size=sm] .layui-table-cell {
   height: inherit;
    line-height: 20px;
}
</style>
</head>
<body class="childrenBody">
	<div>
		<div class="layui-row">
			<form  id="searchForm" class="layui-form">
			<div class="layui-form-item">
				<div class="layui-inline">
					<div class="layui-input-inline">
						<input type="text" name="userid" class="layui-input searchVal" placeholder="请输入用户名" />
					</div>
					<a class="layui-btn layui-btn-sm layui-btn-normal search_btn" data-type="reload">查询</a>
					<button type="reset" class="layui-btn layui-btn-sm layui-btn-danger" data-type="reload">重置</button>
				</div>
			</div>
			</form>
		</div>
		<div class="layui-row">
			<div class="layui-btn-group">
			  <button class="layui-btn layui-btn-sm addNews_btn">增加</button>
			  <button style="display:none;" class="layui-btn layui-btn-sm layui-btn-danger  delAll_btn">删除</button>
			</div>
		</div>
		<table id="userList" lay-filter="userList"></table>
			<!--操作-->
		<script type="text/html" id="userListBar">
			<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		</script>
	</div>
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
        url : '/sys/tuser/userlist',
        method: 'post',
        cellMinWidth : 50,
        page : true,
        height : "full-95",
        limits : [20,50,100,200],
        limit : 20,
        initSort:{
            field: 'userid',
            type: 'asc'
        },
        size:"sm",
        cols : [[
            {type: "checkbox",  width:50},
            {field: 'userFace', title: '头像', width:60, align:"center",
            	templet:function(d){
                return '<img src="'+d.userFace+'" class="layui-nav-img" />';
            }},
            {field: 'userid', title: '登录名',sort:true, minWidth:200, align:'center'},
            {field: 'username', title: '用户名',sort:true, minWidth:200, align:'center'},
            {field: 'rolename', title: '用户组',sort:true, minWidth:200, align:'center'},
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
        if($(".searchVal").val() != ''){
        	tableUsers.reload({
                page: {curr: 1},
                where:  getFormDataToJSON("#searchForm")
            });
        }else{
            layer.msg("请输入搜索的内容");
        }
    });
    

    //添加用户
    function userInit(edit){
    	var url = "/sys/tuser/userinit";
    	var title= "添加用户";
    	if(edit){
    		title = "编辑用户";
    		url += "?userid="+edit.userid;
    	}
        var index = layui.layer.open({
            title : title,
            type : 2,
            content : url,
            area:['520px','520px'],
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
//         layui.layer.full(index);
//         $(window).on("resize",function(){
//             layui.layer.full(index);
//         })
    }
    $(".addNews_btn").click(function(){
        userInit();
    });

    //批量删除
    $(".delAll_btn").click(function(){
    	 layer.msg("暂时未开放删除用户功能");
    	 return ;
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].userid);
            }
            layer.confirm('确定删除选中的用户？<br/>'+newsId, {icon: 3, title: '提示信息'}, function (index) {
                tableUsers.reload();
                layer.close(index);
            });
        }else{
            layer.msg("请选择需要删除的用户");
        }
    });
    //批量删除
    $(".delAll_btn").click(function(){
    	 layer.msg("暂时未开放删除用户功能");
    	 return ;
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].userid);
            }
            layer.confirm('确定删除选中的用户？<br/>'+newsId, {icon: 3, title: '提示信息'}, function (index) {
                tableUsers.reload();
                layer.close(index);
            });
        }else{
            layer.msg("请选择需要删除的用户");
        }
    });
})
</script>
</html>