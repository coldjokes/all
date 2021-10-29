package com.dosth.comm.printer;

import java.awt.Dimension;
import java.awt.Insets;

public class VertiLayout implements PageLayoutManager {

	// these para only for QR object
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	public static final int LEADING = 3;
	public static final int TRAILING = 4;

	int align;
	int hgap;
	int vgap;

	public VertiLayout() {
		this(LEFT, 5, 5);
	}

	public VertiLayout(int align) {
		this(align, 5, 5);
	}

	public VertiLayout(int align, int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
		setAlignment(align);
	}

	public void setAlignment(int align) {
		this.align = align;
	}

	/**
	 * Lays out the container. This method lets each <i>visible</i> component take
	 * its preferred size by reshaping the components in the target container in
	 * order to satisfy the alignment of this <code>FlowLayout</code> object.
	 * 
	 * @param target the specified component being laid out
	 */
	public void layoutContainer(PrintPage container) {
		Insets insets = container.getInsets();
		int maxwidth = container.width - (insets.left + insets.right + hgap * 2);
		int maxheight = container.height - (insets.top + insets.bottom + vgap * 2);
		int nmembers = container.getComponentCount();
		int x = insets.left, y = insets.top;
		PrintObject printObj;
		for (int i = 0; i < nmembers; i++) {
			printObj = container.getComponent(i);
			if (printObj.isVisible()) {
				if (align == LEFT) {
					double rate = container.getCompoRate(i);
					int hei_rated = (int) (Math.floor(maxheight * rate));
					printObj.setSize(maxwidth, hei_rated);
					moveComponents(container, printObj, x, y);
					y = y + hei_rated + vgap;
				} else if (align == CENTER) {
					double rate = container.getCompoRate(i);
					int hei_rated = (int) (Math.floor(maxheight * rate));
					printObj.setSize(maxwidth, hei_rated);
					if (printObj instanceof PrinterQRCode) {
						PrinterQRCode obj = (PrinterQRCode) printObj;
						moveComponents(container, printObj, maxwidth / 2 - obj.offset4Center, y);
					} else {
						moveComponents(container, printObj, x, y);
					}
					y = y + hei_rated + vgap;
				} else if (align == RIGHT) {
					double rate = container.getCompoRate(i);
					int hei_rated = (int) (Math.floor(maxheight * rate));
					printObj.setSize(maxwidth, hei_rated);
					if (printObj instanceof PrinterQRCode) {
						PrinterQRCode obj = (PrinterQRCode) printObj;
						moveComponents(container, printObj, maxwidth / 2 + obj.offset4Center, y);
					} else {
						moveComponents(container, printObj, x, y);
					}
					y = y + hei_rated + vgap;
				}
			}
		}
	}

	public void moveComponents(PrintPage container, PrintObject target, int x, int y) {
		target.setLeftTopPoint(x, y);
	}

	@Override
	public void addLayoutComponent(String name, PrintObject comp) {
	}

	@Override
	public void removeLayoutComponent(PrintObject comp) {
	}

	@Override
	public Dimension preferredLayoutSize(PrintPage parent) {
		return null;
	}

	@Override
	public Dimension minimumLayoutSize(PrintPage parent) {
		return null;
	}
}