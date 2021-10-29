package com.dosth.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.softhand.SoftHandComm;
import com.dosth.comm.softhand.SoftHandCommMsg;

public class UICompoment extends Compoment implements UIAction {
	
	private static final Logger logger = LoggerFactory.getLogger(UICompoment.class);

	public UICompoment(Mediator mediator) {
		super(mediator);
	}

	@Override
    public synchronized void receive(SoftHandCommMsg msg) {
    	super.receive(msg);
//    	Matcher m = CompomentUtil.motorErrPatt.matcher(message);	
//		boolean isMotorErr = m.matches();
    	String message = msg.getMessage();
		logger.info("接收到消息>>" + message);
		if (message.equals(CompomentUtil.Lift_Arrive_At_Door)) {
			processShiftRes(new ShiftResEvent(true, CompomentUtil.Lift_Arrive_At_Door));
			super.send(new SoftHandCommMsg(SoftHandComm.LIFT, CompomentUtil.Open_Door));
		} else if (message.equals(CompomentUtil.Lift_Response_Exception)) {
			processShiftRes(new ShiftResEvent(false, CompomentUtil.Lift_Response_Exception));
		} else if (SoftHandComm.MOTORERR.equals(msg.getSoftHandComm())) {
			logger.info("UI Receive motor Error Message");
			String[] tokens = message.split(",");
			int row = Integer.parseInt(tokens[0]);
			int column = Integer.parseInt(tokens[1]);
			String errdes = String.format("row %s clolum %s motor error", row, column);
			ShiftResEvent e = new ShiftResEvent(row, column, false, errdes, 1);
			processShiftRes(e);
		} else if (message.equals(CompomentUtil.Succ_To_Open_Door) || message.equals(CompomentUtil.Fail_To_Open_Door)) {
			boolean isOpen = message.equals(CompomentUtil.Succ_To_Open_Door);
			processDoorStas(isOpen);
		}
    }

    /**
	 * @description The function will be called after the goods in the shopping cart are output from cabinet
	 * @param ShiftResEvent 
	 * @return
	 */
	@Override
	public synchronized void processShiftRes(ShiftResEvent e) {
		if (e.isShiftSucc) {
			logger.info(
					"Success to take out all the goods in shopping cart,UI handle the else thing,for example,insert record to database,send res msg to web page");
		} else {
			if (e.res_code == 1) {
				logger.error(
						"Fail to take out all the goods in shopping cart,UI handle Motro Board Error:" + e.msg);
			} else {
				logger.error("Fail to take out all the goods in shopping cart,UI handle Lift Error:" + e.msg);
			}
		}
	}

	@Override
	public synchronized void processDoorStas(boolean isDoorOpened) {
		if (isDoorOpened) {
			logger.info("Success to open door");
		} else {
			logger.error("Fail to open door");
		}
	}
}