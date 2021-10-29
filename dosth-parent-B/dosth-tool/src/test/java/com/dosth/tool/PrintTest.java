package com.dosth.tool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dosth.comm.NotASerialPort;
import com.dosth.comm.SendDataToSerialPortFailure;
import com.dosth.comm.SerialPortOutputStreamCloseFailure;
import com.dosth.comm.SerialPortParameterFailure;
import com.dosth.comm.printer.PrinterUtil;
import com.dosth.common.util.DateUtil;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.enums.PrintInfoType;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;

public class PrintTest extends AppTest {

	public static void main(String[] args) {
		List<ReturnBackPrintInfo> infoList = new ArrayList<>();
		StringBuilder builder;
		ReturnBackPrintInfo info;
		String pack; // 包装数量
		String back; // 正常归还数量
		String broken; // 损坏数量
		String lose; // 遗失数量
		String returnTypeId = null;
		String isGetNewOne = "0";
		info = new ReturnBackPrintInfo();
		pack = "10";
		returnTypeId = "报废加修磨";
		back = pack;
		broken = "0";
		lose = "0";
		// 拼接二维码信息
		builder = new StringBuilder("123");
		builder.append(";");
		builder.append("报废");
		builder.append(";");
		builder.append(back);
		builder.append(";");
		builder.append(broken);
		builder.append(";");
		builder.append(lose);
		builder.append(";");
		builder.append("1");
		builder.append(";");
		builder.append(isGetNewOne);
		builder.append(";");
		builder.append("2223323dzfdsfdsf");
		info.getInfoMap().put(PrintInfoType.QRINFO, builder.toString());
		// 刀具名称
		info.getInfoMap().put(PrintInfoType.MATNAME, "铰刀");
		// 型号
		info.getInfoMap().put(PrintInfoType.MATSPEC, "NN-MGX33329 EXYHSS78");
		// 换料人
		info.getInfoMap().put(PrintInfoType.USERNAME, "admin[Admin]");
		// 打印日期
		info.getInfoMap().put(PrintInfoType.PRINTTIME, DateUtil.getDay(new Date()));
		// 还料类型
		info.getInfoMap().put(PrintInfoType.RETURNBACKTYPENAME, returnTypeId);
		// 包装数量
		info.getInfoMap().put(PrintInfoType.PACKNUM, pack);
		// 正常归还
		info.getInfoMap().put(PrintInfoType.BACKNUM, back);
		// 损坏数量
		info.getInfoMap().put(PrintInfoType.BROKENNUM, broken);
		// 遗失数量
		info.getInfoMap().put(PrintInfoType.LOSENUM, lose);
		// 以旧换新标识
		info.getInfoMap().put(PrintInfoType.ISGETNEWONE, isGetNewOne != null && "1".equals(isGetNewOne) ? "是" : "否");
		infoList.add(info);

		List<String> qrList = new ArrayList<>();
		List<String> textList = new ArrayList<>();

		qrList.add(info.getInfoMap().get(PrintInfoType.QRINFO));
		textList = new ArrayList<>();
		for (PrintInfoType type : PrintInfoType.values()) {
			if (PrintInfoType.QRINFO.equals(type)) {
				continue;
			}
			textList.add(type.getDesc() + ":" + info.getInfoMap().get(type));
		}
		try {
			new PrinterUtil("COM6").print(qrList, textList);
		} catch (NoSuchPortException | PortInUseException | SerialPortParameterFailure | NotASerialPort
				| SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		}
	}
}