/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var SystemInfoDlg = {
	systemInfoData: {},
    validateFields: {
    	systemName: {
            validators: {
                notEmpty: {
                    message: '系统名称不能为空'
                }
            }
        },
        url: {
            validators: {
                notEmpty: {
                    message: '系统URL不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
SystemInfoDlg.clearData = function () {
    this.systemInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SystemInfoDlg.set = function (key, val) {
    this.systemInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SystemInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
SystemInfoDlg.close = function () {
    parent.layer.close(window.parent.SystemInfo.layerIndex);
};

/**
 * 收集数据
 */
SystemInfoDlg.collectData = function () {
    this.set('id').set('systemName').set('url');
    
};

/**
 * 验证数据是否为空
 */
SystemInfoDlg.validate = function () {
    $('#systemInfoForm').data("bootstrapValidator").resetForm();
    $('#systemInfoForm').bootstrapValidator('validate');
    return $("#systemInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
SystemInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/systemInfo/add", function (data) {
        Feng.success("添加成功!");
        window.parent.SystemInfo.oTable.refresh();
        SystemInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.systemInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
SystemInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/systemInfo/edit", function (data) {
        Feng.success("修改成功!");
        if (window.parent.SystemInfo != undefined) {
            window.parent.SystemInfo.oTable.refresh();
            SystemInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.systemInfoData);
    ajax.start();
};


$(function () {
    Feng.initValidator("systemInfoForm", SystemInfoDlg.validateFields);

});