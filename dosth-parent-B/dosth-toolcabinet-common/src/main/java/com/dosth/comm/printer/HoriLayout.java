package com.dosth.comm.printer;

import java.awt.Dimension;
import java.awt.Insets;

public class HoriLayout implements PageLayoutManager {

	/**
	 * This value indicates that each row of components should be left-justified.
	 */
	public static final int LEFT = 0;

	/**
	 * This value indicates that each row of components should be centered.
	 */
	public static final int CENTER = 1;

	/**
	 * This value indicates that each row of components should be right-justified.
	 */
	public static final int RIGHT = 2;

	/**
	 * This value indicates that each row of components should be justified to the
	 * leading edge of the container's orientation, for example, to the left in
	 * left-to-right orientations.
	 */
	public static final int LEADING = 3;

	/**
	 * This value indicates that each row of components should be justified to the
	 * trailing edge of the container's orientation, for example, to the right in
	 * left-to-right orientations.
	 * 
	 * @since 1.2
	 */
	public static final int TRAILING = 4;

	int align;
	int hgap;
	int vgap;

	public HoriLayout() {
		this(CENTER, 5, 5);
	}

	/**
	 * Constructs a new <code>FlowLayout</code> with the specified alignment and a
	 * default 5-unit horizontal and vertical gap. The value of the alignment
	 * argument must be one of <code>FlowLayout.LEFT</code>,
	 * <code>FlowLayout.RIGHT</code>, <code>FlowLayout.CENTER</code>,
	 * <code>FlowLayout.LEADING</code>, or <code>FlowLayout.TRAILING</code>.
	 * 
	 * @param align the alignment value
	 */
	public HoriLayout(int align) {
		this(align, 5, 5);
	}

	/**
	 * Creates a new flow layout manager with the indicated alignment and the
	 * indicated horizontal and vertical gaps.
	 * <p>
	 * The value of the alignment argument must be one of
	 * <code>FlowLayout.LEFT</code>, <code>FlowLayout.RIGHT</code>,
	 * <code>FlowLayout.CENTER</code>, <code>FlowLayout.LEADING</code>, or
	 * <code>FlowLayout.TRAILING</code>.
	 * 
	 * @param align the alignment value
	 * @param hgap  the horizontal gap between components and between the components
	 *              and the borders of the <code>Container</code>
	 * @param vgap  the vertical gap between components and between the components
	 *              and the borders of the <code>Container</code>
	 */
	public HoriLayout(int align, int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
		setAlignment(align);
	}

	/**
	 *
	 * @param name the string to be associated with the component
	 * @param comp the component to be added
	 */
	public void addLayoutComponent(String name, PrintObject comp) {
	}

	/**
	 * Removes the specified component from the layout.
	 * 
	 * @param comp the component to be removed
	 */
	public void removeLayoutComponent(PrintObject comp) {
	}

	/**
	 * Calculates the preferred size dimensions for the specified container, given
	 * the components it contains.
	 * 
	 * @param parent the container to be laid out
	 * @see #minimumLayoutSize
	 */
	public Dimension preferredLayoutSize(PrintPage parent) {
		return new Dimension();
	}

	/**
	 * Calculates the minimum size dimensions for the specified container, given the
	 * components it contains.
	 * 
	 * @param parent the component to be laid out
	 * @see #preferredLayoutSize
	 */
	public Dimension minimumLayoutSize(PrintPage parent) {
		return new Dimension();
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
				double rate = container.getCompoRate(i);
				int wid_rated = (int) (Math.floor(maxwidth * rate));
				printObj.setSize(wid_rated, maxheight);
				moveComponents(container, printObj, x, y);
				x = x + wid_rated + hgap;
			}
		}
	}

	public void setAlignment(int align) {
		this.align = align;
	}

	public void moveComponents(PrintPage container, PrintObject target, int x, int y) {
		target.setLeftTopPoint(x, y);
	}
}