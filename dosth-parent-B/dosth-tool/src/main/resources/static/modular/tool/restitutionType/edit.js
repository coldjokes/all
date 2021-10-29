/**
 * 归还类型定义详情对话框（可用于添加和修改对话框）
 */
var ReTypeInfoDlg = {
	ReTypeInfoData: {},
    instance:null,
    validateFields: {
    	restName: {
            validators: {
                notEmpty: {
                    message: '*必填'
                },
                stringLength:{
                	max:8,
                	message : '不超过8个字符'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
ReTypeInfoDlg.clearData = function () {
    this.ReTypeInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ReTypeInfoDlg.set = function (key, val) {
    this.ReTypeInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ReTypeInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
ReTypeInfoDlg.close = function () {
    parent.layer.close(window.parent.ReType.layerIndex);
};

/**
 * 收集数据
 */
ReTypeInfoDlg.collectData = function () {
    this.set('id').set('restName').set('returnBackType').set('remark');
};

/**
 * 验证数据是否为空
 */
ReTypeInfoDlg.validate = function () {
    $('#reTypeForm').data("bootstrapValidator").resetForm();
    $('#reTypeForm').bootstrapValidator('validate');
    return $("#reTypeForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息
 */
ReTypeInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    var url = Feng.ctxPath + "/restitutionType/add";
    var msg = "添加成功!";
    var err = "添加失败!";
    if ($("#id").val() != '') {
    	url = Feng.ctxPath + "/restitutionType/edit";
        msg = "修改成功!";
        err = "修改失败!";
    }
    
    //提交信息
    var ajax = new $ax(url, function (data) {
    	window.parent.Feng.success(msg);
        window.parent.tbl.bootstrapTable('refresh');
        ReTypeInfoDlg.close();
    }, function (data) {
    	window.parent.Feng.error(msg + data.responseJSON.message + "!");
    });
    ajax.set(this.ReTypeInfoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("reTypeForm", ReTypeInfoDlg.validateFields); 
    
});