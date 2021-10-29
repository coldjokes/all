$('input[name="selectAll"]').on("click",function(){
	if($(this).is(':checked')){
		$('input[name="input"]').each(function(){
				$(this).prop("checked",true);
		});
	}else{
		$('input[name="input"]').each(function(){
				$(this).prop("checked",false);
		});
	}
});

$(function () {
	$("input:input[value='1']").attr("checked",true);
});
