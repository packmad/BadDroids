package it.unige.dibris.baddroids.engine;

import java.util.HashSet;
import java.util.Set;

import it.unige.dibris.baddroids.db.PermInvokeDbHelper;


public class MappingGenerator {
    private Extractor extractor;
    private PermInvokeDbHelper db;

    private Set<Long> methodInvocationsIds;
    private Set<Long> androidDeclaredPermissionsIds;


    public MappingGenerator(Extractor extractor, PermInvokeDbHelper db) {
        this.extractor = extractor;
        this.db = db;
        this.methodInvocationsIds = new HashSet<>(extractor.getMethodInvocations().size());
        this.androidDeclaredPermissionsIds = new HashSet<>(extractor.getAndroidDeclaredPermissions().size());
    }


    public void generate() {
        long id;
        for (String pername : extractor.getAndroidDeclaredPermissions()) {
            id = db.getIdFromPermission(pername);
            if (id != -1) {
                androidDeclaredPermissionsIds.add(id);
            }
        }

        for (MethodInvocation mi : extractor.getMethodInvocations()) {
            id = db.getIdFromInvoke(mi.toString());
            if (id != -1) {
                methodInvocationsIds.add(id);
            }
        }
    }

}
