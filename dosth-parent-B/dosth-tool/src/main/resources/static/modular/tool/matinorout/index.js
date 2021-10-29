/**
 * 上下架
 */
var MatInOrOut = {
	equInfoInstance: null
};

/**
*重置 
*/
MatInOrOut.resetSearch = function() {
	$("#matNameBarCodeSpec").val("");
	MatInOrOut.matSearch();
};
MatInOrOut.resetStaSearch = function() {
	$("#staMatNameBarCodeSpec").val("");
	MatInOrOut.staSearch();
	
};


/**
 * 物料全选
 */
MatInOrOut.matCheckAll = function() {
	if ($('#matCheckAll').prop('checked')) {
		$("#matInnerList").find("input[type='checkbox']").each(function() {
			$(this).prop("checked", true);
		});
	} else {
		$("#matInnerList").find("input[type='checkbox']").each(function() {
			$(this).removeAttr("checked");
		});
	}
};

/**
 * 货位全选
 */
MatInOrOut.staCheckAll = function() {
	if ($('#staCheckAll').prop('checked')) {
		$("#staInnerList").find("input[type='checkbox']").each(function() {
			if (!$(this).attr("disabled")) {
				$(this).prop("checked", true);
			}
		});
	} else {
		$("#staInnerList").find("input[type='checkbox']").each(function() {
			$(this).removeAttr("checked");
		});
	}
};

/**
 * 初始化物料列表
 */
MatInOrOut.initMatList = function(matNameBarCodeSpec) {
	var url = Feng.ctxPath + "/matInOrOut/initMatList";
	var ajax = new $ax(url, function(data) {
		$("#matlist").html(data);
	}, function(data) {
		Feng.error("设置失败," + data + "!");
	}, "html");
	if (!matNameBarCodeSpec) {
		matNameBarCodeSpec = "";
	}
	ajax.setData({ "matNameBarCodeSpec": matNameBarCodeSpec });
	ajax.start();
};

/**
 * 物料过滤
 */
MatInOrOut.matSearch = function() {
	MatInOrOut.initMatList($("#matNameBarCodeSpec").val());
};

/**
 * 绑定物料选中释放状态
 */
MatInOrOut.bindMatCheck = function(obj) {
	// 当前选中
	if ($(obj).prop('checked')) {
		if ($("#matInnerList").find("input[type='checkbox']:not(:checked)").length > 0) {
			$('#matCheckAll').removeAttr("checked");
		} else {
			$('#matCheckAll').prop("checked", true);
		}
	} else { // 当前释放
		$('#matCheckAll').removeAttr("checked");
	}
};

/**
 * 绑定货位选中释放状态
 */
MatInOrOut.bindStaCheck = function(obj) {
	// 当前选中
	if ($(obj).prop('checked')) {
		if ($("#staInnerList").find("input[type='checkbox']:not(:disabled):not(:checked)").length > 0) {
			$('#staCheckAll').removeAttr("checked");
		} else {
			$('#staCheckAll').prop("checked", true);
		}
	} else { // 当前释放
		$('#staCheckAll').removeAttr("checked");
	}
};

/**
 * 物料上架
 */
MatInOrOut.matIn = function() {
	if ($("#matInnerList").find("input[type='checkbox']:checked").length == 0) {
		Feng.info("请首先选择上架物料");
		return;
	}
	var numFlg = false;
	var upFlg = false;
	$("#staInnerList").find("input[type='checkbox']:checked").each(function() {
		var curNum = $(this).attr("curNum");
		if (typeof curNum != "undefined" && curNum != 0) {
			numFlg = true;
		}
		if ($(this).attr("matid")) {
			upFlg = true;
		}
	});
	if (numFlg) {
		Feng.info("上架库位库存需为 0");
		return;
	}
	if (upFlg) {
		Feng.info("请确认待上架货位均为空");
		return;
	}
	var matSize = 0;
	var staSize = 0;
	var staIds = "";
	$("#staInnerList").find("input[type='checkbox']:checked").each(function() {
		if (!$(this).attr("matid")) {
			staIds += $(this).attr("id") + ";";
			staSize++;
		}
	});
	if (staIds.length == 0) {
		Feng.info("没有可上架的货位");
		return;
	}
	var matIds = "";
	$("#matInnerList").find("input[type='checkbox']:checked").each(function() {
		matIds += $(this).attr("id") + ";";
		matSize++;
	});
	if (staSize != matSize) {
		Feng.confirm("待上架的物料和货位数量不一致,确认重复上架吗?", function() {
			var url = Feng.ctxPath + "/matInOrOut/matIn";
			var ajax = new $ax(url, function(data) {
				if(data.code == 200){
					var arr = data.message.split(";");
					for (var i = 0; i < arr.length; i++) {
						var smId = arr[i].split(",");
						$("#staInnerList").find("input[id='" + smId[0] + "']").attr("matid", smId[1]);
						$("#icon_" + smId[0]).html($("#icon_" + smId[1]).html());
						$("#matName_" + smId[0]).text($("#matName_" + smId[1]).text());
						$("#barCode_" + smId[0]).text($("#barCode_" + smId[1]).text());
						$("#spec_" + smId[0]).text($("#spec_" + smId[1]).text());
					}
					$("#matlist").find("input[type='checkbox']").each(function() {
						$(this).removeAttr("checked");
					});
					$("#stalist").find("input[type='checkbox']").each(function() {
						$(this).removeAttr("checked");
					});
					Feng.success("上架成功");
				} else {
					Feng.error(data.message);
				}
			}, function(data) {
				Feng.error("设置失败," + data + "!");
			});
			ajax.setData({ "matIds": matIds, "staIds": staIds });
			ajax.start();
		});
		return;
	} else {
		var url = Feng.ctxPath + "/matInOrOut/matIn";
		var ajax = new $ax(url, function(data) {
			if(data.code == 200){
				var arr = data.message.split(";");
				for (var i = 0; i < arr.length; i++) {
					var smId = arr[i].split(",");
					$("#staInnerList").find("input[id='" + smId[0] + "']").attr("matid", smId[1]);
					$("#icon_" + smId[0]).html($("#icon_" + smId[1]).html());
					$("#matName_" + smId[0]).text($("#matName_" + smId[1]).text());
					$("#barCode_" + smId[0]).text($("#barCode_" + smId[1]).text());
					$("#spec_" + smId[0]).text($("#spec_" + smId[1]).text());
				}
				$("#matlist").find("input[type='checkbox']").each(function() {
					$(this).removeAttr("checked");
				});
				$("#stalist").find("input[type='checkbox']").each(function() {
					$(this).removeAttr("checked");
				});
				Feng.success("上架成功");
			} else {
				Feng.error(data.message);
			}
		}, function(data) {
			Feng.error("设置失败," + data + "!");
		});
		ajax.setData({ "matIds": matIds, "staIds": staIds });
		ajax.start();
	}
};

