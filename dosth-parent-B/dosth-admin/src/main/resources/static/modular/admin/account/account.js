/**
 * 系统管理--账户管理的单例对象
 */
var MgrAccount = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 检查是否选中
 */
MgrAccount.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        MgrAccount.seItem = selected[0];
        return true;
    }
};

/**
 * 点击修改按钮时
 * @param accountId 账户id
 */
MgrAccount.openChangeAccount = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改账户',
            area: ['780px', '490px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/mgrAccount/account_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除账户
 */
MgrAccount.delMgrAccount = function () {
    if (this.check()) {
        var operation = function(){
            var accountId = MgrAccount.seItem.id;
            var ajax = new $ax(Feng.ctxPath + "/mgrAccount/delete", function () {
                Feng.success("删除成功!");
                MgrAccount.oTable.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("accountId", accountId);
            ajax.start();
        };

        Feng.confirm("是否删除账户" + MgrAccount.seItem.loginName + "?",operation);
    }
};

/**
 * 重置密码
 */
MgrAccount.openResetAccount = function () {
	if (this.check()) {
        var accountId = this.seItem.id;
        parent.layer.confirm('是否重置密码为111111？', {
            btn: ['确定', '取消'],
            shade: false //不显示遮罩
        }, function () {
            var ajax = new $ax(Feng.ctxPath + "/mgrAccount/reset", function (data) {
            	if(data.code != 200){
            		Feng.info(data.message);
            	}else{
            		Feng.success(data.message);
            	}
            }, function (data) {
                Feng.error(data.message);
            });
            ajax.set("accountId", accountId);
            ajax.start();
        });
    }
};

MgrAccount.resetSearch = function () {
    $("#name").val("");
    MgrAccount.search();
}

MgrAccount.search = function () {
	//点击查询是 使用刷新 处理刷新参数
	var params = {
		name : $("#name").val(),
	};
	MgrAccount.oTable.queryParams = params;
	MgrAccount.oTable.refresh();
}

$(function () {
    var oTable = new BSTable("managerTable", "/mgrAccount/list");
    oTable.setPaginationType("server");
    MgrAccount.oTable = oTable.Init();
});