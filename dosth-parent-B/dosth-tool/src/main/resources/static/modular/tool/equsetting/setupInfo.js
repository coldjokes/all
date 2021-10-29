/**
 * 
 */
var PlcSetupInfo = {
	equInfoInstance:null
};

PlcSetupInfo.getCard = function(cabinetId) {
	var ajax = new $ax(Feng.ctxPath + "/equsetting/card/"+cabinetId, function (data) {
        $("#editContent").html(data);
    }, function (data) {
        Feng.error("设置失败," + data + "!");
    }, "html");
    ajax.start();
};

/**
 * 点击树形节点事件
 */
PlcSetupInfo.onClickEquInfo = function(e, treeId, treeNode) {
	var editId = treeNode.id.length > 2 ? treeNode.id.substring(2) : treeNode.id;
    $("#editId").val(editId);
    if (treeNode.id.startsWith("B_")) {
    	var ajax = new $ax(Feng.ctxPath + "/equsetting/card/"+editId, function (data) {
            $("#editContent").html(data);
        }, function (data) {
            Feng.error("设置失败," + data + "!");
        }, "html");
        ajax.start();
    }
    if (treeNode.id.startsWith("C_")) {
    	var ajax = new $ax(Feng.ctxPath + "/equsetting/channel/"+editId, function (data) {
            $("#editContent").html(data);
        }, function (data) {
            Feng.error("设置失败," + data + "!");
        }, "html");
        ajax.start();
    }
};

/**
 * 点击树形节点前判断
 */
PlcSetupInfo.onBeforeClick = function(treeId, treeNode, clickFlag) {
	if (treeNode.id.startsWith("B_") || treeNode.id.startsWith("C_")) {
		return true;
	}
	return false;
};

$(function () {
	var equSettingId = $("#equSettingId").val();
    var equInfoTree = new $ZTree("equInfoTree", "/equsetting/initStoreEquTree/" + equSettingId);
    equInfoTree.bindOnClick(PlcSetupInfo.onClickEquInfo);
    equInfoTree.bindBeforeClick(PlcSetupInfo.onBeforeClick);
    equInfoTree.init();
});