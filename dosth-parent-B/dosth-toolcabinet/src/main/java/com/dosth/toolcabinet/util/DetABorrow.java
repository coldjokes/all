package com.dosth.toolcabinet.util;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.dosth.comm.audio.MP3Player;
import com.dosth.dto.Card;
import com.dosth.dto.Lattice;
import com.dosth.enums.SetupKey;
import com.dosth.pojo.Col;
import com.dosth.pojo.Row;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description 行列式取料
 * @author guozhidong
 *
 */
@Component
public class DetABorrow {
	private static final Logger logger = LoggerFactory.getLogger(DetABorrow.class);
	
	@Autowired
	private CabinetConfig cabinetConfig;
	
	/**
	 * @description 
	 * @param cardList 封装行列式板数据
	 */
	public void callMotor(List<Card> cardList)  {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(), SetupKey.Cabinet.MAIN_CABINET_ID);
		DetABorrowUtil detABorrow = new DetABorrowUtil() {
			@Override
			public void receiveMessage(Message message) {
				if(message.getType() != null) {
                    logger.info(message.getType().getDesc());
                    if(message.getType().equals(ReturnMsgType.SERVOR)) {//取料进行
                        WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, "取料进行中，请稍后..."));
                    }else if(message.getType().equals(ReturnMsgType.OPEN_SUCC)) {//开门成功
                        WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, "取料口门已打开"));
                    }else if(message.getType().equals(ReturnMsgType.OPEN_FAIL)) {//开门失败
                        WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, "取料口门打开失败"));
                        MP3Player.play("AUDIO_D5.mp3");
                    }else if(message.getType().equals(ReturnMsgType.CLOSED_FAIL)) {//关门失败
                        WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, "取料口门未归位"));
                    }else if(message.getType().equals(ReturnMsgType.MOTORERR)) {//马达异常
                    	logger.error("故障为Id>>>>>>>>>>>>>>>>>>" + message.getHexString() + ";故障信息>>>>>>>>>>>>" + message.getCustomMsg());
    					try {
    						EquProblemUtil.put(message.getHexString());
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
                        WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, "马达异常"));
                    }
				}
			}
		};
		try {
		//通信参数初始化
		BlockingQueue<Row> queue = new LinkedBlockingQueue<>();
		Row row;
		BlockingQueue<Col> colQueue;
		cardList.sort(new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				return o2.getRowNo() - o1.getRowNo();
			}
		});
		//行列式板数据封装
		for (Card card : cardList) {
			colQueue = new LinkedBlockingQueue<>();
			for (Lattice lattice : card.getLatticeList()) {
				colQueue.put(new Col(lattice.getStaId(), lattice.getColNo(), lattice.getColNo(), lattice.getCurReserve()));
			}
			row = new Row(card.getRowNo(), card.getRowNo(), card.getLevelHeight(), colQueue);
			queue.put(row);
		}
		 
		//启动领取
		detABorrow.setQueue(queue);
		detABorrow.receive(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Det.DET_COM), 
				Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Det.DET_BAUD)));
		detABorrow.startD();
		
	} catch (Exception e1) {
		e1.printStackTrace();
	}}
}