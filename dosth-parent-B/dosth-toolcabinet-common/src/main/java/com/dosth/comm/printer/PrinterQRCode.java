package com.dosth.comm.printer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class PrinterQRCode extends PrintObject {

	private int sizeIndex = 5;
	public int offset4Center = 60;

	public PrinterQRCode(int x, int y, Dimension dimension, List<String> strList) {
		super(x, y, dimension, strList);
		// 1A 31 00 05 02 00 00 15 00 05 00 66 64 73 66 73 64 66 73 61 65 00
		// “1A 31 00”二维码打印，“05 02”固定，“00 00 15 00”打印起始位置，“05”二维码大小，大小范围01-08，“66 64 73
		// 66 73 64 66 73 61 65”二维码内容“fdsfsdfsae”
		// 1A 31 00 05 02 00 00 15 00 05 00 66 64 73 66 73 64 66 73 61 65 00
		command[0] = 0x1A;
		command[1] = 0x31;
		command[2] = 0x00;
	}

	public PrinterQRCode(List<String> strList) {
		super(strList);
		command[0] = 0x1A;
		command[1] = 0x31;
		command[2] = 0x00;
		height = 25;
		dimension.setSize(dimension.getWidth(), height);
	}

	// byte array section
	public List<Byte> getCommandByteArray() { // index 1
		List<Byte> commandArray = new ArrayList<>();
		for (byte b : command) {
			commandArray.add(new Byte(b));
		}
		return commandArray;
	}

	public List<Byte> getVersionAndECCByteArray() { // index 2
		List<Byte> stuff = new ArrayList<>();
		byte[] array = { 0x05, 0x02 };

		for (byte b : array) {
			stuff.add(new Byte(b));
		}
		return stuff;
	}

	public List<Byte> getStartPos() { // index 3
		List<Byte> pos = new ArrayList<>();
		byte[] xArray = PrinterUtil.intToByte(x);
		byte[] yArray = PrinterUtil.intToByte(y);
		for (byte x : xArray) {
			pos.add(new Byte(x));
		}
		for (byte y : yArray) {
			pos.add(new Byte(y));
		}
		return pos;
	}

	public void setQRSize(int size) {
		sizeIndex = size;
	}

	public List<Byte> getQRSize() { // index four
		// 大小范围01-08
		List<Byte> size = new ArrayList<>();
		byte[] array = null;
		byte[] array1 = { 0x01, 0x00 };
		byte[] array2 = { 0x02, 0x00 };
		byte[] array3 = { 0x03, 0x00 };
		byte[] array4 = { 0x04, 0x00 };
		byte[] array5 = { 0x05, 0x00 };
		byte[] array6 = { 0x06, 0x00 };
		byte[] array7 = { 0x07, 0x00 };
		byte[] array8 = { 0x08, 0x00 };
		switch (sizeIndex) {
		case 1:
			array = array1;
			break;
		case 2:
			array = array2;
			break;
		case 3:
			array = array3;
			offset4Center = 35;
			break;
		case 4:
			array = array4;
			break;
		case 5:
			array = array5;
			break;
		case 6:
			array = array6;
			break;
		case 7:
			array = array7;
			break;
		case 8:
			array = array8;
			break;
		}
		for (byte b : array) {
			size.add(new Byte(b));
		}
		return size;
	}

	public List<Byte> getString() { // index 5
		List<Byte> strByteList = new ArrayList<>();
		List<String> strContent = getStringList();
		for (String str : strContent) {
			strByteList.addAll(PrinterUtil.array2List(PrinterUtil.getStrGBKCode(str)));
		}
		return strByteList;
	}

	public List<Byte> getEndMark() {
		List<Byte> tail = new ArrayList<>();
		byte[] array = { 0x00 };
		for (byte b : array) {
			tail.add(new Byte(b));
		}
		return tail;
	}

	public byte[] getPrinterIOData() {
		List<Byte> byteArray = new ArrayList<>();
		byteArray.addAll(getCommandByteArray()); // command
		byteArray.addAll(getVersionAndECCByteArray()); // version and ECC stuff
		byteArray.addAll(getStartPos()); // start pos
		byteArray.addAll(getQRSize()); // QR size
		byteArray.addAll(getString()); // string encode
		byteArray.addAll(getEndMark()); // end mark
		Byte[] data1 = (Byte[]) byteArray.toArray(new Byte[] {});
		int len = data1.length;
		byte[] data = new byte[len];
		for (int i = 0; i < len; i++) {
			data[i] = data1[i].byteValue();
		}
		return data;
	}
}