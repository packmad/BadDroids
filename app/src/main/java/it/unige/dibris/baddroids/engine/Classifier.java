package it.unige.dibris.baddroids.engine;

import java.util.HashSet;
import java.util.Set;

import it.unige.dibris.baddroids.db.PermInvokeDbHelper;


public class Classifier {
    private Extractor extractor;
    private PermInvokeDbHelper db;


    public Classifier(Extractor extractor, PermInvokeDbHelper db) {
        this.extractor = extractor;
        this.db = db;
        /*
        this.methodInvocationWeights = new HashSet<>(extractor.getMethodInvocations().size());
        this.androidDeclaredPermissionsWeights = new HashSet<>(extractor.getAndroidDeclaredPermissions().size());
        */
    }


    public class ClassifierResult {
        private boolean isMalware;
        private double probability;

        public ClassifierResult(boolean isMalware, double probability) {
            this.isMalware = isMalware;
            this.probability = probability;
        }

        public boolean isMalware() {
            return isMalware;
        }

        public double getProbability() {
            return probability;
        }
    }


    private static final double CONSTANT_B = -0.099553;
    private static final double CONSTANT_GAMMA= 5.5857;

    public ClassifierResult getResult() {
        double tmp, ris;

        ris = CONSTANT_B;
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
        return new ClassifierResult(ris > 0, 1 / (1 +  java.lang.Math.exp(-CONSTANT_GAMMA * ris)));
    }

}
