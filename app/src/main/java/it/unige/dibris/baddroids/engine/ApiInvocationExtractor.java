package it.unige.dibris.baddroids.engine;

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

import it.unige.dibris.baddroids.domain.MethodInvocation;

/**
 * Created by Simone Aonzo on 13/03/17.
 */
public class ApiInvocationExtractor extends InvocationExtractor {
    private File apk;

    public ApiInvocationExtractor(File apk) {
        this.apk = apk;
    }

    public File getApk() {
        return apk;
    }

    public void setApk(File apk) {
        this.apk = apk;
    }

    @Override
    public Set<MethodInvocation> extractApiInvocations(File apk) throws org.jf.util.ExceptionWithContext {
        if (!apk.exists())
            return null;

        DexFile dexFile;
        try {
            dexFile = DexFileFactory.loadDexFile(apk.toString(), 19, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        HashSet<MethodInvocation> apiInvocations = new HashSet<MethodInvocation>();
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
                        apiInvocations.add(mi);
                    }
                }
            }
        }
        return apiInvocations;
    }
}
