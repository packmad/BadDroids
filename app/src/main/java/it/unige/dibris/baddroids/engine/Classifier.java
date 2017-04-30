package it.unige.dibris.baddroids.engine;

import it.unige.dibris.baddroids.db.PermInvokeDbHelper;


public class Classifier {

    /* model 7497
    private static final double CONSTANT_B = 0.12667684;
    private static final double CONSTANT_GAMMA = 5.6633748;
    */
    private static final double CONSTANT_B = -0.099553;
    private static final double CONSTANT_GAMMA = 5.5857;

    private Extractor extractor;
    private PermInvokeDbHelper db;


    public Classifier(Extractor extractor, PermInvokeDbHelper db) {
        this.extractor = extractor;
        this.db = db;
    }

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

}
