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
    private static int shift = 0;

    private String name;

    public FragmentClassGraphVertex(String name) {
        super(name);
        this.name = name;
        GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(100 + shift, 100, 40, 40));
        shift += 50;
        GraphConstants.setGradientColor(getAttributes(), Color.GREEN);
        GraphConstants.setOpaque(getAttributes(), true);
        GraphConstants.setBorderColor(getAttributes(), Color.black);
        addPort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FragmentClassGraphVertex that = (FragmentClassGraphVertex) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }
}
