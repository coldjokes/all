/**
 * 相关自定义方法
 */
$(function(){

	// 获取表单属性的对象
	$.getFormObject = function(form){
		const formData = form.serializeArray();
    	let d = {};
    	$.each(formData, function() {
    		d[this.name] = this.value;
    	});
    	return d;
	};
	
	// 回填表单
	initFormFillBack();
	
	/**
	 * 自定义select下拉框填充
	 * data :默认格式应为 [{code:1, text: "选项1"},{code:2, text: "选项2"}...]； 如果是字符串，则代表从字典中获取
	 * needAll：是否需要[全部]的选项
	 */
	$.customOptions = function(data, needAll){ 
		
		var optionList = "";
		if(needAll){
			optionList += `<option value="">全部</option>`;
		}
		
        if(typeof data == 'string'){ //从字典中获取
        	data = Dict.getDict(data);
        }
        if(data){
            for(let x of data){
            	optionList += `<option value=${x.code}>${x.text}</option>`;
            }
        }
		return optionList;
	}
	
});

function initFormFillBack(){
	$.fn.extend({  
        initForm:function(options){  
            //默认参数  
            var defaults = {  
                jsonValue:options,  
                isDebug:false   //是否需要调试，这个用于开发阶段，发布阶段请将设置为false，默认为false,true将会把name value打印出来  
            }  
            //设置参数  
            var setting = defaults;  
            var form = this;  
            jsonValue = setting.jsonValue;  
            //如果传入的json字符串，将转为json对象  
            if($.type(setting.jsonValue) === "string"){  
                jsonValue = $.parseJSON(jsonValue);  
            }  
            //如果传入的json对象为空，则不做任何操作  
            if(!$.isEmptyObject(jsonValue)){  
                var debugInfo = "";  
                $.each(jsonValue,function(key,value){  
                    //是否开启调试，开启将会把name value打印出来  
                    if(setting.isDebug){  
                        alert("name:"+key+"; value:"+value);  
                        debugInfo += "name:"+key+"; value:"+value+" || ";  
                    }  
                    var formField = form.find("[name='"+key+"']");  
                    if($.type(formField[0]) === "undefined"){  
                        if(setting.isDebug){  
                            alert("can not find name:["+key+"] in form!!!");    //没找到指定name的表单  
                        }  
                    } else {  
                        var fieldTagName = formField[0].tagName.toLowerCase();  
                        if(fieldTagName == "input"){  
                            if(formField.attr("type") == "radio"){  
                            	$("input:radio[name='"+key+"']").prop("checked", false);  
                                $("input:radio[name='"+key+"'][value='"+value+"']").prop("checked", true);
                            } else {  
                                formField.val(value);  
                            }  
                        } else if(fieldTagName == "select"){  
                            //do something special  
                            formField.val(value);  
                        } else if(fieldTagName == "textarea"){  
                            //do something special  
                            formField.val(value);
                        } else {  
                            formField.val(value);  
                        }  

                    }  
                })  
                if(setting.isDebug){  
                    alert(debugInfo);  
                }  
            }  
            return form;    //返回对象，提供链式操作  
        }  
    });
}



