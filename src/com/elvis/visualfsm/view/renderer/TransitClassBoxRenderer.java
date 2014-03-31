package com.elvis.visualfsm.view.renderer;

import com.intellij.psi.PsiClass;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 23.03.14
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class TransitClassBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);    //To change body of overridden methods use File | Settings | File Templates.
        setText(((PsiClass) value).getName());
        return this;
    }
}
