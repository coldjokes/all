/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var UserInfoDlg = {
	userInfoData: {},
	validateFields: {
		name: {
			validators: {
				notEmpty: {
					message: '*必填'
				}
			}
		},
		loginName: {
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
		},
		email: {
			validators: {
				regexp: {
					regexp: /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/,
					message: '邮箱地址输入不正确'
				}
			}
		},
		extraBoxNum: {
			validators: {
				digits: {
					message: '只能输入数字'
				}
			}
		},
		password: {
			validators: {
				regexp: {
					regexp: /^[a-zA-Z0-9]{6,18}$/,
					message: '密码长度至少6位并只能由数字、字母组成'
				}
			}
		},
		rePassword: {
			validators: {
				regexp: {
					regexp: /^[a-zA-Z0-9]{6,18}$/,
					message: '密码长度至少6位并只能由数字、字母组成'
				},
				identical: {
					field: 'password',
					message: '两次密码不一致！'
				}
			}
		}
	}
};

/**
 * 清除数据
 */
UserInfoDlg.clearData = function() {
	this.userInfoData = {};
};

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
UserInfoDlg.set = function(key, val) {
	this.userInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
	return this;
};

/**
 * 管理员角色状态切换
 */
UserInfoDlg.adminRoleChange = function(obj) {
	if ($(obj).prop("checked")) {
		$("#isAdminRole").val("true");
	} else {
		$("#isAdminRole").val("false");
	}
	//	$(this).prop("checked", true);
};

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
UserInfoDlg.get = function(key) {
	return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
UserInfoDlg.close = function() {
	parent.layer.close(window.parent.MgrUser.layerIndex);
};

/**
 * 点击部门input框时
 * 
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
UserInfoDlg.onClickDept = function(e, treeId, treeNode) {
	$("#citySel").attr("value", instance.getSelectedVal());
	$("#deptId").attr("value", treeNode.id.split('_')[1]);
};

/**
 * 显示部门选择的树
 * 
 * @returns
 */
UserInfoDlg.showDeptSelectTree = function() {
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
UserInfoDlg.hideDeptSelectTree = function() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
}

/**
 * 收集数据
 */
UserInfoDlg.collectData = function() {
	this.set('id').set('accountId').set('avatar').set('name').set('deptId').set('icCard')
		.set('loginName').set('password').set('startTime').set('endTime').set('limitSumNum')
		.set('notReturnLimitNum').set('email').set('extraBoxNum').set('isAdminRole');
}

/**
 * 验证数据是否为空
 */
UserInfoDlg.validate = function() {
	$('#userInfoForm').data("bootstrapValidator").resetForm();
	$('#userInfoForm').bootstrapValidator('validate');
	return $("#userInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 验证两个密码是否一致
 */
/*UserInfoDlg.validatePwd = function () {
	var password = this.get("password");
	var rePassword = this.get("rePassword");
	if (password == rePassword) {
		return true;
	} else {
		return false;
	}
};*/

/**
 * 提交添加用户
 */
UserInfoDlg.addSubmit = function() {
	this.clearData();
	this.collectData();

	if (!this.validate()) {
		return;
	}

	var ajax = new $ax(Feng.ctxPath + "/mgrUser/add", function(data) {
		if (data.code == 200) {
			window.parent.Feng.success(data.message);
			setTimeout(function() {
				UserInfoDlg.close();
			}, 800);
			window.parent.tbl.bootstrapTable('refresh');
		} else {
			window.parent.Feng.info(data.message);
		}
	}, function(data) {
		window.parent.Feng.error(data.message);
	});
	ajax.set(this.userInfoData);
	ajax.start();
};

/**
 * 提交修改
 */
UserInfoDlg.editSubmit = function() {
	this.clearData();
	this.collectData();

	if (!this.validate()) {
		return;
	}

	/*if (!this.validatePwd()) {
		Feng.error("两次密码输入不一致");
		return;
	}*/

	var ajax = new $ax(Feng.ctxPath + "/mgrUser/edit", function(data) {
		if (data.code == 200) {
			window.parent.Feng.success(data.message);
			setTimeout(function() {
				UserInfoDlg.close();
			}, 800);
			window.parent.tbl.bootstrapTable('refresh');
		} else {
			window.parent.Feng.info(data.message);
		}
	}, function(data) {
		window.parent.Feng.error(data.message);
	});
	ajax.set(this.userInfoData);
	ajax.start();
};

function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
		event.target).parents("#menuContent").length > 0)) {
		UserInfoDlg.hideDeptSelectTree();
	}
}

$(function() {
	Feng.initValidator("userInfoForm", UserInfoDlg.validateFields);

	var ztree = new $ZTree("treeDemo", "/mgrDept/tree");
	ztree.bindOnClick(UserInfoDlg.onClickDept);
	ztree.init();
	instance = ztree;

	// 初始化头像上传
	var avatarUp = new $WebUpload("avatar");
	avatarUp.setUploadBarId("progressBar");
	avatarUp.init();
});