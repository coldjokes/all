/**
 * 盘点查询
 */
var Inventory = {
    id: "InventoryTable",	//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 重置
 */
Inventory.resetSearch = function () {
    $("#beginTime").val("");
    $("#endTime").val("");
    Inventory.search();
}

/**
 * 查询列表
 */
Inventory.search = function () {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	var params = {
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val()
		};
	Inventory.oTable.queryParams = params;
	Inventory.oTable.refresh();
};

/**
 * 盘点
 */
Inventory.inventory = function () {
	if ($("#equInfoId").val() == "") {
		Feng.info("请先选择盘点目标");
		return;
	}
	var arr = [];
	$("#equinfos input").each(function() {
		if ($(this).attr("disabled") != "disabled") {
			arr.push({"detailBoxId":$(this).attr("detailboxid"),
				"matId":$(this).attr("matid"),
				"storageNum":$(this).attr("storagenum"),
				"inventoryNum":$(this).val()==''?0:$(this).val(),
				"accountId":$(this).attr("accountId")});
		}
	});
	if (arr.length < 1) {
		Feng.info("没有可盘点数据");
		return;
	}
	var url = Feng.ctxPath + "/inventory/inventory";
	var operation = function(){
		var ajax = new $ax(url,
				function(data) {
					Feng.success(data.message);
				}, function(data) {
					Feng.error("设置失败," + data + "!");
				});
			ajax.setData({"equInfoId":$("#equInfoId").val(),"inventoryVals":JSON.stringify(arr)});
			ajax.start();
	}
	Feng.confirm("确认盘点?",operation);
};

/**
 * @description 加载设备内详情
 */
Inventory.onClickEquInfo = function(e, treeId, treeNode) {
	$("#equInfoId").val(treeNode.id);
	var ajax = new $ax(Feng.ctxPath + "/inventory/getEquStockView",
		function(data) {
			$("#equinfos").html(data);
		}, function(data) {
			Feng.error("设置失败," + data + "!");
		}, "html");
	ajax.setData({"equInfoId":$("#equInfoId").val()});
	ajax.start();
};

/**
 * 点击树形节点前判断
 */
Inventory.onBeforeClick = function(treeId, treeNode, clickFlag) {
	if (treeNode.id.startWith("CM_")) {
		return false;
	}
	return true;
};

$(function () {
	var equInfoTree = new $ZTree("equInfoTree", "/inventory/tree");
	equInfoTree.bindBeforeClick(Inventory.onBeforeClick);
	equInfoTree.bindOnClick(Inventory.onClickEquInfo);
	equInfoTree.init();
});