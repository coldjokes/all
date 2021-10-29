/**
 * 设置定义
 */
var ExtraBoxNumSetting = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
ExtraBoxNumSetting.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	ExtraBoxNumSetting.seItem = selected[0];
        return true;
    }
};

/**
 * 查询
 */
ExtraBoxNumSetting.search = function () {
	//点击查询是 使用刷新 处理刷新参数
	var params = {
		userName : $("#userName").val(),
		status : $("#status").val()
	};
	ExtraBoxNumSetting.oTable.queryParams = params;
	ExtraBoxNumSetting.oTable.refresh();
};

/**
 * 重置
 */
ExtraBoxNumSetting.resetSearch = function() {
    $("#userName").val("");
    $("#status").val("-1");
    ExtraBoxNumSetting.search();
};

/**
 * 添加
 */
ExtraBoxNumSetting.openAdd = function() {
	var index = layer.open({
        type: 2,
        title: '添加暂存柜分配信息',
        area: ['780px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: '/extraBoxNumSetting/extraBoxNumSetting_add'
    });
    this.layerIndex = index;
};

/**
 * 修改
 */
ExtraBoxNumSetting.openChange = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改暂存柜分配数量',
            area: ['800px', '490px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: '/extraBoxNumSetting/extraBoxNumSetting_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除
 */
ExtraBoxNumSetting.update = function() {
	if (this.check()) {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/extraBoxNumSetting/update/" + ExtraBoxNumSetting.seItem.id, function (data) {
            	if(data.code == 200){
            		Feng.success(data.message);
                    ExtraBoxNumSetting.oTable.refresh();
            	}else{
            		Feng.error(data.message);
            	}
            }, function (data) {
                Feng.error(data.message);
            });
            ajax.start();
        };
        Feng.confirm("是否删除当前信息?",operation);
    }
}

$(function () {
    var oTable = new BSTable("managerTable", "/extraBoxNumSetting/list");
    oTable.setPaginationType("server");
    ExtraBoxNumSetting.oTable = oTable.Init();
});