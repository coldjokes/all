/**
 * 工具管理--自定义设置
 */
var Custom = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
Custom.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	Custom.seItem = selected[0];
        return true;
    }
};

/**
 * 搜索
 */
Custom.search = function () {
	//点击查询是 使用刷新 处理刷新参数
	var params = {
		customName : $("#customName").val(),
		status : $("#status").val()
	};
	Custom.oTable.queryParams = params;
	Custom.oTable.refresh();
};

/**
 * 重置
 */
Custom.resetSearch = function() {
    $("#customName").val("");
    $("#status").val("-1");
    Custom.search();
};

/**
 * 添加
 */
Custom.openAddCustom = function() {
	var index = layer.open({
        type: 2,
        title: '添加自定义',
        area: ['780px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: '/custom/custom_add'
    });
    this.layerIndex = index;
};

/**
 * 修改
 */
Custom.openChangeCustom = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改自定义',
            area: ['800px', '490px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: '/custom/custom_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
Custom.delCustom = function () {
    if (this.check()) {
        var operation = function(){
            var id = Custom.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/custom/delete", 
    		function () {
                Feng.success("删除成功!");
                Custom.oTable.refresh();
            }, function (data) {
                Feng.error("删除失败!");
            });
            ajax.set("id", id);
            ajax.start();
        };

        Feng.confirm("是否删除 " + Custom.seItem.customName + " ?",operation);
    }
};

/**
 * 启用/禁用
 */
Custom.updateCustom = function() {
	if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/custom/update/" + Custom.seItem.id, 
    		function (data) {
                Feng.success("设置成功!");
                Custom.oTable.refresh();
            }, function (data) {
                Feng.error("设置失败!");
            });
            ajax.start();
        };
        Feng.confirm("是否变更当前状态?",operation);
    }
};

$(function () {
    var oTable = new BSTable("managerTable", "/custom/list");
    oTable.setPaginationType("server");
    Custom.oTable = oTable.Init();
});