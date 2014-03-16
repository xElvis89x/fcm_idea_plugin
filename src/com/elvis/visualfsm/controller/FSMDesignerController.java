package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.controller.handler.PsiTreeChangeHandler;
import com.elvis.visualfsm.model.FSMGraphModel;
import com.elvis.visualfsm.view.FSMDesignerForm;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.util.ClassUtil;
import org.jgraph.JGraph;

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

    private FSMGraphModel model;
    private JGraph graph;
    private PsiManager psiManager;
    private PsiClass psiClass;
    private PsiTreeChangeHandler psiTreeChangeHandler;

    public FSMDesignerController(Project project, FSMDesignerForm view) {
        this.project = project;
        this.view = view;
        psiManager = PsiManager.getInstance(project);
        init();
    }

    private void init() {
        initGraph();
        psiClass = ClassUtil.findPsiClass(psiManager, "com.example.TestAndro.TestTransitManager");

        psiTreeChangeHandler = new PsiTreeChangeHandler();
        psiTreeChangeHandler.setGraph(graph);
        psiTreeChangeHandler.setModel(model);
        psiTreeChangeHandler.setPsiClass(psiClass);
        psiClass.getManager().addPsiTreeChangeListener(psiTreeChangeHandler);

        view.getAddFragmentButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        createField(psiClass);
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


    }

    private void createField(PsiClass psiClass) {
        PsiField psiTransitionsMapField = psiClass.findFieldByName("transitionsMap", true);
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
        view.getDesignerPanel().add(new JScrollPane(graph));
    }


//    public static DefaultGraphCell createVertex(String name, double x,
//                                                double y, double w, double h, Color bg, boolean raised) {
//
//        // Create vertex with the given name
//        DefaultGraphCell cell = new DefaultGraphCell(name);
//
//        // Set bounds
//        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x, y, w, h));
//
//        // Set fill color
//        if (bg != null) {
//            GraphConstants.setGradientColor(cell.getAttributes(), bg);
//            GraphConstants.setOpaque(cell.getAttributes(), true);
//        }
//
//        // Set raised border
//        if (raised) {
//            GraphConstants.setBorder(cell.getAttributes(),
//                    BorderFactory.createRaisedBevelBorder());
//        } else // Set black border
//        {
//            GraphConstants.setBorderColor(cell.getAttributes(),
//                    Color.black);
//        }
//        // Add a Floating Port
//        cell.addPort();
//
//        return cell;
//    }
}
