var $,tab,dataStr,layer;
var cacheStr = window.sessionStorage.getItem("cache"),
oneLoginStr = window.sessionStorage.getItem("oneLogin");
// 默认切换时刷新
//window.sessionStorage.setItem("changeRefresh",true);

layui.config({
	base : "/resource/layuicms2.0/js/"
}).extend({
	"bodyTab" : "bodyTab",
	"multiSelect" :"multiSelect"
});
layui.use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		element = layui.element;
		$ = layui.$;
    	layer = parent.layer === undefined ? layui.layer : top.layer;
		tab = layui.bodyTab({
			openTabNum : "10"  //最大可打开窗口数量
		});

	// 首先判断是否有登录信息
	var userInfo = window.sessionStorage.getItem('userInfo');
    if(userInfo == undefined){
    	lay.msg("您尚未登录，请先登录！",{time: 1000},function(){
    		location.href="/logon";
    	});
    }else{
		userInfo = JSON.parse(userInfo);
    	$(".userAvatar").attr("src",userInfo.userFace);
    	$(".userName").text(userInfo.username);
    }	
		


	//手机设备的简单适配
    $('.site-tree-mobile').on('click', function(){
		$('body').addClass('site-mobile');
	});
    $('.site-mobile-shade').on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$("body").on("click",".layui-nav .layui-nav-item a:not('.mobileTopLevelMenus .layui-nav-item a')",function(){
		//如果不存在子级
		if($(this).siblings().length == 0){
			addTab($(this));
			$('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
		}
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	});

    //更换皮肤
    skins();
    function skins(){
        var skin = window.sessionStorage.getItem("skin");
        if(skin){  //如果更换过皮肤
            if(window.sessionStorage.getItem("skinValue") != "自定义"){
                $("body").addClass(window.sessionStorage.getItem("skin"));
            }else{
                $(".layui-layout-admin .layui-header").css("background-color",skin.split(',')[0]);
                $(".layui-bg-black").css("background-color",skin.split(',')[1]);
                $(".hideMenu").css("background-color",skin.split(',')[2]);
            }
        }
    }
    $(".changeSkin").click(function(){
        layer.open({
            title : "更换皮肤",
            area : ["310px","280px"],
            type : "1",
            content : '<div class="skins_box">'+
                            '<form class="layui-form">'+
                                '<div class="layui-form-item">'+
                                    '<input type="radio" name="skin" value="默认" title="默认" lay-filter="default" checked="">'+
                                    '<input type="radio" name="skin" value="橙色" title="橙色" lay-filter="orange">'+
                                    '<input type="radio" name="skin" value="蓝色" title="蓝色" lay-filter="blue">'+
                                    '<input type="radio" name="skin" value="自定义" title="自定义" lay-filter="custom">'+
                                    '<div class="skinCustom">'+
                                        '<input type="text" class="layui-input topColor" name="topSkin" placeholder="顶部颜色" />'+
                                        '<input type="text" class="layui-input leftColor" name="leftSkin" placeholder="左侧颜色" style="display:none;" />'+
                                        '<input type="text" class="layui-input menuColor" name="btnSkin" placeholder="顶部菜单按钮" />'+
                                    '</div>'+
                                '</div>'+
                                '<div class="layui-form-item skinBtn">'+
                                    '<a href="javascript:;" class="layui-btn layui-btn-sm layui-btn-normal" lay-submit="" lay-filter="changeSkin">确定更换</a>'+
                                    '<a href="javascript:;" class="layui-btn layui-btn-sm layui-btn-primary" lay-submit="" lay-filter="noChangeSkin">朕再想想</a>'+
                                '</div>'+
                            '</form>'+
                        '</div>',
            success : function(index, layero){
                var skin = window.sessionStorage.getItem("skin");
                if(window.sessionStorage.getItem("skinValue")){
                    $(".skins_box input[value="+window.sessionStorage.getItem("skinValue")+"]").attr("checked","checked");
                };
                if($(".skins_box input[value=自定义]").attr("checked")){
                    $(".skinCustom").css("visibility","inherit");
                    $(".topColor").val(skin.split(',')[0]);
                    $(".leftColor").val(skin.split(',')[1]);
                    $(".menuColor").val(skin.split(',')[2]);
                };
                form.render();
                $(".skins_box").removeClass("layui-hide");
                $(".skins_box .layui-form-radio").on("click",function(){
                    var skinColor;
                    if($(this).find("div").text() == "橙色"){
                        skinColor = "orange";
                    }else if($(this).find("div").text() == "蓝色"){
                        skinColor = "blue";
                    }else if($(this).find("div").text() == "默认"){
                        skinColor = "";
                    }
                    if($(this).find("div").text() != "自定义"){
                        $(".topColor,.leftColor,.menuColor").val('');
                        $("body").removeAttr("class").addClass("main_body "+skinColor+"");
                        $(".skinCustom").removeAttr("style");
                        $(".layui-bg-black,.hideMenu,.layui-layout-admin .layui-header").removeAttr("style");
                    }else{
                        $(".skinCustom").css("visibility","inherit");
                    }
                });
                var skinStr,skinColor;
                $(".topColor").blur(function(){
                    $(".layui-layout-admin .layui-header").css("background-color",$(this).val()+" !important");
                });
                $(".leftColor").blur(function(){
                    $(".layui-bg-black").css("background-color",$(this).val()+" !important");
                });
                $(".menuColor").blur(function(){
                    $(".hideMenu").css("background-color",$(this).val()+" !important");
                });

                form.on("submit(changeSkin)",function(data){
                    if(data.field.skin != "自定义"){
                        if(data.field.skin == "橙色"){
                            skinColor = "orange";
                        }else if(data.field.skin == "蓝色"){
                            skinColor = "blue";
                        }else if(data.field.skin == "默认"){
                            skinColor = "";
                        }
                        window.sessionStorage.setItem("skin",skinColor);
                    }else{
                        skinStr = $(".topColor").val()+','+$(".leftColor").val()+','+$(".menuColor").val();
                        window.sessionStorage.setItem("skin",skinStr);
                        $("body").removeAttr("class").addClass("main_body");
                    }
                    window.sessionStorage.setItem("skinValue",data.field.skin);
                    layer.closeAll("page");
                });
                form.on("submit(noChangeSkin)",function(){
                    $("body").removeAttr("class").addClass("main_body "+window.sessionStorage.getItem("skin")+"");
                    $(".layui-bg-black,.hideMenu,.layui-layout-admin .layui-header").removeAttr("style");
                    skins();
                    layer.closeAll("page");
                });
            },
            cancel : function(){
                $("body").removeAttr("class").addClass("main_body "+window.sessionStorage.getItem("skin")+"");
                $(".layui-bg-black,.hideMenu,.layui-layout-admin .layui-header").removeAttr("style");
                skins();
            }
        });
    });
    
});

//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}

// 先删除旧的再打开新的
function addAfterDeleteTab(_this){
	var title = _this.find("cite").text();
	var tabIndex = tab.hasTab(title);
	if(tabIndex !=-1){
		closeTab(title);
	}
	tab.tabAdd(_this);
}

// 根据标题关闭选项卡
function closeTab(title){
	console.info("关闭的窗口标题： " + title);
	tab.deleteTab(title);
}

// 刷新 选项卡
function refreshTab(title){
	console.info("刷新的窗口标题： " + title);
	var tabIndex = tab.hasTab(title);
	if(tabIndex !=-1){
		$(".clildFrame .layui-tab-item").eq(tabIndex).find("iframe")[0].contentWindow.location.reload();
	}else{
		layer.msg("该窗口不存在");
	}
};

