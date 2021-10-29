/**
 * 结算查询
 */
var StatementQuery = {
    id: "StatementQueryTable",	//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};


/**
 * 重置
 */
StatementQuery.resetSearch = function () {
    $("#startDate").val("");
    $("#endDate").val("");
    StatementQuery.search();
}

/**
 * 查询日志列表
 */
StatementQuery.search = function () {
	if (Feng.compareDate("startDate", "endDate")) {
		return;
	}
	var params = {
		startDate : $("#startDate").val(),
		endDate : $("#endDate").val()
	};
	StatementQuery.oTable.queryParams = params;
	StatementQuery.oTable.refresh();

};

$(function () {
    var oTable = new BSTable("StatementQueryTable", "/statement/page");
    oTable.setPaginationType("server");
    StatementQuery.oTable = oTable.Init();
});