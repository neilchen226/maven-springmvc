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
	
	
	$("#houseStatus .statusItem").click(function(){
		var html=" <span data-url='"+$(this).attr("data-href")+"'><cite>"+$(this).attr("data-title")+"</cite></span>";
    	var _this = $(html);
        if(parent.addAfterDeleteTab != undefined){
     			parent.addAfterDeleteTab(_this);
     	}else{
     		layer.msg("页面异常，请刷新页面重试！",{icon:2});
 		}
	});
	
	var sortCodeDatas = table.render({
        elem: '#sortCodeDatas',
        id : "sortCodeDatas",
        url : "/index/getSortcodeStatus",
        method: 'post',
        cellMinWidth : 50,
        page : true,
        height : "full-135",
        limits : [20,50,100,200],
        limit : 20,
        size:"sm",
        cols : [[
            {field: 'sortcode', title: '分拣号', align:'center', rowspan:'2'},
            {title: '汇总', align:'left', rowspan:'2',templet:function(d){
            	var info = "袋： "+ d.b_A;
            	info += "<br>包裹： "+ d.p_A;
            	return info;
            }},
            {title: '袋', align:'center', colspan:'6'},
            {title: '包裹', align:'center', colspan:'6'},
            ], [
            {field: 'b_y', title: '昨日留仓', align:'center'},
            {field: 'b_0', title: '0时-6时', align:'center'},
            {field: 'b_6', title: '6时-12时',  align:'center'},
            {field: 'b_12', title: '12时-18时',  align:'center'},
            {field: 'b_18', title: '18时-24时', align:'center'},
            {field: 'b_A', title: '合计', align:'center'},
            {field: 'p_y', title: '昨日留仓', align:'center'},
            {field: 'p_0', title: '0时-6时', align:'center'},
            {field: 'p_6', title: '6时-12时',  align:'center'},
            {field: 'p_12', title: '12时-18时',  align:'center'},
            {field: 'p_18', title: '18时-24时', align:'center'},
            {field: 'p_A', title: '合计', align:'center'},
        ]]
    });
	
	form.on('select(sortcodeSelect)', function(data){
		sortCodeDatas.reload({
			page : {
				curr : 1
			},
			where : {
				sortcode : data.value
			}
		});
	});
	
	var getSortCodeStatusInterval = setInterval(function(){
		console.info("获取SortCode status");
		var userInfo = window.sessionStorage.getItem('userInfo');
	    if(userInfo == undefined){
	    	clearInterval(getSortCodeStatusInterval);
	    	layer.msg("您已登录超时，请刷新页面重新登录！",{icon: 2});
	     return;
	    }
		sortCodeDatas.reload({
			page : {
				curr : 1
			},
			where : {
				sortcode : $("#sortcodeSelect").val()
			}
		});
	},1.5*60*1000);

	$("#createBardodeBtn").click(function(){
		layer.prompt({title: "请输出条形码"},function(value, index, elem){
		  if(value != ""){
			  window.open("/getBarcode?code="+value);
		  }
		  layer.close(index);
		});
	});
	
});