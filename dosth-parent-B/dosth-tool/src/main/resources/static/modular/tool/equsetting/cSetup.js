/**
 * 系统设置
 */
var cSetup = {
	validateFields : {
		DET_COM : {
			validators : {
				notEmpty : {
					message : '行列式端口不能为空'
				}
			}
		},
		DET_BAUD : {
			validators : {
				notEmpty : {
					message : '行列式波特率不能为空'
				}
			}
		},
		DET_DOOR_HEIGHT : {
			validators : {
				notEmpty : {
					message : '门高不能为空'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		},
		DET_LEVEL_HEIGHT : {
			validators : {
				notEmpty : {
					message : '初始层高不能为空'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		},
		DET_LEVEL_SPACING : {
			validators : {
				notEmpty : {
					message : '层间距不能为空'
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
cSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 验证数据是否为空
 */
cSetup.validate = function() {
	$('#cForm').data("bootstrapValidator").resetForm();
	$('#cForm').bootstrapValidator('validate');
	return $("#cForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
cSetup.editSubmit = function() {
	if (!this.validate()) {
		return;
	}

	var data = [ {
		equSettingId : $("#equSettingId").val(),
		cabinetType : $("#cabinetType").val(),
		DET_COM : $("#DET_COM").val(),
		DET_BAUD : $("#DET_BAUD").val(),
		DET_PIN : $("#DET_PIN").val(),
		DET_DOOR_HEIGHT : $("#DET_DOOR_HEIGHT").val(),
		DET_LEVEL_HEIGHT : $("#DET_LEVEL_HEIGHT").val(),
		DET_LEVEL_SPACING : $("#DET_LEVEL_SPACING").val(),
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
			window.parent.tb1.bootstrapTable('refresh');
			cSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};

$(function() {
	Feng.initValidator("cForm", cSetup.validateFields);
});