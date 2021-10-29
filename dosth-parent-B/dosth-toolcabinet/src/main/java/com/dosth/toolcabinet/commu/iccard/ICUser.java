package com.dosth.toolcabinet.commu.iccard;

import org.springframework.beans.factory.annotation.Value;

import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

public class ICUser implements ICCardSwipe {

	public final int Log_In_Monitor_Mode = 1;
	public final int Bind_User_Mode = 2;
	private int mode;

	private boolean doneFlag = false;
	private String ic_str = null;
	private ICReaderWriter icObj = null;

	@Value("${dosth.equBar}")
	private String cabinetId;

	public ICUser() {
	}

	public ICUser(ICReaderWriter icObj) {
		this.icObj = icObj;
	}

	public void setICObj(ICReaderWriter icObj) {
		this.icObj = icObj;
	}

	@Override
	public synchronized void onICSwipe(String strOnIC) {
		if (strOnIC.contains("failed")) { // no ic
			return;
		}
		if (mode == Bind_User_Mode) {
			ic_str = strOnIC;
			doneFlag = true;
		} else if (mode == Log_In_Monitor_Mode) {
			if (icObj != null) {
				icObj.stopReadThread();
			}
			WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.ICSWIPING, strOnIC));
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