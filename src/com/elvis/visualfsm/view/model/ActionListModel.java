package com.elvis.visualfsm.view.model;

import javax.swing.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 30.03.14
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
public class ActionListModel extends DefaultListModel<String> {
    public ActionListModel() {
    }

    public void addElementList(List<String> stringList) {
        for (String s : stringList) {
            addElement(s);
        }
    }


}
