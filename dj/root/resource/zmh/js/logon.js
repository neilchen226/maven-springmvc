layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer;
    var $ = layui.jquery;
        
    //登录
    form.on("submit(loginBtn)",function(data){
    	var btn = $(this);
    	btn.text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
    	var datas = $("#logonForm").serialize();
    	$.post("/logon/check",data.field, function(e){
    		layer.msg(e.message,{time: 1000},function(){
    			if(200!= e.status){
	    	    	btn.text("登录").removeAttr("disabled","disabled").removeClass("layui-disabled");
    				$("#randomCode").attr("src","/logon/valicode?_="+new Date().getTime());
    				$("input[valicode]").val();
    			}else{
    				if(e.userinfo != undefined){
    					window.sessionStorage.setItem("userInfo",JSON.stringify(e.userinfo));// 保存用户信息	
    				}
    				location.href="/main";
    			}
    		});
    	},"json");
        return false;
    });

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    });
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    });
    
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    });
});