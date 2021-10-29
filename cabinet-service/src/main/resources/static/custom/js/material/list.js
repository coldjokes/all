$(function(){
	const sourcePictureHtml = $("#pictureDiv").html();
	const sourceBlueprintHtml = $("#blueprintDiv").html();
	
	var matForm = $("#matForm");
	var matSearchForm = $("#matSearchForm");
	var matModal = $("#matModal");
	var matDelModal = $("#matDeleteModal");
	var matRemindModal = $("#matRemindModal");
	
	// 初始化弹出层
	matModal.modal({
		backdrop: 'static', 
		keyboard: false,
		show: false
	}).on("hide.bs.modal", function(e){
		// 关闭时重置表单
		$("#matForm [name='id']").val("");
		$("#matForm [name='picture']").val("");
		$("#matForm [name='blueprint']").val("");
		$("#error-msg").html("");
		
		matForm[0].reset();

		//清空所有错误信息
		matFormValidator.resetForm();
	}).on("show.bs.modal", function(e){
		initMatPicture(sourcePictureHtml);
		initMatBlueprint(sourceBlueprintHtml);
	});

	//渲染表格
	var tbl = $("#matListTbl").bootstrapTable({
		 url: Api.url("material"),
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
				 field: "picture",
				 title: "图片",
				 align: "center",
				 formatter : function (value, row, index) {
					 if(value){
						 return "<img style='width: 30px;height: 30px;' src='"+value+"' alt=''>"
					 } else{
						 return "<img style='width: 30px;height: 30px;' src='/static/custom/images/material/default.png' alt=''>"
					 }
				 }
            }, {
            	field: "name",
                title: "物料编码",
                align: "center",
                sortable : true,
            }, {
                field: "no",
                title: "刀片型号",
                align: "center",
                sortable : true,
		    }, {
		    	field: "spec",
		    	title: "图纸编号",
                align: "center",
                sortable : true,
		    }, {
		    	field: "warnVal",
		    	title: "预警值",
                align: "center",
                sortable : true,
		    }, {
		    	field: "blueprint",
		    	title: "图纸",
                align: "center",
                formatter : function (value, row, index) {
                	if(value){
                		return `<a class='openBlueprint' data-id=${row.id}>查看</a>`
                	} 
                }
		    }, {
		    	field: "remark",
		    	title: "备注",
                align: "center",
                sortable : true,
		    }, {
                field: "id",
                title: "操作",
                align: "center",
                width:"10%",
                formatter: function(value, row, index){
                	const source = row.source;
                	const name = row.name;
                	const no = row.no;
                	const spec = row.spec;
                	const remark = row.remark;

                	let btnStatus;
                	if(source == "outer"){
                		btnStatus = "disabled";
                	}
                	
                	return [
                		`<button class='btn btn-xs btn-warning matEditBtn' type='button' data-id=${value} title='修改'><i class='fa fa-pencil'></i> </button>`,
                		"&nbsp;",
                		`<button class='btn btn-xs btn-danger matlDelBtn' type='button' data-id=${value} data-name=${name} ${btnStatus} title='删除'><i class='fa fa-trash'></i></button>`,
                		"&nbsp;",
                		`<button class='btn btn-xs btn-info matlRemindBtn' type='button' data-id=${value} data-name=${name} data-no=${no} data-spec=${spec} data-remark=${remark} title='加入提醒'><i class='fa fa-bell'></i></button>`,
                		].join("");
                }
            }
        ],
        queryParams : function (params) { //查询的参数
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject(matSearchForm);
        	return $.extend({}, defaultParams, formData);
        },
        onLoadSuccess: function (res) {

        	// 打开图纸浏览
        	$(".openBlueprint").on("click", function(e){
        		const id = $(this).data("id");
        		const data = res.rows.find(item => item.id == id);
        		const blueprint = data.blueprint;
        		
        		if(blueprint){
        			for(let x of blueprint.split(",")){
                    	$("#matBlueprintContainer").append(`<li><img data-original="${x}" src="${x}"></li>`)
                    }
        			var viewer = new Viewer(document.getElementById('matBlueprintContainer'), {
        				url: 'data-original',
        				hidden: function () {
        					viewer.destroy();
        					$("#matBlueprintContainer").html("");
        				},
        			});
        			viewer.show();
        		}
        	})
        	
        	//物料列表编辑按钮
        	$(".matEditBtn").on("click", function(e){
        		matModal.modal();
        		
        		const id = $(this).data("id");
        		const data = res.rows.find(item => item.id == id);
        		
        		// 取出本行数据
        		matForm.initForm(data);
        		
        		// 回填图片
        		const picture = data.picture;
        		if(picture){
        			initMatPicture(sourcePictureHtml, [picture]);
        		}
        		
        		// 回填图纸
        		const blueprint = data.blueprint;
        		if(blueprint){
        			
        			let blueprintArr = [];
        			for(let x of blueprint.split(",")){
        				blueprintArr.push(x);
                    }
        			initMatBlueprint(sourceBlueprintHtml, blueprintArr);
        		}
	       	 })
	       	 
	       	 //物料列表删除按钮
	       	 $(".matlDelBtn").on("click", function () {
	       		 matDelModal.modal();
	       		 $("#deleteNameText").html($(this).data("name"))
	       		 $("#deleteMatId").val($(this).data("id"));
            })
            
            //物料列表提醒按钮
            $(".matlRemindBtn").on("click", function () {
            	
            	const id = $(this).data("id");
            	const name = $(this).data("name");
            	const no = $(this).data("no");
            	const spec = $(this).data("spec");
            	const remark = $(this).data("remark");
            	
            	//先检查此物料是否已有库存
            	$.getJSON(Api.url("materialRemindCheck"), {materialId: $(this).data("id")}, function(response){
            		
            		let msg1 = '物料已有库存，';
            		let msg2 = '是否将此物料加入库存提醒列表？';
                    if(response.code == 200){
                    	$("#remindMsg").html(msg1 + msg2);
                    } else {
                    	$("#remindMsg").html(msg2);
                    }
                	matRemindModal.modal();
                	
                	$("#remindMatId").val(id);
                	$("#remindMatName").val(name);
                	$("#remindMatNo").val(no);
                	$("#remindMatSpec").val(spec);
                	$("#remindMatRemark").val(remark);
                    
                });
            })
        },
    });

	//表单验证
	var matFormValidator = matForm.validate({
		rules: {
			name: {
				required: true,
				maxlength: 100
	    	},
	    	no:{
	    		required: true,
	    		maxlength: 100
	    	},
	    	spec:{
	    		required: true,
	    		maxlength: 100
	    	},
	    	warnVal:{
	    		digits: true,
	    		maxlength: 2
	    	}
	    },
	    messages: {
	    	name: {
	    		required: "物料编码不能为空",
	    		maxlength: "长度不能超过100位"
	    	},
	    	no: {
	    		required: "刀片型号不能为空",
	    		maxlength: "长度不能超过100位"
	    	},
	    	spec: {
	    		required: "图纸编号不能为空",
	    		maxlength: "长度不能超过100位"
	    	},
	    	warnVal: {
	    		digits: "请输入有效数字",
	    		maxlength: "长度不能超过2位"
	    	}
	     },
	     submitHandler: function(){ //验证通过，提交表单
	 		const formData = $.getFormObject(matForm);
	 		const oldId = formData.id
			
 			$.getJSON(Api.url("noSpecCheck"), {no: formData.no, spec:formData.spec}, function(data){
 				if(data.total > 0){
 					var dbResult = data.results[0];
 					if(dbResult.id != oldId){
 						$("#error-msg").html("相同的物料编码或型号已存在，请重新输入！")
 						return;
 					}
 				} 
 				
 				if(formData.id){
 					$.putJSON(Api.url("material"), formData, matCallback)
 				} else {
 					$.postJSON(Api.url("material"), formData, matCallback)
 				}
 			})
	     }
    })
		
    // 更新、新增用户后的回调函数
    var matCallback = function(response){
		if(response.code == 200){
			//关闭modal
			matModal.modal("hide")
			//刷新表格
			$.refreshTable(tbl);
		}
	}

	// 点击查询按钮
	$("#matListSearchBtn").on("click", function(){
		$.refreshTable(tbl);
	})
	
	//点击新增按钮
	$("#openUserAddBtn").on("click", function(){
		matModal.modal();
	})
	
	// 点击保存按钮
	$("#matSaveBtn").on("click", function(){
		matForm.submit();
	})

	// 点击删除按钮
	$("#matDeleteBtn").on("click", function(){
		const id = $("#deleteMatId").val()
		$.deleteJSON(Api.url("material", id), {}, function(response){
			if(response.code == 200){
				// 关闭弹窗
				matDelModal.modal("hide")
				// 刷新表格
				$.refreshTable(tbl);
			}
		})
	})
	// 点击加入提醒按钮
	$("#matRemindBtn").on("click", function(){
		
		const matId = $("#remindMatId").val(); 
		const matName = $("#remindMatName").val(); 
		const matNo = $("#remindMatNo").val(); 
		const matSpec = $("#remindMatSpec").val(); 
		const matRemark = $("#remindMatRemark").val(); 
		
		const data = {
				materialId: matId,
				name: matName,
				no: matNo,
				spec: matSpec,
				remark: matRemark
		}
		
		$.postJSON(Api.url("materialRemind"), data, function(response){
			// 关闭弹窗
			matRemindModal.modal("hide")
			
			let msg = response.message;
			
			if(response.code == 200){
				// 刷新表格
				$.refreshTable(tbl);
				
				toastr.success(msg)
			} else {
				toastr.warning(msg + "，库存提醒列表中已存在相同物料!");
			}
		})
	})
	
	// 点击导出按钮
	$("#matListExportBtn").on("click", function(){
		location.href = Api.url("matExport") + "?" + matSearchForm.serialize();
	})
	
	// 物料导入按钮
	$("#matImport").on("click", function(){ 
		//触发物料上传
		$("#matImportInput").click();
	})
	
	$("#matImportInput").on("change", function(){
		if($(this).val()){ //上传框不为空才开始上传
			$.importFile(Api.url("matImport"), "matImportInput", function(response){
				const msg = response.message;
				if (response.code===200) {
					toastr.success(msg)
					$.refreshTableManual(tbl);
				}else{
					toastr.warning(msg);
				}
			})
			//清空文件框
			$(this).val("");
		}
	})
	
	// 物料导入模板
	$("#matImportTemplate").on("click", function(){
		window.open("/static/物料导入模板.zip", '_self');
	})
	
})

