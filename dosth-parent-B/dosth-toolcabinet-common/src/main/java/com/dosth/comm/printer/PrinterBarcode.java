package com.dosth.comm.printer;

import java.awt.Dimension;
import java.util.List;

public class PrinterBarcode extends PrintObject {
	
	public PrinterBarcode(int x,int y,Dimension dimension,List<String> strList) {
		
    	super(x,y,dimension,strList);
    	height = 15;
    }
	
	public PrinterBarcode(List<String> strList) {
		
    	super(strList);
//    	command[0] = 0x1A;
//    	command[1] = 0x54;
//    	command[2] = 0x01;
    	height = 15;
    	dimension.setSize(dimension.getWidth(), height);
    }
	
	public byte[] getPrinterIOData(){
    	
    	byte[] data = {};
    	return data;
    }

}
