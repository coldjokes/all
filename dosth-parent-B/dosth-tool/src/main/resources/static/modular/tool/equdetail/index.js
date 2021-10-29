/**
 * 工具管理--物料详情
 */
var MgrEquDetail = {
    equInfoInstance: null,
    managerTable: null,
    layerIndex: -1
};
 
/**
 * 重置
 */
MgrEquDetail.resetSearch = function() {
	$("#matInfo").val("");
	MgrEquDetail.search();
}

/**
 * 刷新详情信息
 */
MgrEquDetail.search = function() {
	managerTable.bootstrapTable('refresh');
};
 
/**
 * 生成清单前判断
 */
MgrEquDetail.feedList = function() {
    var equSettingId = $("#equInfoId").val();
    if (equSettingId == "") {
        Feng.info("请先选择柜子");
        return;
    }
    var arrs = new Array();
    var flag;
    var colNo;
    var msg;
    
    var rows = $('#managerTable').bootstrapTable('getData');
    for(var i = 0; i < rows.length; i++){
        var matInfoId = rows[i].matInfoId;
        if(matInfoId){
            var id = rows[i].id;
            var num = parseInt($("#feedingNum_" + id).val().trim());
            if(!isNaN(num) && num != null){
                colNo = rows[i].position;
                if(num != 0){
                    var curNum = rows[i].curNum;
                    var maxNum = rows[i].maxReserve;
                    var waitNum = rows[i].waitFeedingNum;
                    if(num + curNum + waitNum <= maxNum){
                        arrs += id + "," + num + ";";
                        flag = true;
                    }else{
                        msg = "补料数量超出最大库存！";
                        flag = false;
                        break;
                    }
                } else {
                    msg = "补料数量不能为0！";
                    return false;
                }
            }
        }
    };
    if (arrs.length == 0) {
        layer.msg("没有可补料数据");
        return;
    }
    if(flag){
        $("#feedListBtn").css('background', '#bfbfbf');
        $("#feedListBtn").css('pointer-events', 'none');
        MgrEquDetail.generate(arrs, equSettingId);
    }else{
        if(colNo != null){
            Feng.error(colNo + " " + msg);
            return;
        }
    }
    
    setTimeout(function (){
        $("#feedListBtn").css('pointer-events', 'auto');
        $("#feedListBtn").css('background', '#1ab394');
    }, 2000);
};
 
/**
 * 生成补料清单
 */
MgrEquDetail.generate = function(arrs, equSettingId) {
    var url = Feng.ctxPath + "/equdetail/feedList";
    var ajax = new $ax(url, function(data) {
        if (data.code == 200) {
        	Feng.success(data.message);
            managerTable.bootstrapTable('refresh');
            MgrEquDetail.exportLast();
        } else {
        	Feng.error(data.message);
        }
    }, function(data) {
        Feng.error(data.message);
    });
    ajax.setData({"arrs":arrs, "equSettingId":equSettingId});
    ajax.start();
};
 
/**
 * @description 导出最新的补料单记录
 */
MgrEquDetail.exportLast = function() {
    window.location.href = Feng.ctxPath + "/equdetail/export";
};
 
/**
 * 一键补满
 */
MgrEquDetail.fillFull = function() {
    var equSettingId = $("#equInfoId").val();
    if (equSettingId == "") {
        Feng.info("请先选择柜子");
        return;
    }
    
    var rows = $('#managerTable').bootstrapTable('getData');
    for(var i = 0; i < rows.length; i++){
        var id = rows[i].id;
        if (rows[i].matInfoId) {
            var curNum = rows[i].curNum;
            var maxNum = rows[i].maxReserve;
            var waitNum = rows[i].waitFeedingNum;
            var num = maxNum - curNum - waitNum;
            if(num > 0){
                $("#feedingNum_" + id).val(num);
            }
        }
    }
};
 
