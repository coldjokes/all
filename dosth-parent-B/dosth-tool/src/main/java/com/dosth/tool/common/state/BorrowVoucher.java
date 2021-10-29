package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 借出凭证
 * 
 * @author guozhidong
 *
 */
public enum BorrowVoucher implements IEnumState {
	MATTYPE(0, "类型"), REQREF(1, "设备"), PROCREF(2, "工序"), PARTS(4, "零件"), CUSTOM(5, "自定义"), APPLYVOUCHER(6, "申请单");

	private Integer code;
	private String message;

	private BorrowVoucher(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return null;
		} else {
			for (BorrowVoucher voucher : BorrowVoucher.values()) {
				if (voucher.getCode().equals(code)) {
					return voucher.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return BorrowVoucher.valueOf(name).getMessage();
	}
}