package com.elvis.visualfsm.controller.handler;

import com.elvis.visualfsm.controller.PsiTransitClassManager;
import com.elvis.visualfsm.model.ActionGraphEdge;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiStatement;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.ConnectionSet;

/**
 * Created with IntelliJ IDEA.
 * User: elvis
 * Date: 3/19/14
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphEditHandler implements GraphModelListener {
    PsiTransitClassManager psiTransitClassManager;

    public GraphEditHandler(PsiTransitClassManager psiTransitClassManager) {
        this.psiTransitClassManager = psiTransitClassManager;
    }

    @Override
    public void graphChanged(GraphModelEvent graphModelEvent) {
        ConnectionSet connectionSet = graphModelEvent.getChange().getConnectionSet();
        ConnectionSet previosConnectionSet = graphModelEvent.getChange().getPreviousConnectionSet();
        if (connectionSet != null && previosConnectionSet != null && connectionSet.getEdges().size() == 1) {
            ActionGraphEdge actionGraphEdge = (ActionGraphEdge) connectionSet.getEdges().toArray()[0];
            ApplicationManager.getApplication().runWriteAction(new ReplaceTransition(actionGraphEdge));
        }
    }

    private class ReplaceTransition implements Runnable {
        private ActionGraphEdge actionGraphEdge;

        private ReplaceTransition(ActionGraphEdge actionGraphEdge) {
            this.actionGraphEdge = actionGraphEdge;
        }

        @Override
        public void run() {
            PsiStatement psiStatement = psiTransitClassManager.createTransition(
                    actionGraphEdge.getSourceVertex().getUserObject().toString()
                    , actionGraphEdge.getTargetVertex().getUserObject().toString()
                    , actionGraphEdge.getUserObject().toString());
            actionGraphEdge.getPsiElement().replace(psiStatement);
        }
    }
}
