/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
	infoData : {},
	instance : null,
	validateFields : {
		procName : {
			validators : {
				notEmpty : {
					message : '工序名称不能为空'
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
	parent.layer.close(window.parent.MgrProcInfo.layerIndex);
};

/**
 * 收集数据
 */
infoDlg.collectData = function() {
	this.set('id').set('procName');
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

	var url = Feng.ctxPath + "/procinfo/add";
	var msg = "添加成功!";
	var err = "添加失败!";
	if ($("#id").val() != '') {
		url = Feng.ctxPath + "/procinfo/edit";
		msg = "修改成功!";
		err = "修改失败!";
	}

	// 提交信息
	var ajax = new $ax(url, function(data) {
		window.parent.Feng.success(msg);
		window.parent.MgrProcInfo.oTable.refresh();
		infoDlg.close();
	}, function(data) {
		window.parent.Feng.error(msg + data.responseJSON.message + "!");
	});
	ajax.set(this.infoData);
	ajax.start();
};

$(function() {
	Feng.initValidator("infoForm", infoDlg.validateFields);
});