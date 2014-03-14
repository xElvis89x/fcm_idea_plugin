package com.elvis.visualfcm;

import com.elvis.visualfcm.controller.FCMDesignerController;
import com.elvis.visualfcm.view.FCMDesignerForm;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.impl.ContentImpl;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 10.03.14
 * Time: 13:02
 * To change this template use File | Settings | File Templates.
 */
public class FCMToolsAction extends AnAction {
    public static final String FCM_DESIGNER = "Designer";
    private static final String FCM = "FCM";
    private FCMDesignerForm view;

    public void actionPerformed(AnActionEvent e) {
        view = new FCMDesignerForm();
        new FCMDesignerController(e.getProject(), view);
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(e.getProject());
        ToolWindow toolWindow = toolWindowManager.registerToolWindow(FCM, true, ToolWindowAnchor.LEFT);
        toolWindow.getContentManager().addContent(new ContentImpl(view.getContentPane(), FCM_DESIGNER, false));
    }
}
