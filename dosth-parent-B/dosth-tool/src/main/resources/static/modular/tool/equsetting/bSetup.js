/**
 * 系统设置
 */
var bSetup = {
	validateFields : {
		DET_BOARD_NO_B : {
			validators : {
				notEmpty : {
					message : 'B柜栈号不能为空'
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
bSetup.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 验证数据是否为空
 */
bSetup.validate = function() {
	$('#bForm').data("bootstrapValidator").resetForm();
	$('#bForm').bootstrapValidator('validate');
	return $("#bForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
bSetup.editSubmit = function() {
	if (!this.validate()) {
		return;
	}

	var data = [ {
		equSettingId : $("#equSettingId").val(),
		cabinetType : $("#cabinetType").val(),
		DET_BOARD_NO_B : $("#DET_BOARD_NO_B").val(),
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
			bSetup.close();
		},
		error : function(err) {
			window.parent.Feng.error(err.responseJSON.message);
		}
	});
};

$(function() {
	Feng.initValidator("bForm", bSetup.validateFields);
});