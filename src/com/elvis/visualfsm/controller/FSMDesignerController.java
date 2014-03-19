package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.controller.graph.StructureGraph;
import com.elvis.visualfsm.controller.handler.GraphEditHandler;
import com.elvis.visualfsm.controller.handler.PsiTreeChangeHandler;
import com.elvis.visualfsm.model.FSMGraphModel;
import com.elvis.visualfsm.view.FSMDesignerForm;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.ui.components.JBScrollPane;
import org.jgraph.JGraph;
import org.jgrapht.ext.JGraphModelAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

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

    private FSMGraphModel model;
    private JGraph graph;

    private PsiTreeChangeHandler psiTreeChangeHandler;
    private GraphEditHandler graphEditHandler;

    private PsiTransitClassManager psiTransitClassManager;

    public FSMDesignerController(Project project, FSMDesignerForm view) {
        this.project = project;
        this.view = view;

        init();
    }


    private void init() {
        initGraph();
        psiTransitClassManager = new PsiTransitClassManager(project);
        psiTransitClassManager.setPsiClass(psiTransitClassManager.findTransitClasses().get(0));

        psiTreeChangeHandler = new PsiTreeChangeHandler(model, graph, psiTransitClassManager.getPsiClass());
        psiTreeChangeHandler.setGraph(graph);
        psiTreeChangeHandler.setModel(model);
        psiTreeChangeHandler.setPsiClass(psiTransitClassManager.getPsiClass());
        psiTransitClassManager.getPsiClass().getManager().addPsiTreeChangeListener(psiTreeChangeHandler);


        view.getFragmentClassLabel().setText(psiTransitClassManager.getPsiClass().getName());


        view.getAddFragmentButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        List<PsiClass> psiClassList = psiTransitClassManager.findFragmentClasses();
                        String[] strings = new String[psiClassList.size()];
                        int i = 0;
                        for (PsiClass psiClass : psiClassList) {
                            strings[i++] = psiClass.getName();
                        }
                        if (strings.length > 0) {
                            String res = Messages.showEditableChooseDialog("choose", "Fragment", null, strings, strings[0], null);
                            if (res != null) {
                                psiTransitClassManager.createField(psiTreeChangeHandler.getPsiClass(), res, res);
                            }
                        } else {
                            Messages.showMessageDialog(project, "now fragment files", "Warning", null);
                        }
                    }
                });
            }
        });

        view.getRefreshButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                psiTreeChangeHandler.updateStructure();
            }
        });
        psiTreeChangeHandler.updateStructure();
    }

    private void initGraph() {
        model = new FSMGraphModel();
        graph = new StructureGraph(model);
        graph.setAntiAliased(true);
        graph.setConnectable(true);
        graph.setCloneable(false);
//        graph.setInvokesStopCellEditing(true);
//        graph.setJumpToDefaultPort(true);
        graph.setGridVisible(true);
        view.getDesignerPanel().setLayout(new BoxLayout(view.getDesignerPanel(), BoxLayout.LINE_AXIS));
        view.getDesignerPanel().add(new JBScrollPane(graph));


        model.addGraphModelListener(graphEditHandler = new GraphEditHandler());
    }
}
