package com.dosth.toolcabinet.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.exception.BscException;
import com.cnbaosi.printer.DpPrinter;
import com.cnbaosi.printer.dto.Label;
import com.cnbaosi.printer.dto.PrintInfo;
import com.cnbaosi.printer.dto.PrintTextInfo;
import com.cnbaosi.printer.dto.QrInfo;
import com.cnbaosi.printer.dto.Txt;
import com.cnbaosi.printer.enums.LabelType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.toolcabinet.enums.PrintInfoType;
import com.dosth.toolcabinet.service.ReturnBackService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.OpTip;

/**
 * @description 归还Service实现
 * @author guozhidong
 *
 */
@Service
public class ReturnBackServiceImpl implements ReturnBackService {

	private static final Logger logger = LoggerFactory.getLogger(ReturnBackServiceImpl.class);

	@Autowired
	private ToolService toolService;

	@Override
	public OpTip returnBack(ReturnBackPrintInfo printInfo, String cabinetId, Boolean printFlag) throws BscException {
		String qrInfo = printInfo.getInfoMap().get(PrintInfoType.QRINFO);
		String[] qrInfos = qrInfo.split(";");
		if (qrInfos.length < 4) {
			logger.error("返回异常二维码数据:" + qrInfo);
			return new OpTip(201, "返回异常二维码数据");
		}
		OpTip tip = new OpTip(200, qrInfos[0]);
		String accountId = qrInfos[1];
		Boolean isGetNewOne = "1".equals(qrInfos[2]);
		String matId = qrInfos[3];
		if (isGetNewOne != null && isGetNewOne) { // 以旧换新则验证领料限额
			UserInfo userInfo = this.toolService.getUserInfoByAccountId(accountId);
			tip = this.toolService.getDailyLimit(accountId, matId, 0, userInfo);
			if (tip.getCode() != 200) {
				return tip;
			}
			tip = this.toolService.curNumCheck(matId, 1, cabinetId);
			if (tip.getCode() != 200) {
				return tip;
			}
		}
		// 判断是否打印归还
		if(!printFlag) {
			return tip;
		}
		try {
			String printComm = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.PRINT_COM);
			if (printComm != null && !"".equals(printComm)) { // 打印机
				List<PrintTextInfo> textInfoList = new ArrayList<>();
//				textInfoList.add(new PrintTextInfo(new Label(LabelType.USERNAME), new Txt(printInfo.getInfoMap().get(PrintInfoType.USERNAME))));
//				textInfoList.add(new PrintTextInfo(new Label(LabelType.PACKBACKNUM), new Txt(printInfo.getInfoMap().get(PrintInfoType.BACKNUM))));
//				textInfoList.add(new PrintTextInfo(new Label(LabelType.RETURNBACKINFO), new Txt(printInfo.getInfoMap().get(PrintInfoType.RETURNBACKINFO))));
//				
//				textInfoList.add(new PrintTextInfo(new Label(LabelType.MATNAME), new Txt(printInfo.getInfoMap().get(PrintInfoType.MATNAME))));
//				textInfoList.add(new PrintTextInfo(new Label(LabelType.BARCODE), new Txt(printInfo.getInfoMap().get(PrintInfoType.BARCODE))));
//				textInfoList.add(new PrintTextInfo(new Label(LabelType.MATSPEC), new Txt(printInfo.getInfoMap().get(PrintInfoType.MATSPEC))));
//				textInfoList.add(new PrintTextInfo(new Label(LabelType.PRINTTIME), new Txt(printInfo.getInfoMap().get(PrintInfoType.PRINTTIME))));
				textInfoList.add(new PrintTextInfo(new Label(LabelType.BACKNO), new Txt(printInfo.getInfoMap().get(PrintInfoType.RETURNBACKNO))));
				textInfoList.add(new PrintTextInfo(new Label(LabelType.USERNAME), new Txt(printInfo.getInfoMap().get(PrintInfoType.USERNAME))));
				textInfoList.add(new PrintTextInfo(new Label(LabelType.PRINTTIME), new Txt(printInfo.getInfoMap().get(PrintInfoType.PRINTTIME))));
				
				// 归还信息拆分
				String[] returnBackInfos = printInfo.getInfoMap().get(PrintInfoType.RETURNBACKINFO).split(";");
				for (String returnBackInfo : returnBackInfos) {
					textInfoList.add(new PrintTextInfo(new Label(LabelType.RETURNBACKINFO), new Txt(returnBackInfo)));
				}
				textInfoList.add(new PrintTextInfo(new Label(LabelType.ISGETNEWONE), new Txt(printInfo.getInfoMap().get(PrintInfoType.ISGETNEWONE) != null
						&& "1".equals(printInfo.getInfoMap().get(PrintInfoType.ISGETNEWONE)) ? "以旧换新:是" : "以旧换新:否")));
				
				textInfoList.add(new PrintTextInfo(new Label(LabelType.MATNAME), new Txt(printInfo.getInfoMap().get(PrintInfoType.MATNAME))));
				textInfoList.add(new PrintTextInfo(new Label(LabelType.BARCODE), new Txt(printInfo.getInfoMap().get(PrintInfoType.BARCODE))));
				textInfoList.add(new PrintTextInfo(new Label(LabelType.MATSPEC), new Txt(printInfo.getInfoMap().get(PrintInfoType.MATSPEC))));
				
				DpPrinter.put(new PrintInfo(new QrInfo(qrInfos[0]), textInfoList));
			}
			return tip;
			// 条码重用不作处理,扫描仪常开状态
		} catch (Exception e) {
			e.printStackTrace();
			return new OpTip(201, "打印机通讯故障");
		}
	}
}