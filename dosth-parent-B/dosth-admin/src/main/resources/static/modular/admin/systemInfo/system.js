/**
 * 系统维护
 */
var SystemInfo = {
    id: "systemInfoTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1,
};

/**
 * 检查是否选中
 */
SystemInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	SystemInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 添加
 */
SystemInfo.openAddSystemInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: '/systemInfo/systemInfo_add'
    });
    this.layerIndex = index;
};

/**
 * 修改
 */
SystemInfo.openChangeSystemInfo = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改',
            area: ['800px', '490px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/systemInfo/systemInfo_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 启用/禁用
 */
SystemInfo.delSystem = function() {
	if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/systemInfo/update/" + SystemInfo.seItem.id, 
            function (data) {
                Feng.success("设置成功!");
                SystemInfo.oTable.refresh();
            }, function (data) {
                Feng.error("设置失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        };
        Feng.confirm("是否变更当前状态?",operation);
    }
}

/**
 * 删除
 */
SystemInfo.delSystemInfo = function () {
    if (this.check()) {
        var operation = function(){
            var id = SystemInfo.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/systemInfo/delete", function () {
                Feng.success("删除成功!");
                SystemInfo.oTable.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", id);
            ajax.start();
        };

        Feng.confirm("是否删除 " + SystemInfo.seItem.systemName + " ?",operation);
    }
};

/**
 * 重置
 */
SystemInfo.resetSearch = function () {
    $("#systemName").val("");
    SystemInfo.search();
}

/**
 * 搜索
 */
SystemInfo.search = function () {
	//点击查询是 使用刷新 处理刷新参数
	var params = {
		systemName : $("#systemName").val(),
	};
	SystemInfo.oTable.queryParams = params;
	SystemInfo.oTable.refresh();
}

$(function () {
    var oTable = new BSTable("systemInfoTable", "/systemInfo/list");
    oTable.setPaginationType("server");
    SystemInfo.oTable = oTable.Init();
});