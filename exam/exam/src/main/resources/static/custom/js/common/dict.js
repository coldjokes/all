/**
 * 后台字典及字典相关方法
 */
var Dict;
$(function(){
	var results;
	$.getJSON(Api.url("dict"), {}, function(response){
		if(response.code === 200){
			results = response.results[0];
		}
	}, false)
	
	Dict = {
		"getDict": function(dictName){ //通过字典英文名获取整本字典
			return results[dictName];
		},
		"getText": function(dictName, code){ //通过字典英文名和代码，获取相应中文
			const dicts = this.getDict(dictName);
			let returnInfo;
			if(dicts){
				const targetDict = dicts.find(item => item.code === code);
				if(targetDict){
					return targetDict.text;
				} else {
					return "未知";
					returnInfo = `字典[${dictName}]中未能找到匹配到代码[${code}]`;
				}
			} else{
				returnInfo = `未能找到匹配字典[${dictName}]`
			}
			console.log("获取字典失败", returnInfo);
		}
	}
});