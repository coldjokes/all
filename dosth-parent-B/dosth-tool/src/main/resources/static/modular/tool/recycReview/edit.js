/**
 * 审核详情
 */
var RecycInfo = {};

/**
 * 验证数据是否为空
 */
RecycInfo.validate = function () {
    $('#infoForm').data("bootstrapValidator").resetForm();
    $('#infoForm').bootstrapValidator('validate');
    return $("#infoForm").data('bootstrapValidator').isValid();
};

/**
 * 审核通过
 */
RecycInfo.pass = function() {
	console.log("edit");
	var data = [];
	var barCode = $("#barCode").val();
	if(barCode != null && barCode != ""){
		var url = "/recycReview/review_pass/" + barCode;
	    var ajax = new $ax(url, function (data) {
	    	Feng.success("审核成功!");
	    	$("#editContent").html("");
			$("#barCode").val("");
			ctrl=document.getElementById("barCode");    
			ctrl.focus();  
	    }, function (data) {
	    	Feng.error("审核失败!");
	    }, "html");
	    ajax.start();
	}else{
		Feng.error("请扫描条形码！");
		return;
	}
};

/**
 * 审核不通过原因
 */
RecycInfo.editSubmit = function() {	
	var radios = document.getElementsByName("notMatch");
	var realNum = document.getElementById('realbackNum').value;
	var markIn = document.getElementById('otherReason').value;
    var num = 0;
    var mark = " "; 
    for(var i=0;i<radios.length;i++){
        if(radios[i].checked == true){
        	if(i == 0){
        		mark = "刀具不符";
        	}else if(i == 1){
        		if(realNum != null && realNum != ""){
        			num = realNum;
        			mark = "数量不符";
        		}else{
        			Feng.error("请输入实际归还数量！");
        			return;
        		}
        	}else if(i == 2){
        		if(markIn != null && markIn != ""){
        			mark = markIn;
        		}else{
        			Feng.error("请输入其它原因！");
        			return;
        		}
        	}
        }
    }
	var barCode = $("#barCode").val();
	if(barCode != null && barCode != ""){
		var url = "/recycReview/reject_submit/" + barCode +"/"+ num +"/"+ mark;
	    var ajax = new $ax(url, function (data) {
	    	Feng.success("审核成功!");
	    	$("#editContent").html("");
			$("#barCode").val("");
			ctrl=document.getElementById("barCode");    
			ctrl.focus();  
	    }, function (data) {
	    	Feng.error("审核失败!");
	    }, "html");
	    ajax.start();
	}else{
		Feng.error("请扫描条形码！");
		return;
	}    
};
