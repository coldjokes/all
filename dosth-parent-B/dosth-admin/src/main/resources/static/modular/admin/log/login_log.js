/**
 * 日志管理初始化
 */
var LoginLog = {
    id: "LoginLogTable",	//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
LoginLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        LoginLog.seItem = selected[0];
        return true;
    }
};

/**
 * 重置
 */
LoginLog.resetSearch = function () {
    $("#beginTime").val("");
    $("#endTime").val("");
    LoginLog.search();
}

/**
 * 查询日志列表
 */
LoginLog.search = function () {
	var params = {
			beginTime : $("#beginTime").val(),
			endTime : $("#endTime").val(),
			sort: params.createTime,  
            sortOrder: params.asc
		};
	LoginLog.oTable.queryParams = params;
	LoginLog.oTable.refresh();

};

$(function () {
    var oTable = new BSTable("LoginLogTable", "/loginLog/list");
    oTable.setPaginationType("server");
    LoginLog.oTable = oTable.Init();
});

/**
 * 查看日志详情
 */
LoginLog.detail = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/log/detail/" + this.seItem.id, function (data) {
            Feng.infoDetail("日志详情", data.regularMessage);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    }
};
