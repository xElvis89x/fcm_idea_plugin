package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.view.FSMDesignerForm;
import com.elvis.visualfsm.view.FragmentView;
import com.intellij.openapi.project.Project;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 14.12.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class FSMDesignerController {
    private Project project;
    private FSMDesignerForm view;

    public FSMDesignerController(Project project, FSMDesignerForm view) {
        this.project = project;
        this.view = view;
        init();
    }

    private void init() {
        view.getAddFragmentButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                FragmentView fragmentView = new FragmentView();
//                view.getDesignerPanel().setLayout(null);
//                fragmentView.getContentPane().setLocation(20, 20);
//                view.getDesignerPanel().add(fragmentView.getContentPane());


            }
        });

//        DirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
//        g.addEdge("asdfasd", "asdasd");
//        g.addEdge("asdfasd1", "asdasd1");
//        g.addEdge("asdfasd", "asasdasddasd1");

        mxGraph graph = new mxGraph();
        graph.getModel().beginUpdate();


        Object v1 = graph.insertVertex(graph.getDefaultParent(), null, "Hello", 20, 20, 80, 30);
        Object v2 = graph.insertVertex(graph.getDefaultParent(), null, "World!", 240, 150, 80, 30);
        graph.insertEdge(graph.getDefaultParent(), null, "Edge", v1, v2);

        graph.getModel().endUpdate();
        mxGraphComponent mxGraphComponent = new mxGraphComponent(graph);
        view.getDesignerPanel().setLayout(new BoxLayout(view.getDesignerPanel(),BoxLayout.LINE_AXIS));

        view.getDesignerPanel().add(mxGraphComponent.getGraphControl());

    }
}
