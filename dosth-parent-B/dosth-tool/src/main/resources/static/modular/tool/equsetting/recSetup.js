/**
 * 系统设置
 */
var recSetup = {
	validateFields : {
		DET_BOARD_NO : {
			validators : {
				digits : {
					message : '只能输入数字'
				}
			}
		}
	}
};

/**
 * 关闭此对话框
 */
recSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 验证数据是否为空
 */
recSetup.validate = function() {
	$('#recForm').data("bootstrapValidator").resetForm();
	$('#recForm').bootstrapValidator('validate');
	return $("#recForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
recSetup.editSubmit = function() {
	if (!this.validate()) {
		return;
	}

	var data = [ {
		equSettingId : $("#equSettingId").val(),
		cabinetType : $("#cabinetType").val(),
		REC_SCAN_COM : $("#REC_SCAN_COM").val(),
		REC_SCAN_TYPE : $("#REC_SCAN_TYPE").val(),
		REC_SCAN_BAUD : $("#REC_SCAN_BAUD").val()
	} ]

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
			window.parent.tb1.bootstrapTable('refresh');
			recSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};

$(function() {
	Feng.initValidator("recForm", recSetup.validateFields);
});