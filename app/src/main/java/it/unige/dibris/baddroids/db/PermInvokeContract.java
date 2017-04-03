package it.unige.dibris.baddroids.db;

import android.provider.BaseColumns;


public final class PermInvokeContract {
    private PermInvokeContract() {}

    public static class PermissionEntry implements BaseColumns {
        public static final String TABLE_NAME = "permission";
        public static final String COLUMN_NAME_PERNAME = "perm_name";
        public static final String COLUMN_NAME_PERWEIGHT = "perm_weight";
    }

    public static class MethodInvocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "invoke";
        public static final String COLUMN_NAME_CLASS_METHOD = "class_method";
        public static final String COLUMN_NAME_INVWEIGHT = "inv_weight";
    }
}