/**
 * 下架确认
 */
MatInOrOut.matCheck = function() {
	// 已上架货位物料ID
	var matIds = "";
	$("#staInnerList").find("input[type='checkbox']:checked").each(function() {
		if ($(this).attr("matid")) {
			matIds += $(this).attr("matid") + ";";
		}
	});
	// 货位物料不为空
	if (matIds.length == 0) {
		Feng.info("没有可下架的货位");
		return;
	}
	// 下架确认
	var ajax = new $ax(Feng.ctxPath + "/matInOrOut/matCheck", function(data) {
		if (data.code == 200) {
			Feng.confirm("确认下架?", function() {
				MatInOrOut.matOut();
			});
		} else {
			Feng.confirm(data.message + "确认下架?", function() {
				MatInOrOut.matOut();
			});
		}
	}, function(data) {
		Feng.error("设置失败," + data + "!");
	});
	ajax.setData({ "matIds": matIds });
	ajax.start();
}

/**
 * 物料下架
 */
MatInOrOut.matOut = function() {
	var staIds = "";
	$("#staInnerList").find("input[type='checkbox']:checked").each(function() {
		if ($(this).attr("matid")) {
			staIds += $(this).attr("id") + ";";
		}
	});
	if (staIds.length == 0) {
		Feng.info("没有可下架的货位");
		return;
	}
	var cabinetId = $("#selCab").val();
	var url = Feng.ctxPath + "/matInOrOut/matOut";
	var ajax = new $ax(url, function(data) {
		Feng.success(data.message);
		if (data.code == 200) {
			var arr = staIds.split(";");
			for (var i = 0; i < arr.length; i++) {
				$("#staInnerList").find("input[id='" + arr[i] + "']").removeAttr("matid curNum");
				$("#staInnerList tr[id='" + arr[i] + "']").find("img").remove();
				$("#matName_" + arr[i]).text("-");
				$("#barCode_" + arr[i]).text("-");
				$("#spec_" + arr[i]).text("-");
				$("#num_" + arr[i]).text("0");
			}
			$("#stalist").find("input[type='checkbox']").each(function() {
				$(this).removeAttr("checked");
			});
		}
	}, function(data) {
		Feng.error("设置失败," + data + "!");
	});
	ajax.setData({ "staIds": staIds, "cabinetId": cabinetId });
	ajax.start();
};

MatInOrOut.onClickEquInfo = function(e, treeId, treeNode) {
	var cabinetId = treeNode.id;
	$("#cabinetId").val(cabinetId);
	$("#selCab").val(cabinetId);
	if (cabinetId.startWith("D_")) {
		$("#selCab").val(treeNode.pId);
	}
	MatInOrOut.staSearch();
};

/**
 * @description 货道查询
 */
MatInOrOut.staSearch = function() {
	var url = Feng.ctxPath + "/matInOrOut/initStaList";
	var ajax = new $ax(url, function(data) {
		$("#stalist").html(data);
	}, function(data) {
		Feng.error("设置失败," + data + "!");
	}, "html");
	ajax.setData({ "cabinetId": $("#cabinetId").val(), "staMatNameBarCodeSpec": $("#staMatNameBarCodeSpec").val() });
	ajax.start();
};

$(function() {
	MatInOrOut.initMatList();
	var equInfoTree = new $ZTree("equInfoTree", "/equsetting/treeCabDetail");
	equInfoTree.bindBeforeClick(function(treeId, treeNode, clickFlag) {
		if (treeNode.id.startWith("C_")) {
			return false;
		}
		return true;
	});
	equInfoTree.bindOnClick(MatInOrOut.onClickEquInfo);
	equInfoTree.init();
	MatInOrOut.equInfoInstance = equInfoTree;
	var treeObj = $.fn.zTree.getZTreeObj("equInfoTree");
	var nodes = treeObj.getNodes();
	var first = nodes[0];
	if (nodes[0].id.startWith("C_")) {
		treeObj.expandNode(nodes[0], true);
		first = nodes[0].children[0];
	}
	treeObj.selectNode(first);

	$("#cabinetId").val(first.id);
	$("#selCab").val(first.id);
	if (first.id.startWith("D_")) {
		$("#selCab").val(first.pId);
	}
	MatInOrOut.staSearch();
});