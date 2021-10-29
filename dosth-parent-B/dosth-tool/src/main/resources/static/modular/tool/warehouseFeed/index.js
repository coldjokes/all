/**
 * 入库单补料
 */
var WareHouseFeed = {
	layerIndex : -1,
	oTable : null,
	params: {},
	seItem : null

};

/**
 * @description 刷新
 */
WareHouseFeed.getFeedList = function() {
	var equSettingId = $("#equId").val();
	if (equSettingId == "") {
		Feng.info("请先选择柜子");
		return;
	}
	WareHouseFeed.oTable = $("#infos").bootstrapTable({
		url : "/warehouseFeed/getFeedList",
		cache: false,
		showToggle:true,
		showColumns: true,
	    showRefresh: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 10, 
	  	sidePagination: "server",
		columns : [
			{
				checkbox : true,
				visible : true
			},
			{
				field : "stockNo",
				title : "库房号",
				align : "center"
			},
			{
				field : "orderNo",
				title : "单据号",
				align : "center"
			},
			{
				field : "companyNo",
				title : "公司代号",
				align : "center"
			},
			{
				field : "type",
				title : "业务类型",
				align : "center"
			},
			{
				field : "matNo",
				title : "物料编码",
				align : "center"
			},
			{
				field : "feedNum",
				title : "制单数量",
				align : "center"
			}
		],
		responseHandler : function(res) {
			return {
				  "total": res.totalElements,
				  "rows": res.content
			  };
		},
		queryParams : function (params) { //查询的参数
			const defaultParams = {  
					offset: params.offset, //页码
					limit: params.limit, //每页显示个数
	                sortField: params.sort,    //排序列名  
	                sortOrder: params.order, //正序、反序
	                cabinetId: $("#equId").val()
	        };
			//页面参数
            const userParams = WareHouseFeed.params;
            return $.extend({}, defaultParams, userParams);
        },
	});
};

/**
 * 生成清单前判断
 */
WareHouseFeed.buildList = function() {
	var equSettingId = $("#equId").val();
	if (equSettingId == "") {
		Feng.info("请先选择柜子");
		return;
	}
	var arrs = new Array();
	var rows = WareHouseFeed.oTable.bootstrapTable('getSelections');
	if(rows == ""){
		Feng.info("请选择入库单");
		return;
	}
	for(var i = 0; i < rows.length; i++){
		var barCode = rows[i].matNo;
		var feedNum = rows[i].feedNum;
		var orderNo = rows[i].orderNo;
		var stockNo = rows[i].stockNo;
		arrs += barCode + "," + feedNum + "," + orderNo + "," + stockNo + ";";
	};
	WareHouseFeed.generate(arrs, equSettingId);
};

/**
 * 生成补料清单
 */
WareHouseFeed.generate = function(arrs, equSettingId) {
	var url = Feng.ctxPath + "/warehouseFeed/warehouseFeedList";
	var ajax = new $ax(url, function(data) {
		if (data.code == 200) {
    		Feng.success(data.message);
    	}else{
    		Feng.info(data.message);
    	}
	}, function(data) {
		Feng.error("生成清单失败!");
	});
	ajax.setData({"arrs":arrs, "equSettingId":equSettingId});
	ajax.start();
};



/**
 * 点击树形节点前判断
 */
WareHouseFeed.onBeforeClick = function(treeId, treeNode, clickFlag) {
	if (treeNode.id.startWith("C_")) {
		return false;
	}
	return true;
};

WareHouseFeed.onClickEquInfo = function(e, treeId, treeNode) {
	$("#equId").val(treeNode.id);
};

$(function() {
	var equInfoTree = new $ZTree("equInfoTree", "/equsetting/tree");
	equInfoTree.bindBeforeClick(WareHouseFeed.onBeforeClick);
	equInfoTree.bindOnClick(WareHouseFeed.onClickEquInfo);
	equInfoTree.init();
	WareHouseFeed.equInfoInstance = equInfoTree;
});