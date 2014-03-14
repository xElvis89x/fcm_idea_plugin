package com.elvis.visualfsm;

import com.elvis.visualfsm.controller.FSMDesignerController;
import com.elvis.visualfsm.view.FSMDesignerForm;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.impl.ContentImpl;

/**
 * Created with IntelliJ IDEA.
 * User: elvis
 * Date: 3/14/14
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class FSMToolAction extends AnAction {
    public static final String FCM_DESIGNER = "Designer";
    private static final String FCM = "FCM";
    private FSMDesignerForm view;
    private ToolWindow toolWindow;

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (toolWindow == null) {
            view = new FSMDesignerForm();
            new FSMDesignerController(e.getProject(), view);
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(e.getProject());
            ToolWindow toolWindow = toolWindowManager.registerToolWindow(FCM, true, ToolWindowAnchor.LEFT);
            toolWindow.getContentManager().addContent(new ContentImpl(view.getContentPane(), FCM_DESIGNER, false));
        }
        toolWindow.show(null);
    }

}
