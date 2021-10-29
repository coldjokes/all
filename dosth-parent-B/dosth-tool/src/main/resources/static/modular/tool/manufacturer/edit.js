/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
    infoData: {},
    validateFields: {
    	manufacturerName: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        },
        contact: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        },
        phone: {
            validators: {
                notEmpty: {
                    message: '*必填'
                },
                regexp: {
                	regexp: /^1[3456789]\d{9}$/,
                	message: '手机号码输入不正确'
                }
            }
        },
        address: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
infoDlg.clearData = function () {
    this.infoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
infoDlg.set = function (key, val) {
    this.infoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
infoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
infoDlg.close = function () {
    parent.layer.close(window.parent.MgrManufacturer.layerIndex);
};

/**
 * 收集数据
 */
infoDlg.collectData = function () {
    this.set('id').set('manufacturerName').set('contact').set('phone').set('address').set('remark').set('manufacturerNo');
};

/**
 * 验证数据是否为空
 */
infoDlg.validate = function () {
    $('#infoForm').data("bootstrapValidator").resetForm();
    $('#infoForm').bootstrapValidator('validate');
    return $("#infoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交信息-供应商
 */
infoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    var url = Feng.ctxPath + "/manufacturer/add";
    var msg = "添加成功!";
    var err = "添加失败!";
    if ($("#id").val() != '') {
    	url = Feng.ctxPath + "/manufacturer/edit";
        msg = "修改成功!";
        err = "修改失败!";
    }
    
    //提交信息
    var ajax = new $ax(url, function (data) {
    	window.parent.Feng.success(msg);
		setTimeout(function (){
			infoDlg.close();
		}, 600);
		window.parent.tb1.bootstrapTable('refresh');
    }, function (data) {
    	window.parent.Feng.error(msg + data.responseJSON.message + "!");
    });
    ajax.set(this.infoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("infoForm", infoDlg.validateFields);
});