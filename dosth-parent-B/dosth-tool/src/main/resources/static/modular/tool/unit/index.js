/**
 * 工具管理--单位
 */
var MgrUnit = {
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1,
    tbl : null
};

/**
 * 检查是否选中
 */
MgrUnit.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
    	MgrUnit.seItem = selected[0];
        return true;
    }
};


/*
 * 查询
 */
MgrUnit.search = function () {
    //点击查询是 使用刷新 处理刷新参数
    tbl.bootstrapTable('refresh');
};

/**
 * 重置
 */
MgrUnit.resetSearch = function() {
    $("#name").val("");
    $("#status").val("-1");
    MgrUnit.search();
};

/**
 * 新增
 */
MgrUnit.openAddMgr = function() {
	var index = layer.open({
        type: 2,
        title: '添加单位信息',
        fix: false, //不固定
        maxmin: false,
		area:['100%','100%'],
        content: '/unit/addView'
    });
    this.layerIndex = index;
};

MgrUnit.list=function(){
    var that=this;
    tbl=$("#managerTable").bootstrapTable({
        url : "/unit/list",
        toolbar: "#addMgr",//设置自定义工具栏容器ID，也可以是容器样式类名.toolbar
        cache: false,
		showColumns: true,
	    showRefresh: true,
	    clickToSelect: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 10, 
        sidePagination: "server",
        columns :[
            {
                field : "unitName",
                title : "名称",
                align : "center"
            },
            {
                field : "status",
                title : "状态",
                align : "center",
                formatter : function (value, row, index){
                    return statusTools(row);
                }
            },
            {
                field : "id",
                title : "操作",
                align : "center",
                formatter : function(value, row, index){
                    return [
		            	`<button class='btn btn-xs btn btn-warning openEditBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
	            		"&nbsp;"
	            		].join("");
                }
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
		        name : $("#name").val(),
		        status : $("#status").val(),
		    };
		    return temp;
        },
        onLoadSuccess : function (res) {
	    	$(".openEditBtn").on("click", function(e){
	    	    var unitId = $(this).data("id");
                var index = layer.open({
	    			type : 2,
	    			title : '修改单位信息',
	    			fix : false, // 不固定
	    			maxmin : false,
					area:['100%','100%'],
	    			content: '/unit/editView/' + unitId
	    		});
	    		that.layerIndex = index;
	    	});
	    }, 
    });
}

$(function () {
     MgrUnit.list();
});

/* 开启状态显示 */
function statusTools(row) {
    if (row.status == 'ENABLE') {
        return '<i class=\"fa fa-toggle-on  text-navy fa-2x\" onclick="enable(\'' + row.id + '\')"></i> ';
	} else {
		return '<i class=\"fa fa-toggle-off text-navy fa-2x\" onclick="disable(\'' + row.id + '\')"></i> ';
	}
}

/* 单位管理-停用 */
function disable(unitId) {
   var operation = function(){
       var ajax = new $ax("/unit/update/" + unitId , 
	function (data) {
           Feng.success("设置成功!");
           $("#managerTable").bootstrapTable('refresh');
       }, function (data) {
           Feng.error("设置失败!");
       });
       ajax.start();
   };
    Feng.confirm("是否启用此单位信息?",operation);
}

/* 单位管理-启用 */
function enable(unitId) {
	var operation = function(){
        var ajax = new $ax("/unit/update/" + unitId , 
		function (data) {
            Feng.success("设置成功!");
            $("#managerTable").bootstrapTable('refresh');
        }, function (data) {
            Feng.error("设置失败!");
        });
        ajax.start();
    };
    Feng.confirm("是否停用此单位信息?",operation);
}
