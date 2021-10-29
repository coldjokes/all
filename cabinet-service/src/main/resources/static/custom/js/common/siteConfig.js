/**
 * 后台字典及字典相关方法
 */
var SiteConfig;
$(function(){
	var results;
	$.getJSON(Api.url("siteConfig"), {}, function(response){
		if(response.code === 200){
			SiteConfig = response.results[0];
		}
	}, false)
	
});