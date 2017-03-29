package it.unige.dibris.baddroids.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodInvocation implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

    private String definingClass;

    private String methodName;


    public MethodInvocation(String lineFromPermissionMapping) {
        String[] split = lineFromPermissionMapping.split("-");
        this.definingClass = fromDexToJavaPackname(split[0]);
        this.methodName = split[1];
    }

    public MethodInvocation(String definingClass, String methodName) {
        this.definingClass = fromDexToJavaPackname(definingClass);
        this.methodName = methodName;
    }

    static String fromDexToJavaPackname(String dexPackname) {
        dexPackname = dexPackname.substring(1).replaceAll("/", ".");
        dexPackname = dexPackname.replaceAll(";", "");
        return dexPackname;
    }


    public String getDefiningClass() {
        return definingClass;
    }

    public void setDefiningClass(String definingClass) {
        this.definingClass = definingClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodInvocation that = (MethodInvocation) o;

        if (!definingClass.equals(that.definingClass)) return false;
        return methodName.equals(that.methodName);
    }

    @Override
    public int hashCode() {
        int result = definingClass.hashCode();
        result = 31 * result + methodName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s->%s%n", definingClass, methodName);
    }

    public String toHookString() {
        return "{\"class_name\":\"" + definingClass + "\",\"method\":\""+ methodName + "\",\"thisObject\":false,\"type\":\"generic\"}";
    }


    private static final Pattern notObfuscated = Pattern.compile("^[a-zA-Z]{2,}(\\.[a-zA-Z0-9_$]{2,})*$");

    private static final Pattern validJava = Pattern.compile("^java.(time|util|lang|security|math)\\..*$");


    public boolean isRelevant() {
        Matcher matcher = notObfuscated.matcher(definingClass);
        if (matcher.matches()) {
            if (methodName.startsWith("zz")) {
                return false;
            }
            matcher = validJava.matcher(definingClass);
            if (!matcher.matches() && methodName.length() <= 2)
                return false;
            return true;
        }
        return false;
    }
}
