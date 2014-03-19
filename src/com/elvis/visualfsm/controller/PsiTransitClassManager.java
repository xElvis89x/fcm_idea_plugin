package com.elvis.visualfsm.controller;

import com.fsm.transit.core.AbstractTransitManger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.ClassUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: elvis
 * Date: 3/19/14
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class PsiTransitClassManager {
    public static final String TRANSITIONS_MAP = "transitionsMap";

    private PsiClass psiClass;
    private Project project;
    private PsiManager psiManager;

    public PsiTransitClassManager(Project project) {
        this.project = project;
        psiManager = PsiManager.getInstance(project);
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public void createField(PsiClass psiClass, String from, String to) {
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

            PsiType actionType = psiClass.getExtendsList().getReferencedTypes()[0].getParameters()[0];
            String actionTypeName = actionType.getPresentableText();
            String statment = psiTransitionsMapField.getName() + ".put("
                    + "new TransitData<" + actionTypeName + ">(" + from + ".class, " + actionTypeName + ".B1), "
                    + "new TransitResultData<" + actionTypeName + ">(" + to + ".class));";
            PsiStatement psiStatement = psiElementFactory.createStatementFromText(statment, psiClassInitializer.getBody().getContext());
            psiClassInitializer.getModifierList().addBefore(psiStatement, psiClassInitializer.getBody().getRBrace());

            CodeStyleManager.getInstance(psiManager).reformat(psiClassInitializer);
        }
    }

    public List<PsiClass> findTransitClasses() {
        PsiClass transitPsiClass = ClassUtil.findPsiClass(psiManager, AbstractTransitManger.class.getName());
        PsiPackage psiPackage = JavaPsiFacade.getInstance(project).findPackage("");
        List<VirtualFile> vFiles = Arrays.asList(ProjectRootManager.getInstance(project).getContentSourceRoots());
        return findNeedClasses(GlobalSearchScope.filesScope(project, vFiles), psiPackage, transitPsiClass);
    }

    public List<PsiClass> findFragmentClasses() {
        PsiClass transitPsiClass = ClassUtil.findPsiClass(psiManager, "android.support.v4.app.Fragment");
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

}
