package com.elvis.visualfsm.model;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 15.03.14
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
public class FragmentClassGraphVertex extends DefaultGraphCell {
    static int shift = 0;

    public FragmentClassGraphVertex(Object o) {
        super(o);
        GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(100 + shift, 100, 40, 40));
        shift += 50;
        GraphConstants.setGradientColor(getAttributes(), Color.GREEN);
        GraphConstants.setOpaque(getAttributes(), true);
        GraphConstants.setBorderColor(getAttributes(), Color.black);
        addPort();
    }
}
