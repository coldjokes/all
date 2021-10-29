$(function(){

	$("[data-toggle='tooltip']").tooltip();
	
	//初始化各个开关
	$.getJSON(Api.url("setting"), '',function(result){
		var setting = result.results[0];
		$('[name="faceStatus"]').bootstrapSwitch({
			'state': setting.faceStatus == "ON",
			onSwitchChange:function(event,state){
				$("#updateSettingConfirmBtn").removeClass("hide");
	         }
		});
		$('[name="materialCount"]').bootstrapSwitch({
			'state': setting.materialCount == 'ON',
			onSwitchChange:function(event,state){
				$("#updateSettingConfirmBtn").removeClass("hide");
	         }
			
		});
		$('[name="multiMaterialOneCell"]').bootstrapSwitch({
			'state': setting.multiMaterialOneCell == 'ON',
			onSwitchChange:function(event,state){
				$("#updateSettingConfirmBtn").removeClass("hide");
	         }
		});
	})
	
	var settingUpdateModal = $("#settingUpdateModal");
	
	//更新按钮
  	 $("#updateSettingConfirmBtn").on("click", function () {
  		settingUpdateModal.modal();
   	 })
	
   	 //确定更新
	$("#updateSettingBtn").click(function(){
		//拼接数据
		var data = [
			{
				name: 'faceStatus', 
				value: $('[name="faceStatus"]').bootstrapSwitch('state') ? 'ON' : 'OFF'
			},
			{
				name: 'materialCount', 
				value: $('[name="materialCount"]').bootstrapSwitch('state') ? 'ON' : 'OFF'
			},
			{
				name: 'multiMaterialOneCell', 
				value: $('[name="multiMaterialOneCell"]').bootstrapSwitch('state') ? 'ON' : 'OFF'			
			}
		]
		
		//更新后台
		$.putJSON(Api.url("setting"), data, function(result){
			settingUpdateModal.modal("hide")
		})
		
	})
})