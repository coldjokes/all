/**
 * 系统设置
 */
var aSetup = {
	validateFields : {
		DET_BOARD_NO_A : {
			validators : {
				notEmpty : {
					message : 'A柜栈号不能为空'
				},
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
aSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 验证数据是否为空
 */
aSetup.validate = function() {
	$('#aForm').data("bootstrapValidator").resetForm();
	$('#aForm').bootstrapValidator('validate');
	return $("#aForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
aSetup.editSubmit = function() {
	if (!this.validate()) {
		return;
	}

	var data = [ {
		equSettingId : $("#equSettingId").val(),
		cabinetType : $("#cabinetType").val(),
		DET_BOARD_NO_A : $("#DET_BOARD_NO_A").val(),
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
			aSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};

$(function() {
	Feng.initValidator("aForm", aSetup.validateFields);
});