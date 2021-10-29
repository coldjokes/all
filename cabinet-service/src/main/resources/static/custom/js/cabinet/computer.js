var computerTbl;
$(function(){
	var computerForm = $("#computerForm");
	var computerModal = $("#computerModal");
	var computerDelModal = $("#computerDelModal");
	
	// 初始化弹出层
	computerModal.modal({
		backdrop: 'static', 
		keyboard: false,
		show: false
	}).on("hide.bs.modal", function(e){
		// 关闭时重置表单
		$("#computerForm [name='id']").val("");
		computerForm[0].reset();
		
		//清空所有错误信息
		computerFormValidator.resetForm();
	});
	
	//渲染表格
    computerTbl = $("#computerListTbl").bootstrapTable({
      url: Api.url("computer"),
      columns: [
    	    {
    		   field: "seq",
    		   title: "序号",
    		   width:"5%",
    		   align: "center",
    		   formatter: function(value, row, index){
    			   return $.getTableRowNum(computerTbl, index);
    		   }
          	}, {
                field: "name",
                title: "主机名称",
                align: "center"
            }, {
                field: "identifyCode",
                title: "识别码",
                align: "center",
            }, {
                field: "cellPrefix",
                title: "格口前缀",
                align: "center",
            }, {
            	field: "id",
                title: "操作",
                align: "center",
                width:"10%",
                formatter: function(value, row, index){
                	const name = row.name;
                	return [
                		`<button class='btn btn-xs btn-warning openComputerEditBtn' type='button' data-id=${value} title='修改'><i class='fa fa-pencil'></i> </button>`,
                		"&nbsp;",
                		`<button class='btn btn-xs btn-danger openComputerDelBtn' type='button' data-id=${value} data-name=${name} title='删除'><i class='fa fa-trash'></i></button>`
                		].join("");
                }
            }
        ],
        queryParams : function (params) { //查询的参数
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject($("#computerSearchForm"));
        	return $.extend({}, defaultParams, formData);
        },
        onLoadSuccess: function (res) { //页面加载完毕后绑定事件
        	// 打开编辑弹框
        	$(".openComputerEditBtn").on("click", function(e){
        		computerModal.modal();
        		const id = $(this).data("id");
        		const data = res.rows.find(item => item.id == id);
        		
        		// 取出本行数据
        		computerForm.initForm(data);
	       	 })
	       	 
	       	 // 打开删除弹框
	       	 $(".openComputerDelBtn").on("click", function () {
	       		 computerDelModal.modal();
	       		 $("#deleteNameText").html($(this).data("name"))
	       		 $("#deleteComputerId").val($(this).data("id"));
	       	 })
        }, 
    });
    
	//表单验证
	var computerFormValidator = computerForm.validate({
	    rules: {
	    	name: {
	    		required: true,
	    		stringCheck: true,
	    		maxlength: 12,
	    		remote:{
	    			url: Api.url("computerNameCheck"),
	    			type: "get",
	    			data:{
	    				name: function(){
	    					return $("#computerForm [name='name']").val()
	    				}
	    			}
	    		},
	    	},
	    	identifyCode:{
	    		required: true,
	    		stringCheck: true,
	    		maxlength: 32,
	    	},
	    	cellPrefix:{
	    		required: true,
	    		maxlength: 32,
	    	}
	    },
	    messages: {
	    	name: {
	    		required: "主机名不能为空",
	    		maxlength: "长度不能超过12位",
	    		remote: "主机名已存在",
	    	},
	    	identifyCode: {
	    		required: "识别码不能为空",
	    		maxlength: "长度不能超过32位"
	    	},
	    	cellPrefix: {
	    		required: "格口前缀不能为空",
	    		maxlength: "长度不能超过32位"
	    	}
	     },
	     submitHandler: function(){ //验证通过，提交表单
	 		const formData = $.getFormObject(computerForm);
	 		if(formData.id){
	 			$.putJSON(Api.url("computer"), formData, computerCallback)
	 		} else{
	 			$.postJSON(Api.url("computer"), formData, computerCallback)
	 		}
	     }
    })
    
    // 更新、新增主机后的回调函数
    var computerCallback = function(response){
		if(response.code == 200){
			//关闭modal
			computerModal.modal("hide")
			//刷新表格
			$.refreshTableManual(computerTbl);
		}
	}    
    
    // 点击查询按钮
	$("#computerListSearchBtn").on("click", function(){
		$.refreshTable(computerTbl);
	})
	
	// 点击新增按钮
	$("#openComputerAddBtn").on("click", function(){
		computerModal.modal();
	})
	
	// 点击弹框中的保存按钮
	$("#computerSaveBtn").on("click", function(){
		computerForm.submit();
	})
	
	// 点击删除按钮
	$("#computerDeleteBtn").on("click", function(){
		const id = $("#deleteComputerId").val()
		$.deleteJSON(Api.url("computer", id), {}, function(response){
			if(response.code == 200){
				// 关闭弹窗
				computerDelModal.modal("hide")
				// 刷新表格
				$.refreshTable(computerTbl);
				
				toastr.success(response.message);
			} else {
				// 关闭弹窗
				computerDelModal.modal("hide")
				toastr.warning(response.message);
			}
		})
	})
    	
})