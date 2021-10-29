/**
 * 角色管理的单例
 */
var Role = {
    seItemId: null,		//选中的条目
    layerIndex: -1,
    roleInfoData: {},
    deptZtree: null,
    pNameZtree: null,
    menuZtree: null,
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '角色名称不能为空'
                }
            }
        },
        tips: {
            validators: {
                notEmpty: {
                    message: '别名不能为空'
                }
            }
        }
    }
};

/**
 * 创建角色树
 */
Role.init = function () {
	var ztree = new $ZTree("roleTree", "/mgrRole/tree/1");
    ztree.bindOnClick(Role.openChangeRole);
    ztree.init();
};

/**
 * 角色树初始化
 */
Role.roleInit = function () {
	var roleTree = new $ZTree("treeRole", "/mgrRole/tree/0");
	roleTree.bindOnClick(Role.onClickRole);
	roleTree.init();
	Role.pNameZtree = roleTree;
};

/**
 * 部门树初始化
 */
Role.deptInit = function () {
	var deptTree = new $ZTree("treeDept", "/mgrDept/tree");
	deptTree.bindOnClick(Role.onClickDept);
	deptTree.init();
    Role.deptZtree = deptTree;
};

/**
 * 所属部门选择点击
 */
Role.onClickDept = function (e, treeId, treeNode) {
	$("#deptId").val(treeNode.id);
	$("#deptName").val(Role.deptZtree.getSelectedVal());
};

/**
 * 上级角色选择点击
 */
Role.onClickRole = function(e, treeId, treeNode) {
	$("#pId").val(treeNode.id);
	$("#pRoleName").val(Role.pNameZtree.getSelectedVal());
};

Role.showDeptSelectTree = function () {
	Feng.showInputTree("deptName", "deptContent");
};

Role.showRoleSelectTree = function () {
    Feng.showInputTree("pRoleName", "roleContent");
};

/**
 * 验证数据是否为空
 */
Role.validate = function () {
    $('#roleInfoForm').data("bootstrapValidator").resetForm();
    $('#roleInfoForm').bootstrapValidator('validate');
    return $("#roleInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
Role.set = function (key, val) {
    this.roleInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 收集数据
 */
Role.collectData = function () {
    this.set('id').set('name').set('tips').set('pId').set('deptId').set('num');
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
Role.get = function (key) {
    return $("#" + key).val();
};

/**
 * 清除数据
 */
Role.clearData = function () {
    this.roleInfoData = {};
};

/**
 * 重置
 */
Role.resetData = function() {
	this.clearData();
	$("#id").val("");
	$("#name").val("");
	$("#tips").val("");
	$("#pRoleName").val("");
	$("#pId").val("");
	$("#num").val("");
	$("#deptName").val("");
	$("#deptId").val(""); 
	$('#roleInfoForm').data("bootstrapValidator").resetForm();
	$("#btnDel").hide();
	$(".checkbox_true_part").css(".checkbox_false_full");
	$(".checkbox_true_full").css(".checkbox_false_full");
};

/**
 * 权限菜单初始化
 */
Role.initMunuZtree = function(roleId) {
    var setting = {
        check: {
            enable: true,
            chkboxType: { "Y": "ps", "N": "ps" }
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    var ztree = new $ZTree("zTree", "/menu/menuTreeListByRoleId/"
        + roleId);
    ztree.setSettings(setting);
    Role.menuZtree = ztree.init();
}

/**
 * 选择角色加载信息
 */
Role.openChangeRole = function (a,b,c) {
	Role.resetData();
	var roleId = c.id;
    if (roleId == 0) {
        Feng.info("请先选中非根目录的某一角色！");
        return false;
    } else {
    	var ajax = new $ax('/mgrRole/role_edit/' + roleId, function(data) {
    		$("#id").val(data.id);
    		$("#name").val(data.name);
    		$("#tips").val(data.tips);
    		$("#num").val(data.num);
    		$("#deptId").val(data.deptId);
    		$("#pId").val(data.pId);
    		$("#pRoleName").val(data.pRoleName);
    		$("#deptName").val(data.dept.fullName);
    		$("#btnDel").show();
    		Role.initMunuZtree(data.id);
    	}, function(data) {
    		Feng.error("加载角色信息失败!");
    	});
    	ajax.start();
    }
};

/**
 * 提交
 */
Role.editSubmit = function() {
	this.clearData();
	this.collectData();
	if (!this.validate()) {
        return;
    }
	var url = "/mgrRole/";
	var id = $("#id").val();
	if (id == '') {
		url += "add";
	} else {
		url += "edit";
	}
	var menuIds = Feng.zTreeCheckedNodes("zTree");
    //提交信息
    var ajax = new $ax(url, function (data) {
    	if (id == '') {
            Feng.success("添加成功!");
            Role.resetData();
    	} else {
            Feng.success("修改成功!");
    	}
        Role.init();
    	Role.roleInit();
    }, function (data) {
    	if (id == '') {
    		Feng.error("添加失败!" + data.responseJSON.message + "!");
    	} else {
    		Feng.error("修改失败!" + data.responseJSON.message + "!");
		}
    });
    ajax.set("menuIds", menuIds);
    ajax.set(this.roleInfoData);
    ajax.start();
};

/**
 * 删除角色
 */
Role.delRole = function () {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/mgrRole/delete", function () {
            Feng.success("删除成功!");
            Role.init();
        	Role.roleInit();
            Role.resetData();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("roleId", $("#id").val());
        ajax.start();
    };
    Feng.confirm("是否删除角色'" + $("#name").val() + "'?",operation);
};

$(function () {
	Feng.initValidator("roleInfoForm", Role.validateFields);
	Role.init();
	Role.deptInit();
	Role.roleInit();
	Role.initMunuZtree('-1');
	$("#btnDel").hide();
});