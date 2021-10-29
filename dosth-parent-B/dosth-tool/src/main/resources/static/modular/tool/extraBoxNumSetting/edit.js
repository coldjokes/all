/**
 * 归还类型定义详情对话框（可用于添加和修改对话框）
 */
var ExtraBoxNumInfoDlg = {
	ExtraBoxNumInfoData: {},
    userInstance:null,
    validateFields: {
    	userName: {
            validators: {
                notEmpty: {
                    message: '姓名/工号不能为空 。'
                }
            }
        },
    	extraBoxNum: {
            validators: {
                notEmpty: {
                    message: '暂存柜数量不能为空 。可设置为：0'
                },
                regexp: {
                    regexp: /^[0-9]+$/,
                    message: '暂存柜数量只能是数字。'
                }
            }
        }
    }
};

/**
 * 显示物料/设备类型树
 */
ExtraBoxNumInfoDlg.showUserSelectTree = function() {
	Feng.showInputTree("userName", "userContent");
};

/**
 * 点击当前货位树形节点前判断
 */
ExtraBoxNumInfoDlg.beforeOnClickUser = function(e, treeNode, nodeIndex) {
	if (treeNode.id.startsWith("d_")) {
		return false;
	}
	return true;
};

/**
 * 选择物料/设备类型树形节点
 */
ExtraBoxNumInfoDlg.onClickUser = function (e, treeId, treeNode) {
    $("#userName").attr("value", ExtraBoxNumInfoDlg.userInstance.getSelectedVal());
    $("#accountId").attr("value", treeNode.id);
};

/**
 * 清除数据
 */
ExtraBoxNumInfoDlg.clearData = function () {
    this.ExtraBoxNumInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ExtraBoxNumInfoDlg.set = function (key, val) {
    this.ExtraBoxNumInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ExtraBoxNumInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
ExtraBoxNumInfoDlg.close = function () {
    parent.layer.close(window.parent.ExtraBoxNumSetting.layerIndex);
};

/**
 * 收集数据
 */
ExtraBoxNumInfoDlg.collectData = function () {
    this.set('id').set('userName').set('extraBoxNum').set('accountId');
};

/**
 * 验证数据是否为空
 */
ExtraBoxNumInfoDlg.validate = function () {
    $('#extraBoxNumSettingForm').data("bootstrapValidator").resetForm();
    $('#extraBoxNumSettingForm').bootstrapValidator('validate');
    return $("#extraBoxNumSettingForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
ExtraBoxNumInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    var url = Feng.ctxPath + "/extraBoxNumSetting/add";
    if ($("#id").val() != '') {
    	url = Feng.ctxPath + "/extraBoxNumSetting/edit";
    }
    
    //提交信息
    var ajax = new $ax(url, function (data) {
    	if(data.code == 200){
    		window.parent.Feng.success(data.message);
            window.parent.ExtraBoxNumSetting.oTable.refresh();
            ExtraBoxNumInfoDlg.close();
    	}else{
    		window.parent.Feng.error(data.message);
    	}
    }, function (data) {
    	window.parent.Feng.error(data.message);
    });
    ajax.set(this.ExtraBoxNumInfoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("extraBoxNumSettingForm", ExtraBoxNumInfoDlg.validateFields);   
    
    var userZtree = new $ZTree("userTree", "/user/tree");
    userZtree.bindBeforeClick(ExtraBoxNumInfoDlg.beforeOnClickUser);
    userZtree.bindOnClick(ExtraBoxNumInfoDlg.onClickUser);
    userZtree.init();
    ExtraBoxNumInfoDlg.userInstance = userZtree;
});