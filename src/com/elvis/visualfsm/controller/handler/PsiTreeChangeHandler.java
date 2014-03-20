package com.elvis.visualfsm.controller.handler;

import com.elvis.visualfsm.controller.graph.StructureGraph;
import com.elvis.visualfsm.model.ActionGraphEdge;
import com.elvis.visualfsm.model.FragmentClassGraphVertex;
import com.elvis.visualfsm.model.StructureGraphModel;
import com.intellij.psi.*;
import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import org.jetbrains.annotations.NotNull;
import org.jgraph.graph.DefaultGraphCell;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 15.03.14
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class PsiTreeChangeHandler extends PsiTreeChangeAdapter {

    private StructureGraphModel model;
    private StructureGraph graph;
    private PsiClass psiClass;

    private JGraphLayout layout;
    private JGraphFacade facade;

    public PsiTreeChangeHandler(StructureGraphModel model, StructureGraph graph, PsiClass psiClass) {
        this.model = model;
        this.graph = graph;
        this.psiClass = psiClass;
    }

    public PsiTreeChangeHandler() {
    }


    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void setModel(StructureGraphModel model) {
        this.model = model;
    }

    public void setGraph(StructureGraph graph) {
        this.graph = graph;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public void addFragment(PsiClass fragmentPsiClass) {

    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        updateGraph();
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        updateGraph();
    }

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        updateGraph();
    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        updateGraph();
    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        updateGraph();
    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent psiTreeChangeEvent) {
        updateGraph();
    }

    void reformatGraph() {
        if (facade == null) {
            facade = new JGraphFacade(graph);
            facade.setDirected(true);
            facade.setIgnoresUnconnectedCells(true);
        }
        if (layout == null) {
            JGraphTreeLayout localLayout = new JGraphTreeLayout();
            localLayout.setOrientation(SwingConstants.WEST);
            localLayout.setLevelDistance(100);
            localLayout.setNodeDistance(100);
            layout = localLayout;
        }
        layout.run(facade);
        Map nested = facade.createNestedMap(true, true);
        graph.getGraphLayoutCache().edit(nested);

    }

    private void updateGraph() {
        graph.getGraphLayoutCache().remove(graph.getGraphLayoutCache().getCells(true, true, true, true));
        if (psiClass != null) {
            for (PsiClassInitializer initializer : psiClass.getInitializers()) {
                parseExpression(initializer.getBody());
            }
            graph.updateUI();
            graph.invalidate();
        }
    }

    public void updateStructure() {
//        fragmentList.clear();
        actionList.clear();
        updateGraph();
        reformatGraph();
    }


    private static Pattern regexp = Pattern.compile(
            "transitionsMap.put\\(" +
                    "new TransitData<.*?>\\((.*?)\\.class, .*?\\.(.*?)\\)" +
                    ", " +
                    "new TransitResultData<.*?>\\((.*?)\\.class\\)\\);");

    void parseExpression(PsiElement rootPsiElement) {
        if (rootPsiElement != null) {
            for (PsiElement psiElement : rootPsiElement.getChildren()) {
                Matcher matcher = regexp.matcher(psiElement.getText());
                if (matcher.matches()) {
                    createArrow(matcher.group(1), matcher.group(3), matcher.group(2));
                }
            }
        }
    }

    private List<FragmentClassGraphVertex> fragmentList = new ArrayList<FragmentClassGraphVertex>();
    private List<ActionGraphEdge> actionList = new ArrayList<ActionGraphEdge>();


    private FragmentClassGraphVertex findVertexByName(String name) {
        FragmentClassGraphVertex result = new FragmentClassGraphVertex(name);
        int fromIndex = fragmentList.indexOf(result);
        if (fromIndex != -1) {
            result = fragmentList.get(fromIndex);
        } else {
            fragmentList.add(result);
        }
        result.addPort();
        return result;
    }

    private ActionGraphEdge findEdgeByName(String name, Object source, Object target) {
        ActionGraphEdge result = new ActionGraphEdge(name, source, target);
        int fromIndex = actionList.indexOf(result);
        if (fromIndex != -1) {
            result = actionList.get(fromIndex);
        } else {
            actionList.add(result);
        }
        return result;
    }


    private void createArrow(String fromFragment, String toFragment, String action) {
        List<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
        graph.getModel().beginUpdate();
        cells.add(findVertexByName(fromFragment));
        cells.add(findVertexByName(toFragment));
        cells.add(findEdgeByName(action, cells.get(0).getChildAt(0), cells.get(1).getChildAt(0)));

        for (Object cell : graph.getGraphLayoutCache().getCells(true, true, true, true)) {
            cells.remove(cell);
        }

        model.endUpdate();
        graph.getGraphLayoutCache().insert(cells.toArray());

    }

}
