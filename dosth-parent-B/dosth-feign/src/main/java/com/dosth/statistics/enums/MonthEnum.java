package com.dosth.statistics.enums;

/**
 * @description 月份枚举
 * @author guozhidong
 *
 */
public enum MonthEnum {
	January {
		@Override
		public Integer code() {
			return 1;
		}

		@Override
		public String cn() {
			return "一月";
		}

		@Override
		public String en() {
			return "Jan";
		}
	},
	February {
		@Override
		public Integer code() {
			return 2;
		}

		@Override
		public String cn() {
			return "二月";
		}

		@Override
		public String en() {
			return "Feb";
		}
	},
	March {
		@Override
		public Integer code() {
			return 3;
		}

		@Override
		public String cn() {
			return "三月";
		}

		@Override
		public String en() {
			return "Mar";
		}
	},
	April {
		@Override
		public Integer code() {
			return 4;
		}

		@Override
		public String cn() {
			return "四月";
		}

		@Override
		public String en() {
			return "Apr";
		}
	},
	May {
		@Override
		public Integer code() {
			return 5;
		}

		@Override
		public String cn() {
			return "五月";
		}

		@Override
		public String en() {
			return "May";
		}
	},
	June {
		@Override
		public Integer code() {
			return 6;
		}

		@Override
		public String cn() {
			return "六月";
		}

		@Override
		public String en() {
			return "June";
		}
	},
	July {
		@Override
		public Integer code() {
			return 7;
		}

		@Override
		public String cn() {
			return "七月";
		}

		@Override
		public String en() {
			return "July";
		}
	},
	Aguest {
		@Override
		public Integer code() {
			return 8;
		}

		@Override
		public String cn() {
			return "八月";
		}

		@Override
		public String en() {
			return "Aug";
		}
	},
	September {
		@Override
		public Integer code() {
			return 9;
		}

		@Override
		public String cn() {
			return "九月";
		}

		@Override
		public String en() {
			return "Sept";
		}
	},
	October {
		@Override
		public Integer code() {
			return 10;
		}

		@Override
		public String cn() {
			return "十月";
		}

		@Override
		public String en() {
			return "Oct";
		}
	},
	November {
		@Override
		public Integer code() {
			return 11;
		}

		@Override
		public String cn() {
			return "十一月";
		}

		@Override
		public String en() {
			return "Nov";
		}
	},
	December {
		@Override
		public Integer code() {
			return 12;
		}

		@Override
		public String cn() {
			return "十二月";
		}

		@Override
		public String en() {
			return "Dec";
		}
	};

	/**
	 * @description 月份编码
	 * @return
	 */
	public abstract Integer code();

	/**
	 * @description 中文
	 * @return
	 */
	public abstract String cn();

	/**
	 * @description 英文
	 * @return
	 */
	public abstract String en();
	
	/**
	 * @description 将月份转换成字符串
	 * @return
	 */
	public String toStr() {
		return new StringBuilder("-").append(code() < 10 ? "0" : "").append(code()).toString();
	}

	/**
	 * @description 起始日期时间
	 * @return
	 */
	public String startDayTime() {
		return new StringBuilder("-").append(code() < 10 ? "0" : code()).append(code()).append("-01 00:00:00")
				.toString();
	}

	/**
	 * @description 截止日期时间
	 * @return
	 */
	public String endDayTime() {
		StringBuilder endDayTime = new StringBuilder();
		endDayTime.append("-");
		switch (code()) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
			endDayTime.append("0").append(code()).append("-31 23:59:59");
			break;
		case 10:
		case 12:
			endDayTime.append(code()).append("-31 23:59:59");
			break;
		case 2:
			endDayTime.append("03-01 00:00:00");
			break;
		case 4:
		case 6:
		case 9:
			endDayTime.append("0").append(code()).append("-30 23:59:59");
		case 11:
			endDayTime.append(code()).append("-30 23:59:59");
			break;
		default:
			break;
		}
		return endDayTime.toString();
	}
}