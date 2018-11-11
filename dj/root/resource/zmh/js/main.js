var $,tab,dataStr,layer;
var cacheStr = window.sessionStorage.getItem("cache"),
oneLoginStr = window.sessionStorage.getItem("oneLogin");
// 默认切换时刷新
//window.sessionStorage.setItem("changeRefresh",true);

layui.config({
	base : "/resource/layuicms2.0/js/"
}).extend({
	"bodyTab" : "bodyTab"
});
layui.use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		element = layui.element;
		$ = layui.$;
    	layer = parent.layer === undefined ? layui.layer : top.layer;
		tab = layui.bodyTab({
			openTabNum : "10",  //最大可打开窗口数量
			url : "/getTModules" //获取用户模块
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
		
	//获取菜单信息
	getMenuData("contentManagement");
	
	//获取左侧菜单 
	function getMenuData(json){
		$.getJSON(tab.tabConfig.url,function(data){
			if(data.status == 200){
				dataStr = data.menu;
				tab.render();//重新渲染左侧菜单
				
				loadsearchMenus(data.menu);
			}else{
				layer.msg(data.message,{time:1000},function(){
					location.href="/logon";
				});
			}
		});
	}
	
	// 获取查询菜单按钮内容
	function loadsearchMenus(menu){
		var menus ;
		if(typeof(menu) == "string"){
            menus = JSON.parse(menu); //部分用户解析出来的是字符串，转换一下
        }else{
        	menus = menu;
        }
		var opts = "<option>--选择功能--</option>";
		opts += loadsearchMenusLeves(menus);// 获取下拉菜单内容
		$("#searchMenus").html(opts);
		form.render('select'); //刷新select选择框渲染
		
		form.on('select(searchPage)', function(data){
			  var sopt =  $("#searchMenus option:selected");
				if(sopt != undefined && sopt.attr("data-url").length>0){
					var href = sopt.attr("data-url");
					var icon = sopt.attr("data-icon");
					var title = sopt.text();
					var ahtml = "<a data-url='"+href+"'><i data-icon="+icon+"></i><cite>"+title+"</cite></a>";
					addTab($(ahtml));
				}
		});  
		
	}
	function loadsearchMenusLeves(menus){
		var opts ="";
		 for(var i=0;i<menus.length;i++){
			var item = menus[i];
			if(item.isleaf == 1){
				opts += "<option data-url='"+item.href+ "' data-icon='"+item.icon+"' >"+item.title+"</option>";
			}else if(item.children != undefined && item.children.length>0){
				opts +=loadsearchMenusLeves(item.children);
			}
		}
		return opts;
	}
	
	//页面加载时判断左侧菜单是否显示
	//通过顶部菜单获取左侧菜单
	$(".topLevelMenus li,.mobileTopLevelMenus dd").click(function(){
		if($(this).parents(".mobileTopLevelMenus").length != "0"){
			$(".topLevelMenus li").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}else{
			$(".mobileTopLevelMenus dd").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}
		$(".layui-layout-admin").removeClass("showMenu");
		$("body").addClass("site-mobile");
		getMenuData($(this).data("menu"));
		//渲染顶部窗口
		tab.tabMove();
	});

	//隐藏左侧导航
	$(".hideMenu").click(function(){
		if($(".topLevelMenus li.layui-this a").data("url")){
			layer.msg("此栏目状态下左侧菜单不可展开");  //主要为了避免左侧显示的内容与顶部菜单不匹配
			return false;
		}
		$(".layui-layout-admin").toggleClass("showMenu");
		//渲染顶部窗口
		tab.tabMove();
	});


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

	//清除缓存
	$(".clearCache").click(function(){
		window.sessionStorage.clear();
        window.localStorage.clear();
        var index = layer.msg('清除缓存中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("缓存清除成功！");
        },1000);
    });

	//刷新后还原打开的窗口
    if(cacheStr == "true") {
        if (window.sessionStorage.getItem("menu") != null) {
            menu = JSON.parse(window.sessionStorage.getItem("menu"));
            curmenu = window.sessionStorage.getItem("curmenu");
            var openTitle = '';
            for (var i = 0; i < menu.length; i++) {
                openTitle = '';
                if (menu[i].icon) {
                    if (menu[i].icon.split("-")[0] == 'icon') {
                        openTitle += '<i class="seraph ' + menu[i].icon + '"></i>';
                    } else {
                        openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                    }
                }
                openTitle += '<cite>' + menu[i].title + '</cite>';
                openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
                element.tabAdd("bodyTab", {
                    title: openTitle,
                    content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
                    id: menu[i].layId
                });
                //定位到刷新前的窗口
                if (curmenu != "undefined") {
                    if (curmenu == '' || curmenu == "null") {  //定位到后台首页
                        element.tabChange("bodyTab", '');
                    } else if (JSON.parse(curmenu).title == menu[i].title) {  //定位到刷新前的页面
                        element.tabChange("bodyTab", menu[i].layId);
                    }
                } else {
                    element.tabChange("bodyTab", menu[menu.length - 1].layId);
                }
            }
            //渲染顶部窗口
            tab.tabMove();
        }
    }else{
		window.sessionStorage.removeItem("menu");
		window.sessionStorage.removeItem("curmenu");
	}
    

    //公告层
    function showNotice(){
        layer.open({
            type: 1,
            title: "系统公告",
            area: ['300px','450px'],
            shade: 0.8,
            id: 'LAY_layuipro',
            btn: ['查看详情'],
            moveType: 1,
            content: '<div style="padding:15px 20px; text-align:justify; line-height: 22px; text-indent:2em;border-bottom:1px solid #e2e2e2;"><p class="layui-red">请使用模版前请务必仔细阅读首页右下角的《更新日志》，避免使用中遇到一些简单的问题造成困扰。</p></pclass></p><p>1.0发布以后发现很多朋友将代码上传到各种素材网站，当然这样帮我宣传我谢谢大家，但是有部分朋友上传到素材网站后将下载分值设置的相对较高，需要朋友们充钱才能下载。本人发现后通过和站长、网站管理员联系以后将分值调整为不需要充值才能下载或者直接免费下载。在此郑重提示各位：<span class="layui-red">本模版已进行作品版权证明，不管以何种形式获取的源码，请勿进行出售或者上传到任何素材网站，否则将追究相应的责任。</span></p></div>',
            success: function(layero){
                var btn = layero.find('.layui-layer-btn');
                btn.css('text-align', 'center');
                btn.on("click",function(){
                    tipsShow();
                });
            },
            cancel: function(index, layero){
                tipsShow();// 关闭的时候显示
            }
        });
    }
    function tipsShow(){
        window.sessionStorage.setItem("showNotice","true");
        if($(window).width() > 432){  //如果页面宽度不足以显示顶部“系统公告”按钮，则不提示
            layer.tips('系统公告放在了这里', '#userInfo', {
                tips: 3,
                time : 1000
            });
        }
    }
    $(".showNotice").on("click",function(){
        showNotice();
    });
    
    //锁屏
    function lockPage(){
        layer.open({
            title : false,
            type : 1,
            content : '<div class="admin-header-lock" id="lock-box">'+
                            '<div class="admin-header-lock-img"><img src="'+userInfo.userFace+'" class="userAvatar"/></div>'+
                            '<div class="admin-header-lock-name" id="lockUserName">'+userInfo.userName+'</div>'+
                            '<div class="input_btn">'+
                                '<input type="password" class="admin-header-lock-input layui-input" autocomplete="off" placeholder="请输入密码解锁.." name="lockPwd" id="lockPwd" />'+
                                '<button class="layui-btn layui-btn-normal" id="unlock">解锁</button>'+
                            '</div>'+
                            '<p>请输入登录密码解锁桌面</p>'+
                        '</div>',
            closeBtn : 0,
            shade : 0.9,
            success : function(){
            	$(".admin-header-lock-input").focus();
            }
        });
    }
    $(".lockcms").on("click",function(){
        window.sessionStorage.setItem("lockcms",true);
        lockPage();
    });
    // 判断是否显示锁屏
    if(window.sessionStorage.getItem("lockcms") == "true"){
        lockPage();
    }
    // 解锁
    $("body").on("click","#unlock",function(){
        if($(this).siblings(".admin-header-lock-input").val() == ''){
            layer.msg("请输入解锁密码！");
            $(this).siblings(".admin-header-lock-input").focus();
        }else{
            if($(this).siblings(".admin-header-lock-input").val() == "123456"){
                window.sessionStorage.setItem("lockcms",false);
                $(this).siblings(".admin-header-lock-input").val('');
                layer.closeAll("page");
            }else{
                layer.msg("密码错误，请重新输入！");
                $(this).siblings(".admin-header-lock-input").val('').focus();
            }
        }
    });
    $(document).on('keydown', function(event) {
        var event = event || window.event;
        if(event.keyCode == 13) {
            $("#unlock").click();
        }
    });

    //退出
    $(".signOut").click(function(){
        window.sessionStorage.removeItem("menu");
        menu = [];
        window.sessionStorage.removeItem("curmenu");
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
                                        '<input type="text" class="layui-input leftColor" name="leftSkin" placeholder="左侧颜色" />'+
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
    
    var functionSettingHtml  = '<div class="functionSrtting_box">'+
    '<form class="layui-form">'+
    '<div class="layui-form-item">'+
        '<label class="layui-form-label">开启Tab缓存</label>'+
        '<div class="layui-input-block">'+
            '<input type="checkbox" name="cache" lay-skin="switch" lay-text="开|关">'+
            '<div class="layui-word-aux">开启后刷新页面不关闭打开的Tab页</div>'+
        '</div>'+
    '</div>'+
    '<div class="layui-form-item">'+
        '<label class="layui-form-label">Tab切换刷新</label>'+
        '<div class="layui-input-block">'+
            '<input type="checkbox" name="changeRefresh" lay-skin="switch" lay-text="开|关">'+
            '<div class="layui-word-aux">开启后切换窗口刷新当前页面</div>'+
        '</div>'+
    '</div>'+
    '<div class="layui-form-item skinBtn">'+
        '<a href="javascript:;" class="layui-btn layui-btn-sm layui-btn-normal" lay-submit="" lay-filter="settingSuccess">设定完成</a>'+
        '<a href="javascript:;" class="layui-btn layui-btn-sm layui-btn-primary" lay-submit="" lay-filter="noSetting">放弃设定</a>'+
    '</div>'+
'</form>'+
'</div>';
    
  //功能设定
    $(".functionSetting").click(function(){
        layer.open({
            title: "功能设定",
            area: ["380px", "280px"],
            type: "1",
            content :  functionSettingHtml,
            success : function(index, layero){
                //如果之前设置过，则设置它的值
                $(".functionSrtting_box input[name=cache]").prop("checked",cacheStr=="true" ? true : false);
                $(".functionSrtting_box input[name=changeRefresh]").prop("checked",changeRefreshStr=="true" ? true : false);
                $(".functionSrtting_box input[name=oneLogin]").prop("checked",oneLoginStr=="true" ? true : false);
                //设定
                form.on("submit(settingSuccess)",function(data){
                    window.sessionStorage.setItem("cache",data.field.cache=="on" ? "true" : "false");
                    window.sessionStorage.setItem("changeRefresh",data.field.changeRefresh=="on" ? "true" : "false");
                    window.sessionStorage.setItem("oneLogin",data.field.oneLogin=="on" ? "true" : "false");
                    window.sessionStorage.removeItem("menu");
                    window.sessionStorage.removeItem("curmenu");
                    location.reload();
                    return false;
                });
                //取消设定
                form.on("submit(noSetting)",function(){
                    layer.closeAll("page");
                });
                form.render();  //表单渲染
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

