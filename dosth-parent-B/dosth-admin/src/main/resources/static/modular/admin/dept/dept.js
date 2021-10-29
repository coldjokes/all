/**
 * 系统管理--账户管理的单例对象
 */
var MgrDept = {
	id: "managerTable",//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 检查是否选中
 */
MgrDept.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		MgrDept.seItem = selected[0];
		return true;
	}
};

/**
 * 点击添加部门
 */
MgrDept.openAddDept = function() {
	var index = layer.open({
		type: 2,
		title: '添加部门',
		fix: false, //不固定
		maxmin: false,
		area:['100%','100%'],
		content: '/mgrDept/dept_add'
	});
	this.layerIndex = index;
};

/**
 * 打开查看部门详情
 */
MgrDept.openDeptDetail = function() {
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '部门详情',
			area: ['800px', '420px'], //宽高
			fix: false, //不固定
			maxmin: true,
			content: Feng.ctxPath + '/mgrDept/dept_update/' + this.seItem.id
		});
		this.layerIndex = index;
	}
};

/**
 * 重置
 */
MgrDept.resetSearch = function() {
	$("#name").val("");
	MgrDept.search();
}

/**
 * 查询部门列表
 */
MgrDept.search = function() {
	//点击查询是 使用刷新 处理刷新参数
	tbl.bootstrapTable('refresh');
}

MgrDept.onClickDept = function(e, treeId, treeNode) {
	MgrDept.deptid = treeNode.id.split('_')[1];
	MgrDept.search();
};


MgrDept.list = function() {
	var that = this;
	tbl = $("#managerTable").bootstrapTable({
		url: "/mgrDept/list",
		toolbar: "#addMgr",
		cache: false,
		showColumns: true,
		showRefresh: true,
		clickToSelect: false,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		treeView: true,
		treeId: "id",
		treeField: "deptName",
		treeParentId: "pId",
		treeRootLevel: 1,
		treeCollapseAll: false,
		cascadeCheck: false,
		uniqueId: "id",
		columns: [
			{
				field: 'pId',
				title: '上级ID',
				visible: false
			},
			{
				field: "deptName",
				title: "部门名称",
				align: "center",
				width:"33%"
			},
			{
				field: "deptNo",
				title: "部门编号",
				align: "center",
				width:"33%",
				formatter: function(value, row, index) {
					if (value == "") {
						return "-";
					}
					return value;
				}
			},
			{
				field: "id",
				title: "操作",
				align: "center",
				width:"33%",
				formatter: function(value, row, index) {
					if(value != 1){
						return [
							`<button class='btn btn-xs btn btn-warning openEditBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-black btn-outline delBtn' type='button' data-id=${value}><i class='fa fa-trash'>删除</i></button>`,
							].join("");
					}
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
				name: $("#name").val(),
			};
			return temp;
		},
		onLoadSuccess: function(res) {
			$(".openEditBtn").on("click", function(e) {
				var id = $(this).data("id");
				const MgrDept = res.rows.find(item => item.id == id);
				
				if (id == "1") {
					Feng.info(MgrDept.deptName + "不能修改");
					return;
				}
				
				var index = layer.open({
					type: 2,
					title: '修改部门',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/mgrDept/dept_edit/' + id
				});
				that.layerIndex = index;
			});
			$(".delBtn").on("click", function(e) {
				var deptId = $(this).data("id");
				const MgrDept = res.rows.find(item => item.id == deptId);
				
				if (deptId == "1") {
					Feng.info(MgrDept.deptName + "不能删除");
					return;
				}
				
				var operation = function() {
					var ajax = new $ax(Feng.ctxPath + "/mgrDept/delete", function(data) {
						if (data.code == 200) {
							Feng.success(data.message);
							tbl.bootstrapTable('refresh');
						} else {
							Feng.info(data.message);
						}
					}, function(data) {
						Feng.error("删除失败!" + data.responseJSON.message + "!");
					});
					ajax.set("deptId", deptId);
					ajax.start();
				};
				Feng.confirm("是否删除 " + MgrDept.deptName + " ?", operation);
			});
		},
	});
}

$(function() {
	MgrDept.list();
});