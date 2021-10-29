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
	"log": 				"/api/logs", 						//日志
	"siteConfig": 		"/api/siteConfig", 					//网站配置
	"setting": 			"/api/setting", 					//系统配置
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