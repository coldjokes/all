/**
 * 系统设置
 */
var stoSetup = {
	validateFields : {
		STORE_COM : {
			validators : {
				notEmpty : {
					message : '锁控板端口不能为空'
				}
			}
		},
		STORE_BAUD : {
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
stoSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 验证数据是否为空
 */
stoSetup.validate = function() {
	$('#stoForm').data("bootstrapValidator").resetForm();
	$('#stoForm').bootstrapValidator('validate');
	return $("#stoForm").data('bootstrapValidator').isValid();
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
stoSetup.delBtn = function() {
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
stoSetup.editSubmit = function() {
	if (!this.validate()) {
		return;
	}
	
	var data = [];
	var maxId = $("#maxId").val();
	
	for (var i = 0; i <= maxId; i++) {
		var STORE_BOARD_NO = $("#boardNo_" + i).val();
		var STORE_ROW_NO = $("#rowNo_" + i).val();
		var STORE_COL_NO = $("#colNo_" + i).val();
		var STORE_COM = $("#STORE_COM").val();
		var STORE_BAUD = $("#STORE_BAUD").val();
		var equSettingId = $("#equSettingId").val();
		var cabinetType = $("#cabinetType").val();
		var obj = {
			STORE_BOARD_NO : STORE_BOARD_NO,
			STORE_ROW_NO : STORE_ROW_NO,
			STORE_COL_NO : STORE_COL_NO,
			STORE_COM : STORE_COM,
			STORE_BAUD : STORE_BAUD,
			equSettingId : equSettingId,
			cabinetType : cabinetType,
		};
		
		if(STORE_BOARD_NO == null || STORE_BOARD_NO == "" || STORE_BOARD_NO == 0){
			window.parent.Feng.info("栈号不能为空或零");
			return;
		}
		if(STORE_ROW_NO == null || STORE_ROW_NO =="" || STORE_ROW_NO == 0){
			window.parent.Feng.info("行号不能为空或零");
			return;
		}
		if(STORE_COL_NO == null || STORE_COL_NO == "" || STORE_COL_NO == 0){
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
			stoSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};

$(function() {
	if(stoList.length > 0){
		for(var i = 0; i < stoList.length; i++){
			$("#maxId").val(i);
			$("#addDiv").append("");
			$("#addDiv").append(appendHtml( i, stoList[i].boardNo, stoList[i].rowNo, stoList[i].colNo));
		}
	} else {
		$("#maxId").val(0);
		$("#addDiv").append(appendHtml(0));
	}
	
	Feng.initValidator("stoForm", stoSetup.validateFields);
});