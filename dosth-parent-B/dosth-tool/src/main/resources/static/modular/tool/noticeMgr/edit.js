/**
 * 通知管理
 */
var infoDlg = {
	userTree : null,
	userInstance : null,
	infoData : {},
	validateFields : {
		userName : {
			validators : {
				notEmpty : {
					message : '收件人不能为空'
				}
			}
		},
		num : {
			validators : {
				notEmpty : {
					message : '数量不能为空'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		},
		warnValue : {
			validators : {
				notEmpty : {
					message : '预警值不能为空'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		}
	}
};

/**
 * 清除数据
 */
infoDlg.clearData = function() {
	this.infoData = {};
};

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
infoDlg.set = function(key, val) {
	this.infoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;

	// 获取ztree accountId数组
	var accountIds = '';
	var selected = userTree.getCheckedNodes(true);
	if(selected.length > 0){
		for (var i = 0; i < selected.length; i++) {
			if (selected[i].id.startsWith('d_')) {
				continue;
			}
			accountIds += selected[i].id + ',';
		}
	} else {
		accountIds = $("#accountId").val();
	}
	this.infoData['accountId'] = (typeof value == "undefined") ? accountIds : value;
	return this;
};

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
infoDlg.get = function(key) {
	return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
infoDlg.close = function() {
	parent.layer.close(window.parent.NoticeMgr.layerIndex);
};

/**
 * 收集数据
 */
infoDlg.collectData = function() {
	this.set('id').set('equSettingId').set('noticeType').set('num').set('warnValue').set('accountId');
};

/**
 * 验证数据是否为空
 */
infoDlg.validate = function() {
	$('#infoForm').data("bootstrapValidator").resetForm();
	$('#infoForm').bootstrapValidator('validate');
	return $("#infoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
infoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    var url = Feng.ctxPath + "/noticeMgr/add";
    if ($("#id").val() != '') {
    	url = Feng.ctxPath + "/noticeMgr/edit";
    }
    
    //提交信息
    var ajax = new $ax(url, function (data) {
    	if(data.code == 200){
    		window.parent.Feng.success(data.message);
    		setTimeout(function (){
    			infoDlg.close();
    		}, 1000);
    		window.parent.tbl.bootstrapTable('refresh');
    	} else {
    		window.parent.Feng.info(data.message);
    	}
    }, function (data) {
    	window.parent.Feng.error(data.responseJSON.message);
    });
    ajax.set(this.infoData);
    ajax.start();
};

/**
 * 人员树
 */
infoDlg.userTree = function() {
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
					window.parent.Feng.info("请选择员工");
					return false;
				}
				return true;
			},
			beforeCheck : function(treeId, treeNode) {
				if (treeNode.id.startsWith('d_')) {
					window.parent.Feng.info("请选择员工");
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
				$("#userName").attr("value", userName);
				$("#accountId").attr("value", treeNode.id);
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
				$("#userName").attr("value", userName);
				$("#accountId").attr("value", treeNode.id);
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

/**
 * 显示人员树
 */
infoDlg.showUserSelectTree = function() {
	// 复选框回显
	var zTreeObj = $.fn.zTree.getZTreeObj("userTree");
    var zTree = zTreeObj.getCheckedNodes(false);
    var accountId= $("#accountId").val();
    for (var i = 0; i < zTree.length; i++) {
        if (accountId.indexOf(zTree[i].id + ",") != -1) {
            zTreeObj.expandNode(zTree[i], true);
            zTreeObj.checkNode(zTree[i], true, true);
        }
    }
    
	Feng.showInputTree("userName", "userContent");
};

$(function() {
	Feng.initValidator("infoForm", infoDlg.validateFields);
	infoDlg.userTree();
	infoDlg.userInstance = userTree;
});