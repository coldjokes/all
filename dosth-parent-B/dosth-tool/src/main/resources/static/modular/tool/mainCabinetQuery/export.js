/**
 * 主柜领用查询导出
 */
var MainCabinetQuery = {
    id: "MainCabinetQueryTable",	//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1,
    equInfoInstance:null
};

/**
 * 导出
 */
MainCabinetQuery.exportBorrowList = function() {
	var cabinetId = $("#cabinetId").val();
	if (cabinetId == '') {
		cabinetId = "STORAGE";
	}
	var beginTime = $("#begin").val();
	if (beginTime == '') {
		beginTime = "1900-01-01";
	}
	var endTime = $("#end").val();
	if (endTime == '') {
		endTime = "9999-12-31";
	}
	window.location.href = Feng.ctxPath + "/mainCabinetQuery/export/" + cabinetId +"/" + beginTime + "/" + endTime;
};

MainCabinetQuery.showEquInfoSelectTree = function() {
	Feng.showInputTree("equInfoName", "equInfoContent");
};

MainCabinetQuery.onClickEquInfo = function(e, treeId, treeNode) {
    $("#equInfoName").attr("value", MainCabinetQuery.equInfoInstance.getSelectedVal());
    $("#cabinetId").attr("value", treeNode.id);
};


$(function () {
    var equInfoTree = new $ZTree("equInfoTree", "/equsetting/tree");
    equInfoTree.bindOnClick(MainCabinetQuery.onClickEquInfo);
    equInfoTree.bindBeforeClick(function(treeId, treeNode, clickFlag) {
		return treeNode.id != 'STORAGE';
	});
    equInfoTree.init();
    MainCabinetQuery.equInfoInstance = equInfoTree;
});