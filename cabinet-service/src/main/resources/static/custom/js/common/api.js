/**
 * 所有api及api获取方法
 */
var apiUrl = {
	"login":			"/login", 							//登录相关
	"user": 			"/api/users", 						//用户相关
	"usernameCheck": 	"/api/users/nameCheck", 			//用户名校验
	"userImport": 		"/api/users/import", 				//用户导入
	"userPassUpdate": 	"/api/users/passUpdate", 			//用户密码更新
	"dict": 			"/api/dicts", 						//字典
	"material": 		"/api/materials",					 //物料
	"matPicture": 		"/api/materials/picture", 			//物料图片
	"matBlueprint":		"/api/materials/blueprint", 		//物料图纸
	"matBlueprintDel":	"/api/materials/blueprint/delete",  //删除物料图纸
	"matExport":		"/api/materials/export", 			//物料列表导出
	"matImport":		"/api/materials/import", 			//物料导入
	"materialByCategory":		"/api/materials/byCategory",//根据类别查询物料

	"dept":       		"/api/dept",						// 人员列表
	"deptTree":         "/api/dept/tree",					// 部门树
	"deptMap":			"/api/dept/map",					// 用户部门映射
	"userByDepts":		"/api/users/byDepts",				// 根据部门查询用户列表

	//权限设置接口
	"accountTree":		"/api/jurisdiction/initUserTree",	// 初始化人员部门账号树
	"receiveTree":		"/api/jurisdiction/initReceiveTree",// 初始化取料权限-物料类别树
	"getMaterialByUser":"/api/jurisdiction",				// 保存人员部门取料权限
	"getDepts": 		"/api/jurisdiction/getDepts",		// 点击人员部门树获取取料权限

	"noSpecCheck":		"/api/materials/noSpecCheck", 		// 物料编号规格校验
	"category":			"/api/materials/category",     		// 物料类别
	"categoryTree":		"/api/materials/category/tree",		// 物料类别树
	"categoryMap":		"/api/materials/category/map",  	// 物料类别映射

	"log": 				"/api/logs", 						//日志
	"computer":			"/api/computers", 					//主机
	"computerNameCheck":"/api/computers/nameCheck", 		//主机名校验
	"cabinet":			"/api/cabinets", 					//柜体
	"cabinetCell":		"/api/cabinetCells", 				//格口
	"matStock":			"/api/materialStock", 				//物料库存列表
	"matStockExport":	"/api/materialStock/export", 		//物料库存列表导出
	"matBill":			"/api/materialBill", 				//物料流水列表
	"matBillExport":	"/api/materialBill/export", 		//物料流水列表导出
	"siteConfig": 		"/api/siteConfig", 					//网站配置
	
	"setting": 			"/api/setting", 					//系统配置
	
	"materialRemind": 	"/api/materialRemind", 				//物料提醒列表
	"materialRemindCheck": 	"/api/materialRemind/check",	//物料提醒库存检查
}





//var host = "http://localhost:8080";
var host = ""
var Api;
$(function(){
	Api = {
		"get": function(key){
			return host + apiUrl[key];
		},
		"url": function(key, id){
			var url =  host + apiUrl[key];
			if(id){
				url = url + "/" + id;
			}
			return url;
		}
	}
});