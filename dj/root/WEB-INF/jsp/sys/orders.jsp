<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
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
<link rel="stylesheet" href="/resource/css/zmh.css" media="all" />
<style type="text/css">
.layui-table-view .layui-table[lay-size=sm] .layui-table-cell {
   height: inherit;
    line-height: 20px;
}
.layui-form-label{
	padding:5px 2px;
}
.layui-table-view .layui-table[lay-size=sm] .layui-table-cell {
    height: auto;
    text-overflow: inherit;
    overflow: visible;
    white-space: normal;
    word-wrap: break-word;
}
</style>
</head>
<body class="childrenBody">
	<div>
		<div class="layui-row">
			<form  id="searchForm" class="layui-form">
				<input type='hidden' name='isdelete' value="0"/>
				<div class="layui-form-item">
				<c:if test="${user.jobname == 'drawer' }">
					<input type='hidden' name='userid' value="${user.usernumber }"/>
				</c:if>
				<c:if test="${user.jobname != 'drawer' }">
					<div class="layui-inline"> 
						<label class="layui-form-label" style="width: 48px;">制图员</label>
						<div class="layui-input-inline" style="width: 138px;">
							<input id="userids" type='hidden' name='userid' value=""/>
							<select multiple="multiple" lay-filter="userSelect" id="userSelect">
								<option value="">请选择制图员</option>
								<c:forEach var="item" items="${drawers }">
									<option value="${item.usernumber }">${item.usernumber }</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</c:if>
				<div class="layui-inline"> 
					<label class="layui-form-label" style="width:68px">客户/项目</label>
					<div class="layui-input-inline" style="width: 150px;">
						<input type="text" name="client" class="layui-input" placeholder="请输入客户或项目名" />
					</div>
				</div>
				<div class="layui-inline"> 
					<label class="layui-form-label" style="width:60px">订单状态</label>
					<div class="layui-input-inline" style="width: 90px;">
							<select name='status'>
								<option value="">全部</option>
								<option value="0">未完成</option>
								<option value="1">已完成</option>
							</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">接单时间</label>
					<div class="layui-input-inline" style="width: 100px;">
						<input type="text" name="startDate" readonly autocomplete="off" class="layui-input  laydate-item" value="${startDate }">
					</div>
					<div class="layui-form-mid">-</div>
					<div class="layui-input-inline" style="width: 100px;">
						<input type="text" name="endDate" readonly autocomplete="off" class="layui-input  laydate-item" value="${endDate }">
					</div>
				</div>
				<div class="layui-inline"> 
					<a class="layui-btn layui-btn-sm layui-btn-normal search_btn" data-type="reload">查询</a>
					<button type="reset" class="layui-btn layui-btn-sm layui-btn-danger" data-type="reload">重置</button>
				</div>
				
				<div class="layui-inline" style="float:right;">
					<c:if test="${ user.jobname != 'drawer'}">
						<div class="layui-row">
							<div class="layui-btn-group">
								<a class="layui-btn layui-btn-sm addNews_btn">新建订单</a>
								<c:if test="${ user.jobname == 'admin'}">
									<a class="layui-btn layui-btn-sm  layui-btn-warm " id="showDeleteOrders" title='点击查看废弃订单'>废弃订单</a>
								</c:if>
								<a class="layui-btn layui-btn-sm layui-btn-normal export_btn">导出订单</a>
							</div>
						</div>
					</c:if>
				</div>
			</div>
			</form>
		</div>
		
		<table id="orderList" lay-filter="orderList"></table>
		<c:if test="${user.jobname != 'drawer'}">
			<script type="text/html" id="orderListBar">
				<a class="layui-btn layui-btn-xs " lay-event="showDetail">查看详情</a>
				<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="edit">修改订单</a>
				<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="delete">设为废弃</a>
			</script>
		</c:if>
		<c:if test="${user.jobname == 'drawer'}">
			<script type="text/html" id="orderListBar">
				<a class="layui-btn layui-btn-xs " lay-event="showDetail">查看详情</a>
			</script>
		</c:if>
		<div style="display:none;">
			<iframe name="exportExcel"></iframe>
			<form target="exportExcel" id="exportExcel" action='/sys/order/export' method="post"></form>
		</div>
	</div>
