var temSetupInfo = {
	subBoxTable: null
}

temSetupInfo.InitSubBoxTable = function() {
	var that = this;
	subBoxTable = $("#subBoxTable").bootstrapTable({
		url: "/subBox/list",
		cache: false,
// showToggle:true,
// showColumns: true,
// showRefresh: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 10,
	  	singleSelect : true,
	  	clickToSelect : true,
	  	sidePagination: "server",
		columns:[
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
			{
				field : "boardNo",
				title : "栈号",
				align : "center"
			},
			{
				field : "lockIndex",
				title : "针脚号",
				align : "center",
				width : "140px",
				formatter : function(value, row, index) {
	            	return [
	            		`<input type="text" style="width: 70px;" value="${value}" id="${'lockIndex_' + row.id}">`,
	            		].join("");
	            }
			},
			{
				field : "id",
				title : "操作",
				align : "center",
				width : "10%",
				formatter : function(value, row, index) {
	            	return [
		            	`<button class='btn btn-xs btn btn-primary editBtn' type='button' data-id=${value}><i class='fa fa-check'>保存</i></button>`,
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
		onLoadSuccess : function (res) {
			$(".editBtn").on("click", function(e){
				var subBoxId = $(this).data("id");
				var lockIndex = $("#lockIndex_" + subBoxId).val();
				if(lockIndex == null || lockIndex == ""){
					window.parent.Feng.info("针脚号不能为空");
					return;
				}
				$.ajax({
					url: '/subBox/update',
					type: 'post',
				   	data:{
				   		"subBoxId" : subBoxId,
				   		"lockIndex" : lockIndex
				   	},
				    success: function(data) {
			    		window.parent.Feng.success(data.message);
			    		subBoxTable.bootstrapTable('refresh');
				    },
				    error: function(err){
				    	window.parent.Feng.success(err.responseJSON.message);
				    }
				});
			});
		},
		queryParams : function(params) {
		    var temp = {
		        limit: params.limit,
		        offset : params.offset,
		        pageSize : this.pageSize,
		        pageNumber : this.pageNumber,
		        equSettingId : $("#equSettingId").val(),
		    };
		    return temp;
		}
	});
};

$(function() {
	temSetupInfo.InitSubBoxTable();
});