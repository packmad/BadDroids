package it.unige.dibris.baddroids.domain;



import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public class Apk {

    protected String packName;

    protected String md5Hash;

    protected Set<Permission> permissions;

    protected Set<Permission> androidPermissions;

    protected Set<MethodInvocation> apiInvocations;

    protected Set<MethodInvocation> relevantApiInvocations;

    protected boolean isMalware;

    private List<String> libraryPermission;

    public List<String> getLibraryPermission() {
        return libraryPermission;
    }


    public Apk(File apkFile) throws IOException, IllegalArgumentException, net.dongliu.apk.parser.exception.ParserException {
        ApkMeta apkMeta = new ApkFile(apkFile).getApkMeta();
        packName = apkMeta.getPackageName();
        libraryPermission = apkMeta.getUsesPermissions();
        //sha256Hash = Files.hash(apkFile, Hashing.sha256()).toString();
        md5Hash = Files.hash(apkFile, Hashing.md5()).toString();
    }


    public Set<Permission> getAndroidPermissions() {
        return androidPermissions;
    }

    public void setAndroidPermissions(Set<Permission> androidPermissions) {
        this.androidPermissions = androidPermissions;
    }

    public Set<MethodInvocation> getRelevantApiInvocations() {
        return relevantApiInvocations;
    }

    public void setRelevantApiInvocations(Set<MethodInvocation> relevantApiInvocations) {
        this.relevantApiInvocations = relevantApiInvocations;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<MethodInvocation> getApiInvocations() {
        return apiInvocations;
    }

    public void setApiInvocations(Set<MethodInvocation> apiInvocations) {
        this.apiInvocations = apiInvocations;
    }

    public boolean isMalware() {
        return isMalware;
    }

    public void setMalware(boolean malware) {
        isMalware = malware;
    }

    @Override
    public String toString() {
        return "Apk{packName='" + packName + '\'' +
                ", md5Hash='" + md5Hash + '\'' +
                '}';
    }
}
