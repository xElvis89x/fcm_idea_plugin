package com.elvis.visualfsm.controller;

import android.support.v4.app.Fragment;
import com.elvis.visualfsm.controller.handler.PsiTreeChangeHandler;
import com.elvis.visualfsm.model.FSMGraphModel;
import com.elvis.visualfsm.view.FSMDesignerForm;
import com.fsm.transit.core.AbstractTransitManger;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.ClassUtil;
import com.intellij.ui.components.JBScrollPane;
import org.jgraph.JGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: el
 * Date: 14.12.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class FSMDesignerController {
    public static final String TRANSITIONS_MAP = "transitionsMap";
    private Project project;
    private FSMDesignerForm view;

    private FSMGraphModel model;
    private JGraph graph;
    private PsiManager psiManager;
    //private PsiClass psiClass;
    private PsiTreeChangeHandler psiTreeChangeHandler;

    public FSMDesignerController(Project project, FSMDesignerForm view) {
        this.project = project;
        this.view = view;
        psiManager = PsiManager.getInstance(project);
        init();
    }


    private void init() {
        initGraph();
        //psiClass = ClassUtil.findPsiClass(null, AbstractTransitManger.class.getName();
//        ;

        List<PsiClass> psiClasses = findTransitClasses();
        PsiClass currentFile = psiClasses.get(0);


        psiTreeChangeHandler = new PsiTreeChangeHandler();
        psiTreeChangeHandler.setGraph(graph);
        psiTreeChangeHandler.setModel(model);
        psiTreeChangeHandler.setPsiClass(currentFile);
        currentFile.getManager().addPsiTreeChangeListener(psiTreeChangeHandler);


        view.getFragmentClassLabel().setText(currentFile.getName());


        view.getAddFragmentButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        List<PsiClass> psiClassList = findFragmentClasses();
//                        Project project = event.getData(PlatformDataKeys.PROJECT);
//                        String txt= Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
                        String[] strings = new String[psiClassList.size()];
                        int i = 0;
                        for (PsiClass psiClass : psiClassList) {
                            strings[i++] = psiClass.getName();
                        }
                        String res = Messages.showEditableChooseDialog("choose", "Fragment", null, strings, strings[0], null);
                        createField(psiTreeChangeHandler.getPsiClass(), res, res);
                    }
                });
            }
        });

        view.getRefreshButton().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                psiTreeChangeHandler.updateStructure();
            }
        });


        psiTreeChangeHandler.updateStructure();
    }

    private List<PsiClass> findTransitClasses() {
        PsiClass transitPsiClass = ClassUtil.findPsiClass(psiManager, AbstractTransitManger.class.getName());
        PsiPackage psiPackage = JavaPsiFacade.getInstance(project).findPackage("");
        List<VirtualFile> vFiles = Arrays.asList(ProjectRootManager.getInstance(project).getContentSourceRoots());
        return findNeedClasses(GlobalSearchScope.filesScope(project, vFiles), psiPackage, transitPsiClass);
    }

    private List<PsiClass> findFragmentClasses() {
        PsiClass transitPsiClass = ClassUtil.findPsiClass(psiManager, Fragment.class.getName());
        PsiPackage psiPackage = JavaPsiFacade.getInstance(project).findPackage("");
        List<VirtualFile> vFiles = Arrays.asList(ProjectRootManager.getInstance(project).getContentSourceRoots());
        return findNeedClasses(GlobalSearchScope.filesScope(project, vFiles), psiPackage, transitPsiClass);
    }

    private List<PsiClass> findNeedClasses(GlobalSearchScope globalSearchScope, PsiPackage psiPackage, PsiClass psiClass) {
        List<PsiClass> result = new ArrayList<PsiClass>();
        PsiPackage[] psiPackages = globalSearchScope != null ? psiPackage.getSubPackages(globalSearchScope) : psiPackage.getSubPackages();
        for (PsiPackage aPackage : psiPackages) {
            result.addAll(findNeedClasses(null, aPackage, psiClass));
            for (PsiClass aClass : aPackage.getClasses()) {
                if (aClass.getSuperClass() != null && aClass.getSuperClass().equals(psiClass)) {
                    result.add(aClass);
                }
            }
        }
        return result;
    }


    private void createField(PsiClass psiClass, String from, String to) {
        PsiField psiTransitionsMapField = psiClass.findFieldByName(TRANSITIONS_MAP, true);
        if (psiTransitionsMapField != null) {
            PsiClassInitializer psiClassInitializer = null;
            PsiElementFactory psiElementFactory = new PsiElementFactoryImpl(psiManager);
            if (psiClass.getInitializers().length > 0) {
                psiClassInitializer = psiClass.getInitializers()[psiClass.getInitializers().length - 1];
            } else {
                psiClassInitializer = psiElementFactory.createClassInitializer();
                psiClass.getModifierList().addBefore(psiClassInitializer, psiClass.getRBrace());
            }

            String statment = psiTransitionsMapField.getName() + ".put(new TransitData<FragmentAction>("
                    + from
                    + ".class, FragmentAction.B1), new TransitResultData<FragmentAction>("
                    + to
                    + ".class));";
            PsiStatement psiStatement = psiElementFactory.createStatementFromText(statment, psiClassInitializer.getBody().getContext());
            psiClassInitializer.getModifierList().addBefore(psiStatement, psiClassInitializer.getBody().getRBrace());

            CodeStyleManager.getInstance(psiManager).reformat(psiClassInitializer);
        }
    }

    void initGraph() {
        model = new FSMGraphModel();
        graph = new JGraph(model);
//        graph.setCloneable(true);
        graph.setInvokesStopCellEditing(true);
        graph.setJumpToDefaultPort(true);

        view.getDesignerPanel().setLayout(new BoxLayout(view.getDesignerPanel(), BoxLayout.LINE_AXIS));
        view.getDesignerPanel().add(new JBScrollPane(graph));
    }
}
