/**
 * 定时任务
 */
var TimeTask = {
	id : "managerTable",
	seItem : null,
	oTable : null,
	layerIndex : -1,
	tbl : null,
    userTree : null,
    userInstance : null
};

/**
 * 检查是否选中
 */
TimeTask.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		TimeTask.seItem = selected[0];
		return true;
	}
};

TimeTask.getDetail = function() {
	$.ajax({
        url : '/timeTaskDetail/getDetail',
        type : 'get',
        dataType : 'json',
        success : function(data) {
            $("#id").val(data.id);
            $("#userName").val(data.userName);
            $("#accountId").val(data.accountId);
            $("#executionTime").val(data.executionTime);
        },
        error : function(err) {
            Feng.error(err);
        }
    });
}

/**
 * 保存
 */
TimeTask.saveAndFlush = function() {
	var id = $("#id").val();
	var executionTime = $("#executionTime").val();
	
	var accountId = "";
	var selected = userTree.getCheckedNodes(true);
	if (selected.length > 0) {
        for (var i = 0; i < selected.length; i++) {
            if (selected[i].id.startsWith('d_')) {
                continue;
            }
            accountId += selected[i].id + ',';
        }
    } else {
        accountId = $("#accountId").val();
    }
	
	if (accountId == '') {
		Feng.info("收件人不能为空");
		return;
	}

	if (executionTime == '') {
		Feng.info("执行时间不能为空");
		return;
	}

	var data = {
		"id" : id,
		"accountId" : accountId,
		"executionTime" : executionTime
	};

	$.ajax({
		url : '/timeTaskDetail/saveAndFlush',
		type : 'post',
		data : {
			"data" : JSON.stringify(data)
		},
		success : function(data) {
			if (data.code == 200) {
				TimeTask.getDetail();
				Feng.success(data.message);
			} else {
				Feng.error(data.message);
			}
		},
		error : function(err) {
			Feng.error(err);
		}
	});
};

TimeTask.reset = function() {
	userTree.checkAllNodes(false);
    $("#accountId").val("");
    $("#userName").val("");
    $("#cronExpression").val("");

}
/* 列表 */
TimeTask.list = function() {
	var that = this;
	tbl = $("#managerTable").bootstrapTable({
		url : "/timeTask/list",
		cache : false,
		showToggle : false,
		showColumns : true,
		showRefresh : true,
		singleSelect : true,
		clickToSelect : true,
		pagination : true,
		pageNumber : 1,
		pageSize : 10,
		sidePagination : "server",
		columns : [ 
		{
			field : "id",
			visible : false
		}, {
			field : "name",
			title : "任务名称",
			align : "center",
			formatter : function(value, row, index) {
				if (value == 'USE_RECORD') {
					return "领用记录";
				} else if (value == 'RETURN_RECORD') {
					return "归还记录";
				} else if (value == 'STOCK_RECORD') {
					return "库存明细";
				} else if (value == 'FEED_RECORD') {
					return "补料记录";
				} else if (value == 'LOWER_RECORD') {
					return "下架记录";
				} else if (value == 'USE_SUMMARY') {
					return "领用汇总";
				} else if (value == 'FEED_SUMMARY') {
					return "补料汇总";
				} else if (value == 'INVENTORY_RECORD') {
					return "盘点记录";
				} else {
					return "-";
				}
			}
		}, {
			title : '任务状态',
			align : 'center',
			formatter : function(value, row, index) {
				return statusTools(row);
			}
		} ],
		responseHandler : function(res) {
			return {
				"total" : res.totalElements,
				"rows" : res.content
			};
		},
		queryParams : function(params) {
			var temp = {
				limit : params.limit,
				offset : params.offset,
				pageSize : this.pageSize,
				pageNumber : this.pageNumber,
			};
			return temp;
		}
	});
}


$(function() {
	TimeTask.getDetail();
	TimeTask.list();
});

/* 定时任务状态显示 */
function statusTools(row) {
	if (row.status == 'OFF') {
		return '<i class=\"fa fa-toggle-off text-navy fa-2x\" onclick="enable(\''
				+ row.id + '\')"></i> ';
	} else {
		return '<i class=\"fa fa-toggle-on text-navy fa-2x\" onclick="disable(\''
				+ row.id + '\')"></i> ';
	}
}

/* 定时任务-停用 */
function disable(id) {
	var operation = function() {
		var ajax = new $ax("/timeTask/statusSet/" + id + "/1", function(data) {
			Feng.success("设置成功!");
			$("#managerTable").bootstrapTable('refresh');
		}, function(data) {
			Feng.error("设置失败!");
		});
		ajax.start();
	};
	Feng.confirm("是否停用?", operation);
}

/* 定时任务-启用 */
function enable(id) {
	var operation = function() {
		var ajax = new $ax("/timeTask/statusSet/" + id + "/0", function(data) {
			Feng.success("设置成功!");
			$("#managerTable").bootstrapTable('refresh');
		}, function(data) {
			Feng.error("设置失败!");
		});
		ajax.start();
	};
	Feng.confirm("是否启用?", operation);
}

 
/* 用户树 */
TimeTask.userTree = function() {
    var setting = {
        data : {
            simpleData : {
                enable : true
            }
        },
        view : {
            nameIsHTML : true,
            showLine : true,
            dblClick : false
        },
        check : {
            enable : true,
            chkStyle : "checkbox",
            chkboxType : {
                "Y" : "ps",
                "N" : "ps"
            }
        },
        callback : {
            beforeClick : function(treeId, treeNode, clickFlag) {
                if (treeNode.id.startsWith('d_')) {
                    Feng.info("请选择员工");
                    return false;
                }
                return true;
            },
            beforeCheck : function(treeId, treeNode) {
                if (treeNode.id.startsWith('d_')) {
                    Feng.info("请选择员工");
                    return false;
                }
                return true;
            },
            onClick : function(event, treeId, treeNode, clickFlag) {
                userTree.checkNode(treeNode, !treeNode.checked, true);
                var userName = '';
                var selected = userTree.getCheckedNodes(true);
                for (var i = 0; i < selected.length; i++) {
                    if (selected[i].id.startsWith('d_')) {
                        continue;
                    }
                    userName += selected[i].name + ',';
                }
                $("#userName").val(userName);
                $("#accountId").val(treeNode.id);
            },
            onCheck : function(event, treeId, treeNode) {
                var userName = '';
                var selected = userTree.getCheckedNodes(true);
                for (var i = 0; i < selected.length; i++) {
                    if (selected[i].id.startsWith('d_')) {
                        continue;
                    }
                    userName += selected[i].name + ',';
                }
                $("#userName").val(userName);
                $("#accountId").val(treeNode.id);
            },
        }
    };
 
    $.ajax({
        type : "post",
        url : "/user/tree",
        success : function(data) {
            userTree = $.fn.zTree.init($("#userTree"), setting, data);
        }
    });
};
 
/* 用户树显示 */
TimeTask.showUserSelectTree = function() {
    // 复选框回显
    var zTreeObj = $.fn.zTree.getZTreeObj("userTree");
    var zTree = zTreeObj.getCheckedNodes(false);
    var accountId = $("#accountId").val();
    for (var i = 0; i < zTree.length; i++) {
        if (accountId.indexOf(zTree[i].id + ",") != -1) {
            zTreeObj.expandNode(zTree[i], true);
            zTreeObj.checkNode(zTree[i], true, true);
        }
    }
    Feng.showInputTree("userName", "userContent");
};
 
$(function() {
    TimeTask.userTree();
    TimeTask.userInstance = userTree;
    TimeTask.getDetail();
    TimeTask.list();
});