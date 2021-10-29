/**
 * 系统管理--主页的单例对象
 */
var MgrIndex = {
    layerIndex: -1,
    validateFields: {
    	oldPwd: {
            validators: {
                notEmpty: {
                    message: '原密码不能为空'
                }
            }
        },
        newPwd: {
            validators: {
                notEmpty: {
                    message: '新密码不能为空'
                },
                identical: {
                    field: 'rePassword',
                    message: '两次密码不一致'
                },
            }
        },
        rePwd: {
            validators: {
                notEmpty: {
                    message: '确认密码不能为空'
                },
                identical: {
                    field: 'password',
                    message: '两次密码不一致'
                },
            }
        }
    }
};

/**
 * 关闭此对话框
 */
MgrIndex.close = function () {
    parent.layer.close(window.parent.MgrIndex.layerIndex);
};

/**
 * 点击修改密码
 * @param accountId 账户id
 */
MgrIndex.openChangeAccount = function () {
    var index = layer.open({
        type: 2,
        title: '修改密码',
        area: ['800px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/mgrAccount/user_chpwd'
    });
    this.layerIndex = index;
};

/**
 * 验证数据是否为空
 */
MgrIndex.validate = function () {
    $('#accountInfoForm').data("bootstrapValidator").resetForm();
    $('#accountInfoForm').bootstrapValidator('validate');
    return $("#accountInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 验证两个密码是否一致
 */
MgrIndex.validatePwd = function () {
    var newPwd = $("#newPwd").val();
    var rePwd = $("#rePwd").val();
    if (newPwd == rePwd) {
        return true;
    } else {
        return false;
    }
};

/**
 * 修改密码
 */
MgrIndex.openResetAccount = function () {
	if (!this.validate()) {
        return;
    }
    
    if (!this.validatePwd()) {
        Feng.error("两次密码输入不一致");
        return;
    }
	
	var ajax = new $ax(Feng.ctxPath + "/mgrAccount/changePwd", function (data) {
        Feng.success("修改成功!");
        MgrIndex.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set("oldPwd");
    ajax.set("newPwd");
    ajax.set("rePwd");
    ajax.start();
};

/**
 * 个人资料
 */
MgrIndex.openUserInfo = function () {
	var index = layer.open({
        type: 2,
        title: '个人资料',
        area: ['800px', '490px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/mgrUser/user_info'
    });
    this.layerIndex = index;
}

$(function () {
    Feng.initValidator("accountInfoForm", MgrIndex.validateFields);
});