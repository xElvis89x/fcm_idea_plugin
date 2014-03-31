package com.elvis.visualfsm.model;

import com.intellij.psi.PsiElement;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 16.03.14
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class ActionGraphEdge extends DefaultEdge {
    private String name;
    private PsiElement psiElement;

    public ActionGraphEdge(PsiElement psiElement, String name) {
        super(name);
        this.name = name;
        this.psiElement = psiElement;
    }

    public PsiElement getPsiElement() {
        return psiElement;
    }

    public FragmentClassGraphVertex getSourceVertex() {
        DefaultPort defaultPort = (DefaultPort) getSource();
        return (FragmentClassGraphVertex) defaultPort.getParent();
    }

    public FragmentClassGraphVertex getTargetVertex() {
        DefaultPort defaultPort = (DefaultPort) getTarget();
        return (FragmentClassGraphVertex) defaultPort.getParent();
    }

    public ActionGraphEdge(PsiElement psiElement, String name, Object source, Object target) {
        this(psiElement, name);
        setSource(source);
        setTarget(target);
        GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_TECHNICAL);
        GraphConstants.setEndFill(getAttributes(), true);
        GraphConstants.setLabelAlongEdge(getAttributes(), true);
        GraphConstants.setBendable(getAttributes(), true);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionGraphEdge that = (ActionGraphEdge) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;

        return true;
    }
}
