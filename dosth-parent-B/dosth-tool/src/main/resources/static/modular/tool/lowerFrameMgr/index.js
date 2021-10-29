/**
 * 下架管理
 */
var MgrLowerFrame = {
	layerIndex : -1,
	oTable : null,
	seItem : null,
	tb1 : null,
	tb2 : null
};

/**
 * 刷新
 */
MgrLowerFrame.search = function queryParams(params) {
	var cabinetId = $("#cabinetId").val();
	var params = {
		cabinetId : cabinetId
	};
	MgrLowerFrame.oTable.queryParams = params;
	MgrLowerFrame.oTable.refresh();
};

/**
 * 下架/批量下架
 */
MgrLowerFrame.changeLowerFrame = function() {
	var cabinetId = $("#cabinetId").val();
	var data = $('#managerTable').bootstrapTable('getSelections');
    if (data.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return;
    }
    for(var i=0; i<data.length; i++){
    	if(data[i].matInfoName == null || data[i].matInfoName == ""){
    		Feng.info(data[i].rowNo + "行" + data[i].colNo + "列，没有物料！");
    		return;
    	}
    }
    var operation = function(){
    	$.ajax({
		    url: Feng.ctxPath + '/lowerFrameMgr/unFrame',
		    type: 'post',
		    data: {
		    	"data" : JSON.stringify(data),
		    	"cabinetId" : cabinetId
		    },
		    success: function(data){
		    	if(data.code == 201){
		    		Feng.success(data.message);
		    		tbl.bootstrapTable('refresh');
		    	} else if(data.code == 202){
		    		Feng.success(data.message);
		    		tb2.bootstrapTable('refresh');
		    	} else {
		    		Feng.info(data.message);
		    	}
		    },
		    error: function(error){
		    	Feng.error(error);
		    }
		});
	}
	Feng.confirm("是否下架 ?",operation);
};

/**
 * 批量上架页面跳转
 */
MgrLowerFrame.openUpFrame = function() {
    var cabinetId = $("#cabinetId").val();
	var index = layer.open({
		type : 2,
		title : '批量上架',
		shadeClose : true,
		area : [ '60%', '85%' ], // 宽高
		fix : false, // 不固定
		maxmin : true,
		content : '/lowerFrameMgr/upFrameViewAll/' + cabinetId
	});
	this.layerIndex = index;
};

/**
 * 点击设备树
 */
MgrLowerFrame.onClickEquInfo = function(e, treeId, treeNode) {
	$("#cabinetId").val(treeNode.id);
	var cabinetId = treeNode.id;
	var url = Feng.ctxPath + "/lowerFrameMgr/initlist/" + cabinetId;
	var ajax = new $ax(url, function(data) {
			$("#details").html(data);
		}, function(data) {
			Feng.error("设置失败," + data + "!");
		}, "html");
	ajax.start();
};

/**
 * @description 刷新主柜大类或主柜
 */
