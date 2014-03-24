package com.elvis.visualfsm.model;

import com.intellij.psi.PsiClass;
import com.intellij.ui.JBColor;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 15.03.14
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
public class FragmentClassGraphVertex extends DefaultGraphCell {
    private String name;

    private static Random random = new Random(System.nanoTime());

    private PsiClass item;

    public FragmentClassGraphVertex(PsiClass psiClass) {
        super(psiClass.getName());
        item = psiClass;
        name = psiClass.getName();
        GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(random.nextInt(200), random.nextInt(200), 40, 40));
        GraphConstants.setGradientColor(getAttributes(), JBColor.GREEN);
        GraphConstants.setOpaque(getAttributes(), true);
        GraphConstants.setBorderColor(getAttributes(), JBColor.BLACK);

//        GraphConstants.setSizeable(getAttributes(), false);
//        GraphConstants.setConnectable(getAttributes(), true);
//        GraphConstants.setDisconnectable(getAttributes(), true);
//        GraphConstants.setBendable(getAttributes(), true);
//        GraphConstants.setEditable(getAttributes(), false);
    }

    public PsiClass getItem() {
        return item;
    }

    public int getWidth() {
        return (int) GraphConstants.getBounds(getAttributes()).getWidth();
    }

    public int getHeight() {
        return (int) GraphConstants.getBounds(getAttributes()).getHeight();
    }

    public int getX() {
        return (int) GraphConstants.getBounds(getAttributes()).getX();
    }

    public int getY() {
        return (int) GraphConstants.getBounds(getAttributes()).getY();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FragmentClassGraphVertex that = (FragmentClassGraphVertex) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }
}
