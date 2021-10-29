package com.dosth.toolcabinet.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.printer.DpPrinter;
import com.cnbaosi.printer.dto.PrintInfo;
import com.cnbaosi.printer.dto.PrintTextInfo;
import com.cnbaosi.printer.dto.QrInfo;
import com.cnbaosi.printer.dto.Txt;
import com.cnbaosi.printer.enums.FontSize;
import com.cnbaosi.printer.enums.FontSpec;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.enums.TestWorkType;
import com.dosth.toolcabinet.service.TestWorkService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.OpTip;

/**
 * @description 测试工作接口实现
 * @author guozhidong
 *
 */
@Service
public class TestWorkServiceImpl implements TestWorkService {

	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private ToolService toolService;
	
	@Override
	public OpTip testWork(TestWorkType testWork) {
		OpTip opTip = new OpTip(testWork.getDesc() + "测试成功");
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(), SetupKey.Cabinet.MAIN_CABINET_ID);
		switch (testWork) {
		case IC:
			break;
		case PRINT:
			try {
				List<PrintTextInfo> textInfoList = new ArrayList<>();
				Txt txt = new Txt("鲍斯智造");
				txt.setFontSpec(FontSpec.BOLD);
				txt.setFontSize(FontSize.M);
				textInfoList.add(new PrintTextInfo(txt));
				textInfoList.add(new PrintTextInfo(new Txt("智能刀具柜")));
				DpPrinter.put(new PrintInfo(new QrInfo("0512-68152076"), textInfoList));
				// 打印机纸数量-1
				this.toolService.editCount(mainCabinetId);
			} catch (Exception e) {
				e.printStackTrace();
				opTip = new OpTip(201, e.getMessage());
			}
			break;
		case SCAN:
			break;
		default:
			break;
		}
		return opTip;
	}
}