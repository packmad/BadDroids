package it.unige.dibris.baddroids.engine;

import java.util.HashSet;
import java.util.Set;

import it.unige.dibris.baddroids.db.PermInvokeDbHelper;


public class Classifier {
    private Extractor extractor;
    private PermInvokeDbHelper db;

    /*
    private Set<Double> methodInvocationWeights;
    private Set<Double> androidDeclaredPermissionsWeights;
*/

    public Classifier(Extractor extractor, PermInvokeDbHelper db) {
        this.extractor = extractor;
        this.db = db;
        /*
        this.methodInvocationWeights = new HashSet<>(extractor.getMethodInvocations().size());
        this.androidDeclaredPermissionsWeights = new HashSet<>(extractor.getAndroidDeclaredPermissions().size());
        */
    }


    private static final double CONSTANT = -0.134799;

    public boolean isMalware() {
        double tmp, ris = CONSTANT;

        for (String pername : extractor.getAndroidDeclaredPermissions()) {
            tmp = db.getWeightromPermission(pername);
            if (tmp != -1) {
                ris += tmp;
            }
        }
        for (MethodInvocation mi : extractor.getMethodInvocations()) {
            tmp = db.getWeightFromInvoke(mi.toString());
            if (tmp != -1) {
                ris += tmp;
            }
        }
        return ris > 0;
    }

}
