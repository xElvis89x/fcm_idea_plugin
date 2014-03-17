package com.elvis.visualfsm.view;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 13.03.14
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */
public class FSMDesignerForm {
    private JPanel contentPane;
    private JButton addFragmentButton;
    private JPanel designerPanel;
    private JButton refreshButton;
    private JLabel fagmentClass;

    public JLabel getFragmentClassLabel() {
        return fagmentClass;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public JButton getAddFragmentButton() {
        return addFragmentButton;
    }

    public JPanel getDesignerPanel() {
        return designerPanel;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }
}
