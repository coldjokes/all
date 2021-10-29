/**
 * 智能柜设置-card、channel设置
 */
var infoDlg = {
	infoData : {},
	validateFields : {
		ip : {
			validators : {
				notEmpty : {
					message : 'ip'
				}
			}
		},
		port : {
			validators : {
				notEmpty : {
					message : '端口'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		}
	}
};

/**
 * 清除数据
 */
infoDlg.clearData = function() {
	this.infoData = {};
};

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
infoDlg.set = function(key, val) {
	this.infoData[key] = (typeof value == "undefined") ? $("#" + key).val()
			: value;
	return this;
};

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
infoDlg.get = function(key) {
	return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
infoDlg.close = function() {
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 收集数据
 */
infoDlg.collectData = function() {
	this.set('id').set('equSettingId').set('rowNo').set('ip').set('port');
};

/**
 * 验证数据是否为空
 */
infoDlg.validate = function() {
	$('#infoForm').data("bootstrapValidator").resetForm();
	$('#infoForm').bootstrapValidator('validate');
	return $("#infoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
infoDlg.editSubmit = function() {
	this.clearData();
	this.collectData();
	if (!this.validate()) {
		return;
	}

	var ajax = new $ax(Feng.ctxPath + "/equsetting/editEquDetail", function(
			data) {
		Feng.success("修改成功!");
	}, function(data) {
		Feng.error("修改失败!" + data.responseJSON.message + "!");
	});
	ajax.set(this.infoData);
	ajax.start();
};

/**
 * 修改channel状态
 */
infoDlg.edit = function(equDetailStaId) {
	var url = Feng.ctxPath + "/equsetting/updateChannelStatus/"
			+ equDetailStaId;
	// 提交信息
	var ajax = new $ax(url, function(data) {
		Feng.success(data.message);
		infoDlg.flushStaList();
	}, function(data) {
		Feng.error("修改失败!" + data.responseJSON.message + "!");
	});
	ajax.start();
}

/**
 * 刷新
 */
infoDlg.flushStaList = function() {
	var ajax = new $ax(Feng.ctxPath + "/equsetting/getStaList/"
			+ $("#id").val(), function(data) {
		$("#stalist").html(data);
	}, function(data) {
		Feng.error("设置失败," + data + "!");
	}, "html");
	ajax.start();
}

$(function() {
	Feng.initValidator("infoForm", infoDlg.validateFields);
	infoDlg.flushStaList();
});