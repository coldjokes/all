/**
 * 领取权限
 */
var tree;
var tree2;

$(function() {
	loadTree();
	loadTree2();
});

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
		callback : {
			beforeClick:function(treeId, treeNode, clickFlag) {
				if (treeNode.id.startsWith('d_')) {
					Feng.info('请选择人员帐号!');
				}
				return !treeNode.id.startsWith('d_');
			},
			onClick:function(event, treeId, treeNode) {
				loadTree2(treeNode.id);
			}
		}
	};

var setting2 = {
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
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		},
		callback : {
			onClick:function(event, treeId, treeNode) {
				tree2.checkNode(treeNode, !treeNode.checked, true);
			}
		}
	};

/**
 * 初始化帐号树
 */
function loadTree() {
	var url = "/borrowPopedom/initAccountTree";
	$.ajax({
		type : "post",
		url : url,
		async : false,
		dataType : "json",
		success : function(data) {
			tree = $.fn.zTree.init($("#accountTree"), setting, data);
		}
	});
	return tree;
}
/**
 * 初始化权限树
 * @param opObjId 操作对象Id
 */
function loadTree2(opObjId) {
	var url = "/borrowPopedom/initBorrowPopedomTree";
	$.ajax({
		type : "post",
		url : url,
		async : false,
		dataType : "json",
		data : {
			"opObjId" : opObjId == undefined ? '-1' : opObjId,
		},
		success : function(data) {
			tree2 = $.fn.zTree.init($("#roleTree"), setting2, data);
		}
	});
	return tree2;
}

/**
 * 设置领取权限
 */
function icBind() {
	var accounts = tree.getSelectedNodes();
	if (accounts.length < 1) {
		Feng.info("请先选择绑定部门或帐号!");
		return;
	}
	var selected = tree2.getCheckedNodes(true);
	if (selected.length < 1) {
		Feng.info("请先选择取料权限!");
		return;
	}
	var firstFlag = true;
	var oldNode = '';
	var popedoms = '';
	for (var i = 0; i < selected.length; i++) {
		if(selected[i].id == 'ROOT'){
			continue;
		}
		var parentNode = selected[i].getParentNode();
		// 判断该节点是否是最上级全选节点
		if(selected[i].check_Child_State != 2 && selected[i].check_Child_State != -1){
			continue;
		}else if(parentNode.id != 'ROOT' && parentNode != null && parentNode.check_Child_State == 2){
			continue;
		}
		// 获取该节点所有的父节点（包括自己）
		var node = selected[i].getPath();
		if(oldNode != node[1].id ){
			if(!firstFlag){
				popedoms += ';';
			}
			popedoms += node[1].id + ':';
		}
		popedoms += selected[i].id + ',';
		oldNode = node[1].id;
		firstFlag = false;
	}
	console.log(popedoms);
 	$.ajax({
		url : '/borrowPopedom/bindBorrowPopedoms',
		type : 'post',//提交方式
		data:{"opObjId":accounts[0].id, "popedoms":popedoms},
		dataType : "json",
		success : function(result) {
			Feng.success(result.message);
		},
		error : function(err) {//后台处理数据失败后的回调函数
			Feng.error(err);
		}
	});
}
