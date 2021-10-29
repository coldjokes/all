/**
 * 系统管理--用户管理的单例对象
 */
var MgrUser = {
	id: "managerTable",//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 检查是否选中
 */
MgrUser.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		MgrUser.seItem = selected[0];
		return true;
	}
};

/**
 * 点击添加管理员
 */
MgrUser.openAddMgr = function() {
	var index = layer.open({
		type: 2,
		title: '添加用户',
		fix: false, // 不固定
		maxmin: false,
		area:['100%','100%'],
		content: '/mgrUser/user_add'
	});
	this.layerIndex = index;
};

/**
 * 删除用户
 */
MgrUser.delMgrUser = function() {
	var data = $('#' + this.id).bootstrapTable('getSelections');
	if (data.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return;
	}
	var operation = function() {
		$.ajax({
			url: Feng.ctxPath + '/mgrUser/delete',
			type: 'post',
			data: {
				"data": JSON.stringify(data),
			},
			success: function(data) {
				if (data.code == 200) {
					Feng.success(data.message);
					tbl.bootstrapTable('refresh');
				} else {
					Feng.info(data.message);
					tbl.bootstrapTable('refresh');
				}
			},
			error: function(error) {
				Feng.error(error);
			}
		});
	}
	Feng.confirm("该用户关联设置都会被删除，是否删除 ?", operation);
};

/**
 * 导入
 */
MgrUser.openUpload = function() {
	var index = layer.open({
		type: 2,
		title: '导入',
		fix: false, //不固定
		maxmin: false,
		area:['100%','100%'],
		content: '/mgrUser/uploadView'
	});
	this.layerIndex = index;
};

MgrUser.resetSearch = function() {
	$("#name").val("");
	$("#dept").val("");
	MgrUser.search();
}

MgrUser.search = function() {
	tbl.bootstrapTable('refresh');
}

MgrUser.list = function() {
	var that = this;
	tbl = $("#managerTable").bootstrapTable({
		url: "/mgrUser/list",
		toolbar: "#managerTableToolbar1",
		toolbarAlign: 'left',
		cache: false,
		showColumns: true,
		showRefresh: true,
		clickToSelect: false,
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
				field: "account.loginName",
				title: "帐号",
				align: "center"
			},
			{
				field: "name",
				title: "姓名",
				align: "center"
			},
			{
				field: "deptName",
				title: "部门",
				align: "center"
			},
			{
				field: "icCard",
				title: "IC卡号",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return "-";
					}
					return value;
				}
			},
			{
				field: "email",
				title: "邮箱",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return "-";
					}
					return value;
				}
			},
			{
				field: "extraBoxNum",
				title: "暂存柜数量",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return "-";
					}
					return value;
				}
			},
			{
				field: "createtime",
				title: "创建时间",
				align: "center"
			},
			{
				field: "id",
				title: "操作",
				align: "center",
				formatter: function(value, row, index) {
					return [
						`<button class='btn btn-xs btn btn-warning openEditBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
						"&nbsp;"
					].join("");
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
				dept: $("#dept").val(),
			};
			return temp;
		},
		onLoadSuccess: function(res) {
			$(".openEditBtn").on("click", function(e) {
				var userId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '修改用户',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/mgrUser/user_edit/' + userId
				});
				that.layerIndex = index;
			});
		},
	});
}

$(function() {
	MgrUser.list();
});