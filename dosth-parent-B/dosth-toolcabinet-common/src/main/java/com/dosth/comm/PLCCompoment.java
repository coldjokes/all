package com.dosth.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.plc.PLCDoorStatsReader;
import com.dosth.comm.plc.PLCStatusReader;
import com.dosth.comm.plc.PLCUtil;
import com.dosth.comm.plc.ProcessPLCStatus;
import com.dosth.comm.softhand.SoftHandComm;
import com.dosth.comm.softhand.SoftHandCommMsg;
import com.dosth.constant.EnumDoor;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

public class PLCCompoment extends Compoment implements ProcessPLCStatus {

	private static final Logger logger = LoggerFactory.getLogger(PLCCompoment.class);
	private String ip;
	private String cabinetId;
	private static final int slave_id = 1;
	private String currMsg = "";
	private int currFloor;
	// 要控制的门的全局变量
	public static EnumDoor door = null;

	public PLCCompoment(Mediator mediator) {
		super(mediator);
	}

	public void setIp(String ip) {
		this.ip = ip;
		PLCUtil.setModbusConfig(this.ip, 502, slave_id);
		logger.info("IP:" + this.ip + " Port:" + 502 + " Slave ID:" + slave_id);
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	@Override
	public synchronized void onLiftArrive() {
		String messageToWeb;
		if (currFloor == 100) {
			messageToWeb = "料斗到达取料口";
		} else {
			messageToWeb = String.format("料斗到达第%s层", currFloor);
		}
		WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));
		PLCUtil.clearArriveFlag();

		super.send(new SoftHandCommMsg(SoftHandComm.LIFT, CompomentUtil.Lift_Response_Arrived));
	}

	// cmd or status received,so will trigger cmd or process status
	@Override
	public synchronized void receive(SoftHandCommMsg msg) {

		super.receive(msg);
		String message = msg.getMessage();
		logger.info("PLC compoment Handle Receive Msg:" + msg.getMessage());
		currMsg = message;

//		Matcher m = CompomentUtil.uiShiftCmdPatt.matcher(message);
//		boolean isMatchUIShiftCmd = m.matches();
//		Matcher n = CompomentUtil.liftMoveCmdPatt.matcher(message);
//		boolean isMatchLiftMoveCmd = n.matches();

//		if (isMatchUIShiftCmd) {
		if (SoftHandComm.SHIFT.equals(msg.getSoftHandComm())) {
			// "Shift,1,2,3" -- message like this
			// Shift,2;192.168.6.112,2-1;192.168.6.102,2-1;
			message = message.substring("Shift,".length());
			String[] paras = message.split(";");
			boolean isStartMove = moveLift(Integer.valueOf(paras[0]));
			if (!isStartMove) {
				super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, CompomentUtil.Lift_Response_Exception));
			}
		} else if (SoftHandComm.LIFT.equals(msg.getSoftHandComm())) {
			// "Lift,5" -- message like this
			String[] paras = message.split(",");
			int row = Integer.parseInt(paras[1]);
			boolean isStartMove = moveLift(row);
			if (!isStartMove) {
				super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, CompomentUtil.Lift_Response_Exception));
			}
		} else if (message.equals(CompomentUtil.Already_Taken_Out_All_Row)) {
			boolean isStartMove = moveLiftToDoor();
			if (!isStartMove) {
				super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, CompomentUtil.Lift_Response_Exception));
			}
		} else if (message.equals(CompomentUtil.Open_Door)) {
			openDoor(door);
		} else {
			WebSocketUtil.sendMsgSingle(this.cabinetId,
					new WsMsg(WsMsgType.TEXT, "PLC receive CMD of incorrect format"));
			System.err.println("MsgType : " + msg.getSoftHandComm().desc());
			System.err.println("Message : " + message);
		}
	}

	private boolean moveLift(int floor) {

		currFloor = floor;
		String messageToWeb = String.format("启动料斗移动至第%s层", currFloor);
		logger.info(messageToWeb);
		WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));

		boolean isPLCConn = PLCUtil.setModbusConfig(ip, 502, slave_id);
		if (!isPLCConn) {
			return false;
		}
		PLCUtil.moveLiftToTargetPosi(floor);
		PLCStatusReader reader = new PLCStatusReader(ip, 502, slave_id, this);
		reader.startRead();

		return true;
	}

	private boolean moveLiftToDoor() {
		currFloor = 100;
		String messageToWeb = "启动料斗移动至取料口";
		logger.info(messageToWeb);
		WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));

		boolean isPLCConn = PLCUtil.setModbusConfig(ip, 502, slave_id);
		if (!isPLCConn) {
			return false;
		}
		PLCUtil.moveLiftToDoorPosi();
		PLCStatusReader reader = new PLCStatusReader(ip, 502, slave_id, this);
		reader.startRead();

		return true;
	}

	public synchronized boolean openDoor(EnumDoor door) {
		String messageToWeb = "开始打开取料口门";
		logger.info(messageToWeb);
		WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));

		boolean isPLCConn = PLCUtil.setModbusConfig(ip, 502, slave_id);
		if (!isPLCConn) {
			return false;
		}
		PLCUtil.onOpenDoor(door);
		PLCDoorStatsReader reader = new PLCDoorStatsReader(ip, 502, slave_id, this, door);
		reader.startRead();
		return true;
	}

	@Override
	public synchronized void onLiftNotArrive() {
		String messageToWeb = "料斗没有在设定时间内到达,超时错误";
		logger.info(messageToWeb);
		WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));
		super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, CompomentUtil.Lift_Response_Exception));
	}

	public synchronized String getCurrMsg() {
		return currMsg;
	}

	@Override
	public synchronized void onScanSignal() {
		PLCUtil.setModbusConfig(ip, 502, slave_id);
		PLCUtil.clearScanFlag();
		String messageToWeb = "PLC发送启动扫描信号";
		logger.info(messageToWeb);
		WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));
		super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, CompomentUtil.Return_Starting_Scan));
	}

	@Override
	public synchronized void onDoorOpened(boolean isOpened) {
		if (isOpened) {
			String messageToWeb = "取料口门已打开";
			WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.DOOROPENED, messageToWeb));
			MP3Player.play("AUDIO_D3.mp3");
			super.send(new SoftHandCommMsg(SoftHandComm.LIFT, CompomentUtil.Succ_To_Open_Door));
		} else {
			String messageToWeb = "打开取料口门失败";
			WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));
			MP3Player.play("AUDIO_D5.mp3");
			super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, CompomentUtil.Fail_To_Open_Door));
		}
	}
}