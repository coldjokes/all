/**
 * 常量
 */

const DATE_FMT = "YYYY-MM-DD HH:mm"
	
const startTime = moment().subtract(1, 'month').startOf('day');
const endTime = moment().endOf('day');