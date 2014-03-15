package com.elvis.visualfsm.controller;

import com.elvis.visualfsm.model.FSMGraphModel;
import com.elvis.visualfsm.view.FSMDesignerForm;
import com.fsm.transit.core.TransitData;
import com.fsm.transit.core.TransitResultData;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
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

    public FSMDesignerController(Project project, FSMDesignerForm view) {
        this.project = project;
        this.view = view;
        psiManager = PsiManager.getInstance(project);
        init();
    }

    private void init() {

        view.getAddFragmentButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PsiClass psiClass = ClassUtil.findPsiClass(psiManager, "com.example.TestAndro.TestTransitManager");

                createField(psiClass);

//                DefaultGraphCell[] cells = new DefaultGraphCell[3];
//
//                cells[0] = new FragmentClassGraphVertex("sdsd");
//                cells[1] = new FragmentClassGraphVertex("World");
//
//                DefaultEdge edge = new DefaultEdge("edge");
//                edge.setSource(cells[0].getChildAt(0));
//                edge.setTarget(cells[1].getChildAt(0));
//                cells[2] = edge;
//
//                GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
//                GraphConstants.setEndFill(edge.getAttributes(), true);
//
//                graph.getGraphLayoutCache().insert(cells);
            }
        });

        initGraph();
    }

    private void createField(PsiClass psiClass) {
        PsiField psiTransitionsMapField = null;
        for (PsiField psiField1 : psiClass.getAllFields()) {
            if (psiField1.getName().equals("transitionsMap")) {
                psiTransitionsMapField = psiField1;
                break;
            }
        }
        if (psiTransitionsMapField != null) {
            PsiClassInitializer psiClassInitializer = null;
            PsiElementFactory psiElementFactory = new PsiElementFactoryImpl(psiManager);
            if (psiClass.getInitializers().length > 0) {
                psiClassInitializer = psiClass.getInitializers()[psiClass.getInitializers().length - 1];
            } else {
                psiClassInitializer = psiElementFactory.createClassInitializer();
                psiClass.add(psiClassInitializer);
            }
            //.put(new TransitData<FragmentAction>(F1.class, FragmentAction.B1), new TransitResultData<FragmentAction>(F2.class));
            String statment = psiTransitionsMapField.getName() + ".put(new TransitData<FragmentAction>(F1.class, FragmentAction.B1), new TransitResultData<FragmentAction>(F2.class));";
            PsiStatement psiStatement = psiElementFactory.createStatementFromText(statment, psiClassInitializer.getBody().getContext());
            psiClassInitializer.addBefore(psiStatement, psiClassInitializer.getBody().getRBrace());
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
