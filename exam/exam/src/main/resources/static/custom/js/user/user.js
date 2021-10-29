$(function(){
	
	$(".status").append($.customOptions("status"));
	
	var userForm = $("#userForm");
	var userModal = $("#userModal");
	var userDelModal = $("#userDeleteModal");
	var userPassUpdateModal = $("#userPassUpdateModal");
	var userPassUpdateForm = $("#userPassUpdateForm");

	// 人员信息弹框
	userModal.modal({
		backdrop: 'static', 
		keyboard: false,
		show: false
	}).on("hide.bs.modal", function(e){
		// 关闭时重置表单
		$("#userForm [name='id']").val("");
		$("#userForm [name='username']").attr("disabled", false);
		userForm[0].reset();

		$("#passwordArea").show();
		
		//清空所有错误信息
		userFormValidator.resetForm();
		
	});
	
	// 用户密码更新弹框
	userPassUpdateModal.modal({
		backdrop: 'static', 
		keyboard: false,
		show: false
	}).on("hide.bs.modal", function(e){
		// 关闭时重置表单
		$("#userPassUpdateForm [name='id']").val("");
		userPassUpdateForm[0].reset();
		$("#passwordArea").show();
		//清空所有错误信息
		userPassUpdateFormValidator.resetForm();
	});
	
	//渲染表格
    var tbl = $("#userListTbl").bootstrapTable({
      url: Api.url("user"),
      columns: [
    	    {
    		   field: "seq",
    		   title: "序号",
    		   width:"5%",
    		   align: "center",
    		   formatter: function(value, row, index){
    			   return $.getTableRowNum(tbl, index);
    		   }
          	}, {
                field: "username",
                title: "用户名",
                sortable : true,
                align: "center"
            }, {
                field: "fullname",
                title: "姓名",
                sortable : true,
                align: "center",
            }, {
		    	field: "email",
		    	title: "邮箱",
                sortable : true,
                align: "center"
		    }, {
                field: "status",
                title: "状态",
                sortable : true,
                align: "center",
                formatter: function(value, row, index){
            		let icon = value == 2 ? "fa-warning" : "fa-check";
                	return Dict.getText("status", value) +  ` <i class="fa ${icon}"></i> `;
                }
            },{
            	field: "id",
                title: "操作",
                align: "center",
                width:"10%",
                formatter: function(value, row, index){
                	const source = row.source;
                	const username = row.username;
                	let btnStatus;
                	if(source == "outer"){
                		btnStatus = "disabled";
                	}
                	return [
                		`<button class='btn btn-xs btn-warning openUserEditBtn' type='button' data-id=${value} title='修改信息'><i class='fa fa-pencil'></i> </button>`,
                		"&nbsp;",
                		`<button class='btn btn-xs btn-info openPassResetResetBtn' type='button' data-id=${value} title='修改密码'><i class='fa fa-lock'></i> </button>`,
                		"&nbsp;",
                		`<button class='btn btn-xs btn-danger openUserDelBtn' type='button' data-id=${value} data-username=${username} ${btnStatus} title='删除'><i class='fa fa-trash'></i></button>`
                		].join("");
                }
            }
        ],
        queryParams : function (params) { //查询的参数
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject($("#userSearchForm"));
        	return $.extend({}, defaultParams, formData);
        },
        onLoadSuccess: function (res) { //页面加载完毕后绑定事件
        	
        	// 打开编辑弹框
        	$(".openUserEditBtn").on("click", function(e){
        		userModal.modal();
        		
        		$("#passwordArea").hide();
        		const id = $(this).data("id");
        		const data = res.rows.find(item => item.id == id);
        		
        		// 取出本行数据
        		userForm.initForm(data);
        		
        		$("#userForm [name='username']").attr("disabled", true);
        		$("#userForm [name='confirmPassword']").val($("#userForm [name='password']").val());
	       	 })
	       	 
	       	 // 打开密码重置框
	       	 $(".openPassResetResetBtn").on("click", function () {
	       		userPassUpdateModal.modal();
	       		$("#passUpdateUserId").val($(this).data("id"));
	       		 
	       	 })
	       	 
	       	 // 打开删除弹框
	       	 $(".openUserDelBtn").on("click", function () {
	       		 userDelModal.modal();
	       		 $("#deleteNameText").html($(this).data("username"))
	       		 $("#deleteUserId").val($(this).data("id"));
	       	 })
        }, 
    });

	//表单验证
	var userFormValidator = userForm.validate({
	    rules: {
	    	username: {
	    		required: true,
	    		remote:{
	    			url: Api.url("usernameCheck"),
	    			type: "get",
	    			data:{
	    				username: function(){
	    					return $("#userForm [name='username']").val()
	    				}
	    			}
	    		},
	    		maxlength: 12,
	    		stringCheck: true,
	    	},
	    	password:{
	    		required: true,
	    		minlength: 6,
	    		maxlength: 12,
	    		stringCheck: true,
	    	},
	    	fullname: {
	    		required: true,
	    		stringCheck: true,
	    		maxlength: 12,
	    	},
	    	icCard: {
	    		maxlength: 32,
	    	},
	    	confirmPassword:{
	    		equalTo:"#userForm [name='password']"
	    	},
	    	email: {
	    		checkEmail: true,
	    		maxlength: 32,
	    	}
	    },
	    messages: {
	    	username: {
	    		required: "用户名不能为空",
	    		remote: "用户名已存在",
	    		maxlength: "长度不能超过12位"
	    	},
	    	password: {
	    		required: "密码不能为空",
	    		minlength: "密码长度至少6位",
	    		maxlength: "长度不能超过12位"
	    	},
	    	confirmPassword: {
	    		equalTo: "两次密码输入不一致"
	    	},
	    	fullname: {
	    		required: "姓名不能为空",
	    		maxlength: "长度不能超过12位"
	    	},
	    	icCard: {
	    		maxlength: "长度不能超过32位",
	    	},
	    	email: {
	    		checkEmail: "请输入正确的邮箱地址",
	    		maxlength: "长度不能超过32位",
	    	}
	     },
	     submitHandler: function(){ //验证通过，提交表单
	 		const formData = $.getFormObject(userForm);
	 		if(formData.id){
	 			$.putJSON(Api.url("user"), formData, userCallback)
	 		} else{
	 			$.postJSON(Api.url("user"), formData, userCallback)
	 		}
	     }
    })
    //表单验证
    var userPassUpdateFormValidator = userPassUpdateForm.validate({
    	rules: {
    		password:{
    			required: true,
    			minlength: 6,
    			maxlength: 12,
    			stringCheck: true,
    		},
    		confirmPassword:{
    			equalTo:"#userPassUpdateForm [name='password']"
    		},
    	},
    	messages: {
    		password: {
    			required: "密码不能为空",
    			minlength: "密码长度至少6位",
    			maxlength: "长度不能超过12位"
    		},
    		confirmPassword: {
    			equalTo: "两次密码输入不一致"
    		},
    	},
    	submitHandler: function(){ //验证通过，提交表单
    		const formData = $.getFormObject(userPassUpdateForm);

    		$.postJSON(Api.url("userPassUpdate"), formData, function(response){
    			const msg = response.message;
    			if(response.code == 200){
    				// 关闭弹窗
    				userPassUpdateModal.modal("hide")
    				// 刷新表格
    				$.refreshTable(tbl);
    				
    				toastr.success(msg);
    			} else {
    				toastr.warning(msg);
    			}
    		})
    		
    	}
    })
	
    // 更新、新增用户后的回调函数
    var userCallback = function(response){
		if(response.code == 200){
			//关闭modal
			userModal.modal("hide")
			//刷新表格
			$.refreshTable(tbl);
		}
	}
	
    // 点击查询按钮
	$("#userListSearchBtn").on("click", function(){
		$.refreshTable(tbl);
	})
	
	// 点击新增按钮
	$("#openUserAddBtn").on("click", function(){
		userModal.modal();
	})
	
	// 点击弹框中的保存按钮
	$("#userSaveBtn").on("click", function(){
		userForm.submit();
	})

	// 点击删除按钮
	$("#userDeleteBtn").on("click", function(){
		const id = $("#deleteUserId").val()
		$.deleteJSON(Api.url("user", id), {}, function(response){
			if(response.code == 200){
				// 关闭弹窗
				userDelModal.modal("hide")
				// 刷新表格
				$.refreshTable(tbl);
			}
		})
	})
	
	// 点击密码更新按钮
	$("#userPassUpdateBtn").on("click", function(){
		userPassUpdateForm.submit();
	})

	// 用户导入按钮
	$("#userImport").on("click", function(){ 
		//触发用户上传
		$("#userImportInput").click();
	})
	
	$("#userImportInput").on("change", function(){
		if($(this).val()){ //上传框不为空才开始上传
			$.importFile(Api.url("userImport"), "userImportInput", function(response){
				const msg = response.message;
				if (response.code===200) {
					toastr.success(msg)
					$.refreshTable(tbl);
				}else{
					toastr.warning(msg);
				}
			})
			//清空文件框
			$(this).val("");
		}
	})
	
	// 用户导入模板
	$("#userImportTemplate").on("click", function(){
		window.open("/static/用户导入模板.xls", '_self');
	})
	
})