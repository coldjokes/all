/**
 * 每日限额
 */
var infoDlg = {};

/**
 * 提交信息
 */
infoDlg.editSubmit = function () {
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var limitSumNum = $("#limitSumNum").val();
	var notReturnLimitNum = $("#notReturnLimitNum").val();
	var accountId = $("#accountId").val();
	var limitListArr = [];
	$(".limit-item").each(function() {
		var limitList;
		var limitId = $(this).find("input[id='limitId']").val();
		var limitNum = $(this).find("input[class='limitNum']").val();
		var notReturnNum = $(this).find("input[class='notReturnNum']").val();
		
		if(limitNum == "" || limitNum == null){
			limitNum = 0;
		}
		if(notReturnNum == "" || notReturnNum == null){
			notReturnNum = 0;
		}
		limitList = {
			"limitId" : limitId,
			"limitNum" : limitNum,
			"notReturnNum" : notReturnNum,
		};
		limitListArr.push(limitList);
	});
	
	if(startTime == "" || startTime == null){
		startTime = "00:00:00";
	}
	if(endTime == "" || endTime == null){
		endTime = "23:59:59";
	}
	if(limitSumNum == "" || limitSumNum == null){
		limitSumNum = 0;
	}
	if(notReturnLimitNum == "" || notReturnLimitNum == null){
		notReturnLimitNum = 0;
	}
	
	var data = {
			"startTime": startTime,
			"endTime": endTime,
			"limitSumNum": limitSumNum,
			"notReturnLimitNum": notReturnLimitNum,
			"accountId": accountId,
			"limitListArr": limitListArr
			
		};
	$.ajax({
	    url: '/dailyLimit/saveDailyLimit',
	    type: 'post',
	    data: {"data" : JSON.stringify(data)},
	    success: function(data){
	    	if(data.code == 200){
	    		 Feng.success(data.message);
	    	}
	    },
	    error: function(err){
	    	Feng.error(err);
	    }
	});
};