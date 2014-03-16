package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.controller.handler.PsiTreeChangeHandler;
import com.elvis.visualfsm.model.FSMGraphModel;
import com.elvis.visualfsm.view.FSMDesignerForm;
import com.fsm.transit.core.AbstractTransitManger;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.util.ClassUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBScrollPane;
import org.jgraph.JGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 14.12.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class FSMDesignerController {
    public static final String TRANSITIONS_MAP = "transitionsMap";
    private Project project;
    private FSMDesignerForm view;

    private FSMGraphModel model;
    private JGraph graph;
    private PsiManager psiManager;
    //private PsiClass psiClass;
    private PsiTreeChangeHandler psiTreeChangeHandler;

    public FSMDesignerController(Project project, FSMDesignerForm view) {
        this.project = project;
        this.view = view;
        psiManager = PsiManager.getInstance(project);
        init();
    }

    private void init() {
        initGraph();
        //psiClass = ClassUtil.findPsiClass(null, AbstractTransitManger.class.getName();
        PsiElement psiElement = ClassUtil.findPsiClass(psiManager, AbstractTransitManger.class.getName());




        psiTreeChangeHandler = new PsiTreeChangeHandler();
        psiTreeChangeHandler.setGraph(graph);
        psiTreeChangeHandler.setModel(model);
//        psiTreeChangeHandler.setPsiClass(psiClass);
//        psiClass.getManager().addPsiTreeChangeListener(psiTreeChangeHandler);


        view.getAddFragmentButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
//                        createField(psiClass);
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

    private void createField(PsiClass psiClass) {
        PsiField psiTransitionsMapField = psiClass.findFieldByName(TRANSITIONS_MAP, true);
        if (psiTransitionsMapField != null) {
            PsiClassInitializer psiClassInitializer = null;
            PsiElementFactory psiElementFactory = new PsiElementFactoryImpl(psiManager);
            if (psiClass.getInitializers().length > 0) {
                psiClassInitializer = psiClass.getInitializers()[psiClass.getInitializers().length - 1];
            } else {
                psiClassInitializer = psiElementFactory.createClassInitializer();
                psiClass.getModifierList().add(psiClassInitializer);
            }

            String statment = psiTransitionsMapField.getName() + ".put(new TransitData<FragmentAction>(F1.class, FragmentAction.B1), new TransitResultData<FragmentAction>(F2.class));";
            PsiStatement psiStatement = psiElementFactory.createStatementFromText(statment, psiClassInitializer.getBody().getContext());
            psiClassInitializer.getModifierList().addBefore(psiStatement, psiClassInitializer.getBody().getRBrace());

            CodeStyleManager.getInstance(psiManager).reformat(psiClassInitializer);
        }
    }

    void initGraph() {
        model = new FSMGraphModel();
        graph = new JGraph(model);
//        graph.setCloneable(true);
        graph.setInvokesStopCellEditing(true);
        graph.setJumpToDefaultPort(true);

        view.getDesignerPanel().setLayout(new BoxLayout(view.getDesignerPanel(), BoxLayout.LINE_AXIS));
        view.getDesignerPanel().add(new JBScrollPane(graph));
    }
}
