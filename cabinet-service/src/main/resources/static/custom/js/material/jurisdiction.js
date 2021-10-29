var selectedCategoryParentId;
var selectedCategoryId;
var currentMaterialMap;
/**
 * 初始化人员部门树
 */
$(function(){
	$('#accountTree').jstree({
		"core":{
			"check_callback":true,
			"data":function (obj,callback) {
				let that = this;
				$.getJSON(Api.url("accountTree"),{},function (response) {
					if(response.code === 200){
						callback.call(that,response.results[0]);
					}
				});
			}
		},
		"plugins":[
			"types"
		],
		"types":{
			"user":{
				"icon":'fa fa-file-o'
			}
		}
	}).bind("loaded.jstree",function () {
		$("#accountTree").jstree("open_all");
	}).bind("select_node.jstree",function (event,data) {
		selectedType = data.node.type;
		if(data.node.type !== "user"){
			selectedType = "dept";
		}else{
			selectedType = "user";
		}
		selectedUserId = data.node.id;
		let selectIdList = data.node.parents;
		selectIdList.pop();
		selectIdList.push(selectedUserId);
		var selectes = {
			idList: selectIdList,
			text: selectedType
		};
		$.postJSON(Api.url("getDepts"),selectes, function (res) {
			if(res.code === 200){
				selectCategory(res.results)
			}
		})
		//

	});
	function selectCategory(data){
		// var nodes = ["0489579d0341466d8af7315c83a08b1e","50804074df3c4eb8aef25fa697a33fe8"];
		// 取消选中节点
		$("#categoryTree").jstree('uncheck_all',true);
		// 选中节点
		$("#categoryTree").jstree('check_node', data);
		// $.jstree.reference("#categoryTree").select_node(nodes);
		// $("#categoryTree").jstree(true).select_node(nodes);
		// $('#categoryTree').jstree("destroy");
		// $('#categoryTree').jstree(true).select_node(nodes);
	}

	/**
	 * 取料权限树
	 */
	$('#categoryTree').jstree({
		"core":{
			"check_callback":true,
			"data":function (obj,callback) {
				var that = this;
				$.getJSON(Api.url("receiveTree"),{},function (response) {
					if(response.code === 200){
						callback.call(that,response.results[0]);
					}
				});
			}
		},
		"checkbox":{
			"keep_selected_style":false,  	// 默认选中
			"three_state":true,			  	// 父子级别级联选择
			"tie_selection":false,
			"whole_node":true				// 点击节点名字不触发checkbox
		},
		"plugins":[
			"checkbox","types"
		],
		"types":{
			"user":{
				"icon":'fa fa-file-o'
			}
		}
	}).bind("loaded.jstree",function () {
		$("#categoryTree").jstree("open_all");
	}).bind("select_node.jstree",function (event,data) {
		selectedCategoryId = data.node.id;
		selectedCategoryParentId = data.node.parent;
		$.getJSON(Api.url("accountTree"),{ id: selectedCategoryId },function (response) {
			if (response.code === 200){
				currentMaterialMap = response.results;
			}
		})
	})
});

/**
 * 设置领取权限
 */
function icBind() {
	var treeNode = $("#accountTree").jstree(true).get_selected();
	// var nodeId = treeNode.original.id;
	var type = $("#accountTree").jstree(true).get_selected(true)[0].type;
	if (type === "user"){
		type = '0';
	} else {
		type = '1';
	}
	if (treeNode.length < 1) {
		Feng.info("请先选择绑定部门或帐号!");
		return;
	}

	var selected = $('#categoryTree').jstree(true).get_checked();
	if (selected.length < 1) {
		Feng.info("请先选择取料权限!");
		return;
	}
	var data = {
		text: treeNode[0],
		username: type,
		idList: selected
	};
	$.postJSON(Api.url("getMaterialByUser"),data,function (res) {
		if (res.code === 200){
			Feng.success(res.message)
		}

	})

}
