package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.controller.graph.StructureGraph;
import com.elvis.visualfsm.controller.handler.AddEdgeHandler;
import com.elvis.visualfsm.controller.handler.GraphEditHandler;
import com.elvis.visualfsm.controller.handler.MouseClickHandler;
import com.elvis.visualfsm.controller.handler.PsiTreeChangeHandler;
import com.elvis.visualfsm.model.StructureGraphModel;
import com.elvis.visualfsm.model.TransitClassComboBoxModel;
import com.elvis.visualfsm.view.DesignerForm;
import com.elvis.visualfsm.view.renderer.TransitClassBoxRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.ui.components.JBScrollPane;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 14.12.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class DesignerController {

    private Project project;
    private DesignerForm view;

    private StructureGraphModel model;
    private StructureGraph graph;

    private PsiTreeChangeHandler psiTreeChangeHandler;
    private GraphEditHandler graphEditHandler;

    private PsiTransitClassManager psiTransitClassManager;

    private TransitClassComboBoxModel comboBoxModel = new TransitClassComboBoxModel();

    public DesignerController(Project project, DesignerForm view) {
        this.project = project;
        this.view = view;
    }


    public void init() {
        initGraph();
        psiTransitClassManager = new PsiTransitClassManager(project);
        List<PsiClass> psiClasses = psiTransitClassManager.findTransitClasses();
        for (PsiClass psiClass : psiClasses) {
            comboBoxModel.addElement(psiClass);
        }
        view.getTransitClassBox().setModel(comboBoxModel);
        view.getTransitClassBox().setRenderer(new TransitClassBoxRenderer());
        view.getTransitClassBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                PsiClass psiClass = (PsiClass) e.getItem();
                transitClassChanged(psiClass);
            }
        });
        if (psiClasses.size() > 0) {
            transitClassChanged(psiClasses.get(0));
        }
        view.getAddEdgeButton().setEnabled(false);
        view.getAddEdgeButton().addActionListener(new AddEdgeHandler(graph, psiTransitClassManager));

        view.getRefreshButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (psiTreeChangeHandler != null) {
                    psiTreeChangeHandler.updateStructure();
                }
            }
        });

        model.addGraphModelListener(graphEditHandler = new GraphEditHandler(psiTransitClassManager));
        graph.addGraphSelectionListener(new GraphSelectionListener() {
            @Override
            public void valueChanged(GraphSelectionEvent graphSelectionEvent) {
                view.getAddEdgeButton().setEnabled(graph.getSelectionCells().length > 1);
            }
        });
        graph.addMouseListener(new MouseClickHandler(graph, psiTransitClassManager));
    }

    private void transitClassChanged(PsiClass psiClass) {
        psiTransitClassManager.setPsiClass(psiClass);
        recreatePsiTreeChangeHandler(psiClass);
        for (PsiClass psiClass1 : psiTransitClassManager.findFragmentClasses()) {
            psiTreeChangeHandler.addFragment(psiClass1);
        }
    }

    void recreatePsiTreeChangeHandler(PsiClass psiClass) {
        if (psiTreeChangeHandler != null && psiTreeChangeHandler.getPsiClass() != null) {
            psiTreeChangeHandler.getPsiClass().getManager().removePsiTreeChangeListener(psiTreeChangeHandler);
        }
        psiTreeChangeHandler = new PsiTreeChangeHandler(model, graph);
        psiTreeChangeHandler.setPsiClass(psiClass);
        psiClass.getManager().addPsiTreeChangeListener(psiTreeChangeHandler);
    }

    private void initGraph() {
        model = new StructureGraphModel();
        graph = new StructureGraph(model);

        graph.setInvokesStopCellEditing(true);
        graph.setJumpToDefaultPort(true);

        graph.setCloneable(false);
        graph.setEditable(false);
        graph.setSizeable(false);

        graph.setAutoResizeGraph(true);
        graph.setAntiAliased(true);

        graph.setTolerance(5);
        graph.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        view.getDesignerPanel().setLayout(new BoxLayout(view.getDesignerPanel(), BoxLayout.LINE_AXIS));
        view.getDesignerPanel().add(new JBScrollPane(graph));
    }
}
