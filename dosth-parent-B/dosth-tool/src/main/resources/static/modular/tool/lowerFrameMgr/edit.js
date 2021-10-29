/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var infoDlg = {
	tbl: null,
	tb2: null
};

/**
 * 关闭此对话框
 */
infoDlg.close = function () {
    parent.layer.close(window.parent.MgrLowerFrame.layerIndex);
};

/**
 * 物料信息
 */
infoDlg.search = function() {
	var that = this;
	tbl = $("#matInfoList").bootstrapTable({
		url : "/lowerFrameMgr/getMatInfoList",
		cache: false,
		search: true,
		searchAlign: "left",
		showToggle:true,
		showColumns: true,
	    showRefresh: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 10, 
	  	sidePagination: "server",
	  	paginationDetailHAlign : ' hidden',
		columns : [
			{
				field : "matEquName",
				title : "物料名称",
				align : "center"
			},
			{
				field : "barCode",
				title : "编号",
				align : "center"
			},
			{
				field : "spec",
				title : "型号",
				align : "center"
			},
			{
				field : "num",
				title : "包装数量",
				align : "center"
			},
			{
				field : "id",
				title : "操作",
				align : "center",
				width : "10%",
				formatter : function(value, row, index){
	            	return [
	            		`<button class='btn btn-xs btn-warning upFrameBtn' type='button' data-id=${value}><i class='fa fa-pencil'>上架</i> </button>`,
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
		        params : params.search
		    };
		    return temp;
		},
	    onLoadSuccess : function (res) {
	    	$(".upFrameBtn").on("click", function(e){
	    		var matInfoId = $(this).data("id");
	    		var cabinetId = $("#cabinetId").val();
	    		var equDetailStaId = $("#equDetailStaId").val();
	    		var url = Feng.ctxPath + "/lowerFrameMgr/upFrame/" + equDetailStaId + "/" + matInfoId;
	    		var ajax = new $ax(url, function(data) {
	    			if(data.code == 200){
	    	    		Feng.success(data.message);
	    	    		setTimeout(function (){
	    					infoDlg.close();
	    				}, 1000);
	    	    		window.parent.tbl.bootstrapTable('refresh');
	    	    	}else{
	    	    		Feng.info(data.message);
	    	    	}
	    		}, function(data) {
	    			Feng.error(data.message);
	    		});
	    		ajax.start();
	    	})
	    }, 
	});
};

/**
 * 批量上架物料信息
 */
infoDlg.searchAll = function() {
	var that = this;
	tb2 = $("#matInfoListAll").bootstrapTable({
		url : "/lowerFrameMgr/getMatInfoListAll",
		cache: false,
		height: 550,
		search: true,
		searchAlign: "left",
		showToggle:true,
		showColumns: true,
	    showRefresh: true,
	    clickToSelect: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 1000, 
	  	sidePagination: "server",
	  	paginationDetailHAlign : ' hidden',
		columns : [
			{
				checkbox : true,
				visible : true
			},
			{
				field : "matEquName",
				title : "物料名称",
				align : "center"
			},
			{
				field : "barCode",
				title : "编号",
				align : "center"
			},
			{
				field : "spec",
				title : "型号",
				align : "center"
			},
			{
				field : "num",
				title : "包装数量",
				align : "center"
			},
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
		        params : params.search
		    };
		    return temp;
		},
	});
};

/**
 * 批量上架操作
 */
infoDlg.upFrameAll = function() {
	var cabinetId = $("#cabinetId").val();
	var data = $('#matInfoListAll').bootstrapTable('getSelections');
	
	if(data.length <= 0){
		Feng.info("上架物料不能为空");
		return;
	}
	
	$.ajax({
	    url: Feng.ctxPath + "/lowerFrameMgr/upFrameAll/" + cabinetId,
	    type: 'post',
	    data: {"data" : JSON.stringify(data)},
	    success: function(data){
	    	if(data.code == 200){
	    		Feng.success(data.message);
	    		setTimeout(function (){
					infoDlg.close();
				}, 1000);
	    		window.parent.tbl.bootstrapTable('refresh');
	    	}else{
	    		Feng.info(data.message);
	    	}
	    },
	    error: function(err){
	    	Feng.error(data.message);
	    }
	});
};
