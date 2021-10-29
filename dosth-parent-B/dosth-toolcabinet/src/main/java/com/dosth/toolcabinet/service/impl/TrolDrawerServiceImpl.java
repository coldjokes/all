package com.dosth.toolcabinet.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.ConsumingDetail;
import com.cnbaosi.dto.Message;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.spring.TrolDrawerCabinet;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.service.TrolDrawerService;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description 可控抽屉Service实现
 * @author Zhidong.Guo
 *
 */
@Service
public class TrolDrawerServiceImpl implements TrolDrawerService {
	
	private static final Logger logger = LoggerFactory.getLogger(TrolDrawerServiceImpl.class);

	@Override
	public void openTrol(Byte boardNo, int drawerNo, int openNum) {
		StorageMedium storageMedium = new TrolDrawerCabinet(boardNo, drawerNo, openNum) {
			@Override
			public void receiveMessage(Message message) throws BscException {
				switch (message.getType()) {
				case BUSY:
				case TIME_OUT:
					WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.WARN_TIP, message.getCustomMsg()));
					break;
				case NO_CLOSE:
					WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.TROL_DRAWER_AGAIN, message.getCustomMsg()));
					break;
				case OPEN_SUCC:
					WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
					break;
				default:
					break;
				}
			}
			
			@Override
			public void notice(ConsumingDetail detail) {
				logger.info(detail.toString());
			}
		};
		StorageMediumPicker.putStorageMedium(storageMedium);
	}
}