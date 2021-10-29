/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
    infoData: {}
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
    parent.layer.close(window.parent.MatReturnBackBill.layerIndex);
};

/**
 * 收集数据
 */
infoDlg.collectData = function () {
    this.set('num').set('remark').set('auditStatus');
};

/**
 * 提交信息
 */
infoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();
    var num = $("#num").val();
    if (num == '') {
    	Feng.info("实际数量不能为空。");
        return;
    }
    var msg;
    var err;
    var matReturnBackId = $("#matReturnBackId").val();
    if ($("#id").val() != '') {
    	url = Feng.ctxPath + "/matReturnBackBill/abnormalConfirmSubmit/" + matReturnBackId;
        msg = "确认成功!";
        err = "确认失败!";
    }
    var ajax = new $ax(url, function (data) {
    	window.parent.Feng.success(msg);
        window.parent.MatReturnBackBill.search();
        infoDlg.close();
    }, function (data) {
    	window.parent.Feng.error(msg + data.responseJSON.message + "!");
    });
    ajax.set(this.infoData);
    ajax.start();
};