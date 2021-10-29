/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
    matEquTypeInstance: null,
    infoData: {},
    validateFields: {
        barCode: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        },
        spec: {
            validators: {
                notEmpty: {
                    message: '*必填'
                },
                stringLength: {
                    max: 50,
                    message: '备注长度必须50字符'
                }
            }
        },
        matEquName: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        },
        packUnit: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        },
        num: {
            validators: {
                notEmpty: {
                    message: '*必填'
                },
                digits: {
                    message: '只能输入数字'
                }
            }
        },
        lowerStockNum: {
            validators: {
                notEmpty: {
                    message: '*必填'
                },
                digits: {
                    message: '只能输入数字'
                }
            }
        },
        storePrice: {
            validators: {
                notEmpty: {
                    message: '*必填'
                },
                number: {
                    message: '只能输入数字'
                }
            }
        },
        manufacturerName: {
        	trigger: "change",
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
    parent.layer.close(window.parent.MgrMatEquInfo.layerIndex);
};

/**
 * 收集数据
 */
infoDlg.collectData = function () {
    this.set('id').set('barCode').set('matEquName').set('packUnit').set('spec').set('brand')
        .set('storePrice').set('remark').set('icon').set('borrowType').set('oldForNew').set('num').set('lowerStockNum').set('useLife').set('manufacturerId');
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
 * 提交信息
 */
infoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    var icon = $("#icon").val();
    if (icon == "") {
        Feng.info("请先选择上传的图片！");
        return;
    }
    var url = Feng.ctxPath + "/matequinfo/add";
    var msg = "添加成功!";
    var err = "添加失败!";
    if ($("#id").val() != '') {
        url = Feng.ctxPath + "/matequinfo/edit";
        msg = "修改成功!";
        err = "修改失败!";
    }

    //提交信息
    var ajax = new $ax(url, function (data) {
        window.parent.Feng.success(msg);
        window.parent.tbl.bootstrapTable('refresh');
        infoDlg.close();
    }, function (data) {
        window.parent.Feng.error(msg + data.responseJSON.message + "!");
    });
    ajax.set(this.infoData);
    ajax.start();
};

/**
 * 显示供应商树
 */
infoDlg.showManufacturerSelectTree = function () {
    Feng.showInputTree("manufacturerName", "manufacturerContent");
};

/**
 * 选择供应商树形节点
 */
infoDlg.onClickManufacturer = function (e, treeId, treeNode) {
    $("#manufacturerName").attr("value", infoDlg.manufacturerInstance.getSelectedVal());
    $("#manufacturerId").attr("value", treeNode.id);
    $("#manufacturerName").change();
};

$(function () {
    Feng.initValidator("infoForm", infoDlg.validateFields);

    var manufacturerZtree = new $ZTree("manufacturerTree", "/manufacturer/tree");
    manufacturerZtree.bindOnClick(infoDlg.onClickManufacturer);
    manufacturerZtree.init();
    infoDlg.manufacturerInstance = manufacturerZtree;

    // 初始化上传控件
    var iconUp = new $WebUpload("icon");
    iconUp.setUploadBarId("progressBar");
    iconUp.init();
});