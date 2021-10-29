/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var statement = {
    infoData: {},
    validateFields: {
    	manufacturerName: {
            validators: {
                notEmpty: {
                    message: '供应商不能为空'
                }
            }
        },
        startDate: {
            validators: {
                notEmpty: {
                    message: '起始日期不能为空'
                }
            }
        },
        endDate: {
            validators: {
                notEmpty: {
                    message: '截止日期不能为空'
                }
            }
        }
    }
};

/**
 * 验证数据是否为空
 */
statement.validate = function () {
    $('#infoForm').data("bootstrapValidator").resetForm();
    $('#infoForm').bootstrapValidator('validate');
    return $("#infoForm").data('bootstrapValidator').isValid();
};

/**
 * 搜索
 */
statement.search = function () {
	if (Feng.compareDate("startDate", "endDate")) {
		return;
	}
    if (!this.validate()) {
        return;
    }
    var manufacturerId = $("#manufacturerId").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var status = $("#status").val();
    var url = Feng.ctxPath + "/statement/list";
    //提交信息
    var ajax = new $ax(url, function (data) {
    	$("#listContent").html(data);
    }, function (data) {
    	Feng.error(data);
    }, "html");
    ajax.setData({"manufacturerId":manufacturerId,
    	"startDate":startDate,
    	"endDate":endDate});
    ajax.start();
};

/**
 * 重置
 */
statement.resetSearch = function() {
	$("#manufacturerName").val("");
	$("#manufacturerId").val("");
	$("#status").val("-1");
	$("#startDate").val("");
	$("#endDate").val("");
};

/**
 * 核对
 */
statement.statement = function() {
	var statementId = $("input[name='waitStatement']:checked").val();
	if (statementId) {
		var tr = $("#listContent tr[id='"+statementId+"']")[0];
	    var ajax = new $ax(Feng.ctxPath + "/statement/saveStatement", function (data) {
	        Feng.success(data.message);
	        statement.search();
	    }, function (data) {
	        Feng.error("添加失败!");
	    });
	    ajax.set({"statementId":statementId});
	    ajax.start();
	} else {
		Feng.info("请先选择未核对的物料!");
	}
};

/**
 * 查看
 */
statement.view = function() {
	var statementId = $("input[name='waitStatement']:checked").val();
	if (statementId) {
		var index = layer.open({
	        type: 2,
	        title: '核对明细',
	        area: ['780px', '600px'], //宽高
	        shadeClose: true,
	        fix: false, //不固定
	        maxmin: true,
	        content: '/statement/view?statementId='+statementId
	    });
		this.layerIndex = index;
	} else {
		Feng.info("请先选择需查询的物料!");
	}
};

/**
 * 导出明细
 */
statement.exportDetail = function() {
	var statementId = $("input[name='waitStatement']:checked").val();
	if (statementId) {
		window.location.href = Feng.ctxPath + "/statement/exportDetail/" + statementId;
	} else {
		Feng.info("请先选择需导出的物料!");
	}
};

/**
 * 显示供应商树
 */
statement.showManufacturerSelectTree = function() {
	Feng.showInputTree("manufacturerName", "manufacturerContent");
};

/**
 * 选择供应商树形节点
 */
statement.onClickManufacturer = function (e, treeId, treeNode) {
    $("#manufacturerName").attr("value", statement.manufacturerInstance.getSelectedVal());
    $("#manufacturerId").attr("value", treeNode.id);
};

$(function () {
    Feng.initValidator("infoForm", statement.validateFields);
    var manufacturerZtree = new $ZTree("manufacturerTree", "/manufacturer/tree");
    manufacturerZtree.bindOnClick(statement.onClickManufacturer);
    manufacturerZtree.init();
    statement.manufacturerInstance = manufacturerZtree;
});