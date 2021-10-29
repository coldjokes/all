/**
 * 工具管理--PLC设置
 */
var Parts = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
Parts.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	Parts.seItem = selected[0];
        return true;
    }
};

/**
 * 搜索
 */
Parts.search = function () {
	//点击查询是 使用刷新 处理刷新参数
	var params = {
		partsName : $("#partsName").val(),
		status : $("#status").val()
	};
	Parts.oTable.queryParams = params;
	Parts.oTable.refresh();
};

/**
 * 重置
 */
Parts.resetSearch = function() {
    $("#partsName").val("");
    $("#status").val("-1");
    Parts.search();
};

/**
 * 添加
 */
Parts.openAddParts = function() {
	var index = layer.open({
        type: 2,
        title: '添加零件定义',
        area: ['780px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: '/parts/parts_add'
    });
    this.layerIndex = index;
};

/**
 * 修改
 */
Parts.openChangeParts = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改零件定义',
            area: ['800px', '490px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: '/parts/parts_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
Parts.delParts = function () {
    if (this.check()) {
        var operation = function(){
            var id = Parts.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/parts/delete", function () {
                Feng.success("删除成功!");
                Parts.oTable.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", id);
            ajax.start();
        };

        Feng.confirm("是否删除 " + Parts.seItem.partsName + " ?",operation);
    }
};

/**
 * 启用/禁用
 */
Parts.updateParts = function() {
	if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/parts/update/" + Parts.seItem.id, function (data) {
                Feng.success("设置成功!");
                Parts.oTable.refresh();
            }, function (data) {
                Feng.error("设置失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        };
        Feng.confirm("是否变更当前状态?",operation);
    }
};

$(function () {
    var oTable = new BSTable("managerTable", "/parts/list");
    oTable.setPaginationType("server");
    Parts.oTable = oTable.Init();
});