/**
 * 归还管理
 */
var MatReturnBackBill = {
	id: "MatReturnBackBillTable",	//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 检查是否选中
 */
MatReturnBackBill.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		MatReturnBackBill.seItem = selected[0];
		return true;
	}
};

/**
 * 初始检索时间设定（当前时间一个月内）
 */
MatReturnBackBill.timeSet = function() {
	//起始时间默认设置为当前时间一个月前
	var myDate = new Date();
	//获取当前年份
	var EY = myDate.getFullYear();
	var SY = EY;
	//获取一个月前月份 
	//返回值为：0(一月) ~ 11(十二月)
	var preM = myDate.getMonth();
	//获取当前月份
	var curM = myDate.getMonth() + 1;
	//获取月份值等于0，往后退一年
	if (preM == 0) {
		SY = SY - 1;
		preM = 12;
	}
	preM = (preM > 9) ? preM : "0" + preM;
	curM = (curM > 9) ? curM : "0" + curM;
	//获取当前日
	var D = (myDate.getDate() > 9) ? myDate.getDate() : "0" + myDate.getDate();
	//设定起始时间默认值
	$("#beginTime").val(SY + '-' + preM + '-' + D);
	//设定截止时间默认值
	$("#endTime").val(EY + '-' + curM + '-' + D);
};

/**
 * 重置
 */
MatReturnBackBill.resetSearch = function() {
	MatReturnBackBill.timeSet();
	$("#beginTime").val(""),
		$("#endTime").val(""),
		$("#userName").val(""),
		$("#status").val("-1"),
		$("#backType").val("-1");
	$("#equSettingName").val("-1");
	MatReturnBackBill.search();
}

/**
 * 关闭此对话框
 */
MatReturnBackBill.close = function() {
	parent.layer.close(window.parent.MatReturnBackBill.layerIndex);
};

/**
 * 显示人员树
 */
MatReturnBackBill.showUserSelectTree = function() {
	Feng.showInputTree("userName", "userContent");
};

/**
 * 选择树形节点
 */
MatReturnBackBill.onClickUser = function(e, treeId, treeNode) {
	$("#userName").val(treeNode.name);
	$("#accountId").val(treeNode.id);
};

/**
 * 点击树形节点前判断
 */
MatReturnBackBill.onBeforeClick = function(treeId, treeNode, clickFlag) {
	if (treeNode.id.startsWith("d_")) {
		return false;
	}
	return true;
};

/**
 * 扫描确认
 */
MatReturnBackBill.scanConfirm = function() {
	var index = layer.open({
		type: 2,
		title: '扫描确认',
		fix: false, //不固定
		maxmin: false,
		area: ['100%', '100%'],
		content: '/matReturnBackBill/scanView'
	});
	this.layerIndex = index;
};

/**
 * 查询列表详细
 */
MatReturnBackBill.search = function() {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	tbl.bootstrapTable('refresh');
};

/**
 * 扫描确认提交
 */
MatReturnBackBill.scanConfirmSubmit = function() {
	var ajax = new $ax(Feng.ctxPath + "/matReturnBackBill/scanConfirm",
		function(data) {
			window.parent.Feng.success("确认成功!");
			MatReturnBackBill.close();
		}, function(data) {
			window.parent.Feng.error("确认失败!");
		});
	var matReturnBackId = $("#matReturnBackId").val();
	ajax.setData({ "matReturnBackId": matReturnBackId });
	ajax.start();
	tbl.bootstrapTable('refresh');
};

/**
 * 正常确认
 */
MatReturnBackBill.normalConfirm = function() {
	var arr = new Array();
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		for (var i = 0; i < selected.length; i++) {
			arr[i] = (selected[i].id);
		}
		var arrs = arr.join(",");
		var ajax = new $ax(Feng.ctxPath + "/matReturnBackBill/normalConfirm/" + arrs,
			function(data) {
				Feng.success("确认成功!");
				tbl.bootstrapTable('refresh');
			}, function(data) {
				Feng.error("确认失败!" + data.responseJSON.message + "!");
			});
		ajax.start();
		tbl.bootstrapTable('refresh');
	}
};

/**
 * 异常确认
 */
MatReturnBackBill.abnormalConfirm = function() {
	var arr = new Array();
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	}
	if (selected.length > 1) {
		Feng.info("异常确认只能逐条操作！");
		return false;
	}
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '异常确认',
			fix: false, //不固定
			maxmin: false,
			area: ['100%', '100%'],
			content: '/matReturnBackBill/abnormalView/' + this.seItem.id
		});
		this.layerIndex = index;
	}
};

