/**
 * 系统设置
 */
var trolSetup = {
	validateFields : {
		TROL_COM : {
			validators : {
				notEmpty : {
					message : '可控板端口不能为空'
				}
			}
		},
		TROL_BAUD : {
			validators : {
				notEmpty : {
					message : '可控板波特率不能为空'
				}
			}
		}
	}
};

/**
 * 关闭此对话框
 */
trolSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 加号事件
 */
$("body").on('click', '.addInput', function() {
	var rowCount = parseInt($("#maxId").val()) + 1;
	$("#maxId").val(rowCount);
	$("#editDiv input:eq(0)").attr("value", $("#maxId").val());
	$("#editDiv input:eq(1)").attr("value", $("#maxId").val());
	$("#addDiv").append($("#editDiv").html());
});

/**
 * 清空事件
 */
trolSetup.delBtn = function() {
	$("#maxId").val(1);
	$("#editDiv input:eq(0)").attr("value", $("#maxId").val());
	$("#editDiv input:eq(1)").attr("value", $("#maxId").val());
	$("#addDiv").empty();
	$("#addDiv").append($("#editDiv").html());
};

/**
 * 提交信息
 */
trolSetup.editSubmit = function() {
	
	var data = [];
	var maxId = $("#maxId").val();
	var equSettingId = $("#equSettingId").val();
	var cabinetType = $("#cabinetType").val();
	
	var divs = $("#addDiv div[class='form-group']");
	for (var index = 0; index < divs.length; index++) {
		var rowNo = $(divs[index]).find("input:eq(0)").val();
		var boardNo = $(divs[index]).find("input:eq(1)").val();
		var colNo = $(divs[index]).find("input:eq(2)").val();
		if (boardNo == '' || parseInt(boardNo) < 1) {
			window.parent.Feng.info("栈号不能为空或零");
			return;
		}
		if (colNo == '' || parseInt(colNo) < 1) {
			window.parent.Feng.info("抽屉号不能为空或零");
			return;
		}
		data.push({"rowNo": rowNo, "boardNo": boardNo, "colNo": colNo});
	}
	
	var url = Feng.ctxPath + "/equsetting/editTroSetup";
	$.ajax({
		url : url,
		type : "post",
		dataType : "json",
		data : {
			"equSettingId" : equSettingId,
			"cabinetType" : cabinetType,
			"trolCom" : $("#TROL_COM").val(),
			"trolBaud" : $("#TROL_BAUD").val(),
			"data" : JSON.stringify(data)
		},
		success : function(data) {
			window.parent.Feng.success(data.message);
			window.parent.tb2.bootstrapTable('refresh');
			trolSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};