/**
 * 工具管理--物料关联设置
 */
var MatCategory = {
	equInfoInstance:null,
    id: "managerTable",//表格id
    seItem: null,		//选中的条目
    oTable: null,
    layerIndex: -1
};

/**
 * 操作前校验
 */
MatCategory.check = function() {
	if ($("#typeTreeId").val() == "") {
		Feng.info("请先选择关联类型");
		return;
	}
};

/**
 * 重置
 */
MatCategory.resetSearch = function() {
	MatCategory.check();
    $("#knifeInfo").val("");
    //$("#onoffswitch").attr("checked",false);
    MatCategory.search();
};


/**
 * 搜索
 */
MatCategory.search = function () {
	
	MatCategory.check();
	
	var flag;
	if ($("#onoffswitch").is(':checked')) {
		flag = "1";
	} else {
		flag = "0";
	}
	
	//点击查询是 使用刷新 处理刷新参数
//	$("#onoffswitch").attr("checked",false);

	var knifeInfo = $("#knifeInfo").val();
	var typeTreeId = $("#typeTreeId").val();
	var parentTreeId = $("#parentTreeId").val();
	
	if(typeTreeId == "" || parentTreeId == ""){
		return;
	}
	var infos = new Array();
	infos[0] = knifeInfo;
	infos[1] = typeTreeId;
	infos[2] = parentTreeId;
	infos[3] = flag;
	var ajax = new $ax(Feng.ctxPath + "/matCategory/search/" + infos, function(data) {
		$("#infos").html(data);
	}, function(data) {
		Feng.error("加载失败!");
	}, "html");
	ajax.start();
};


/**
 * 表示选中项
 */
MatCategory.dipCheck = function (typeTreeId) {
	var ajax = new $ax(Feng.ctxPath + "/matCategory/dipCheck/" + typeTreeId, function(data) {
		$("#infos").html(data);
	}, function(data) {
		Feng.error("加载失败!");
	}, "html");
	ajax.start();
}

/**
 * 保存
 */
MatCategory.submit = function () {
	MatCategory.check();
	var typeTreeId = $("#typeTreeId").val();
	if( typeTreeId == "" || typeTreeId == "0"){
		return;
	}
	//获取被选中物料的id
	obj = document.getElementsByName("input");
	var check_val =[];
	var unCheck_val =[];
	for(k in obj){
		if(obj[k].checked){
			check_val.push(obj[k].id);
		}else{
			unCheck_val.push(obj[k].id);
		}
	}
	
	if(check_val == ""){
		check_val.push("empty");
	}
	if(unCheck_val == ""){
		unCheck_val.push("empty");
	}
	
	$.ajax({
        url: Feng.ctxPath + "/matCategory/submmit/" + typeTreeId +"/"+ check_val +"/"+ unCheck_val,
        dataType: 'html',
        beforeSend: function(data) {
        	MatCategory.showModal();
        },
        success: function(data) {
        	MatCategory.hideModal();
        	Feng.success("绑定成功");
        },
        error: function(data) {
        	MatCategory.hideModal();
            Feng.error("加载失败!");
        }
    });
};

$(function () {
	//初始化时默认加载全部物料
	$.ajax({
        url: Feng.ctxPath + "/matCategory/getEquInfos/" + [0, null],
        type: "post",
        success: function (data) {
        	$("#infos").html(data);
        },
        error: function (data) {
        	Feng.error("加载失败!");
        }
    })
});

MatCategory.hideModal = function(){
    $('#myModal').modal('hide');
}
 
MatCategory.showModal = function(){
    $('#myModal').modal({backdrop:'static',keyboard:false});
}