MgrEquDetail.getEquInfos = function() {
    var that = this;
    managerTable = $("#managerTable").bootstrapTable({
	      url : "/equsetting/getEquInfos",
	      cache: false,
	      height: 600,
	      pagination: true,
          pageNumber: 1,
          pageSize: 1000, 
          paginationDetailHAlign : ' hidden',
          columns : [
            {
                field : "position",
                title : "物料位置",
                align : "center"
            },
			{
                field : "id",
                title : "物料信息",
                align : "center",
                formatter : function(value, row, index){
                    return [
						`<div>
							<img style="width: 60px; height: 60px; float:left; margin-right:5px" src="${row.icon}" />
							<div style="float:left;text-align:left">
								<span>` + '名称:' + row.matInfoName + `</span>
								<br>
								<span>` + '编号:' + row.barCode + `</span>
								<br>
								<span>` + '型号:' +row.spec + `</span>
							</div>
						 </div>`,
                        ].join("");
                }
            },
			{
                field : "maxReserve",
                title : "库位最大存储",
                align : "center"
            },
            {
                field : "curNum",
                title : "当前数量",
                align : "center",
                cellStyle : function(value, row, index){
                    if(row.matInfoName != null && row.matInfoName != ''){
                        if(value <= row.warnVal){
                            return {css:{"color":"red"}}
                        } else {
                            return {css:{"color":"black"}}
                        }
                    } else {
                        return {css:{"color":"black"}}
                    }
                }
            },
            {
                field : "waitFeedingNum",
                title : "待确认数量",
                align : "center"
            },
            {
                field : "id",
                title : "补料数量",
                align : "center",
                width : "70px",
                formatter : function(value, row, index){
                    return [
                        `<input type='text' style='width: 70px;' id='feedingNum_${value}' placeholder='请输入...'>`,
                        ].join("");
                }
            },
            {
                field : "id",
                title : "操作",
                align : "center",
                formatter : function(value, row, index){
                    return [
                        `<button class='btn btn-xs btn-primary openSetupBtn' type='button' data-id=${value}><i class='fa fa-plus'>设置存储</i> </button>`,
                        ].join("");
                }
            } 
        ],
        responseHandler : function(res) {
			return res.content;
        },
		queryParams: function(params) {
			var temp = {
				limit: params.limit,
				offset: params.offset,
				pageSize: this.pageSize,
				pageNumber: this.pageNumber,
				matInfo: $("#matInfo").val(),
				equSettingId: $("#equInfoId").val(),
			};
			return temp;
		},
        onLoadSuccess : function (res) {
            $(".openSetupBtn").on("click", function(e){
                var equDetailStaId = $(this).data("id");
                var index = layer.open({
                    type : 2,
                    title : '设置',
                    area : [ '800px', '400px' ], // 宽高
                    fix : false, // 不固定
                    maxmin : false,
					area:['100%','100%'],	
                    content : '/equdetail/editView/' + equDetailStaId
                });
                that.layerIndex = index;
            });
        }, 
    });
};

MgrEquDetail.showEquInfoSelectTree = function() {
    Feng.showInputTree("equInfoName", "equInfoContent");
};
 
MgrEquDetail.onClickEquInfo = function(e, treeId, treeNode) {
	var equSettingId = treeNode.id;
	$("#equInfoId").val(equSettingId);
	managerTable.bootstrapTable('refresh');
};
 
/**
 * 点击树形节点前判断
 */
MgrEquDetail.onBeforeClick = function(treeId, treeNode, clickFlag) {
    if (treeNode.id.startWith("C_")) {
        return false;
    }
    return true;
};
 
$(function() {
	MgrEquDetail.getEquInfos();
	
    var equInfoTree = new $ZTree("equInfoTree", "/equsetting/treeCabDetail");
    equInfoTree.bindBeforeClick(MgrEquDetail.onBeforeClick);
    equInfoTree.bindOnClick(MgrEquDetail.onClickEquInfo);
    equInfoTree.init();
    MgrEquDetail.equInfoInstance = equInfoTree;

	var treeObj = $.fn.zTree.getZTreeObj("equInfoTree");
	var nodes = treeObj.getNodes();
	var first = nodes[0];
	if (nodes[0].id.startWith("C_")) {
		treeObj.expandNode(nodes[0], true);
		first = nodes[0].children[0];
	}
	treeObj.selectNode(first);
	$("#equInfoId").val(first.id);
	if (first.id.startWith("D_")) {
		$("#equInfoId").val(first.pId);
	}
	MgrEquDetail.search();
});