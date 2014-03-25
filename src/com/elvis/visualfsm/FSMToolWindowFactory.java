package com.elvis.visualfsm;

import com.elvis.visualfsm.controller.DesignerController;
import com.elvis.visualfsm.view.DesignerForm;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.impl.ContentImpl;

/**
 * Created with IntelliJ IDEA.
 * User: elvis
 * Date: 3/14/14
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class FSMToolWindowFactory implements ToolWindowFactory {
    public static final String FCM_DESIGNER = "Designer";

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        DesignerForm view = new DesignerForm();
        DesignerController designerController = new DesignerController(project, view);
        designerController.init();
        toolWindow.getContentManager().addContent(new ContentImpl(view.getContentPane(), FCM_DESIGNER, false));
    }
}
