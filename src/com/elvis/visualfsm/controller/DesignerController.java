package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.controller.graph.StructureGraph;
import com.elvis.visualfsm.controller.handler.GraphEditHandler;
import com.elvis.visualfsm.controller.handler.PsiTreeChangeHandler;
import com.elvis.visualfsm.model.StructureGraphModel;
import com.elvis.visualfsm.model.TransitClassComboBoxModel;
import com.elvis.visualfsm.view.FSMDesignerForm;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
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
    private FSMDesignerForm view;

    private StructureGraphModel model;
    private StructureGraph graph;

    private PsiTreeChangeHandler psiTreeChangeHandler;
    private GraphEditHandler graphEditHandler;

    private PsiTransitClassManager psiTransitClassManager;

    private TransitClassComboBoxModel comboBoxModel = new TransitClassComboBoxModel();

    public DesignerController(Project project, FSMDesignerForm view) {
        this.project = project;
        this.view = view;

        init();
    }


    private void init() {
        initGraph();
        psiTransitClassManager = new PsiTransitClassManager(project);


        List<PsiClass> psiClasses = psiTransitClassManager.findTransitClasses();
        for (PsiClass psiClass : psiClasses) {
            comboBoxModel.addElement(psiClass);
        }
        view.getTransitClassBox().setModel(comboBoxModel);
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
                if (psiTreeChangeHandler != null) {
                    psiTreeChangeHandler.updateStructure();
                }
            }
        });
    }

    private void transitClassChanged(PsiClass psiClass) {
        psiTransitClassManager.setPsiClass(psiClass);
        recreatePsiTreeChangeHandler(psiClass);
    }

    void recreatePsiTreeChangeHandler(PsiClass psiClass) {
        psiTreeChangeHandler = new PsiTreeChangeHandler(model, graph);
        psiTreeChangeHandler.setPsiClass(psiClass);
        psiClass.getManager().addPsiTreeChangeListener(psiTreeChangeHandler);
    }

    private void initGraph() {
        model = new StructureGraphModel();
        graph = new StructureGraph(model);
        graph.setAntiAliased(true);
        graph.setCloneable(false);
//        graph.setInvokesStopCellEditing(true);
//        graph.setJumpToDefaultPort(true);
        graph.setGridVisible(true);

        view.getDesignerPanel().setLayout(new BoxLayout(view.getDesignerPanel(), BoxLayout.LINE_AXIS));
        view.getDesignerPanel().add(new JBScrollPane(graph));


        model.addGraphModelListener(graphEditHandler = new GraphEditHandler());
    }
}