</body>
<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<script>
layui.config({
	base : "/resource/layuicms2.0/js/"
}).extend({
	"multiSelect" :"multiSelect"
});
layui.use(['form','layer','table','laytpl','laydate','multiSelect'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        multiSelect = layui.multiSelect,
        laydate = layui.laydate,
        table = layui.table;
    

	lay('.laydate-item').each(function() {
		laydate.render({
			elem : this,
			format: "yyyy-MM-dd",
			type: 'date',
			theme: "#ff8200"
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

	form.on('select(userSelect)',function(data){
		var checked = $("#userSelect").siblings(".layui-form-select").find("input:checked");
		var vals = [];
		checked.each(function(){
			vals.push($(this).closest("dd").attr("lay-value"));
		});
		$("#userids").val(vals);
	});
	
	
    //订单列表
    var tableOrders = table.render({
        elem: '#orderList',
        id : "orderListTable",
        url : '/sys/orders',
        where: getFormDataToJSON("#searchForm"),
        method: 'post',
        done:function(res, curr, count){
        	if(res.ordersInfo != undefined){
        		var o = res.ordersInfo;
        		var html='<span style="float:right;margin-right:20px;">统计信息：';
        		html+="图数：<b style='margin-right:5px;'>"+o.vmap+"+"+o.smap+"</b>, ";
        		html+="费用合计：<b style='margin-right:5px;'>"+o.cost+"元</b>, ";
        		html+="定金合计：<b style='margin-right:5px;'>"+o.precost+"元</b>, ";
        		html+="已付合计：<b style='margin-right:5px;'>"+o.paycost+"元</b> ";
        		html+="</div>";
        		$("#layui-table-page1 .layui-laypage-default").append(html);
        	}
        },
        cellMinWidth : 35,
        page : true,
        height : "full-95",
        limits : [20,50,100,200],
        limit : 20,
        initSort:{
            field: 'editdate',
            type: 'asc'
        },
        size:"sm",
        cols : [[
            {field: 'editdate', title: '接单时间',width:100 ,sort:true, align:'center'},
            {field: 'client', title: '客户',sort:true, align:"center",},
            {field: 'projectname', title: '项目名',minWidth:140,sort:true, align:'center'},
            {field: 'userid', title: '制图员',width:60,sort:true, align:'center'},
            {title: '图数', width:60, align:'center', templet:function(d){
            	var vmap = d.vmap||0;
            	var smap = d.smap||0;
            	if(vmap> 0 && smap > 0){
            		return vmap+"+"+ smap;
            	}else{
            		if(vmap>0){
            			return vmap+"(视频图)"
            		}else{
            			return smap;
            		}
            	}
            }},
            {field: 'cost', title: '总费用',sort:true, align:'center', width:60},
            {field: 'precost', title: '定金',sort:true, align:'center', width:60},
            {field: 'paycost', title: '已付金额',sort:true, align:'center', width:70},
            {field: 'payway', title: '支付方式',sort:true, align:'center', width:70},
            {field: 'status', title: '订单状态',sort:true, align:'center', width:70, templet:function(d){
            	var status = d.status||0;
				if(status == 0){
					return '<span class="layui-badge layui-bg-orange">未完成</span>'
				}else if(status == 1){
					return '<span class="layui-badge layui-bg-blue">已完成</span>'
				}
            }},
            {field: 'checkdate', title: '对图时间',sort:true, align:'center', width:100},
            {field: 'note', title: '备注', align:'center'},
            {title: '操作', width:220, templet:'#orderListBar',align:"center"}
        ]]
    });
    // 排序
    table.on('sort(orderList)', function(obj){
   	  var data = getFormDataToJSON("#searchForm");
   	  data.sortfield=obj.field;
   	  data.sorttype=obj.type;
   		tableUsers.reload({
   	    initSort: obj ,
   	    where: data
   	  });
    });
    //列表操作
    table.on('tool(orderList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit'){ //编辑
        	orderInit(data);
        }else if(layEvent === 'showDetail'){ //编辑
        	showDetail(data);
        }else if(layEvent === 'delete'){ //编辑
        	deleteItem(data);
        }
    });

    //搜索
    $(".search_btn").on("click",function(){
       	tableOrders.reload({
              page: {curr: 1},
              where:  getFormDataToJSON("#searchForm")
          });
    });
    

    //修改订单
    function orderInit(edit){
    	var url = "/sys/order/init";
    	var title= "创建订单";
    	if(edit){
    		title = "修改订单";
    		url += "?orderid="+edit.orderid;
    	}else{
    		url += "?orderid=0";
    	}
        var index = layui.layer.open({
            title : title,
            type : 2,
            content : url,
            area:['720px','520px'],
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
    //查看订单
    function showDetail(edit){
    	var url = "/sys/order/detail?orderid="+edit.orderid;
    	var title= "查看订单详情";
        var index = layui.layer.open({
            title : title,
            type : 2,
            content : url,
            area:['720px','520px'],
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
        orderInit();
    });

    // 废弃
    function deleteItem(data){
    	var orderid = data.orderid;
        layer.confirm('确定废弃该订单？<br/>', {icon: 3, title: '提示信息'}, function (index) {
        	$.post("/sys/order/deleteItem",{orderid: orderid},function(res){
        		layer.msg(res.message)
        		if(res.status == 200){
        	       	tableOrders.reload({
        	              page: {curr: 1},
        	              where:  getFormDataToJSON("#searchForm")
        	          });
                    layer.close(index);
        		}
        	},'json');
    	});
    };

	$("#showDeleteOrders").click(function(){
		var html = "<a href='javascript:;' data-url='/sys/deleteOrders'><i class='lyui-icon'></i><cite>废弃订单</cite></a>";
		parent.addTab($(html));
	});	
	
	$(".export_btn").click(function(){
		var data = getFormDataToJSON("#searchForm");
		var html = "";
		for(var i in data){
			html +="<input name='"+i+"' type='hidden' value='"+data[i]+"' />";
		}
		$("#exportExcel").html(html);
		$("#exportExcel").submit();
		
	});
	
})
</script>
</html>
