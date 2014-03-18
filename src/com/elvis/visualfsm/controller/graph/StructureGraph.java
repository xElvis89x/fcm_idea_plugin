package com.elvis.visualfsm.controller.graph;

import com.elvis.visualfsm.model.FragmentClassGraphVertex;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;

/**
 * Created with IntelliJ IDEA.
 * User: elvis
 * Date: 3/18/14
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class StructureGraph extends JGraph {
    public StructureGraph() {
    }

    public StructureGraph(GraphModel graphModel) {
        super(graphModel);
    }

    @Override
    public boolean isCellEditable(Object cell) {
        if (cell instanceof FragmentClassGraphVertex) {
            return false;
        }
        return super.isCellEditable(cell);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
