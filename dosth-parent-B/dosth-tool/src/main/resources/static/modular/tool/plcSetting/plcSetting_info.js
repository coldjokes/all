/**
 * PLC设置详情对话框（可用于添加和修改对话框）
 */
var PlcSettingInfoDlg = {
	plcSettingInfoData: {},
    validateFields: {
    	plcName: {
            validators: {
                notEmpty: {
                    message: 'PLC名称不能为空'
                }
            }
        },
        address: {
            validators: {
                notEmpty: {
                    message: 'PLC地址不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
PlcSettingInfoDlg.clearData = function () {
    this.plcSettingInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PlcSettingInfoDlg.set = function (key, val) {
    this.plcSettingInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PlcSettingInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
PlcSettingInfoDlg.close = function () {
    parent.layer.close(window.parent.PlcSetting.layerIndex);
};

/**
 * 收集数据
 */
PlcSettingInfoDlg.collectData = function () {
    this.set('id').set('plcName').set('address').set('defaultValue').set('remark');
};

/**
 * 验证数据是否为空
 */
PlcSettingInfoDlg.validate = function () {
    $('#plcSettingInfoForm').data("bootstrapValidator").resetForm();
    $('#plcSettingInfoForm').bootstrapValidator('validate');
    return $("#plcSettingInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
PlcSettingInfoDlg.addSubmit = function () {
	
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/plcSetting/add", function (data) {
    	
    		console.log(data);
    		console.log(typeof(data));
    		console.log(data.code);
    		console.log(data.message);
    	
        Feng.success("添加成功!");
        window.parent.PlcSetting.oTable.refresh();
        PlcSettingInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.plcSettingInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
PlcSettingInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/plcSetting/edit", function (data) {
        Feng.success("修改成功!");
        if (window.parent.PlcSetting != undefined) {
            window.parent.PlcSetting.oTable.refresh();
            PlcSettingInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.plcSettingInfoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("plcSettingInfoForm", PlcSettingInfoDlg.validateFields);

});