package com.elvis.visualfsm.controller.handler;

import com.elvis.visualfsm.controller.graph.StructureGraph;
import com.elvis.visualfsm.model.ActionGraphEdge;
import com.elvis.visualfsm.model.FragmentClassGraphVertex;
import com.elvis.visualfsm.model.StructureGraphModel;
import com.intellij.psi.*;
import com.jgraph.layout.JGraphFacade;
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

    public PsiTreeChangeHandler(StructureGraphModel model, StructureGraph graph) {
        this.model = model;
        this.graph = graph;
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
        fireDataChange();
    }

    public void addFragment(PsiClass fragmentPsiClass) {
        FragmentClassGraphVertex vertex = findVertexByName(fragmentPsiClass);
        if (!fragmentList.contains(vertex)) {
            fragmentList.add(vertex);
        }
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
        JGraphFacade facade = new JGraphFacade(graph);
        facade.setDirected(true);
        facade.setIgnoresUnconnectedCells(true);
        JGraphTreeLayout localLayout = new JGraphTreeLayout();
        localLayout.setOrientation(SwingConstants.WEST);
        localLayout.setLevelDistance(100);
        localLayout.setNodeDistance(100);
        localLayout.run(facade);
        Map nested = facade.createNestedMap(true, true);
        graph.getGraphLayoutCache().edit(nested);
    }

    private void updateGraph() {
        graph.getGraphLayoutCache().remove(graph.getGraphLayoutCache().getCells(true, true, true, true));
        if (psiClass != null) {
            List<DefaultGraphCell> cells = new ArrayList<DefaultGraphCell>();
            for (FragmentClassGraphVertex vertex : fragmentList) {
                vertex.addPort();
                cells.add(vertex);
            }
            for (PsiClassInitializer initializer : psiClass.getInitializers()) {
                parseExpression(initializer.getBody(), cells);
            }
            graph.getGraphLayoutCache().insert(cells.toArray());
            graph.updateUI();
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

    void parseExpression(PsiElement rootPsiElement, List<DefaultGraphCell> cells) {
        if (rootPsiElement != null) {
            for (PsiElement psiElement : rootPsiElement.getChildren()) {
                Matcher matcher = regexp.matcher(psiElement.getText());
                if (matcher.matches()) {
                    cells.add(findEdgeByName(psiElement, matcher.group(2)
                            , findVertexByName(findClassByName(matcher.group(1))).getChildAt(0)
                            , findVertexByName(findClassByName(matcher.group(3))).getChildAt(0)));
                }
            }
        }
    }

    private List<FragmentClassGraphVertex> fragmentList = new ArrayList<FragmentClassGraphVertex>();
    private List<ActionGraphEdge> actionList = new ArrayList<ActionGraphEdge>();

    private PsiClass findClassByName(String name) {
        PsiClass result = null;
        for (FragmentClassGraphVertex vertex : fragmentList) {
            if (vertex.getItem().getName().equals(name)) {
                result = vertex.getItem();
                break;
            }
        }
        return result;
    }

    private FragmentClassGraphVertex findVertexByName(PsiClass psiClass) {
        FragmentClassGraphVertex result = new FragmentClassGraphVertex(psiClass);
        int fromIndex = fragmentList.indexOf(result);
        if (fromIndex != -1) {
            result = fragmentList.get(fromIndex);
        } else {
            fragmentList.add(result);
        }
        result.addPort();
        return result;
    }

    private ActionGraphEdge findEdgeByName(PsiElement psiElement, String name, Object source, Object target) {
        ActionGraphEdge result = new ActionGraphEdge(psiElement, name, source, target);
        int fromIndex = actionList.indexOf(result);
        if (fromIndex != -1) {
            result = actionList.get(fromIndex);
        } else {
            actionList.add(result);
        }
        return result;
    }


    private void createArrow(String fromFragment, String toFragment, String action) {

//        for (Object cell : graph.getGraphLayoutCache().getCells(true, true, true, true)) {
//            cells.remove(cell);
//        }

    }

    private void fireDataChange() {
        updateStructure();
        fragmentList.clear();
    }

}
