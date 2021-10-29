var cabinetTbl;
var computerList;
var oldRowNum;
$(function(){
	
	var cabinetForm = $("#cabinetForm");
	var cabinetModal = $("#cabinetModal");
	var cabinetCellModal = $("#cabinetCellModal");
	var cabinetDelModal = $("#cabinetDelModal");
	
	$("#cabinetForm [name='rowNum']").on("keyup", function(){
		var rowNum = $(this).val();

		//行数不能超过
		if(rowNum > 100){
			rowNum = 100;
			$(this).val(rowNum);
		}
		
		//输入不一致时才会触发
		if(oldRowNum != rowNum){
			$("#rows").html("");
			for(var i = 1; i <= rowNum; i ++){
				$("#rows").append(rowTemplate(i, 10));
			}
		}
		oldRowNum = rowNum;
	})
	
	
	// 初始化弹出层
	cabinetModal.modal({
		backdrop: 'static', 
		keyboard: false,
		show: false
	}).on("hide.bs.modal", function(e){
		// 关闭时重置表单
		$("#cabinetForm [name='id']").val("");
		cabinetForm[0].reset();
		
		// 清空行信息
		$("#rows").html("");
		
		//清空所有错误信息
		cabinetFormValidator.resetForm();
	});
	
	cabinetCellModal.modal({
		backdrop: 'static', 
		keyboard: false,
		show: false
	}).on("hide.bs.modal", function(e){
		// 关闭时重置表单
		$("#rowSetting").html("");
	});
	
	
	
	//渲染表格
	cabinetTbl = $("#cabinetListTbl").bootstrapTable({
      url: Api.url("cabinet"),
      pagination: false,
      columns: [
    	    {
    		   field: "seq",
    		   title: "序号",
    		   width:"5%",
    		   align: "center",
    		   formatter: function(value, row, index){
    			   return $.getTableRowNum(cabinetTbl, index);
    		   }
          	}, {
                field: "name",
                title: "柜体名称",
                align: "center",
            }, {
                field: "computerName",
                title: "主机名称",
                align: "center"
            }, {
                field: "computerPort",
                title: "主机端口",
                align: "center"
            }, {
		    	field: "computerBaudRate",
		    	title: "端口波特率",
                align: "center"
		    }, {
            	field: "id",
                title: "操作",
                align: "center",
                width:"10%",
                formatter: function(value, row, index){
                	const name = row.name;
                	return [
                		`<button class='btn btn-xs btn-warning openCabinetEditBtn' type='button' data-id=${value} title='修改柜体'><i class='fa fa-pencil'></i> </button>`,
                		"&nbsp;",
                		`<button class='btn btn-xs btn-info openCabinetCellEditBtn' type='button' data-id=${value} title='修改格口'><i class='fa fa-pencil-square'></i> </button>`,
                		"&nbsp;",
                		`<button class='btn btn-xs btn-danger openCabinetDelBtn' type='button' data-id=${value} data-name=${name} title='删除'><i class='fa fa-trash'></i></button>`
                		].join("");
                }
            }
        ],
        queryParams : function (params) { //查询的参数
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject($("#cabinetSearchForm"));
        	return $.extend({}, defaultParams, formData);
        },
        onLoadSuccess: function (res) { //页面加载完毕后绑定事件
        	// 打开编辑弹框
        	$(".openCabinetEditBtn").on("click", function(e){

        		const id = $(this).data("id");
        		const data = res.rows.find(item => item.id == id);
        		
    	       	//渲染主机选择下拉框
	     		$.getJSON(Api.url("computer"), {}, function(response){
	     			if(response.code == 200){
	     				computerList = response.results;
	     				var computerListHtml = "";
	     				for(let x of computerList){
	     					computerListHtml += `<option value=${x.id}>${x.name}</option>`;
	     				}
	     				$(".computer").html("");
	     				$(".computer").append(computerListHtml);
	     				
	     				//渲染表单
	     				cabinetModal.modal();
	            		var rows = data.rows;
	            		data.rowNum = rows.length;
	            		
	            		for(var i = 0; i < rows.length; i ++){
	            			const cells = rows[i].cells;
	            			
	    	        		$("#rows").append(rowTemplate(i+1, cells.length));
	            		}
	            		cabinetForm.initForm(data);
	     			}
	     		})
	       	 })
	       	 
	       	 // 打开格口编辑弹框
	       	 $(".openCabinetCellEditBtn").on("click", function(e){
	       		 cabinetCellModal.modal();
	       		
	       		 const id = $(this).data("id");
	       		 const data = res.rows.find(item => item.id == id);
	       		 
	       		 const rows = data.rows;
	       		 rows.forEach((row, rowIndex)=>{
	       		    const rowId = row.id;
	       		    const rowName = row.name;
	       		    $("#rowSetting").append(rowSettingTemplate(rowId, rowName));
	       		    
	       		    const cells = row.cells;
	       		    
	       		    cells.forEach((cell, cellIndex)=> {
	       		    	$("#row_" + rowId).append(cellSettingTemplate(cell.id, cell.name, cell.stack, cell.pin));
	       		    })
	       		 })
	       		 
	       	 })
	       	 
	       	 // 打开删除弹框
	       	 $(".openCabinetDelBtn").on("click", function () {
	       		 cabinetDelModal.modal();
	       		 $("#deleteNameText").html($(this).data("name"))
	       		 $("#deleteCabinetId").val($(this).data("id"));
	       	 })
        	
        }, 
    });
	
	//表单验证
	var cabinetFormValidator = cabinetForm.validate({
	    rules: {
	    	name: {
	    		required: true,
	    		stringCheck: true,
	    	},
	    	computerId:{
	    		required: true
	    	},
	    	rowNum: {
	    		required: true,
	    		min: 1,
	    		max: 100
	    	}
	    },
	    messages: {
	    	name: {
	    		required: "柜体名不能为空",
	    	},
	    	computerId: {
	    		required: "主机不能为空",
	    	},
	    	rowNum: {
	    		required: "行数不能为空",
	    		min: "最小值为1",
	    		max: "最大值为100"
	    	}
	     },
	     submitHandler: function(){ //验证通过，提交表单
	 		const formData = $.getFormObject(cabinetForm);
			const computerId = formData.computerId;
			const selectedComputer = computerList.find(item => item.id == computerId);
			
			//增加主机name
			formData.computerName = selectedComputer.name;
			
			//增加rows
			var rows = [];
			
			var rowInputs = $("#rows [name='row']");
			for(var i = 0; i < rowInputs.length; i ++){
				var row = new Object();
				var cells = [];
				var cellSize = $(rowInputs[i]).val();
				
				for(var j = 0; j < cellSize; j ++){
					var cell = new Object();
					cells.push(cell);
				}
				row.cells = cells;
				rows.push(row);
			}
			formData.rows = rows;
			
	    	//关闭modal
	    	cabinetModal.modal("hide");
	    	 
	 		if(formData.id){
	 			$.putJSON(Api.url("cabinet"), formData, cabinetCallback)
	 		} else{
	 			$.postJSON(Api.url("cabinet"), formData, cabinetCallback)
	 		}
	     }
    })
	
    // 更新、新增设备后的回调函数
    var cabinetCallback = function(response){
		if(response.code == 200){
			//刷新表格
			cabinetTbl.bootstrapTable("selectPage", 1);
			cabinetTbl.bootstrapTable("refresh")
		}
	}  
    
	// 点击查询按钮
	$("#cabinetListSearchBtn").on("click", function(){
//		$.refreshTable(cabinetTbl);
		cabinetTbl.bootstrapTable("selectPage", 1);
		cabinetTbl.bootstrapTable("refresh")
	})
	
	// 点击新增按钮
	$("#openCabinetAddBtn").on("click", function(){
		
		//渲染主机选择下拉框
		$.getJSON(Api.url("computer"), {}, function(response){
			if(response.code == 200){
				computerList = response.results;
				var computerListHtml = "";
				for(let x of computerList){
					computerListHtml += `<option value=${x.id}>${x.name}</option>`;
				}
				$(".computer").html("");
				$(".computer").append(computerListHtml);
				
				cabinetModal.modal();
				$("#cabinetForm [name='rowNum']").trigger("keyup");
			}
		})
	})
	
	// 点击弹框中的保存按钮
	$("#cabinetSaveBtn").on("click", function(){
		cabinetForm.submit();
	})
	
	// 点击删除按钮
	$("#cabinetDeleteBtn").on("click", function(){
		const id = $("#deleteCabinetId").val()
		$.deleteJSON(Api.url("cabinet", id), {}, function(response){
			if(response.code == 200){
				// 关闭弹窗
				cabinetDelModal.modal("hide")
				// 刷新表格
				$.refreshTableManual(cabinetTbl);
				
				toastr.success(response.message);
			} else {
				// 关闭弹窗
				cabinetDelModal.modal("hide")
				toastr.warning(response.message);
			}
		})
	})
	
	// 点击格口设置中的保存按钮
	$("#cabinetCellSaveBtn").on("click", function(){
		var cells = [];
		$(".cell").each(function(index, cell){
			const id = $(cell).attr("id");
			const stack = $($(cell).children(".cellStack")[0]).val();
			const pin = $($(cell).children(".cellPin")[0]).val();
			cells.push({
				id: id,
				stack: stack,
				pin: pin
			})
		})
		
		$.putJSON(Api.url("cabinetCell"), cells, function(response){
			if(response.code == 200){
				// 关闭弹窗
				cabinetCellModal.modal("hide")
				// 刷新表格
				$.refreshTableManual(cabinetTbl);
			}
		})
	})
    	
})

function rowTemplate(name, value){
	var tpl = `<div class="row">
					<div class="form-group">
				   	<label class="col-sm-3 control-label">行&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${name}：</label>
				    <div class="col-sm-2">
				      <input type="text" value="${value}" class="form-control" name="row" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
				  </div>
				</div>
			</div>`
	return tpl;
}

function rowSettingTemplate(id, name){
	var tpl = `<div class="form-group">
					<label class="col-md-1 control-label">${name}</label>
				    <div class="col-md-11" id="row_${id}"></div>
				</div>`
		return tpl;
}

function cellSettingTemplate(id, name, stack, pin){
	var tpl = `<label class="control-label cellLabel">${name}</label>:
				<div class="cell" id="${id}">
					<input type="text" class="cellStack" name="stack" value="${stack}">:
					<input type="text" class="cellPin" name="pin" value="${pin}">
				</div>`
		return tpl;
}

