/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var AccountInfoDlg = {
    accountInfoData: {},
    validateFields: {
    	loginName: {
            validators: {
                notEmpty: {
                    message: '账户不能为空'
                }
            }
        },
        password: {
            validators: {
                notEmpty: {
                    message: '密码不能为空'
                }
            }
        },
        rePassword: {
            validators: {
                notEmpty: {
                    message: '确认密码不能为空'
                },
                identical: {
                    field: 'password',
                    message: '两次密码不一致'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
AccountInfoDlg.clearData = function () {
    this.accountInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AccountInfoDlg.set = function (key, val) {
    this.accountInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AccountInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
AccountInfoDlg.close = function () {
    parent.layer.close(window.parent.MgrAccount.layerIndex);
};

/**
 * 收集数据
 */
AccountInfoDlg.collectData = function () {
    this.set('id').set('loginName').set('password');
};

/**
 * 验证数据是否为空
 */
AccountInfoDlg.validate = function () {
    $('#accountInfoForm').data("bootstrapValidator").resetForm();
    $('#accountInfoForm').bootstrapValidator('validate');
    return $("#accountInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 验证两个密码是否一致
 */
AccountInfoDlg.validatePwd = function () {
    var password = this.get("password");
    var rePassword = this.get("rePassword");
    if (password == rePassword) {
        return true;
    } else {
        return false;
    }
};

/**
 * 提交添加账户
 */
AccountInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    if (!this.validatePwd()) {
        Feng.error("两次密码输入不一致");
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgrAccount/add", function (data) {
        Feng.success("添加成功!");
        window.parent.MgrAccount.oTable.refresh();
        AccountInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.accountInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
AccountInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    
    if (!this.validatePwd()) {
        Feng.error("两次密码输入不一致");
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgrAccount/edit", 
	    function (data) {
	    	if(data.code != 201){
				Feng.success(data.message);
		        if (window.parent.MgrAccount != undefined) {
		            window.parent.MgrAccount.oTable.refresh();
		            AccountInfoDlg.close();
		        }
			}else{
				Feng.info(data.message);
			}
	    }, function (data) {
	        Feng.error("修改失败!" + data.responseJSON.message + "!");
	    });
    ajax.set(this.accountInfoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("accountInfoForm", AccountInfoDlg.validateFields);
});