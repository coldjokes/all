package com.dosth.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.motorboard.ModbusUtil;
import com.dosth.comm.motorboard.ModorResponseReader;
import com.dosth.comm.motorboard.ProcessMotorResponse;
import com.dosth.comm.softhand.SoftHandComm;
import com.dosth.comm.softhand.SoftHandCommMsg;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

public class MotorCompoment extends Compoment implements ProcessMotorResponse {
	public static final Logger logger = LoggerFactory.getLogger(MotorCompoment.class);
	private String cabinetId;
	private int toMoveMotorCount;
	private int movedMotorCount;
	private int currRow;

	public MotorCompoment(Mediator mediator) {
		super(mediator);
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	@Override
	public synchronized void processMotorResponse(int motorIndex, int motorResponse) {
		if (motorResponse == -1) {
			// no output from cabinet,fail
			logger.info("row " + currRow + " column " + motorIndex + " Motor "
					+ "No response returned,maybe exception or timeout occur");

			String msg = String.format("%s,%s,%s", currRow, motorIndex, CompomentUtil.Motor_Response_Exception);

			String messageToWeb = String.format("取第%s层%s列刀具失败", currRow, motorIndex);
			WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));

			super.send(new SoftHandCommMsg(SoftHandComm.LIFT, msg));
			logger.info("MotorCompoment.processMotorResponse()," + messageToWeb);
			return; // directly return if exception occur
		} else {
			// success,cabinet output
			String statusStr = ModbusUtil.parseStatusData(motorResponse);
			logger.info("row " + currRow + " column " + motorIndex + " Motor Status Description " + statusStr);

			if (motorResponse != 0) {
				String msg = String.format("%s,%s,%s", currRow, motorIndex, CompomentUtil.Motor_Response_Exception);
				String messageToWeb = String.format("取第%s层%s列刀具失败,原因:%s", currRow, motorIndex, statusStr);
				WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));
				super.send(new SoftHandCommMsg(SoftHandComm.SHIFT, msg));
				logger.info("MotorCompoment.processMotorResponse()," + messageToWeb);
				return; // directly return if exception occur
			} else {
				String messageToWeb = String.format("第%s层,%s/%s完成取出操作", currRow, movedMotorCount + 1, toMoveMotorCount);
				WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));
				logger.info("MotorCompoment.processMotorResponse()," + messageToWeb);
			}
		}

		movedMotorCount++; // count taken out

		if (movedMotorCount == toMoveMotorCount) {

			// already take out all the objects
			logger.info("No." + currRow + " floor" + " to take out total count " + toMoveMotorCount
					+ ",already taken out " + movedMotorCount);

			String messageToWeb = String.format("第%s层已完成所有取出操作", currRow);
			WebSocketUtil.sendMsgSingle(this.cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));

			super.send(new SoftHandCommMsg(SoftHandComm.SHIFT, CompomentUtil.All_Motors_On_Board_Work_Done));
			logger.warn("MotorCompoment.processMotorResponse()," + messageToWeb);
		}
	}

	// cmd or status received,so will trigger cmd or process status
	public synchronized void receive(SoftHandCommMsg msg) {
		super.receive(msg);
		String message = msg.getMessage();
		logger.info("Motor Board Compoment Handle Receive Msg:" + message);

		movedMotorCount = 0;
		toMoveMotorCount = 0;
		// like this "Shift,1,2-6,3-6,5-6,8-6,10-6" -- message like this,motorMove
//		Shift,1;192.168.6.111,2-1;192.168.6.101,2-1;
//		Matcher m = CompomentUtil.uiShiftCmdPatt.matcher(message);
//		boolean isMatch = m.matches();
//		if (true) {
		message = message.substring("Shift,".length());
		String[] paras = message.split(";");
		int row = Integer.valueOf(paras[0]);
		currRow = row;
		if (paras.length == 2) {
			toMoveMotorCount = paras[1].split(",").length - 1;
		} else {
//			toMoveMotorCount = paras.length - 1;
			// 遍历同行取料种类数量
			for (int i = 1; i < paras.length; i++) {
				toMoveMotorCount += paras[i].split(",").length - 1;
			}
		}
		String ip = null;
		String[] colNum = null;
		for (int i = 1; i <paras.length; i++) {
			ip = paras[i].split(",")[0];
			if (ip == null || "".equals(ip)) {
				String m = String.format("%s,%s", currRow, CompomentUtil.Motor_Board_NO_IP_MATCH_ROW_Exception);
				super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, m));
				logger.info("MotorCompoment.receive()," + m);
				continue;
			}
			logger.info("MotorCompoment.receive(),PLC Conn " + ip);
			boolean isPLCConn = ModbusUtil.setModbusConfig(ip, 502, ModbusUtil.SLAVE_ID);
			if (!isPLCConn) {
				String m = String.format("%s,%s", currRow, CompomentUtil.Motor_Board_PLC_Conn_Exception);
				super.send(new SoftHandCommMsg(SoftHandComm.MOTORERR, m));
				logger.info("Enter MotorCompoment.receive()," + m);
				continue;
			}
			for (int j = 1; j < paras[i].split(",").length; j++) {
				colNum = paras[i].split(",")[j].split("-");
				ModbusUtil.motorMove(Integer.valueOf(colNum[0]), Integer.valueOf(colNum[1]));
				ModorResponseReader reader = new ModorResponseReader(ip, 502, ModbusUtil.SLAVE_ID, Integer.valueOf(colNum[0]), this);
				reader.startRead();
			}
		}
//		}
	}
}