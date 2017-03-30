package it.unige.dibris.baddroids.engine;

import android.database.sqlite.SQLiteDatabase;

import java.util.Set;

/**
 * Created by simo on 30/03/17.
 */

public class MappingGenerator {
    private Extractor extractor;
    private Set<Long> methodInvocationsIds;
    private Set<Long> androidDeclaredPermissionsIds;

    public MappingGenerator(Extractor extractor) {
        this.extractor = extractor;
    }

    public void generate() {
        for (MethodInvocation mi : extractor.getMethodInvocations()) {

        }
    }

}
