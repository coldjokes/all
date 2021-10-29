/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
	infoData : {}
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
	if (!this.validate()) {
		return;
	}

	var infoData = {};
	infoData["equSettingId"] = $("#cabinetId").val();
	infoData["ip"] = $("#ip").val();
	infoData["port"] = $("#port").val();

	var equSettingId = $("#cabinetId").val();
	var url = Feng.ctxPath + "/equsetting/editCabinet/" + equSettingId;
	var err = "修改失败!";

	// 提交信息
	var ajax = new $ax(url, function(data) {
		Feng.success(data.message);
	}, function(data) {
		Feng.error(data.message + data.responseJSON.message + "!");
	});

	ajax.set(infoData);
	ajax.start();
};

$(function() {
	Feng.initValidator("infoForm", infoDlg.validateFields);

	$("input[type='button']").click(
			function() {
				var infoData = {};
				var id = $(this).parent().parent().attr("id");
				if (!id) {
					id = "";
				}
				infoData["id"] = id;
				infoData["cabinetId"] = $("#cabinetId").val();
				infoData["plcSettingId"] = $(this).parent().attr("id");
				infoData["settingVal"] = $(this).parent().parent().find(
						"input[type='text']").val();
				var url = Feng.ctxPath + "/equsetting/saveCabinetPlcSetting";
				// 提交信息
				var ajax = new $ax(url, function(data) {
					Feng.success(data.message);
					MgrSmartCabinet.getCard($("#cabinetId").val());
				}, function(data) {
					Feng.error(data.message + "!");
				});
				ajax.set(infoData);
				ajax.start();
			});
});