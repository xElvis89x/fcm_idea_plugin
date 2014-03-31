package com.elvis.visualfsm.controller.handler;

import com.elvis.visualfsm.controller.PsiTransitClassManager;
import com.elvis.visualfsm.view.SelectActionDialog;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import org.jgraph.JGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 31.03.14
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class AddEdgeHandler extends AbstractAction {
    private JGraph graph;
    private PsiTransitClassManager psiTransitClassManager;

    public AddEdgeHandler(JGraph graph, PsiTransitClassManager psiTransitClassManager) {
        this.graph = graph;
        this.psiTransitClassManager = psiTransitClassManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object[] cells = graph.getSelectionCells();
        if (cells.length == 2) {
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
                    ApplicationManager.getApplication().runWriteAction(new WriteChanges(cells[0].toString(), cells[1].toString(), res));
                }
            }
        }
    }

    private class WriteChanges implements Runnable {
        private String from;
        private String to;
        private String action;

        private WriteChanges(String from, String to, String action) {
            this.from = from;
            this.to = to;
            this.action = action;
        }

        @Override
        public void run() {
            psiTransitClassManager.addTransitionBetween(from, to, action);
        }
    }
}
