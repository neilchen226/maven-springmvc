<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css" media="all" />

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

	<div class="layui-container">
		<form id="userinfo" class="layui-form layui-row">
			<input type="hidden" name="orderid" value="${order.orderid}">
			<div class="layui-row">
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 客户名称 <span
							class="layui-badge-dot"
							style="position: relative; top: -3px; left: 5px;" title="必填项"></span>
						</label>
						<div class="layui-input-block">
							<input type="text" name="client" value="${order.client }" class="layui-input" lay-verify="required">
						</div>
					</div>
				</div>

				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 项目名称 <span
							class="layui-badge-dot"
							style="position: relative; top: -3px; left: 5px;" title="必填项"></span>
						</label>
						<div class="layui-input-block">
							<input type="text" name="projectname"
								value="${order.projectname }" class="layui-input"
								lay-verify="required">
						</div>
					</div>
				</div>
			</div>

			<div class="layui-row">
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 制图员 <span
							class="layui-badge-dot"
							style="position: relative; top: -3px; left: 5px;" title="必填项"></span>
						</label>
						<div class="layui-input-block">
							<select name="userid" lay-verify="required" class="layui-input"
								style="width: 300px">
								<option value="">请选择制图员</option>
								<c:forEach items="${drawers }" var="i">
									<option value="${i.usernumber }"
										<c:if test="${i.usernumber == order.userid }"> selected</c:if>>${i.usernumber}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 图数 </label>
						<div class="layui-input-block">
							<input type="text" name="vmap" value="${order.vmap }"
								class="layui-input" placeholder="视频图张数"
								style="width: 100px; display: inline;">+<input
								type="text" name="smap" value="${order.smap }"
								class="layui-input" placeholder="静态图张数"
								style="width: 100px; display: inline;">
						</div>
					</div>
				</div>
			</div>

			<div class="layui-row">
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 总费用 </label>
						<div class="layui-input-block">
							<input type="text" name="cost" value="${order.cost }"
								class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 定金 </label>
						<div class="layui-input-block">
							<input type="text" name="precost" value="${order.precost }"
								class="layui-input">
						</div>
					</div>
				</div>
			</div>

			<div class="layui-row">
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 已付费用 </label>
						<div class="layui-input-block">
							<input type="text" name="paycost" value="${order.paycost }"
								class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 支付方式 </label>
						<div class="layui-input-block">
							<input type="text" name="payway" value="${order.payway }"
								class="layui-input">

						</div>
					</div>
				</div>
			</div>

			<div class="layui-row">
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 对图时间 </label>
						<div class="layui-input-block">
					      <input  class='layui-input laydate-item' name="checkdate" value="${order.checkdate }" readonly  >
						</div>
					</div>
				</div>
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 订单状态 </label>
						<div class="layui-input-block">
					      <input type="radio" name="status" value="0" title="未完成" <c:if test="${empty order.status || order.status == '0' }">checked</c:if> >
					      <input type="radio" name="status" value="1" title="已完成" <c:if test="${order.status == '1' }">checked</c:if> >
						</div>
					</div>
				</div>
			</div>

			<div class="layui-row">
				<div class="layui-col-xs6">
					<div class="layui-form-item">
						<label class="layui-form-label"> 接单时间 </label>
						<div class="layui-input-block">
					      <input class='layui-input laydate-item' name="editdate" value="${order.editdate }" readonly  >
						</div>
					</div>
				</div>
			</div>

			<div class="layui-row">
				<div class="layui-form-item">
					<label class="layui-form-label"> 备注 </label>
					<div class="layui-input-block">
						<textarea name="note" class="layui-textarea">${order.note}</textarea>
					</div>
				</div>
			</div>

			<div class="layui-row">
				<div class="layui-form-item">
					<div class="layui-input-block">
						<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="saveOrder">保存</button>
						<button type="reset"
							class="layui-btn layui-btn-sm layui-btn-primary">重置</button>
					</div>
				</div>
			</div>
	</form>
</div>
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

		lay('.laydate-item').each(function() {
			laydate.render({
				elem : this,
				format: "yyyy-MM-dd",
				type: 'date',
				theme: "#ff8200"
			});
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
		form.on('submit(saveOrder)', function(data) {
			if(data.field.paycost ==undefined){
				data.field.paycost = 0;
			}
			console.info("提交数据： " +JSON.stringify(data.field));
			$.post("/sys/order/save",data.field,function(res){
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