MgrLowerFrame.freshMain = function() {
	$("#upFrameAll").show();
	var that = this;
	tbl = $("#managerTable").bootstrapTable({
		url : "/lowerFrameMgr/mainCabinet" ,
		cache: false,
		height: 600,
		showToggle:true,
		showColumns: true,
	    showRefresh: true,
	    clickToSelect: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 1000, 
	  	paginationDetailHAlign : ' hidden',
	  	sidePagination: "server",
		columns : [
			{
				checkbox : true,
				visible : true
			},
			{
				field : "cabinetName",
				title : "刀具柜名称",
				align : "center"
			},
			{
				field : "rowNo",
				title : "行号",
				align : "center"
			},
			{
				field : "colNo",
				title : "列号",
				align : "center"
			},
			{
				field : "matInfoName",
				title : "物料名称",
				align : "center"
			},
			{
				field : "barCode",
				title : "物料编号",
				align : "center"
			},
			{
				field : "spec",
				title : "物料型号",
				align : "center"
			},
			{
				field : "maxReserve",
				title : "最大存储",
				align : "center"
			},
			{
				field : "warnVal",
				title : "告警阀值",
				align : "center"
			},
			{
				field : "curNum",
				title : "当前数量",
				align : "center"
			},
			{
				field : "lastFeedTime",
				title : "最后上架时间",
				align : "center"
			},
			{
				field : "status",
				title : "状态",
				align : "center"
			}, {
				field : "id",
				title : "操作",
				align : "center",
				width : "10%",
				formatter : function(value, row, index){
	            	return [
	            		`<button class='btn btn-xs btn-warning openUpFrameBtn' type='button' data-id=${value}><i class='fa fa-pencil'>上架</i> </button>`,
	            		].join("");
	            }
			} 
		],
		responseHandler : function(res) {
			return {
				  "total": res.totalElements,
				  "rows": res.content
			  };
		},
		queryParams : function(params) {
		    var temp = {
		        limit: params.limit,
		        offset : params.offset,
		        pageSize : this.pageSize,
		        pageNumber : this.pageNumber,
		        cabinetId : $("#cabinetId").val(),
		    };
		    return temp;
		},
	    onLoadSuccess : function (res) {
	    	$(".openUpFrameBtn").on("click", function(e){
	    	    var cabinetId = $("#cabinetId").val();
	    	    var equDetailStaId = $(this).data("id");
	    		var index = layer.open({
	    			type : 2,
	    			title : '上架',
	    			shadeClose : true,
	    			area : [ '60%', '80%' ], // 宽高
	    			fix : false, // 不固定
	    			maxmin : true,
	    			content : '/lowerFrameMgr/upFrameView/' + equDetailStaId + "/" + cabinetId
	    		});
	    		that.layerIndex = index;
	    	})
	    }, 
	});
};

/**
 * @description 刷新暂存柜大类或暂存柜
 */
MgrLowerFrame.freshTemp = function() {
	$("#upFrameAll").hide();
	tb2 = $("#managerTable").bootstrapTable({
		url : "/lowerFrameMgr/tempCabinet",
		cache: false,
		height: 600,
		showToggle:true,
		showColumns: true,
	    showRefresh: true,
	    clickToSelect: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 1000, 
	  	paginationDetailHAlign : ' hidden',
	  	sidePagination: "server",
		columns : [
			{
				checkbox : true,
				visible : true
			},
			{
				field : "cabinetName",
				title : "暂存柜名称",
				align : "center"
			},
			{
				field : "boxIndex",
				title : "索引号",
				align : "center"
			},
			{
				field : "rowNo",
				title : "行号",
				align : "center"
			},
			{
				field : "colNo",
				title : "列号",
				align : "center"
			},
//			{
//				field : "cabinetSta",
//				title : "状态",
//				align : "center"
//			},
//			{
//				field : "status",
//				title : "状态",
//				align : "center"
//			},
			{
				field : "userName",
				title : "当前用户",
				align : "center"
			}, {
				field : "matInfoName",
				title : "物料名称",
				align : "center"
			}, {
				field : "barCode",
				title : "物料编号",
				align : "center"
			}, {
				field : "spec",
				title : "物料型号",
				align : "center"
			}, {
				field : "num",
				title : "剩余数量",
				align : "center"
			} 
		],
		responseHandler : function(res) {
			return {
				  "total": res.totalElements,
				  "rows": res.content
			  };
		},
		queryParams : function(params) {
		    var temp = {
			    limit: params.limit,
		        offset : params.offset,
		        pageSize : this.pageSize,
		        pageNumber : this.pageNumber,
		        cabinetId : $("#cabinetId").val(),
		    };
		    return temp;
		},
	});
};

/**
 * 显示设备树
 */
MgrLowerFrame.showEquInfoSelectTree = function() {
	Feng.showInputTree("equInfoName", "equInfoContent");
};

/**
 * 点击树形节点前判断
 */
MgrLowerFrame.onBeforeClick = function(treeId, treeNode, clickFlag) {
	if (treeNode.id.startWith("C_")) {
		return false;
	}
	return true;
};

$(function() {
	var equInfoTree = new $ZTree("equInfoTree", "/tree/createCabinetTree");
	equInfoTree.bindBeforeClick(MgrLowerFrame.onBeforeClick);
	equInfoTree.bindOnClick(MgrLowerFrame.onClickEquInfo);
	equInfoTree.init();
});