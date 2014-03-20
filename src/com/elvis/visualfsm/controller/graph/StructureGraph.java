package com.elvis.visualfsm.controller.graph;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
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

}
