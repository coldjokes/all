//zTree的配置
var setting = {
    view: {
        addHoverDom: addHoverDom,
        removeHoverDom: removeHoverDom,
        selectedMulti: false
    },
    edit: {
        enable: true,
        editNameSelectAll: true, 
		removeTitle:'删除',
        renameTitle:'重命名'
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeRemove:beforeRemove,//点击删除时触发，用来提示用户是否确定删除（可以根据返回值 true|false 确定是否可以删除）
		beforeEditName: beforeEditName,//点击编辑时触发，用来判断该节点是否能编辑
		beforeRename:beforeRename,//编辑结束时触发，用来验证输入的数据是否符合要求(也是根据返回值 true|false 确定是否可以编辑完成)
		onRemove:onRemove,//(beforeRemove返回true之后可以进行onRemove)删除节点后触发，用户后台操作
		onRename:onRename,//编辑后触发，用于操作后台
		beforeDrag:beforeDrag,//用户禁止拖动节点
		onClick:zTreeOnClick//点击节点触发的事件
    }
};

var log, className = "dark";
function zTreeOnClick(event, treeId, treeNode) {
	$("#typeTreeId").val(treeNode.id);
	$("#onoffswitch").attr("checked",false);
	var parentNode = treeNode.getParentNode();
	// 默认当前节点为根节点
	$("#parentTreeId").val(treeNode.id);
	var parentNodeId = treeNode.id;
	if(parentNode != null){
		$("#parentTreeId").val(treeNode.getParentNode().id);
		parentNodeId = treeNode.getParentNode().id;
	}
	var treeObj = $.fn.zTree.getZTreeObj("categoryTree"),
	    nodes = treeObj.getCheckedNodes(true),
	    v = "";
	for (var i = 0; i < nodes.length; i++) {
        v += nodes[i].name + ",";
    }
	
	var ids = new Array();
	ids[0] = treeNode.id;
	ids[1] = parentNodeId;
	// 获取绑定物料信息
	$.ajax({
        url: Feng.ctxPath + "/matCategory/getEquInfos/" + ids,
        type: "post",
        success: function (data) {
        	$("#infos").html(data);
        },
        error: function (data) {
        	Feng.error("加载失败!");
        }
    });
}
function beforeDrag(treeId, treeNode) { 
	return false; //false=禁用拖拽功能， true=开启
}
function beforeEditName(treeId, treeNode) {
	if(treeNode.level == 0){
		layer.msg("主分支名不能修改！");
		return false;
	}
    className = (className === "dark" ? "":"dark");
    var zTree = $.fn.zTree.getZTreeObj("categoryTree");
    zTree.selectNode(treeNode);
    zTree.editName(treeNode);
}
function beforeRemove(treeId, treeNode) {
	if(treeNode.level == 0){
		layer.msg("主分支不能删除！");
		return false;
	}
	if(treeNode.isParent){
		layer.msg("请先删除该分支下的子分支!");
		return false;
	}
    className = (className === "dark" ? "":"dark");
    var zTree = $.fn.zTree.getZTreeObj("categoryTree");
    zTree.selectNode(treeNode);
    return confirm("确认删除分支： " + treeNode.name + " 吗？");
}
function onRemove(e, treeId, treeNode) { //确定删除节点
     $.ajax({
        url: "/matCategory/nodeDel/" + treeNode.id,
        type: "post",
        dataType: "json",
        success: function (data) {
        	if (data == "success") {
        		
            } else {
            	layer.msg("分支删除失败!");
            }
        }
    });
}
function beforeRename(treeId, treeNode, newName, isCancel) {
    className = (className === "dark" ? "":"dark");
    var zTree = $.fn.zTree.getZTreeObj("categoryTree");
    if (newName.trim().length == 0) {
        setTimeout(function() {
            zTree.cancelEditName();
            layer.msg("分支名称不能为空");
        }, 0);
        return false;
    } else {
    	var flag;
    	$.ajax({
	        url: "/matCategory/checkName",
	        type: "get",
	        data : {"categoryTreePId":treeNode.pId, "categoryTreeId": treeNode.id, "newName":newName},
	        dataType: "json",
            async: false,
	        success: function (data) {
				if (data.code != 200) {
		        	setTimeout(function() {
			            zTree.cancelEditName();
			            layer.msg(data.message);
			        }, 0);
					flag = false;
		    	} else {
    				flag = true;
		    	}
	        }
	    });
	    return flag;
    }
}
function onRename(e, treeId, treeNode, isCancel) {
    $.ajax({
        url: "/matCategory/editName/" + treeNode.id +"/"+ treeNode.name,
        type: "post",
        dataType: "json",
        success: function (data) {
        	if (data == "success") {

            } else {
            	layer.msg("分支名修改失败!");
            }
        }
    });
}

function showAddBtn(treeId, treeNode) {
	return true;
}
function showRemoveBtn(treeId, treeNode) {
    return true;
}
function showRenameBtn(treeId, treeNode) {
    return true;
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
    if (treeNode.level < 3) {
	    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
	        + "' title='添加子分支' onfocus='this.blur();'></span>";
	    sObj.after(addStr);
    }
    $('.edit').attr('title', '编辑');
    $('.remove').attr('title', '删除');
    
    var btn = $("#addBtn_"+treeNode.tId);
    if (btn) btn.bind("click", function(){
        var zTree = $.fn.zTree.getZTreeObj("categoryTree");
        var pId = treeNode.id;
        var level = treeNode.level + 1;
        var name = "新增分支点 " + (newCount++);
        var nodes = treeNode.getPath();
        var path = "";
        for(var i = 0; i < nodes.length; i++){
        	path += nodes[i].id + ",";
        }
		var icon = "/static/img/three.png";
		if (level == 1) {
			icon = "/static/img/three.png";
		}
        $.ajax({
	        url: "/matCategory/nodeAdd/" + pId + "/" + level + "/"+ name + "/"+path,
	        type: "post",
	        dataType: "json",
	        success: function (data) {
        		zTree.addNodes(treeNode, {id:data.id, pId:data.pId, name:data.name,icon:icon});//实现加载树的方法
	        },
	        error: function () {
	        	layer.msg("添加失败!");
	        }
	    })
    });
};
function removeHoverDom(treeId, treeNode) {
    $("#addBtn_"+treeNode.tId).unbind().remove();
};

$(document).ready(function(){
    $.ajax({
        url: "/matCategory/categoryTree",
        type: "post",
        dataType: "json",
        success: function (data) {
        	 $.fn.zTree.init($("#categoryTree"), setting, data);//实现加载树的方法
        },
        error: function (data) {
        	layer.msg("树形加载失败！");
        }
    })
});