/**
 * 归还清单导出
 */
MatReturnBackBill.infoExport = function() {
	var params = [$("#beginTime").val(), $("#endTime").val(), $("#userName").val(),
	$("#status").val(), $("#backType").val(), $("#equSettingName").val()];
	window.location.href = Feng.ctxPath + "/matReturnBackBill/infoExport/" + params;
};

MatReturnBackBill.list = function() {
	var that = this;
	tbl = $("#MatReturnBackBillTable").bootstrapTable({
		url: "/matReturnBackBill/list",
		toolbar: "#button",//设置自定义工具栏容器ID，也可以是容器样式类名.toolbar
		cache: false,
		showColumns: true,
		showRefresh: true,
		clickToSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				checkbox: true,
				visible: true
			},
			{
				field: "equSettingName",
				title: "刀具柜名称",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "returnBackNo",
				title: "归还流水号",
				align: "center"
			},
			{
				field: "matUseBill.matEquName",
				title: "物料名称",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "knifeId",
				title: "物料编号",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "returnDetailInfo",
				title: "归还详情",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "receiveInfo",
				title: "领用途径",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "matLife",
				title: "使用寿命",
				align: "center"
			},
			{
				field: "realLife",
				title: "制造产量",
				align: "center"
			},
			{
				field: "surplusLife",
				title: "剩余寿命",
				align: "center"
			},
			{
				field: "serialNum",
				title: "产品流水号",
				align: "center",
				formatter: function(value, index, row) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "userName",
				title: "归还人员",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "opDate",
				title: "归还时间",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "confirmUser",
				title: "审核人员",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "confirmMode",
				title: "审核方式",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "ABNORCONF") {
						return '<span style="color:#FF0000;">' + "异常确认" + '</span>';
					} else if (value == "SCANCONF") {
						return '<span style="color:#1ab394">' + "扫描确认" + '</span>';
					} else if (value == "APPCONF") {
						return '<span style="color:#1ab394">' + "APP确认" + '</span>';
					} else if (value == "NORCONF") {
						return '<span style="color:#1ab394">' + "正常确认" + '</span>';
					} else if (value == "") {
						return '-';
					}
					return value;
				},
			},
			{
				field: "confirmDate",
				title: "审核时间",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "packageNum",
				title: "应还数量",
				align: "center"
			},
			{
				field: "num",
				title: "已归还数量",
				align: "center"
			},
			{
				field: "auditStatus",
				title: "审核状态",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "NO_CHECK") {
						return '<span style="color:#000000;">' + "未审核" + '</span>';
					} else if (value == "NOT_PASS") {
						return '<span style="color:#FF0000">' + "未通过" + '</span>';
					} else if (value == "PASS") {
						return '<span style="color:#1ab394">' + "通过" + '</span>';
					} else if (value == "") {
						return '-';
					}
					return value;
				},
			},
			{
				field: "remark",
				title: "备注",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
		],
		responseHandler: function(res) {
			return {
				"total": res.totalElements,
				"rows": res.content
			};
		},
		queryParams: function(params) {
			var temp = {
				limit: params.limit,
				offset: params.offset,
				pageSize: this.pageSize,
				pageNumber: this.pageNumber,
				beginTime: $("#beginTime").val(),
				endTime: $("#endTime").val(),
				userName: $("#userName").val(),
				status: $("#status").val(),
				backType: $("#backType").val(),
				equSettingName: $("#equSettingName").val()
			};
			return temp;
		},
	});
}
$(function() {
	MatReturnBackBill.list();
	//初始时间设定
	MatReturnBackBill.timeSet();

	//var oTable = new BSTable("MatReturnBackBillTable", "/matReturnBackBill/list");
	//var params = {
	//beginTime: $("#beginTime").val()
	//};
	//	oTable.queryParams = params;
	//  oTable.setPaginationType("server");
	// MatReturnBackBill.oTable = oTable.Init();

	var userZtree = new $ZTree("userTree", "/user/tree");
	userZtree.bindBeforeClick(MatReturnBackBill.onBeforeClick);
	userZtree.bindOnClick(MatReturnBackBill.onClickUser);
	userZtree.init();
	MatReturnBackBill.userInstance = userZtree;
});
