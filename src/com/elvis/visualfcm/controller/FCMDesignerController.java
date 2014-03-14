package com.elvis.visualfcm.controller;

import com.elvis.visualfcm.view.FCMDesignerForm;
import com.elvis.visualfcm.view.FragmentView;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 14.12.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class FCMDesignerController {
    private Project project;
    private FCMDesignerForm view;

    public FCMDesignerController(Project project, FCMDesignerForm view) {
        this.project = project;
        this.view = view;
        init();
    }

    private void init() {
        view.getAddFragmentButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FragmentView fragmentView = new FragmentView();
                view.getDesignerPanel().setLayout(null);
                fragmentView.getContentPane().setLocation(20, 20);
                view.getDesignerPanel().add(fragmentView.getContentPane());


            }
        });


    }
}
