/**
 * 每日限额
 */
var DailyLimit = {
	userInstance:null
};

/**
 * 查询
 */
DailyLimit.search = function(accountId) {
	var url = Feng.ctxPath + "/dailyLimit/search/" + accountId;
    var ajax = new $ax(url, function (data) {
    	$("#editContent").html(data);
    }, function (data) {
    	Feng.error("查询失败!");
    }, "html");
    ajax.start();
};

/**
 * 同步
 */
DailyLimit.dataSync = function() {
	var accountId = $("#accountId").val();
	if(accountId != null && accountId != ""){
		var url = Feng.ctxPath + "/dailyLimit/dataSync/" + accountId;
	    var ajax = new $ax(url, function (data) {
	    	if(data.code == 200){
	    		Feng.success(data.message);
	    		DailyLimit.search(accountId);
	    	}
	    }, function (data) {
	    	Feng.error("查询失败");
	    });
	    ajax.start();
	}else{
		Feng.error("请选择某一员工！");
		return;
	}
};

/**
 * 重置
 */
DailyLimit.resetSearch = function() {
	$("#accountId").val("");
	$("#userName").val("");
	$("#startTime").val("");
	$("#endTime").val("");
	$("#limitSumNum").val("");
	$("#notReturnLimitNum").val("");
	$("#editContent").html("");
};

/**
 * 显示人员树
 */
DailyLimit.showUserSelectTree = function () {
    Feng.showInputTree("userName", "userContent");
};
 
/**
 * 选择树形节点
 */
DailyLimit.onClickUser = function (e, treeId, treeNode) {
    $("#userName").val(treeNode.name);
    $("#accountId").val(treeNode.id);
    var accountId = $("#accountId").val();
    if(accountId != null && accountId != ""){
        var url = Feng.ctxPath + "/dailyLimit/searchUser/" + accountId;
        var ajax = new $ax(url, function (data) {
            $("#limitSumNum").val(data.limitSumNum);
            $("#notReturnLimitNum").val(data.notReturnLimitNum);
            $("#startTime").val(data.startTime);
            $("#endTime").val(data.endTime);
        }, function (data) {
            Feng.error("获取限额信息失败!");
        });
        ajax.start();
    }else{
        Feng.error("请选择某一员工！");
        return;
    }
};
 
/**
 * 点击树形节点前判断
 */
DailyLimit.onBeforeClick = function(treeId, treeNode, clickFlag) {
    if (treeNode.id.startsWith("d_")) {
        return false;
    }
    return true;
};
 
$(function() {
    var userZtree = new $ZTree("userTree", "/user/tree");
    userZtree.bindBeforeClick(DailyLimit.onBeforeClick);
    userZtree.bindOnClick(DailyLimit.onClickUser);
    userZtree.init();
    DailyLimit.userInstance = userZtree;
});