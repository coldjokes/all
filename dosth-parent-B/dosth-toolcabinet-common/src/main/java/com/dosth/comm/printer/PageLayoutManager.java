package com.dosth.comm.printer;

import java.awt.Dimension;

public interface PageLayoutManager {

	/**
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    void addLayoutComponent(String name, PrintObject comp);

    /**
     * Removes the specified component from the layout.
     * @param comp the component to be removed
     */
    void removeLayoutComponent(PrintObject comp);

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     * @param parent the container to be laid out
     * @see #minimumLayoutSize
     */
    Dimension preferredLayoutSize(PrintPage parent);

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     * @param parent the component to be laid out
     * @see #preferredLayoutSize
     */
    Dimension minimumLayoutSize(PrintPage parent);

    /**
     * Lays out the specified container.
     * @param parent the container to be laid out
     */
    void layoutContainer(PrintPage parent);
    
    public void setAlignment(int align);
}
