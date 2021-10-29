var selectedCategoryId;
var selectedCategoryParentId;
var currentCategoryMap;
var currentCategoryMapSource;
let isNewNodeFlag = false; //是否是新建node标识

let treeInst;
let treeNeedDeleteNode;

let needAll = true;

$(function(){
	
	var categoryDelModal = $("#matCategoryDeleteModal");
	
	$('[name="status"]').bootstrapSwitch({    //初始化按钮
	       onColor:"success",
	       offColor:"info",
	       onText: "已选",
	       offText: "全部",
	       state: false,
	       onSwitchChange:function(event,state){
	    	   if(state){//已选
	    		   needAll = false;
	    	   }else{ //全部
	    		   needAll = true;
	    	   }
	    	   tbl.bootstrapTable('refresh');
         }
    });
	

	//渲染表格
	var tbl = $("#matList").bootstrapTable({
		 url: Api.url("materialByCategory"),
         search: true,
         searchAlign: "left",
         sidePagination : "client",
 		 pagination : true, // 是否显示分页（*）
 		 clickToSelect: true, //点击行选中
 		 maintainSelected : true, //如果是客户端分页，这个设为 true 翻页后已经选中的复选框不会丢失 
		 columns: [
				 {
					 checkbox:true,
					 align: "center",
					 formatter:function(value, row, index){
						 const materialId = row.id;
						 
						 //如果当前选中的是全部，则打钩
						 if(selectedCategoryId == "-1" || !currentCategoryMap){
							 return {
								 checked: true,
								 disabled : true
							 }
						 } else {
							 let matched = currentCategoryMap.find(map => map.materialId == materialId);
							 
							 if(matched){
								 return {
									 checked: true, //存在映射关系中，则选中
								 }
							 }
						 }
//						 if(currentCategoryMap){
//							 let matched = currentCategoryMap.find(map => map.materialId == materialId);
//							 
//							 if(matched){
//								 return {
//									 checked: true //存在映射关系中，则选中
//								 }
//							 }
//						 }
						 
					}
				 }, {
					 field: "seq",
					 title: "序号",
					 width:"5%",
					 align: "center",
					 formatter: function(value, row, index) {
						 return $.getTableRowNum(tbl, index);
					 }
				 }, {
	            	field: "name",
	                title: "物料编码",
	                align: "center"
	            }, {
	                field: "no",
	                title: "刀片型号",
	                align: "center"
			    }, {
			    	field: "spec",
			    	title: "图纸编号",
	                align: "center"
			    }
        ],
        queryParams : function (params) { //查询的参数
//        	const defaultParams = $.getTableDefaultQueryParams(params);
//        	const formData = $.getFormObject(matSearchForm);
//        	return $.extend({}, defaultParams, formData);
        	return {
        		categoryId: selectedCategoryParentId
        	}
        },
        formatSearch: function formatSearch() {
            return '编码、刀片型号或图纸编号';
        },
        
        onLoadSuccess: function () {
        	//全选按钮
        	if(selectedCategoryId == "-1" || !currentCategoryMap) {
        		 $("#matList input[name='btSelectAll']").prop("disabled",true);
        	} else {
        		 $("#matList input[name='btSelectAll']").prop("disabled",false);
        	}
        },
        responseHandler: function(res){
	    	  if(res.code == 200){
	    		  let sourceResult = res.results;
	    		  let sourceTotal = res.total
	    		  
	    		  //将所有非关联的物料过滤掉
	    		  let newResult = []; 
	    		  
	    		  if(selectedCategoryId == "-1" || !currentCategoryMap) {
	    			  newResult = sourceResult;
	    		  } else if(currentCategoryMap){
	    			  if(currentCategoryMap){
		    			  if(!needAll){
		    				  currentCategoryMap.forEach(map => {
		    					  const maId = map.materialId;

		    					  for(let i = 0; i < sourceResult.length; i ++){
		    						  if(sourceResult[i].id == maId){
		    							  newResult.push(sourceResult[i]);
		    							  break;
		    						  }
		    					  }
		    				  })
			    		  } else {
			    			  newResult = sourceResult;
			    		  }
		    		  }
	    		  }
	    		  
	    		  return {
	    			  "total": newResult.length,
	    			  "rows": newResult
	    		  };
	    	  }
	    	  return res;
	      },

    });

	// 初始化分类树
	$('#categoryTree').jstree({
		"core" : {
			"check_callback" :  true,
			"data":	function (obj, callback) {
  	        	let that = this;
  	        	$.getJSON(Api.url("categoryTree"), {}, function(response){
  	        		if(response.code == 200){
  	        			callback.call(that, response.results[0]);
  	        		}
  	        	});
              }
	      },
	      "plugins" : [
	        "contextmenu"
	      ],
	      "contextmenu":{
	        "items":{
	            "create":{
	                "label":"新建",
	                "icon" : "glyphicon glyphicon-file",
	                "action":function(data){
	                    var inst = $.jstree.reference(data.reference);
						obj = inst.get_node(data.reference);
						const parentLength = obj.parents.length;
						if(parentLength < 3){ //限制只有两级类别
							inst.create_node(obj, {text: "类别"}, "last", function (newNode) {
								try {
									inst.edit(newNode);
								} catch (ex) {
									setTimeout(function () { inst.edit(newNode); },0);
								}
							});
						} else {
							toastr.warning("创建失败！只能创建二级类别！");
						}
	                }
	            },
				"edit":{
					"label":"编辑",
					"icon" : "glyphicon glyphicon-pencil",
					"action":function(data){
						var inst = $.jstree.reference(data.reference),
							obj = inst.get_node(data.reference);
						const id = obj.id;
						if(id == -1){
							toastr.warning("根节点不能修改！");
						} else {
							inst.edit(obj);
						}
					}
				},
	            "remove":{
	                "label":"删除",
	                "icon" : "glyphicon glyphicon-trash",
	                "action":function(data){
	                	treeInst = $.jstree.reference(data.reference);
						var obj = treeInst.get_node(data.reference);
	                	var id = obj.id;
	                	
	                	// 打开删除弹框
	                	categoryDelModal.modal();
	       	       		$("#deleteCategoryId").val(id);
	       	       		
		       	       	if(treeInst.is_selected(obj)) {
		       	       		treeNeedDeleteNode = treeInst.get_selected();
	                    } else {
	                    	treeNeedDeleteNode = obj;
	                    }
	       	       		
	                	
	                }
	            }

	        }
	      }
	   
	}).bind("loaded.jstree", function () {
        $("#categoryTree").jstree("open_all");
    }).bind("create_node.jstree",function(event,data){
    	isNewNodeFlag = true;
    }).bind("rename_node.jstree",function(event, data){ //重命名节点
    	const text = data.text;
    	const pId = data.node.parent;
    	const id = data.node.id;
    	let oldText = data.old; //New node
    	if(oldText == "New node"){
    		oldText = "类别";
    	}
    	if(text.substr(0,1) == " "){
    		toastr.warning("类别名不能以空格开头！");
    		$("#categoryTree").jstree(true).refresh();
		}else{
			//当前名字
			$.getJSON(Api.url("category"), {text: text}, function(response){
				if(response.total > 0){ //名称已存在，返回重新修改
					//如果根据名字返回的正是本身类别，则不处理
					if(response.results[0].id == id){
						return;
					}

					var inst = data.instance;
					var currentNode = data.node;

					//把刚编辑、新增的节点删除
					var ref = $('#categoryTree').jstree(true);
					ref.delete_node(currentNode)
//				psel = ref.get_parent(data.node);
//				console.log(psel)
					inst.create_node(currentNode.parent, {id:id, text: oldText}, "last", function (newNode) {
						try {
							inst.edit(newNode);
						} catch (ex) {
							setTimeout(function () { inst.edit(newNode); },0);
						}
					});
					toastr.warning("已有同名类别，请重新命名！");
					$("#categoryTree").jstree(true).refresh();
				} else { //名称校验成功，新建或者更新
					if(isNewNodeFlag){
						$.postJSON(Api.url("category"), {pId: pId, text: text}, matCategoryCallback)
						isNewNodeFlag = false;
					} else {
						$.putJSON(Api.url("category"), {id: id, pId: pId, text: text}, matCategoryCallback)
					}
					//更新完成后 刷新树（尤其是新建后，防止页面停留在旧的ID上）
					$("#categoryTree").jstree(true).refresh();
				}
			})
		}

    }).bind("select_node.jstree", function(event, data){ //选中节点
    	selectedCategoryParentId = data.node.parent;
    	selectedCategoryId = data.node.id;
    	$.getJSON(Api.url("categoryMap"), {categoryId: selectedCategoryId}, function(response){
    		if(response.code == 200){
    			currentCategoryMap = response.results;
//    			currentCategoryMapSource = response.results;
    			//重新加载表格（为了刷新复选框）
    			tbl.bootstrapTable('refresh');
    		}
    	})
    });
	
	// 更新、新增类别后的回调函数
    var matCategoryCallback = function(response){
    	const msg = response.message;
		if(response.code == 200){
			toastr.success(msg);
		}else{
			toastr.warning(msg);
		}
	};
	
	// 点击删除按钮
	$("#categoryDeleteBtn").on("click", function(){
		const id = $("#deleteCategoryId").val();
		$.deleteJSON(Api.url("category", id), {}, function(response){
			const msg = response.message;
			if(response.code == 200){
				//后台成功才能删除节点
				treeInst.delete_node(treeNeedDeleteNode);
				
				toastr.success(msg)
			} else {
				toastr.warning(msg);
			}
			// 关闭弹窗
			categoryDelModal.modal("hide");
		})
	})
	
	// 点击保存按钮
	$("#updateCategoryMapBtn").on("click", function(){
		if(!selectedCategoryId || selectedCategoryId == "-1"){
			toastr.warning("无任何修改");
			return;
		}
		
		//获取所有选中物料
		var selectedItems = $("#matList").bootstrapTable("getAllSelections");
		let materialIdArr = [];
		if(selectedItems){
			selectedItems.forEach(item => {
				materialIdArr.push(item.id);
			})
		}
		
		//更新关系
		$.postJSON(Api.url("categoryMap"), {categoryId: selectedCategoryId, materialIdList:materialIdArr}, function(response){
			const msg = response.message;
			if(response.code == 200){
				toastr.success(msg)
//				tbl.bootstrapTable('refresh');
				
		    	$.getJSON(Api.url("categoryMap"), {categoryId: selectedCategoryId}, function(response){
		    		if(response.code == 200){
		    			currentCategoryMap = response.results;
		//    			currentCategoryMapSource = response.results;
		    			//重新加载表格（为了刷新复选框）
		    			tbl.bootstrapTable('refresh');
		    		}
		    	})
				
			} else {
				toastr.warning(msg);
			}
		})
	})
	
})
