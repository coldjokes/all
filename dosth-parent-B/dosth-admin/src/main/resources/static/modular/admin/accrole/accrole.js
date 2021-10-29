/**
 * 角色分配
 */
var AccRole = {
	accountTree:null,
	roleTree:null,
	accountId:null
};

/**
 * 账户树
 */
AccRole.initAccountTree = function() {
	var accountTree = new $ZTree("accountTree", "/mgrAccount/crAccTree");
	accountTree.bindOnClick(AccRole.onClickAccount);
	accountTree.init();
	AccRole.accountTree = accountTree;
};

/**
 * 账户树点击事件
 */
AccRole.onClickAccount = function(e, treeId, treeNode) {
	AccRole.roleTree.clearAllCheck();
	AccRole.accountId = treeNode.id;
	if (AccRole.accountId == null || AccRole.accountId.indexOf('d_')!=-1) {
		Feng.info("请先选择某一账户！");
		return;
	}
	var ajax = new $ax('/mgrAcRole/getRoleIdListByAccountId/'+AccRole.accountId, function(data) {
		for (var i = 0; i < data.length; i++) {
			AccRole.roleTree.setNodeCheckByParam("id", data[i], 1);
		}
	}, function(data) {
		Feng.error("加载角色信息失败!");
	});
	ajax.start();
};

/**
 * 角色树
 */
AccRole.initRoleTree = function() {
	var roleTree = new $ZTree("roleTree", "/mgrRole/createRoleTree");
	var setting = {
	        check: {
	            enable: true,
	            chkboxType: { "Y": "ps", "N": "ps" }
	        },
	        data: {
	            simpleData: {
	                enable: true,
	                idKey : "id",
	                PIdKey : "pId"
	            }
	        }
	    };
	roleTree.setSettings(setting);
	roleTree.init();
	AccRole.roleTree = roleTree;
};

/**
 * 角色分配
 */
AccRole.editSubmit = function() {
	if (AccRole.accountId == null || AccRole.accountId.indexOf('d_')!=-1) {
		Feng.info("请先选择某一账户！");
		return;
	}
	var roleIds = Feng.zTreeCheckedNodes("roleTree");
	var ajax = new $ax("/mgrAcRole/updateRoles", function (data) {
        Feng.success("修改成功!");
    }, function (data) {
    	Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set("accountId", AccRole.accountId);
    ajax.set("roleIds", roleIds);
    ajax.start();
};

/**
 * 重置
 */
AccRole.resetData = function() {
	AccRole.accountTree.clearAllCheck();
	AccRole.roleTree.clearAllCheck();
	AccRole.accountId = null;
};

/**
 * 初始化
 */
$(function() {
	AccRole.initAccountTree();
	AccRole.initRoleTree();
});