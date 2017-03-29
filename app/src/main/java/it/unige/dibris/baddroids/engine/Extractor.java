package it.unige.dibris.baddroids.engine;


import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.reference.MethodReference;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class Extractor {
    private Set<MethodInvocation> methodInvocations;
    private Set<String> androidDeclaredPermissions;
    private File apkFile;

    public Extractor(File apkFile) {
        this.apkFile = apkFile;
    }

    public Set<MethodInvocation> getMethodInvocations() {
        return methodInvocations;
    }

    public void setMethodInvocations(Set<MethodInvocation> methodInvocations) {
        this.methodInvocations = methodInvocations;
    }

    public Set<String> getAndroidDeclaredPermissions() {
        return androidDeclaredPermissions;
    }

    public void setAndroidDeclaredPermissions(Set<String> androidDeclaredPermissions) {
        this.androidDeclaredPermissions = androidDeclaredPermissions;
    }

    public File getApkFile() {
        return apkFile;
    }

    public void setApkFile(File apkFile) {
        this.apkFile = apkFile;
    }

    public void extract() throws IOException {
        extractPermissions();
        extractMethodInvocations();
    }


    private void extractPermissions() throws IOException {
        ApkMeta apkMeta = new ApkFile(apkFile).getApkMeta();
        androidDeclaredPermissions = new HashSet<>(apkMeta.getUsesPermissions());
    }


    private void extractMethodInvocations() throws IOException  {
        DexFile dexFile = DexFileFactory.loadDexFile(apkFile.toString(), 19, false);
        methodInvocations = new HashSet<>();
        for (ClassDef classDef : dexFile.getClasses()) {
            for (Method method : classDef.getMethods()) {
                MethodImplementation implementation = method.getImplementation();
                if (implementation == null) {
                    continue;
                }
                for (Instruction instruction : implementation.getInstructions()) {
                    switch (instruction.getOpcode()) {
                        case INVOKE_VIRTUAL:
                        case INVOKE_SUPER:
                        case INVOKE_DIRECT:
                        case INVOKE_STATIC:
                        case INVOKE_INTERFACE:
                            break;
                        default:
                            continue;
                    }
                    Instruction35c i35c = (Instruction35c) instruction;
                    MethodReference mr = (MethodReference) i35c.getReference();
                    String definingClass = mr.getDefiningClass();
                    if (definingClass.matches("^L(com/android|android|com/google|java).*$")) { // is an API
                        MethodInvocation mi = new MethodInvocation(definingClass, mr.getName());
                        methodInvocations.add(mi);
                    }
                }
            }
        }
    }
}
