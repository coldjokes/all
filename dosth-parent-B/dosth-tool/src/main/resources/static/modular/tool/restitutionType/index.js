/**
 * 归还类型定义
 */
var ReType = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1,
    tbl: null
};

/**
 * 检查是否选中
 */
ReType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	ReType.seItem = selected[0];
        return true;
    }
};

/**
 * 查询
 */
ReType.search = function () {
    tbl.bootstrapTable('refresh');
};

/**
 * 重置
 */
ReType.resetSearch = function() {
    $("#name").val("");
    $("#status").val("-1");
    ReType.search();
};

/**
 * 添加
 */
ReType.openAddReType = function() {
	var index = layer.open({
        type: 2,
        title: '添加归还类型定义',
        fix: false, //不固定
        maxmin: false,
		area:['100%','100%'],
        content: '/restitutionType/restitutionType_add'
    });
    this.layerIndex = index;
};

ReType.list=function(){
    var that=this;
    tbl=$("#managerTable").bootstrapTable({
        url : "/restitutionType/list",
        toolbar : "#addReType",
        cache : false,
        showColumns : true,
        showRefresh : true,
        clickToSelect : true,
        pagination : true,
        pageNumber : 1,
        pageSize : 10,
        sidePagination : "server",
        columns :[
            {
                field : "restName",
                title : "归还名称",
                align : "center"
            },
            {
                field : "returnBackType",
                title : "归还类型",
                align : "center",
                formatter : function(value,row,index){
                    return returnBackTypeTools(row);
                }
            },
            {
                field : "status",
                title : "状态",
                align : "center",
                formatter : function(value,row,index){
                    return statusTools(row);
                }
            },
            {
                field : "remark",
                title : "备注",
                align : "center",
                 formatter : function(value, row, index){
					if (value == '') {
						return "-";
					}
					return value;
				},
            },
            {
                field : "id",
                title : "操作",
                align : "center",
                formatter : function(value,row,index){
                    return [
                        `<button class='btn btn-xs btn btn-warning openEditBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
	            		"&nbsp;"
	            		].join("");
                }
            },
        ],
        responseHandler : function(res){
            return {
                "total": res.totalElements,
                "rows" : res.content
            };
        },
        queryParams : function(params){
            var temp={
                limit : params.limit,
                offset : params.offset,
                pageSize : this.pageSize,
                pageNumber : this.pageNumber,
                name : $("#name").val(),
                status : $("#status").val(),
            };
            return temp;
        },
        onLoadSuccess : function(res){
            $(".openEditBtn").on("click",function(e){
                var id=$(this).data("id");
                var index=layer.open({
                    type :2,
                    title : '修改归还类型定义',
                    fix : false,
                    maxmin : false,
					area:['100%','100%'],
                    content : '/restitutionType/restitutionType_edit/' + id
                });
                that.layerIndex=index;
            });
        },
    });
}

$(function () {
     ReType.list();
});

/*归还类型*/
function returnBackTypeTools(row){
    if(row.returnBackType == 'NORMAL'){
        return '可复用归还';
    } else{
        return '不可复用归还';
    }
}

/* 开启状态显示 */
function statusTools(row) {
    if (row.status == 'ENABLE') {
        return '<i class=\"fa fa-toggle-on  text-navy fa-2x\" onclick="disable(\'' + row.id + '\')"></i> ';
	} else {
		return '<i class=\"fa fa-toggle-off text-navy fa-2x\" onclick="enable(\'' + row.id + '\')"></i> ';
	}
}

/* 归还管理-启用 */
function disable(id){
    var operation = function (){
        var ajax =new $ax("/restitutionType/update/" + id,
        function(date){
            Feng.success("设置成功!");
            $("#managerTable").bootstrapTable('refresh');
        },function(data){
            Feng.error("设置失败!");
        });
        ajax.start();
    };
    Feng.confirm("是否停用此归还信息?",operation);
}

/*归还管理-停用 */
function enable(id){
    var operation = function(){
        var ajax = new $ax("/restitutionType/update/" + id,
      	function (data) {
            Feng.success("设置成功!");
            $("#managerTable").bootstrapTable('refresh');
        }, function (data) {
            Feng.error("设置失败!");
        });
        ajax.start();
    };
	Feng.confirm("是否启用此归还信息?",operation);
    
}