function initMatPicture(sourcePictureHtml, initialPreviewData){
	$("#pictureDiv").children().remove();
	$("#pictureDiv").html(sourcePictureHtml);
	var option = {
		    overwriteInitial: true,
		 	language:'zh',
		    maxFileSize: 2048,
		    showCaption: false,
		    showBrowse: false,
		    showRemove : false,
		    showUpload: false,
		    uploadUrl: Api.url("matPicture"),
	        browseOnZoneClick: true,
		    defaultPreviewContent: '<img src="/static/custom/images/material/default.png" style="width:120px;" alt="物料图片"><h6 class="text-muted">点击选择图片</h6>',
		    allowedFileExtensions: ["jpg", "png", "gif"],
		    layoutTemplates:{
		    	footer :"",
		    },
			initialPreviewAsData: true,
		}
	if(initialPreviewData){
		option.initialPreview = initialPreviewData;
	}
	 
	 $("#matForm input[name='prePicture']").fileinput(option).on("filebatchselected", function(event, files) {  //实现图片被选中后自动提交
		 $(this).fileinput("upload");
	 }).on("fileuploaded", function(event, data) {
		 const response = data.response;
		 if(response.code == 200){
			 $("#matForm input[name='picture']").val(response.results[0]);
		 };
	 }).on('filesuccessremove', function(event, id, index) {
	   	 $("#matForm [name='picture']").val("");
	 }).on('fileclear', function(event, id, index) {
	   	 $("#matForm [name='picture']").val("");
	 });
}
function initMatBlueprint(sourceBlueprintHtml, initialPreviewData){
	$("#blueprintDiv").children().remove();
	$("#blueprintDiv").html(sourceBlueprintHtml);
	var option = {
			overwriteInitial: false,
			initialCaption:"请选择图纸",
			language:'zh',
		    theme: "explorer-fa",
		    uploadUrl: Api.url("matBlueprint"),
		    maxFileSize: 2048,
		    showClose: false,
		    showUpload: false, //是否显示上传按钮
		    showRemove : false, //显示移除按钮
		    deleteUrl: Api.url("matBlueprintDel"),
		    dropZoneEnabled: false,//是否显示拖拽区域
		    maxFileCount: 5, //表示允许同时上传的最大文件个数
		    autoReplace: false, //是否自动替换当前图片
		    allowedFileExtensions: ["jpg", "png", "gif"],
		    fileActionSettings: {                               // 在预览窗口中为新选择的文件缩略图设置文件操作的对象配置
	           showRemove: true,                                   // 显示删除按钮
	           showZoom: false,                                    // 显示预览按钮
	           showUpload: false,                                   // 显示上传按钮
	           showDrag: false,
		    },
		    initialPreviewAsData: true,
	 }
	
	if(initialPreviewData){
		var previewConfig = [];
		for(let x of initialPreviewData){
			previewConfig.push({
				key: x,
				caption: x.split("/file/blueprint/")[1]
			});
		}
		
		option.initialPreview = initialPreviewData;
		option.initialPreviewConfig = previewConfig;
	}
	
	$("#matForm input[name='preBlueprint']").fileinput(option
	).on("filebatchselected", function(event, files) {  //实现图片被选中后自动提交
		$(this).fileinput("upload");
	}).on("fileuploaded", function(event, data, index, fileId) {
		const response = data.response;
		if(response.code == 200){
			const result = response.results[0];
			
			let newArr = [];
			
			//如果已经存在图纸，则将路径继续拼接在后面
			let currentBlueprint = $("#matForm [name='blueprint']").val();
			if(currentBlueprint){
				newArr = currentBlueprint.split(",");
			}
			newArr.push(result);
			
			$("#matForm input[name='blueprint']").val(newArr.join(","));
			
			//重新渲染图纸上传框
			initMatBlueprint(sourceBlueprintHtml, newArr);
		};
	}).on('filedeleted', function(event, id, index) { //删除回填的图片
		var current = $("#matForm [name='blueprint']").val();
		var newArr = [];
		for(let x of current.split(",")){
			if(x != id){
				newArr.push(x);
			}
        }
		$("#matForm [name='blueprint']").val(newArr.join(","));
	});
}
