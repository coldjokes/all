/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
    infoData: {},
    validateFields: {
    	contactName: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        },
        contactPhone: {
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
        mailAddress: {
            validators: {
                regexp: {
                	regexp: /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/,
        			message: '邮箱地址输入不正确'
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
    this.set('id').set('manufacturerId').set('contactName').set('contactPhone').set('mailAddress').set('remark');
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
 * 提交信息-供应商详情
 */
infoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    var url = Feng.ctxPath + "/manufacturer/addInfo";
    var msg = "添加成功!";
    var err = "添加失败!";
    if ($("#id").val() != '') {
    	url = Feng.ctxPath + "/manufacturer/editInfo";
        msg = "修改成功!";
        err = "修改失败!";
    }
    
    //提交信息
    var ajax = new $ax(url, function (data) {
    	window.parent.Feng.success(msg);
		setTimeout(function (){
			infoDlg.close();
		}, 600);
		window.parent.tb2.bootstrapTable('refresh');
    }, function (data) {
    	window.parent.Feng.error(msg + data.responseJSON.message + "!");
    });
    ajax.set(this.infoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("infoForm", infoDlg.validateFields);
});