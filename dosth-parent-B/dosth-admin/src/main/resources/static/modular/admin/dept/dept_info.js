/**
 * 初始化部门详情对话框
 */
var DeptInfoDlg = {
    deptInfoData: {},
    validateFields: {
        deptName: {
            validators: {
                notEmpty: {
                    message: '*必填'
                }
            }
        },
        citySel: {
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
DeptInfoDlg.clearData = function () {
    this.deptInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeptInfoDlg.set = function (key, val) {
    this.deptInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeptInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
DeptInfoDlg.close = function () {
    parent.layer.close(window.parent.MgrDept.layerIndex);
}

/**
 * 点击部门ztree列表的选项时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
DeptInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#citySel").attr("value", instance.getSelectedVal());
    $("#pId").attr("value", treeNode.id.split('_')[1]);
}

/**
 * 显示部门选择的树
 *
 * @returns
 */
DeptInfoDlg.showDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityOffset = $("#citySel").offset();
    $("#menuContent").css({
        left: cityOffset.left + "px",
        top: cityOffset.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
}

/**
 * 隐藏部门选择的树
 */
DeptInfoDlg.hideDeptSelectTree = function () {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 收集数据
 */
DeptInfoDlg.collectData = function () {
    this.set('id').set('deptName').set('simpleName').set('fullName').set('pId').set('deptNo');
}

/**
 * 验证数据是否为空
 */
DeptInfoDlg.validate = function () {
    $('#deptInfoForm').data("bootstrapValidator").resetForm();
    $('#deptInfoForm').bootstrapValidator('validate');
    return $("#deptInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加部门
 */
DeptInfoDlg.addSubmit = function () {
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgrDept/add", function (data) {
        if (data.code == 200) {
            Feng.success(data.message);
            window.parent.tbl.bootstrapTable('refresh');
            DeptInfoDlg.close();
        } else {
            Feng.info(data.message);
        }
    }, function (data) {
        Feng.error("添加失败");
    });
    ajax.set(this.deptInfoData);
    ajax.start();
}

/**
 * 提交修改部门
 */
DeptInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgrDept/edit", function (data) {
        Feng.success("修改成功");
        window.parent.tbl.bootstrapTable('refresh');
        DeptInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败");
    });
    ajax.set(this.deptInfoData);
    ajax.start();
}

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
        event.target).parents("#menuContent").length > 0)) {
        DeptInfoDlg.hideDeptSelectTree();
    }
}

$(function () {
    Feng.initValidator("deptInfoForm", DeptInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/mgrDept/tree");
    ztree.bindOnClick(DeptInfoDlg.onClickDept);
    ztree.init();
    instance = ztree;

});

