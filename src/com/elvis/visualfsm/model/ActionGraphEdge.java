package com.elvis.visualfsm.model;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 16.03.14
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class ActionGraphEdge extends DefaultEdge {
    private String name;

    public ActionGraphEdge(String name) {
        this.name = name;
    }

    public ActionGraphEdge(String name, Object source, Object target) {
        this.name = name;
        setSource(source);
        setTarget(target);
        GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_CLASSIC);
        GraphConstants.setEndFill(getAttributes(), true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionGraphEdge that = (ActionGraphEdge) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }
}
