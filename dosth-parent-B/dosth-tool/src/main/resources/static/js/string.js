/**
 * @description 判断字符串结尾
 */
String.prototype.endWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length) {
		return false;
	}
	if (this.substring(this.length - s.length) == s) {
		return true;
	} else {
		return false;
	}
	return true;
}

/**
 * @description 判断字符串开头
 */
String.prototype.startWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length) {
		return false;
	}
	if (this.substr(0, s.length) == s) {
		return true;
	} else {
		return false;
	}
	return true;
}