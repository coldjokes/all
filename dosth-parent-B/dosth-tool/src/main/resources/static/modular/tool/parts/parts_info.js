/**
 * PLC设置详情对话框（可用于添加和修改对话框）
 */
var PartsInfoDlg = {
	partsInfoData: {},
    validateFields: {
    	partsName: {
            validators: {
                notEmpty: {
                    message: '零件名称不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
PartsInfoDlg.clearData = function () {
    this.partsInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PartsInfoDlg.set = function (key, val) {
    this.partsInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PartsInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
PartsInfoDlg.close = function () {
    parent.layer.close(window.parent.Parts.layerIndex);
};

/**
 * 收集数据
 */
PartsInfoDlg.collectData = function () {
    this.set('id').set('partsName');
};

/**
 * 验证数据是否为空
 */
PartsInfoDlg.validate = function () {
    $('#partsInfoForm').data("bootstrapValidator").resetForm();
    $('#partsInfoForm').bootstrapValidator('validate');
    return $("#partsInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
PartsInfoDlg.addSubmit = function () {
	
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/parts/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Parts.oTable.refresh();
        PartsInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.partsInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
PartsInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/parts/edit", function (data) {
        Feng.success("修改成功!");
        if (window.parent.Parts != undefined) {
            window.parent.Parts.oTable.refresh();
            PartsInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.partsInfoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("partsInfoForm", PartsInfoDlg.validateFields);

});