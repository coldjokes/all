/**
 * 系统设置
 */
var plcSetup = {
	validateFields : {
		PLC_IP : {
			validators : {
				notEmpty : {
					message : 'PLC IP不能为空'
				}
			}
		},
		PLC_PORT : {
			validators : {
				notEmpty : {
					message : 'PLC PORT不能为空'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		},
		FACE_LOGIN : {
			validators : {
				notEmpty : {
					message : '人脸识别不能为空'
				}
			}
		}
	}
};

/**
 * 关闭此对话框
 */
plcSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 验证数据是否为空
 */
plcSetup.validate = function() {
	$('#plcForm').data("bootstrapValidator").resetForm();
	$('#plcForm').bootstrapValidator('validate');
	return $("#plcForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
plcSetup.editSubmit = function() {
	if (!this.validate()) {
		return;
	}

	var data = [ {
		equSettingId : $("#equSettingId").val(),
		cabinetType : $("#cabinetType").val(),
		PLC_IP : $("#PLC_IP").val(),
		PLC_PORT : $("#PLC_PORT").val(),
		FACE_LOGIN : $("#FACE_LOGIN").val(),
		PRINT_COM : $("#PRINT_COM").val(),
		PRINT_CUT : $("#PRINT_CUT").val(),
		PRINT_TYPE_CODE:$("#PRINT_TYPE_CODE").val(),
		SCAN_COM : $("#SCAN_COM").val(),
		SCAN_TYPE : $("#SCAN_TYPE").val(),
		SCAN_BAUD : $("#SCAN_BAUD").val()
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
			window.parent.tb2.bootstrapTable('refresh');
			plcSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};

$(function() {
	Feng.initValidator("plcForm", plcSetup.validateFields);
});