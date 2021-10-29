package com.dosth.toolcabinet.commu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dosth.comm.Compoment;
import com.dosth.comm.CompomentUtil;
import com.dosth.comm.Mediator;
import com.dosth.comm.MotorCompoment;
//import com.dosth.comm.MotorPara;
import com.dosth.comm.PLCCompoment;
import com.dosth.comm.UICompoment;
import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.softhand.SoftHandComm;
import com.dosth.comm.softhand.SoftHandCommMsg;
import com.dosth.constant.EnumDoor;
import com.dosth.dto.Card;
import com.dosth.dto.Lattice;
import com.dosth.toolcabinet.commu.serialScan.SerialScanCompoment;

//这个类做为界面和各种外设的controller
@Component
public class ConcreteMediator extends Mediator {

    private static final Logger logger = LoggerFactory.getLogger(ConcreteMediator.class);
    
	private PLCCompoment plcCom;
    private MotorCompoment motorCom;
    private UICompoment uiCom;
    private ReturnScanCompoment returnScanCom;
    private AppointmentScanCompoment appScanCom;
    private SerialScanCompoment serialScanCom;
    
    private boolean isOnMotorBoardTaken = false;
    private Stack<String> shiftCmdStack = new Stack<>();
    private volatile boolean isCurrShoppingCartTakenOut = true;
    
    public ConcreteMediator() {
    }
    
    public void setPLCCompoment(PLCCompoment compoment) {
        this.plcCom = compoment;
    }
    
    public void setMotorCompoment(MotorCompoment compoment) {
        this.motorCom = compoment;
    }

    public void setUICompoment(UICompoment compoment) {
        this.uiCom = compoment;
    }
    
    public void setReturnScanCompoment(ReturnScanCompoment compoment) {
        this.returnScanCom = compoment;
    }
    
    public void setAppScanCompoment(AppointmentScanCompoment compoment) {
        this.appScanCom = compoment;
    }
    public void setSerialScanCom(SerialScanCompoment serialScanCom) {
		this.serialScanCom = serialScanCom;
	}

