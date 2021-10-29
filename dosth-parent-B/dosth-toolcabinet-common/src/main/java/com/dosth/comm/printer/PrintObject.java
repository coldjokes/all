package com.dosth.comm.printer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class PrintObject implements PrinterIOData {
	
	protected Dimension dimension = new Dimension();
	protected int x,y; //  let top x,y coordinate,on the print page
	private List<String> strList = new ArrayList<>();
	protected byte[] command = new byte[3];
	protected int height = 0;
    public List<Byte> byteArray = new ArrayList<>(); // this print object to IO
	
    boolean isShow = true;
	public PrintObject() {
		
	}
	
	public PrintObject(List<String> strList) {
		
		this.x = 0;
		this.y = 0;
		this.dimension.setSize(0,0);
		this.strList.addAll(strList);
    }

	public PrintObject(int x,int y,Dimension dimension,List<String> strList) {
		
		this.x = x;
		this.y = y;
		this.dimension.setSize(dimension);
		this.strList.addAll(strList);
    }


	public void setSize(Dimension dimension) {
		
		this.dimension.setSize(dimension);
	}
	
	public void setSize(int wid,int hei) {
		
		this.dimension.setSize(wid, hei);
	}
	
	public void setWidth(int wid) {
		
		int hei =  (int)this.dimension.getHeight();
		this.dimension.setSize(wid, hei);
	}
	
	public void setHeight(int hei) {
		
		int wid =  (int)this.dimension.getWidth();
		this.dimension.setSize(wid, hei);
	}
	
	public void setLeftTopPoint(int x,int y) {
		
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		
		this.x = x;
	}
	
    public void setY(int y) {
		
		this.y = y;
	}
    
    public List<Byte> getByteArray(){
    	
    	return byteArray;
    }
    
    public boolean isVisible() {
    	
    	return isShow;
    }
    
    public Dimension getPreferredSize() {
    	
    	return dimension;
    }
    
    public byte[] getPrinterIOData(){
    	
    	byte[] data = {};
    	return data;
    }
    
    public List<String> getStringList(){
    	
    	return strList;
    }
}
