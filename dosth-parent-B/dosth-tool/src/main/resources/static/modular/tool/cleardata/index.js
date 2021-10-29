/**
 * 系统管理--清理数据
 */
var ClearData = {
};

/**
 * @description 清理物料
 */
ClearData.clearMat = function() {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/clearData/cleatMat", 
		function (data) {
            if(data.code == 200){
            	Feng.success(data.message);
            } else {
            	Feng.error(data.message);
            }
        }, function (data) {
			console.log(data);
            Feng.error("清理物料信息失败!");
        });
        ajax.start();
    };
    Feng.confirm("清理所有物料以及相关信息且不可恢复,确认吗?",operation);
};

/**
 * @description 清理设备
 */
ClearData.clearEqu = function() {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/clearData/clearEqu", 
		function (data) {
        	if(data.code == 200){
            	Feng.success(data.message);
            } else {
            	Feng.error(data.message);
            }
        }, function (data) {
			console.log(data);
            Feng.error("清理设备信息失败!");
        });
        ajax.start();
    };
    Feng.confirm("清理所有设备以及相关信息且不可恢复,确认吗?",operation);
};

/**
 * @description 清理人员
 */
ClearData.clearUser = function () {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/clearData/clearUser", 
		function (data) {
            if(data.code == 200){
            	Feng.success(data.message);
            } else {
            	Feng.error(data.message);
            }
        }, function (data) {
			console.log(data);
            Feng.error("清理人员信息失败!");
        });
        ajax.start();
    };
    Feng.confirm("清理所有人员以及相关信息且不可恢复,确认吗?",operation);
};

/**
 * @description 一键恢复
 */
ClearData.resetInit = function () {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/clearData/resetInit", 
		function (data) {
        	if(data.code == 200){
            	Feng.success(data.message);
            } else {
            	Feng.error(data.message);
            }
        }, function (data) {
			console.log(data);
            Feng.error("一键恢复失败!");
        });
        ajax.start();
    };
    Feng.confirm("一键恢复出厂数据且不可恢复,确认吗?",operation);
};