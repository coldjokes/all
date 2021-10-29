/**
 * 工具管理--PLC设置
 */
var PlcSetting = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
PlcSetting.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	PlcSetting.seItem = selected[0];
        return true;
    }
};

/**
 * 搜索
 */
PlcSetting.search = function () {
	//点击查询是 使用刷新 处理刷新参数
	var params = {
		plcName : $("#plcName").val(),
		status : $("#status").val()
	};
	PlcSetting.oTable.queryParams = params;
	PlcSetting.oTable.refresh();
};

/**
 * 重置
 */
PlcSetting.resetSearch = function() {
    $("#plcName").val("");
    $("#status").val("-1");
    PlcSetting.search();
};

/**
 * 添加
 */
PlcSetting.openAddPlcSetting = function() {
	var index = layer.open({
        type: 2,
        title: '添加PLC参数',
        area: ['780px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: '/plcSetting/plcSetting_add'
    });
    this.layerIndex = index;
};

/**
 * 修改
 */
PlcSetting.openChangePlcSetting = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改PLC设置',
            area: ['800px', '490px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: '/plcSetting/plcSetting_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
PlcSetting.delPlcSetting = function () {
    if (this.check()) {
        var operation = function(){
            var id = PlcSetting.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/plcSetting/delete", function () {
                Feng.success("删除成功!");
                PlcSetting.oTable.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", id);
            ajax.start();
        };

        Feng.confirm("是否删除 " + PlcSetting.seItem.plcName + " ?",operation);
    }
};

/**
 * 启用/禁用
 */
PlcSetting.updatePlcSetting = function() {
	if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/plcSetting/update/" + PlcSetting.seItem.id, function (data) {
                Feng.success("设置成功!");
                PlcSetting.oTable.refresh();
            }, function (data) {
                Feng.error("设置失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        };
        Feng.confirm("是否变更当前状态?",operation);
    }
};

$(function () {
    var oTable = new BSTable("managerTable", "/plcSetting/list");
    oTable.setPaginationType("server");
    PlcSetting.oTable = oTable.Init();
});