/**
 * 日志管理初始化
 */
var OperationLog = {
    id: "OperationLogTable",	//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
OperationLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	OperationLog.seItem = selected[0];
        return true;
    }
};

/**
 * 重置
 */
OperationLog.resetSearch = function () {
    $("#name").val("");
    $("#logName").val("-1");
    $("#beginTime").val("");
    $("#endTime").val("");
    OperationLog.search();
}

/**
 * 查询日志列表
 */
OperationLog.search = function () {
	var params = {
			name : $("#name").val(),
			logName : $("#logName").val(),
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val()
		};
	OperationLog.oTable.queryParams = params;
	OperationLog.oTable.refresh();

};

$(function () {
    var oTable = new BSTable("OperationLogTable", "/operationLog/list");
    oTable.setPaginationType("server");
    OperationLog.oTable = oTable.Init();
});

/**
 * 查看日志详情
 */
OperationLog.detail = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/log/detail/" + this.seItem.id, function (data) {
            Feng.infoDetail("日志详情", data.regularMessage);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    }
};
