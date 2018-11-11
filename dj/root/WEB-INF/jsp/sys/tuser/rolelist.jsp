<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户组管理</title>
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
						<label class="layui-form-label">用户组编码</label>
						<div class="layui-input-inline">
							<input type="text" name="roleid"  autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">用户组名</label>
						<div class="layui-input-inline">
							<input type="text" name="rolename" autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<a class="layui-btn layui-btn-sm layui-btn-normal search_btn" data-type="reload">查询</a>
						<button type="reset" class="layui-btn layui-btn-sm layui-btn-danger" data-type="reload">重置</button>
					</div>
				</div>
			</form>
		</div>
		<div class="layui-row">
			<div class="layui-btn-group">
			  <button class="layui-btn layui-btn-sm addNews_btn">增加</button>
			  <button class="layui-btn layui-btn-sm layui-btn-danger  delAll_btn">删除</button>
			</div>
		</div>
		<table id="roleList" lay-filter="roleList"></table>
			<!--操作-->
		<script type="text/html" id="roleListBar">
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
    var tableRoles = table.render({
        elem: '#roleList',
        id : "roleListTable",
        url : '/sys/tuser/rolelist',
        method: 'post',
        cellMinWidth : 50,
        page : true,
        height : "full-90",
        limits : [20,50,100,200],
        limit : 20,
        initSort:{
            field: 'roleid',
            type: 'asc'
        },
        size:"sm",
        cols : [[
            {type: "checkbox",  width:50},
            {field: 'roleid', title: '用户组编码',sort:true, width:200, align:'center'},
            {field: 'rolename', title: '用户组名',sort:true, align:'center'},
            {field: 'roledesc', title: '说明',sort:true, align:'center'},
            {title: '操作', width:120, templet:'#roleListBar',align:"center"}
        ]]
    });
    // 排序
    table.on('sort(roleList)', function(obj){
   	  var data = getFormDataToJSON("#searchForm");
   	  data.sortfield=obj.field;
   	  data.sorttype=obj.type;
   		tableRoles.reload( {
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
    table.on('tool(roleList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit'){ //编辑
        	initRole(data);
        }
    });

    //搜索
    $(".search_btn").on("click",function(){
       	tableRoles.reload({
             page: {curr: 1},
             where:  getFormDataToJSON("#searchForm")
         });
    });
    
    //添加用户
    function initRole(edit){
    	var initurl = "/sys/tuser/roleinit";
    	var title = "添加用户组";
    	if(edit){
    		initurl += "?roleid="+edit.roleid;
    		title = "修改用户组";
    	}
        var initRolePanel = layui.layer.open({
            title : title,
            type : 2,
            content : initurl,
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
        layui.layer.full(initRolePanel);
        $(window).on("resize",function(){
            layui.layer.full(initRolePanel);
        });
    }
    
    $(".addNews_btn").click(function(){
    	initRole();
    });

    //删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('roleListTable'),
            data = checkStatus.data;
        if(data.length > 0) {
         	var selectedname = [];
         	var roleids = '';
            for (var i in data) {
            	selectedname.push(data[i].rolename);
            	roleids += data[i].roleid+",";
            }
            layer.confirm('确定删除选中的用户组？<br/>'+selectedname, {icon: 3, title: '提示信息'}, function (index) {
                $.post("/sys/tuser/roleDel",{roleids: roleids},function(res){
                	layer.msg(res.message);
                	if(res.status == 200){
		                tableRoles.reload();
		                layer.close(index);
                	}
                },"json");

            });
        }else{
            layer.msg("请选择需要删除的用户");
        }
    });

});
</script>
</html>