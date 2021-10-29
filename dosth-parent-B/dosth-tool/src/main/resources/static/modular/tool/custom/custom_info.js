/**
 * PLC设置详情对话框（可用于添加和修改对话框）
 */
var CustomInfoDlg = {
	customInfoData: {},
    validateFields: {
    	customName: {
            validators: {
                notEmpty: {
                    message: '定义名称不能为空'
                }
            }
        },
        oneMenu: {
            validators: {
                notEmpty: {
                    message: '一级菜单不能为空'
                }
            }
        } ,
        twoMenu: {
            validators: {
                notEmpty: {
                    message: '二级菜单不能为空'
                }
            }
        } 
    }
};

/**
 * 清除数据
 */
CustomInfoDlg.clearData = function () {
    this.customInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomInfoDlg.set = function (key, val) {
    this.customInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
CustomInfoDlg.close = function () {
    parent.layer.close(window.parent.Custom.layerIndex);
};

/**
 * 收集数据
 */
CustomInfoDlg.collectData = function () {
    this.set('id').set('customName').set('oneMenu').set('twoMenu').set('remark');
};

/**
 * 验证数据是否为空
 */
CustomInfoDlg.validate = function () {
    $('#customInfoForm').data("bootstrapValidator").resetForm();
    $('#customInfoForm').bootstrapValidator('validate');
    return $("#customInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
CustomInfoDlg.addSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/custom/add", 
	function (data) {
        Feng.success("添加成功!");
        window.parent.Custom.oTable.refresh();
        CustomInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
CustomInfoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/custom/edit", function (data) {
        Feng.success("修改成功!");
        if (window.parent.Custom != undefined) {
            window.parent.Custom.oTable.refresh();
            CustomInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customInfoData);
    ajax.start();
};

/**
 * 显示树
 */
CustomInfoDlg.showCustomTree1 = function () {
    Feng.showInputTree("oneMenu", "customContent1");
};

/**
 * 显示树
 */
CustomInfoDlg.showCustomTree2 = function () {
    Feng.showInputTree("twoMenu", "customContent2");
};

/**
 * 点击树形节点事件
 */
CustomInfoDlg.onClickCustom1 = function(e, treeId, treeNode) {
	$("#oneMenu").attr("value", CustomInfoDlg.instance1.getSelectedVal());
	var oneMenuId = treeNode.id;
    $("#oneMenuId").attr("value", oneMenuId.substr(2, oneMenuId.length));
};

/**
 * 点击树形节点事件
 */
CustomInfoDlg.onClickCustom2 = function(e, treeId, treeNode) {
    $("#twoMenu").attr("value", CustomInfoDlg.instance2.getSelectedVal());
    var twoMenuId = treeNode.id;
    $("#twoMenuId").attr("value", twoMenuId.substr(2, twoMenuId.length));
};

/**
 * 点击树形节点前判断
 */
CustomInfoDlg.onBeforeClick = function(treeId, treeNode, clickFlag) {
	if (treeNode.id.startsWith("A_") || treeNode.id.startsWith("B_") || treeNode.id.startsWith("C_")) {
		return true;
	}
	return false;
};

$(function () {
    var ztree1 = new $ZTree("customTree1", "/matAssociation/customTree");
    ztree1.bindBeforeClick(CustomInfoDlg.onBeforeClick);
    ztree1.bindOnClick(CustomInfoDlg.onClickCustom1);
    ztree1.init();
    CustomInfoDlg.instance1 = ztree1;
    
    var ztree2 = new $ZTree("customTree2", "/matAssociation/customTree");
    ztree2.bindBeforeClick(CustomInfoDlg.onBeforeClick);
    ztree2.bindOnClick(CustomInfoDlg.onClickCustom2);
    ztree2.init();
    CustomInfoDlg.instance2 = ztree2;
    
    Feng.initValidator("customInfoForm", CustomInfoDlg.validateFields);

});