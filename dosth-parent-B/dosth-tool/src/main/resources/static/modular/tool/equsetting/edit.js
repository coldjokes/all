/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
	infoData : {},
	userInstance : null,
	validateFields : {
		equSettingName : {
			validators : {
				notEmpty : {
					message : '名称不能为空'
				}
			}
		},
		serialNo : {
			validators : {
				notEmpty : {
					message : '序列号不能为空'
				}
			}
		},
		userName : {
			trigger: "change",
			validators : {
				notEmpty : {
					message : '管理员不能为空'
				}
			}
		},
		rowNum : {
			validators : {
				notEmpty : {
					message : '行数不能为空'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		},
		colNum : {
			validators : {
				notEmpty : {
					message : '列数不能为空'
				},
				digits : {
					message : '只能输入数字'
				}
			}
		},
		cabinetType : {
			validators : {
				notEmpty : {
					message : '类型不能为空'
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
	this.infoData[key] = (typeof value == "undefined") 
			? $("#" + key).val()
			: value;
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
	parent.layer.close(window.parent.MgrEquSetting.layerIndex);
};

/**
 * 收集数据
 */
infoDlg.collectData = function() {
	this.set('id').set('equInfoId').set('equSettingName').set('rowNum').set(
			'colNum').set('accountId').set('serialNo').set('status').set(
			'cabinetType').set('equSettingParentId').set('shareSwitch');
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
 * 提交信息-主柜
 */
infoDlg.editSubmit = function() {
	this.clearData();
	this.collectData();
	if (!this.validate()) {
		return;
	}
	var url = Feng.ctxPath + "/equsetting/add";
	if ($("#id").val() != '') {
		url = Feng.ctxPath + "/equsetting/edit";
	}

	// 提交信息
	var ajax = new $ax(url, function(data) {
		window.parent.Feng.success(data.message);
		window.parent.tb1.bootstrapTable('refresh');
		infoDlg.close();
	}, function(err) {
		window.parent.Feng.error(err.responseJSON.message);
	});
	ajax.set(this.infoData);
	ajax.start();
};

/**
 * 提交信息-子柜
 */
infoDlg.editInfoSubmit = function() {
	
	var shareSwitch = $("input[id='shareSwitch']").is(':checked') == true ? 'TRUE': 'FALSE';
	$("#shareSwitch").val(shareSwitch);
	this.clearData();
	this.collectData();
	if (!this.validate()) {
		return;
	}
	var url = Feng.ctxPath + "/equsetting/add";
	if ($("#id").val() != '') {
		url = Feng.ctxPath + "/equsetting/edit";
	}

	// 提交信息
	var ajax = new $ax(url, function(data) {
		if (data.code == 200) {
			window.parent.Feng.success(data.message);
			window.parent.tb1.bootstrapTable('refresh');
			infoDlg.close();
		} else {
			window.parent.Feng.info(data.message);
		}
	}, function(data) {
		window.parent.Feng.error(data.message);
	});
	ajax.set(this.infoData);
	ajax.start();
};

 
/**
 * 显示人员树
 */
infoDlg.showUserSelectTree = function() {
    Feng.showInputTree("userName", "userContent");
};
 
/**
 * 选择树形节点
 */
infoDlg.onClickUser = function(e, treeId, treeNode) {
    $("#userName").attr("value", infoDlg.userInstance.getSelectedVal());
    $("#accountId").attr("value", treeNode.id);
    $("#userName").change();
};
 
/**
 * 点击树形节点前判断
 */
infoDlg.onBeforeClick = function(treeId, treeNode, clickFlag) {
    if (treeNode.id.startsWith("d_")) {
        return false;
    }
    return true;
};

$(function() {
	Feng.initValidator("infoForm", infoDlg.validateFields);
	
    var userZtree = new $ZTree("userTree", "/user/tree");
    userZtree.bindBeforeClick(infoDlg.onBeforeClick);
    userZtree.bindOnClick(infoDlg.onClickUser);
    userZtree.init();
    infoDlg.userInstance = userZtree;
});