package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.controller.graph.StructureGraph;
import com.elvis.visualfsm.controller.handler.GraphEditHandler;
import com.elvis.visualfsm.controller.handler.PsiTreeChangeHandler;
import com.elvis.visualfsm.model.ActionGraphEdge;
import com.elvis.visualfsm.model.StructureGraphModel;
import com.elvis.visualfsm.model.TransitClassComboBoxModel;
import com.elvis.visualfsm.view.DesignerForm;
import com.elvis.visualfsm.view.SelectActionDialog;
import com.elvis.visualfsm.view.renderer.TransitClassBoxRenderer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import com.intellij.ui.components.JBScrollPane;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
        view.getAddEdgeButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Object[] cells = graph.getSelectionCells();
                if (cells.length > 1) {
                    PsiType psiType = psiTransitClassManager.getActionType();
                    PsiClass psiClass = psiTransitClassManager.findActionClass(psiType.getCanonicalText());
                    PsiField[] psiField = psiClass.getAllFields();
                    List<String> fields = new ArrayList<String>();
                    for (PsiField field : psiField) {
                        if (field.getType().equals(psiType)) {
                            fields.add(field.getName());
                        }
                    }
                    if (fields.size() > 0) {
                        SelectActionDialog selectActionDialog = new SelectActionDialog(fields, fields.get(0));
                        selectActionDialog.setVisible(true);
                        final String res = selectActionDialog.getAction();
                        if (res != null) {
                            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                                @Override
                                public void run() {
                                    psiTransitClassManager.addTransitionBetween(cells[0].toString(), cells[1].toString(), res);
                                }
                            });
                        }
                    }
                }
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

        model.addGraphModelListener(graphEditHandler = new GraphEditHandler(psiTransitClassManager));
        graph.addGraphSelectionListener(new GraphSelectionListener() {
            @Override
            public void valueChanged(GraphSelectionEvent graphSelectionEvent) {
                view.getAddEdgeButton().setEnabled(graph.getSelectionCells().length > 1);
            }
        });
        graph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Object selection = graph.getSelectionCell();
                    if (selection instanceof ActionGraphEdge) {
                        final ActionGraphEdge edge = (ActionGraphEdge) selection;
                        PsiType psiType = psiTransitClassManager.getActionType();
                        PsiClass psiClass = psiTransitClassManager.findActionClass(psiType.getCanonicalText());
                        PsiField[] psiField = psiClass.getAllFields();
                        List<String> fields = new ArrayList<String>();
                        for (PsiField field : psiField) {
                            if (field.getType().equals(psiType)) {
                                fields.add(field.getName());
                            }
                        }

                        if (fields.size() > 0) {

                            final String res = Messages.showEditableChooseDialog("Choose", "Action", null, fields.toArray(new String[fields.size()]), edge.getUserObject().toString(), null);
                            if (res != null) {
                                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        PsiStatement psiStatement = psiTransitClassManager.createTransition(
                                                edge.getSourceVertex().getUserObject().toString()
                                                , edge.getTargetVertex().getUserObject().toString()
                                                , res);
                                        edge.getPsiElement().replace(psiStatement);

                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
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

        graph.setTolerance(10);
        graph.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        view.getDesignerPanel().setLayout(new BoxLayout(view.getDesignerPanel(), BoxLayout.LINE_AXIS));
        view.getDesignerPanel().add(new JBScrollPane(graph));
    }
}
