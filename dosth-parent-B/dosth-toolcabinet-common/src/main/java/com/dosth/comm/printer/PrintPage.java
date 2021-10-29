package com.dosth.comm.printer;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
public class PrintPage implements PrinterIOData {
	
	/**
     * The PrintObjects on this print page.
     */
    private java.util.List<PrintObject> component = new ArrayList<>();
    private Insets insets = new Insets(20,0,5,5);
    PageLayoutManager layoutMgr;
    public List<Byte> byteArrayPaye = new ArrayList<>(); // this print object to IO
    
    public int width;
    public int height;
    boolean isLeftToRight = true; // Orientation
    String occupyRate = "1:1"; // for example 1:1
    
    public PrintPage() {
    	
    }
    
    public PrintPage(int wid,int hei) {
    	
    	this.width = wid;
    	this.height = hei;
    }

	public PrintObject add(PrintObject comp) {
        addImpl(comp, null, -1);
        return comp;
    }
	
	protected void addImpl(PrintObject comp, Object constraints, int index) {

//            if (index > component.size() || (index < 0 && index != -1)) {
//                throw new IllegalArgumentException(
//                          "illegal component position");
//            }

            //index == -1 means add to the end.
            if (index == -1) {
                component.add(comp);
            } else {
                component.add(index, comp);
            }


            /* Notify the layout manager of the added component. */
            if (layoutMgr != null) {
            	layoutMgr.addLayoutComponent((String)constraints, comp);
            }
    }

	
	public int getComponentCount() {
		
		int count = component.size();
		return count;
		
	}
	
	public PrintObject getComponent(int index) {
		
		PrintObject obj = component.get(index);
		return obj;
	}
	
	public boolean isOrientLeftToRight() {
		
		return isLeftToRight;	
	}
    public void setLayoutManager(PageLayoutManager layoutMgr) {
    	
    	this.layoutMgr = layoutMgr;
    	
    }
    
    // trigger layout calculate
    public void triggerLayout() {
    	
    	if(layoutMgr!=null) {
    		
    		layoutMgr.layoutContainer(this);
    		
    	}
    }
    
    public void setInsetsTop(int s) {
        insets.set(s, insets.left, insets.bottom, insets.right);;
    }
    
    public void setInsetsLeft(int s) {
        insets.set(insets.top, s, insets.bottom, insets.right);;
    }
    
	public Insets getInsets() {
        return insets;
    }
	
	public List<Byte> getPageByteArray(){
    	
    	return byteArrayPaye;
    }
	
	public void setOccupyRate(String str) {
		
		this.occupyRate = str;
	}
	
	/*
	 * @param str,the format like this 1:1
	 */
	public void setCompoRate(String str) {
		
		this.occupyRate = str;
	}
	
	public double getCompoRate(int index) {
		
		String[] ratesStr = occupyRate.split(":");
		double[] rates = new double[ratesStr.length];
		double sum = 0;
		for(int i=0;i<ratesStr.length;i++) {
			
			double val = Double.parseDouble(ratesStr[i]);
			rates[i] = val;
			sum = sum+val;
		}
		
		double rate = (double)rates[index]/sum;
		return rate;
	}
	
	// byte array section
	public List<Byte> getTagCommand(){  // index 1.1
		
		//"1A 5B 01" 00 00 00 00 80 01 FA 00 00
		
		List<Byte> command = new ArrayList<>();
		byte[] array= {0x1A,0x5B,0x01};
		
		for (byte b : array){
			command.add(new Byte(b));
		}
		
		return command;
	}
	
	public List<Byte> getTagStartPos(){  // index 1.2
		
		//1A 5B 01 "00 00 00 00" 80 01 FA 00 00
		
		List<Byte> pos = new ArrayList<>();
		byte[] array= {0x00,0x00,0x00,0x00}; // position Point(0,0)
		
		for (byte b : array){
			pos.add(new Byte(b));
		}
		
		return pos;
	}
	
	public List<Byte> getRange(){  // index 1.3
		
		//1A 5B 01 00 00 00 00 "80 01 FA 00" 00
		
		List<Byte> range = new ArrayList<>();
		byte[] widArray= PrinterUtil.intToByte(width); 
		byte[] heiArray= PrinterUtil.intToByte(height); 
		
		for (byte w : widArray){
			range.add(new Byte(w));
		}
		
		for (byte h : heiArray){
			range.add(new Byte(h));
		}
		
		return range;
	}
	
	public List<Byte> getTagHead()
	{
		List<Byte> tagHead = new ArrayList<>();
		
		List<Byte> command = getTagCommand();
		List<Byte> start_pos = getTagStartPos();
		List<Byte> range = getRange();
		byte end = 0x00;
		
		tagHead.addAll(command);
		tagHead.addAll(start_pos);
		tagHead.addAll(range);
		tagHead.add(end);
			
		return tagHead;
		
	}
	
	public List<Byte> getTagTail()
	{
		// 1A 5D 00 1A 4F 00  标签结束指令

		List<Byte> tagTail = new ArrayList<>();
		byte[] array= {0x1A,0x5D,0x00,0x1A, 0x4F, 0x00};
		
		for (byte b : array){
			tagTail.add(new Byte(b));
		}
		
		return tagTail;	
	}
	
	public byte[] getPrinterIOData(){
    	
    	List<Byte> byteArray = new ArrayList<>();
    	
    	byteArray.addAll(getTagHead());  // tag head
    	// Add each print object
    	for(PrintObject obj : component) {
    		
    		byte[] objData = obj.getPrinterIOData();
    		byteArray.addAll(PrinterUtil.array2List(objData));
    	}
    	byteArray.addAll(getTagTail()); // tag tail
    	
    	Byte[] data1 = (Byte[]) byteArray.toArray(new Byte[] {});
    	int len = data1.length;
    	byte[] data  = new byte[len];
    	for(int i=0;i<len;i++) {
    		
    		data[i] = data1[i].byteValue();
    	}
    	return data;
    }
	
}
