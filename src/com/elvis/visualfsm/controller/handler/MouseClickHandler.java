package com.elvis.visualfsm.controller.handler;

import com.elvis.visualfsm.controller.PsiTransitClassManager;
import com.elvis.visualfsm.model.ActionGraphEdge;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import org.jgraph.JGraph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 31.03.14
 * Time: 21:17
 * To change this template use File | Settings | File Templates.
 */
public class MouseClickHandler extends MouseAdapter {
    private JGraph graph;
    private PsiTransitClassManager psiTransitClassManager;

    public MouseClickHandler(JGraph graph, PsiTransitClassManager psiTransitClassManager) {
        this.graph = graph;
        this.psiTransitClassManager = psiTransitClassManager;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Object selection = graph.getSelectionCell();
            if (selection instanceof ActionGraphEdge) {
                ActionGraphEdge edge = (ActionGraphEdge) selection;
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
                        ApplicationManager.getApplication().runWriteAction(new WriteChanges(edge, res));
                    }
                }
            }
        }
    }

    private class WriteChanges implements Runnable {
        private ActionGraphEdge edge;
        private String res;

        private WriteChanges(ActionGraphEdge edge, String res) {
            this.edge = edge;
            this.res = res;
        }

        @Override
        public void run() {
            PsiStatement psiStatement = psiTransitClassManager.createTransition(
                    edge.getSourceVertex().getUserObject().toString()
                    , edge.getTargetVertex().getUserObject().toString()
                    , res);
            edge.getPsiElement().replace(psiStatement);
        }
    }

}
