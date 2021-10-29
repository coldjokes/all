/**
 * 工具管理--工序信息
 */
var MgrProcInfo = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
MgrProcInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	MgrProcInfo.seItem = selected[0];
        return true;
    }
};

/*
 * 查询
 */
MgrProcInfo.search = function () {
	//点击查询是 使用刷新 处理刷新参数
	var params = {
		name : $("#name").val(),
		status : $("#status").val()
	};
	MgrProcInfo.oTable.queryParams = params;
	MgrProcInfo.oTable.refresh();
};

/**
 * 重置
 */
MgrProcInfo.resetSearch = function() {
    $("#name").val("");
    $("#status").val("-1");
    MgrProcInfo.search();
};

/**
 * 新增
 */
MgrProcInfo.openAddMgr = function() {
	var index = layer.open({
        type: 2,
        title: '添加工序信息',
        area: ['780px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: '/procinfo/addView'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 */
MgrProcInfo.openChangeMgr = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改工序信息',
            area: ['800px', '490px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: '/procinfo/editView/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
MgrProcInfo.delMgr = function() {
	if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/procinfo/update/"+MgrProcInfo.seItem.id, function (data) {
                Feng.success("设置成功!");
                MgrProcInfo.oTable.refresh();
            }, function (data) {
                Feng.error("设置失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        };
        Feng.confirm("是否变更当前状态?",operation);
    }
}

$(function () {
    var oTable = new BSTable("managerTable", "/procinfo/list");
    oTable.setPaginationType("server");
    MgrProcInfo.oTable = oTable.Init();
});