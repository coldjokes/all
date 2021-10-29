$(function(){
	
	$("#loginBtn").on("click", function(){
		$("#errorMsgDiv").html("");
		
		const username = $("#loginForm [name='username']").val();
		const password = $("#loginForm [name='password']").val();
		
		if(!username || !password){
			$("#errorMsgDiv").removeClass("hide");
			$("#errorMsgDiv").html("账号或密码不能为空！");
			return;
		}
		
		const form = $.getFormObject($("#loginForm"));
		$.postJSON(Api.url("login"), form, function(data){
			if(data.code != 200){
				$("#errorMsgDiv").removeClass("hide");
				$("#errorMsgDiv").html(data.message);
			} else{
				location.href = "";
			}
		})
	})
})

