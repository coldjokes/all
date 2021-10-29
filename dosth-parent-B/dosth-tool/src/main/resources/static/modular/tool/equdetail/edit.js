/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
    matInfoInstance:null,
    infoData: {},
    validateFields: {
        totalReserve: {
            validators: {
                notEmpty: {
                    message: '总存储量不能为空'
                }
            }
        },
        warnVal: {
            validators: {
                notEmpty: {
                    message: '告警阀值不能为空'
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
    parent.layer.close(window.parent.MgrEquDetail.layerIndex);
};
 
/**
 * 收集数据
 */
infoDlg.collectData = function () {
    this.set('id').set('equDetailId').set('colNo').set('maxReserve').set('warnVal');
};
 
/**
 * 验证数据是否为空
 */
infoDlg.validate = function () {
    $('#infoForm').data("bootstrapValidator");
    $('#infoForm').bootstrapValidator('validate');
    return $("#infoForm").data('bootstrapValidator').isValid();
};
 
/**
 * 提交信息
 */
infoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();
 
    if (!this.validate()) {
        return;
    }
    
    var url = Feng.ctxPath + "/equdetail/edit";
    //提交信息
    var ajax = new $ax(url, function (data) {
        if (data.code == 200) {
            Feng.success(data.message);
            setTimeout(function (){
                infoDlg.close();
            }, 1000);
            window.parent.managerTable.bootstrapTable('refresh');
        }else{
            Feng.info(data.message);
        }
    }, function (data) {
        Feng.error(data.message);
    });
    ajax.set(this.infoData);
    ajax.start();
};
 
$(function () {
    Feng.initValidator("infoForm", infoDlg.validateFields);
});