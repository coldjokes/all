/**
 * 系统设置
 */
var temSetup = {
	validateFields : {
		TEM_COM : {
			validators : {
				notEmpty : {
					message : '锁控板端口不能为空'
				}
			}
		},
		TEM_BAUD : {
			validators : {
				notEmpty : {
					message : '锁控板波特率不能为空'
				}
			}
		}
	}
};

/**
 * 关闭此对话框
 */
temSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 验证数据是否为空
 */
temSetup.validate = function() {
	$('#temForm').data("bootstrapValidator").resetForm();
	$('#temForm').bootstrapValidator('validate');
	return $("#temForm").data('bootstrapValidator').isValid();
};

/**
 * 加号事件
 */
$("body").on('click', '.addInput', function() {
	var maxNum = parseInt($("#maxId").val()) + 1;
	$("#maxId").val(maxNum);
	$("#addDiv").append(appendHtml($("#maxId").val(), '', '', ''));
});

/**
 * 清空事件
 */
temSetup.delBtn = function() {
	$("#maxId").val(0);
	$("#addDiv").empty();
	$("#addDiv").append(appendHtml($("#maxId").val(), '', '', ''));
};

function appendHtml(rowId, boardNo, rowNo, colNo){
	var html = `
		<div class="form-group" id="${rowId != null ? rowId : ''}">
			<label class="col-sm-2 control-label">栈号</label>
			<div class="col-sm-2">
				<input class="form-control" type="text" id="${rowId != null ? 'boardNo_' + rowId : ''}"
					value="${boardNo != null ? boardNo : ''}"/>
			</div>
			<label class="col-sm-1 control-label">行号</label>
			<div class="col-sm-2">
				<input class="form-control" type="text" id="${rowId != null ? 'rowNo_' + rowId : ''}"
					value="${rowNo != null ? rowNo : ''}"/>
			</div>
			<label class="col-sm-1 control-label">列号</label>
			<div class="col-sm-2">
				<input class="form-control" type="text" id="${rowId != null ? 'colNo_' + rowId : ''}"
					value="${colNo != null ? colNo : ''}"/>
			</div>
		</div>`;
	return html;
}

/**
 * 提交信息
 */
temSetup.editSubmit = function() {
	if (!this.validate()) {
		return;
	}
	
	var data = [];
	var maxId = $("#maxId").val();
	
	for (var i = 0; i <= maxId; i++) {
		var TEM_BOARD_NO = $("#boardNo_" + i).val();
		var TEM_ROW_NO = $("#rowNo_" + i).val();
		var TEM_COL_NO = $("#colNo_" + i).val();
		var TEM_COM = $("#TEM_COM").val();
		var TEM_BAUD = $("#TEM_BAUD").val();
		var equSettingId = $("#equSettingId").val();
		var cabinetType = $("#cabinetType").val();
		var obj = {
			TEM_BOARD_NO : TEM_BOARD_NO,
			TEM_ROW_NO : TEM_ROW_NO,
			TEM_COL_NO : TEM_COL_NO,
			TEM_COM : TEM_COM,
			TEM_BAUD : TEM_BAUD,
			equSettingId : equSettingId,
			cabinetType : cabinetType,
		};
		
		if(TEM_BOARD_NO == null || TEM_BOARD_NO == "" || TEM_BOARD_NO == 0){
			window.parent.Feng.info("栈号不能为空或零");
			return;
		}
		if(TEM_ROW_NO == null || TEM_ROW_NO =="" || TEM_ROW_NO == 0){
			window.parent.Feng.info("行号不能为空或零");
			return;
		}
		if(TEM_COL_NO == null || TEM_COL_NO == "" || TEM_COL_NO == 0){
			window.parent.Feng.info("列号不能为空或零");
			return;
		}
		data.push(obj);
	}
	
	var url = Feng.ctxPath + "/equsetting/editSetup";
	$.ajax({
		url : url,
		type : "post",
		dataType : "json",
		data : {
			"data" : JSON.stringify(data)
		},
		success : function(data) {
			window.parent.Feng.success(data.message);
			window.parent.tb2.bootstrapTable('refresh');
			temSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};

$(function() {
	if(temList.length > 0){
		for(var i = 0; i < temList.length; i++){
			$("#maxId").val(i);
			$("#addDiv").append("");
			$("#addDiv").append(appendHtml( i, temList[i].boardNo, temList[i].rowNo, temList[i].colNo));
		}
	} else {
		$("#maxId").val(0);
		$("#addDiv").append(appendHtml(0));
	}
	
	Feng.initValidator("temForm", temSetup.validateFields);
});