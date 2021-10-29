package com.dosth.comm.iccard4bind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ICUser4bind implements ICCardSwipe4bind {
	
	private static final Logger logger = LoggerFactory.getLogger(ICUser4bind.class);

	public final int Log_In_Monitor_Mode = 1;
	public final int Bind_User_Mode = 2;
	private int mode;

	private boolean doneFlag = false;
	private String ic_str = null;
	private ICReaderWriter4bind icObj = null;

	private boolean DEBUG = true;

	public ICUser4bind() {
	}

	public ICUser4bind(ICReaderWriter4bind icObj) {
		this.icObj = icObj;
	}

	public void setICObj(ICReaderWriter4bind icObj) {
		this.icObj = icObj;
	}

	@Override
	public synchronized void onICSwipe(String strOnIC) {
		if (DEBUG)
			logger.debug("IC User prcessing Str on IC:" + strOnIC);

		if (strOnIC.contains("failed")) // no ic
			return;

		if (mode == Bind_User_Mode) {
			if (DEBUG)
				logger.debug("Enter onICSwipe().Bind_User_Mode ");
			ic_str = strOnIC;
			doneFlag = true;
		} else if (mode == Log_In_Monitor_Mode) {
			if (DEBUG)
				logger.debug("Enter onICSwipe().Log_In_Monitor_Mode, Start login procedure caused by IC swipe");

			// start login procedure
			if (icObj != null)
				icObj.stopReadThread();
//        	ICLogin icLogin = new ICLogin("admin","111111", "UserName");
//        	AccountUserInfo account_user_info = adminService.getAccountUserInfoByCardStr(strOnIC);
//    		WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.ICSWIPING, icLogin));
		}
	}

	public void setDoneFlag(boolean flag) {
		doneFlag = flag;
	}

	public boolean isDone() {
		return doneFlag;
	}

	public String getStrOnIC() {

		return ic_str;
	}

	public void clearStrOnIC() {
		ic_str = null;
	}

	public synchronized void setMode(int mode) {
		this.mode = mode;
	}

}
