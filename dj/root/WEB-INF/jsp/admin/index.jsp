<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>首页</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="/resource/layuicms2.0/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="/resource/layuicms2.0/css/public.css" media="all" />
<style type="text/css">
.logonusername {
	font-size: 20px;
	font-weight: bold;
	color: #FF5722;
}

.nowTime {
	font-weight: bold;
	font-size: 16px;
}

#houseStatus {
	width: 100%;
	text-align: right;
	margin-bottom: 10px;
}

#houseStatus li {
	display: inline-block;
	vertical-align: middle;
	font-size: 14px;
	transition: all .2s;
	-webkit-transition: all .2s;
	text-align: center;
	cursor: pointer;
	margin-top: 10px;
}

#houseStatus li .statusItem {
	padding: 10px 15px;
	min-width: 45px;
	height: 38px;
}

#houseStatus li .statusItem:hover {
	filter: alpha(opacity :   80);
	opacity: 0.8;
	-moz-opacity: 0.8;
	-khtml-opacity: 0.8
}
.layui-table th{
	text-align: center;
}
.layui-table-view .layui-table[lay-size=sm] .layui-table-cell {
   height: inherit;
    line-height: 20px;
}
</style>
</head>
<body class="childrenBody">
	<div class="layui-row">
		<div style="float: left;">
			<div style="margin: 10px 0;">
				<span class="logonusername"></span> <span class="nowTime_01"></span> 欢迎进入中马航订单管理系统。
			</div>
			<div style="margin: 10px 0;">
				当前时间为：
				<span class="nowTime"></span>
			</div>
		</div>
		
	</div>
	

</body>
<script type="text/javascript" src="/resource/echars/echarts.min.js"></script>
<script type="text/javascript" src="/resource/echars/macarons.js"></script>
<script type="text/javascript" src="/resource/layuicms2.0/layui/layui.js"></script>
<script type="text/javascript">
function dateFilter(date){
    if(date < 10){return "0"+date;}
    return date;
}
function setNowTime(){
		var dateObj = new Date(); // 表示当前系统时间的Date对象
		var year = dateObj.getFullYear(); // 当前系统时间的完整年份值
		var month = dateObj.getMonth() + 1; // 当前系统时间的月份值
		var date = dateObj.getDate(); // 当前系统时间的月份中的日
		var day = dateObj.getDay(); // 当前系统时间中的星期值
		var weeks = [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ];
		var week = weeks[day]; // 根据星期值，从数组中获取对应的星期字符串
		var hour = dateObj.getHours(); // 当前系统时间的小时值
		var minute = dateObj.getMinutes(); // 当前系统时间的分钟值
		var second = dateObj.getSeconds(); // 当前系统时间的秒钟值
		var timeValue = "" + ((hour >= 12) ? ((hour >= 18) ? "晚上" : "下午") : "上午"); // 当前时间属于上午、晚上还是下午
		var newDate = dateFilter(year) + "年" + dateFilter(month) + "月" + dateFilter(date) + "日 " + " " + dateFilter(hour) + ":" + dateFilter(minute) + ":" + dateFilter(second);
		document.getElementsByClassName("nowTime")[0].innerHTML =    newDate + "　" + week;
		document.getElementsByClassName("nowTime_01")[0].innerHTML =   timeValue + "好，　";
		window.setTimeout("setNowTime()", 1000);
}

setNowTime();
var userInfo = window.sessionStorage.getItem('userInfo');
userInfo = JSON.parse(userInfo);
document.getElementsByClassName("logonusername")[0].innerHTML = userInfo.username;

layui.use([ 'form', 'layer', 'element', 'upload', 'laydate','table' ], function() {
	var form = layui.form;
	var upload = layui.upload;
	var laydate = layui.laydate;
	var table = layui.table;
	var element = layui.element;
	var layer = parent.layer === undefined ? layui.layer : top.layer;
	var $ = layui.jquery;
});
</script>
</html>