package com.elvis.visualfsm.controller.handler;

import com.elvis.visualfsm.model.ActionGraphEdge;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.SharedPsiElementImplUtil;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;

/**
 * Created with IntelliJ IDEA.
 * User: elvis
 * Date: 3/19/14
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphEditHandler implements GraphModelListener {
    @Override
    public void graphChanged(GraphModelEvent graphModelEvent) {
        ConnectionSet connectionSet = graphModelEvent.getChange().getConnectionSet();
        ConnectionSet previosConnectionSet = graphModelEvent.getChange().getPreviousConnectionSet();
        if (connectionSet != null && previosConnectionSet != null && connectionSet.getEdges().size() == 1) {
            ActionGraphEdge actionGraphEdge = (ActionGraphEdge) connectionSet.getEdges().toArray()[0];

            ConnectionSet.Connection connection = (ConnectionSet.Connection) connectionSet.getConnections().toArray()[0];
            ConnectionSet.Connection prevConnection = (ConnectionSet.Connection) previosConnectionSet.getConnections().toArray()[0];
            //connection.isSource()     F3 -> F4
            int i = 0;

            PsiElement psiElement = actionGraphEdge.getPsiElement();

            DefaultPort prevDefaultPort = (DefaultPort) prevConnection.getPort();
            DefaultGraphCell prevDefaultGraphCell = (DefaultGraphCell) prevDefaultPort.getParent();

            DefaultPort defaultPort = (DefaultPort) prevConnection.getPort();
            DefaultGraphCell defaultGraphCell = (DefaultGraphCell) prevDefaultPort.getParent();



        }

    }
}