	/**
	 * @description 处理各个组件（界面，外设）间的收发消息
	 */
	public synchronized void send(String message, Compoment compoment) {
		if (compoment == plcCom) { // plc just send "arrive" or "not arrive" message
			if (message.equals(CompomentUtil.Lift_Response_Arrived)) {
				if (isTakenOutAllRows()) {  // to make lift go to door
					setShoppCartTakenOut();
					// send msg to PLC
					uiCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, CompomentUtil.Lift_Arrive_At_Door));
				} else {
					isOnMotorBoardTaken = false;
					motorCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, plcCom.getCurrMsg())); // pass on the "Shift cmd" to motorboard
				}
			} else if (message.equals(CompomentUtil.Lift_Response_Exception)) {
				setShoppCartTakenOut();
				uiCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, CompomentUtil.Lift_Response_Exception));
			} else if (message.equals(CompomentUtil.Succ_To_Open_Door) || message.equals(CompomentUtil.Fail_To_Open_Door)) {
				uiCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, message));
				if (appScanCom != null && !appScanCom.isOnceScanDone()) {
					appScanCom.setOnceScanDoneFlag();
				}
			}
		} else if (compoment == motorCom) {
			Matcher m = CompomentUtil.motorErrPatt.matcher(message);
			boolean isMotorErr = m.matches();
			if (message.equals(CompomentUtil.All_Motors_On_Board_Work_Done)
					|| message.contains(CompomentUtil.Motor_Board_PLC_Conn_Exception)) {
				isOnMotorBoardTaken = true;
				if (isTakenOutAllRows()) {
					plcCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, CompomentUtil.Already_Taken_Out_All_Row));
				} else {
					String msg = getNextShift();
					if (msg != null && !"".equals(msg)) {
						if (msg.startsWith("Shift")) {
							plcCom.receive(new SoftHandCommMsg(SoftHandComm.SHIFT, msg));
						} else {
							plcCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, msg));
						}
					}
				}
			} else if (isMotorErr) {
				logger.info("马达板没有输出全部项");
				setShoppCartTakenOut();
				uiCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, message));
			}
		} else if (compoment == uiCom) {
			if (message.equals(CompomentUtil.Open_Door)) {
				plcCom.receive(new SoftHandCommMsg(SoftHandComm.UNDEFINED, CompomentUtil.Open_Door));
			} else if (message.startsWith(CompomentUtil.Return_Starting_Scan)) {
				int sepIndex = message.indexOf(":");
				String accountID = message.substring(sepIndex + 1);
				if (returnScanCom != null) {
					returnScanCom.setAccountID(accountID);
					returnScanCom.startMonitorScan();
				}
			} else {
				isCurrShoppingCartTakenOut = false;
				plcCom.receive(new SoftHandCommMsg(SoftHandComm.SHIFT, message));
			}
		} else if (compoment == appScanCom) {
			if (message.startsWith(CompomentUtil.Appointment_Cmd)) {
				// Appointment_Cmd:2,3-1;2,5-2;3,6-7
				String appCmdStr = message.substring(message.indexOf(':') + 1);
				fillShiftCmdStack(appCmdStr);
				uiCom.send(new SoftHandCommMsg(SoftHandComm.SHIFT, getNextShift()));
			}
		} else if(compoment == serialScanCom) {
			if (message.startsWith(CompomentUtil.Appointment_Cmd)) {
				// Appointment_Cmd:2,3-1;2,5-2;3,6-7
				fillShiftCmdStack(message);
				uiCom.send(new SoftHandCommMsg(SoftHandComm.UNDEFINED, getNextShift()));
			}
		}
	}
    
    /**
	 * @description 判断每层抽屉都已经取完
	 */
	private boolean isTakenOutAllRows() {
		return shiftCmdStack.empty() && isOnMotorBoardTaken;
	}
    
    /**
	 * @description 获得下一个抽屉要取出的命令描述
	 */
	public synchronized String getNextShift() {
		String message = "";
		if (!shiftCmdStack.empty()) {
			message = (String) shiftCmdStack.pop();
		}
		return message;
	}
	
    /**
	 * @description 获得手机app预约要取出的所有命令
	 */
	public synchronized void fillShiftCmdStack(String appCmdStr) {
		PLCCompoment.door = null;
		shiftCmdStack.clear();
		shiftCmdStack.push(CompomentUtil.Already_Taken_Out_All_Row);
		String[] tokens = appCmdStr.split("&&");
		for (int i = 0; i < tokens.length; i++) {
			String str = tokens[i].toString();
			String[] para = tokens[i].split(",");
			String ip = para[1].substring(para[1].indexOf(';') + 1);
			EnumDoor door = serialScanCom.getDoor(ip);
			if (PLCCompoment.door == null || !PLCCompoment.door.equals(EnumDoor.ALL)) {
    			if (PLCCompoment.door == null) { // 未指定门默认第一个门
    				PLCCompoment.door = door;
    			} else {
    				if (!door.equals(PLCCompoment.door)) { // 要开与当前不一样的门则指定全部门
    					PLCCompoment.door = EnumDoor.ALL;
    				}
    			}
    		}
			String appstr = str.substring(str.indexOf(':') + 1);
			shiftCmdStack.push(appstr);
		}
	}
    /**
	 * @description 获得web界面要取出的所有命令
	 */
    public synchronized void fillShiftCmdStack(List<Card> cardList) {
    	PLCCompoment.door = null;
    	fillShiftCmdStack(this.convert2Map(cardList));
    }

	 /**
	 * @description 获得要取出的所有命令
	 */
	private synchronized void fillShiftCmdStack(Map<String, Map<String, List<Lattice>>> map) {
		shiftCmdStack.clear();
		shiftCmdStack.push(CompomentUtil.Already_Taken_Out_All_Row);
		Map<String, List<Lattice>> ipParaMap;
		List<Lattice> list = null;
		Lattice para = null;
		StringBuilder b;
		for (Entry<String, Map<String, List<Lattice>>> entry : map.entrySet()) {
			ipParaMap = entry.getValue();
			b = new StringBuilder();
			b.append("Shift,");
			b.append(entry.getKey());// 行号
			b.append(";");
			for (Entry<String, List<Lattice>> rowMotor : ipParaMap.entrySet()) {
				b.append(rowMotor.getKey());
				b.append(",");
				list = rowMotor.getValue();
				for (int i = 0; i < list.size(); i++) {
					para = list.get(i);
					b.append(String.format("%s-%s", para.getColNo(), para.getCurReserve()));
					b.append(i == list.size() - 1 ? "" : ","); // 最后一个叠加空,其余的添加逗号
				}
				b.append(";");
			}
			shiftCmdStack.push(b.toString());
		}
	}
    
    /**
     * @description 处理购物车信息为行列对照集合
     * @param cardList
     * @return 行号;马达板IP/列号,数量
     */
    private Map<String, Map<String, List<Lattice>>> convert2Map(List<Card> cardList){
    	Map<String, Map<String, List<Lattice>>> map = new HashMap<>();
    	Map<String, List<Lattice>> ipPara = null;
    	List<Lattice> nParaList = null;
    	Lattice l;
    	for (Card card : cardList) {
    		// 首次或已经设置全部门
    		if (PLCCompoment.door == null || !PLCCompoment.door.equals(EnumDoor.ALL)) {
    			if (PLCCompoment.door == null) { // 未指定门默认第一个门
    				PLCCompoment.door = EnumDoor.valueOf(card.getDoor());
    			} else {
    				if (!card.getDoor().equals(PLCCompoment.door.name())) { // 要开与当前不一样的门则指定全部门
    					PLCCompoment.door = EnumDoor.ALL;
    				}
    			}
    		}
    		// 按行号获取当前行信息
			ipPara = map.get(String.valueOf(card.getRowNo()));
			if (ipPara == null) {
				ipPara = new HashMap<>();
			}
			// 循环列信息
			for (Lattice lattice : card.getLatticeList()) {
				nParaList = ipPara.get(card.getHost());
				if (nParaList == null) {
					nParaList = new ArrayList<>();
				}
				l = new Lattice();
				l.setColNo(lattice.getColNo());
				l.setCurReserve(lattice.getCurReserve());
				nParaList.add(l);
				ipPara.put(card.getHost(), nParaList);
			}
			ipPara.put(card.getHost(), nParaList);
			map.put(String.valueOf(card.getRowNo()), ipPara);
		}
    	return map;
    }
    
    public synchronized boolean isCurrShoppingCartTakenOut() {
    	return isCurrShoppingCartTakenOut;
    }
    
    public synchronized void setShoppCartTakenOut() {
    	isCurrShoppingCartTakenOut = true;
    }
    
	public synchronized void startGetingTheSameOneAsReturned(List<Card> cardList) {
		logger.info("启动以旧换新功能");
		if (isCurrShoppingCartTakenOut()) {
			MP3Player.play("AUDIO_D1.mp3");
			fillShiftCmdStack(cardList);
			uiCom.send(new SoftHandCommMsg(SoftHandComm.SHIFT, getNextShift()));
		} else {
			logger.warn("等待进行中领取");
			MP3Player.play("AUDIO_D2.mp3");
		}
	}
}