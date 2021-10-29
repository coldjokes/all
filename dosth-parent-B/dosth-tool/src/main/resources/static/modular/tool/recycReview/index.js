/**
 * 回收审核
 */
var RecycReview = {};

/**
 * 识别码焦点
 */
ctrl=document.getElementById("barCode"); 
setTimeout(function(){
	ctrl.focus();
},50);

var inputStr = "";
document.onkeydown=function(event){
    var e = event || window.event || arguments.callee.caller.arguments[0];
    if(e && e.keyCode==13) { // enter键
    	RecycReview.search();
    }
   }

/**
 * 查询
 */
RecycReview.search = function() {
	var barCode = $("#barCode").val();
	barCode = barCode.replace(/[^a-z\d]/ig,"");
	if(barCode != null && barCode != ""){
		var url = Feng.ctxPath + "/recycReview/search/" + barCode;
	    var ajax = new $ax(url, function (data) {
	    	$("#editContent").html(data);
	    	RecycReview.pass();
	    	$("#barCode").val("");
	    }, function (data) {
	    	Feng.error("搜索失败!");
	    	$("#barCode").val("");
	    }, "html");
	    ajax.start();
	}else{
		Feng.error("请扫描条形码！");
		return;
	}
};

/**
 * 审核通过
 */
RecycReview.pass = function() {
	console.log("index");
	var data = [];
	var barCode = $("#barCode").val();
	if(barCode != null && barCode != ""){
		var url = "/recycReview/review_pass/" + barCode;
	    var ajax = new $ax(url, function (result) {
	    	if(result.code == 200){
	    		Feng.success("审核成功!");
	    	}else{
	    		Feng.error(result.message);
	    	}
	    }, function (data) {
	    	Feng.error("审核失败!");
	    }, "json");
	    ajax.start();
	}else{
		Feng.error("请扫描条形码！");
		return;
	}
};

/**
 * 重置
 */
RecycReview.reset = function() {
	$("#editContent").html("");
	$("#barCode").val("");
	ctrl.focus();
};
