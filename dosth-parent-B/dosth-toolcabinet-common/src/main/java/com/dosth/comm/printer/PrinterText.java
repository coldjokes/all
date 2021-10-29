package com.dosth.comm.printer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class PrinterText extends PrintObject {
	
	public PrinterText(int x,int y,Dimension dimension,List<String> strList) {
		
    	super(x,y,dimension,strList);
    	// 1A 54 01 c5 00 15 00 00 60 00 11 4D 43 20 31 30 30 31 00
		// “1A 54 01”打印文本，“c5 00 15 00” 打印起始位置，“00 60 00 11”11正常字体大小，22字体放一倍
		// “4D 43 20 31 30 30 31”打印内容“MC 1001”

    	command[0] = 0x1A;
    	command[1] = 0x54;
    	command[2] = 0x01;

    }

	public PrinterText(List<String> strList) {
		
    	super(strList);
    	command[0] = 0x1A;
    	command[1] = 0x54;
    	command[2] = 0x01;
    	height = 0x1B;
    	dimension.setSize(dimension.getWidth(), height);
    }
	
	// byte array section
 	public List<Byte> getCommandByteArray(){  // index 1
 		
 		List<Byte> commandArray = new ArrayList<>();
 
 		for (byte b : command){
 			commandArray.add(new Byte(b));
 		}
 		
 		return commandArray;
 	}
 	
 	public List<Byte> getStartPos(int xcor,int ycor){  // index 2
		
		List<Byte> pos = new ArrayList<>();
		byte[] xArray= PrinterUtil.intToByte(xcor); 
		byte[] yArray= PrinterUtil.intToByte(ycor); 
		
		for (byte x : xArray){
			pos.add(new Byte(x));
		}
		
		for (byte y : yArray){
			pos.add(new Byte(y));
		}
		
		return pos;
	}
 	
 	public List<Byte> getFontSize(){  // index 3
 		
 		List<Byte> stuff = new ArrayList<>();
 		// 		“00 60 00 11”     11正常字体大小，22字体放一倍
		byte[] array= {0x00,0x60,0x00,0x11};
		
		for (byte b : array){
			stuff.add(new Byte(b));
		}
		
		return stuff;
 	}
 	
 	public List<Byte> getString(String str){  // index 4
 		
 		List<Byte> strByteList = new ArrayList<>();
 		
 		strByteList.addAll(PrinterUtil.array2List(PrinterUtil.getStrGBKCode(str)));
 		
		return strByteList;
 	}
 	
 	public List<Byte> getEndMark() {    // index 5
		List<Byte> tail = new ArrayList<>();
		byte[] array= {0x00};
		
		for (byte b : array){
			tail.add(new Byte(b));
		}
		
		return tail;	
	}
 	
 	public List<Byte> getStrLineBytes(String line,int x,int y){
 		
 		List<Byte> lineByte = new ArrayList<>();

 		lineByte.addAll(getCommandByteArray());
 		lineByte.addAll(getStartPos(x,y));
 		lineByte.addAll(getFontSize());
 		lineByte.addAll(getString(line));
 		lineByte.addAll(getEndMark());
 		
 		return lineByte; 
 	}
 	
 	public List<Byte> getAllLinesBytes(){
 		
 		List<Byte> sectionByte = new ArrayList<>();

 		List<String> section = getStringList();
 		int len = section.size();
 		for(int i=0;i<len;i++){
 			
 			String lineStr = section.get(i);
 			int yPos = this.y+height*i;
 			sectionByte.addAll(getStrLineBytes(lineStr,this.x,yPos));
 		}
 		
 		return sectionByte;
 	}
 	
 	public byte[] getPrinterIOData(){
	  	
    	List<Byte> byteArray = getAllLinesBytes();	
    	
    	Byte[] data1 = (Byte[]) byteArray.toArray(new Byte[] {});
    	int len = data1.length;
    	byte[] data  = new byte[len];
    	for(int i=0;i<len;i++) {
    		
    		data[i] = data1[i].byteValue();
    	}
    	return data;
    }
}
