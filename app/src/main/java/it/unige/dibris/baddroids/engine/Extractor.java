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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        androidDeclaredPermissions = new HashSet<>();
        for (String p : apkMeta.getUsesPermissions()) {
            if (androidPermissions.contains(p))
                androidDeclaredPermissions.add(p);
        }
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
                    String name = mr.getName();
                    if (isRelevant(definingClass, name)) {
                        methodInvocations.add(new MethodInvocation(mr.getDefiningClass(), mr.getName()));
                    }
                }
            }
        }
    }

    private static final Pattern notObfuscated = Pattern.compile("^L[a-zA-Z]{2,}(/[a-zA-Z0-9_$]{2,})*;");
    private static final Pattern validJava = Pattern.compile("^Ljava/(time|util|lang|security|math)/.*$");
    private static final Pattern apiPackage = Pattern.compile("^L(com/android|android|com/google|java).*$");


    private boolean isRelevant(String definingClass, String name) {
        Matcher matchApi = apiPackage.matcher(definingClass);
        Matcher matchNotObfuscated = notObfuscated.matcher(definingClass);
        if (matchApi.matches() && matchNotObfuscated.matches()) {
            if (name.startsWith("zz")) {
                return false;
            }
            Matcher matchValidJava = validJava.matcher(definingClass);
            return !(!matchValidJava.matches() && name.length() <= 2);
        }
        return false;
    }


    private static final Set<String> androidPermissions = new HashSet<>(138);
    static {
        androidPermissions.add("com.android.voicemail.permission.ADD_VOICEMAIL");
        androidPermissions.add("com.android.launcher.permission.INSTALL_SHORTCUT");
        androidPermissions.add("com.android.voicemail.permission.READ_VOICEMAIL");
        androidPermissions.add("com.android.alarm.permission.SET_ALARM");
        androidPermissions.add("com.android.launcher.permission.UNINSTALL_SHORTCUT");
        androidPermissions.add("com.android.voicemail.permission.WRITE_VOICEMAIL");
        androidPermissions.add("android.permission.ACCESS_CHECKIN_PROPERTIES");
        androidPermissions.add("android.permission.ACCESS_COARSE_LOCATION");
        androidPermissions.add("android.permission.ACCESS_FINE_LOCATION");
        androidPermissions.add("android.permission.ACCESS_LOCATION_EXTRA_COMMANDS");
        androidPermissions.add("android.permission.ACCESS_NETWORK_STATE");
        androidPermissions.add("android.permission.ACCESS_NOTIFICATION_POLICY");
        androidPermissions.add("android.permission.ACCESS_WIFI_STATE");
        androidPermissions.add("android.permission.ACCOUNT_MANAGER");
        androidPermissions.add("android.permission.BATTERY_STATS");
        androidPermissions.add("android.permission.BIND_ACCESSIBILITY_SERVICE");
        androidPermissions.add("android.permission.BIND_APPWIDGET");
        androidPermissions.add("android.permission.BIND_CARRIER_MESSAGING_SERVICE");
        androidPermissions.add("android.permission.BIND_CARRIER_SERVICES");
        androidPermissions.add("android.permission.BIND_CHOOSER_TARGET_SERVICE");
        androidPermissions.add("android.permission.BIND_CONDITION_PROVIDER_SERVICE");
        androidPermissions.add("android.permission.BIND_DEVICE_ADMIN");
        androidPermissions.add("android.permission.BIND_DREAM_SERVICE");
        androidPermissions.add("android.permission.BIND_INCALL_SERVICE");
        androidPermissions.add("android.permission.BIND_INPUT_METHOD");
        androidPermissions.add("android.permission.BIND_MIDI_DEVICE_SERVICE");
        androidPermissions.add("android.permission.BIND_NFC_SERVICE");
        androidPermissions.add("android.permission.BIND_NOTIFICATION_LISTENER_SERVICE");
        androidPermissions.add("android.permission.BIND_PRINT_SERVICE");
        androidPermissions.add("android.permission.BIND_QUICK_SETTINGS_TILE");
        androidPermissions.add("android.permission.BIND_REMOTEVIEWS");
        androidPermissions.add("android.permission.BIND_SCREENING_SERVICE");
        androidPermissions.add("android.permission.BIND_TELECOM_CONNECTION_SERVICE");
        androidPermissions.add("android.permission.BIND_TEXT_SERVICE");
        androidPermissions.add("android.permission.BIND_TV_INPUT");
        androidPermissions.add("android.permission.BIND_VOICE_INTERACTION");
        androidPermissions.add("android.permission.BIND_VPN_SERVICE");
        androidPermissions.add("android.permission.BIND_VR_LISTENER_SERVICE");
        androidPermissions.add("android.permission.BIND_WALLPAPER");
        androidPermissions.add("android.permission.BLUETOOTH");
        androidPermissions.add("android.permission.BLUETOOTH_ADMIN");
        androidPermissions.add("android.permission.BLUETOOTH_PRIVILEGED");
        androidPermissions.add("android.permission.BODY_SENSORS");
        androidPermissions.add("android.permission.BROADCAST_PACKAGE_REMOVED");
        androidPermissions.add("android.permission.BROADCAST_SMS");
        androidPermissions.add("android.permission.BROADCAST_STICKY");
        androidPermissions.add("android.permission.BROADCAST_WAP_PUSH");
        androidPermissions.add("android.permission.CALL_PHONE");
        androidPermissions.add("android.permission.CALL_PRIVILEGED");
        androidPermissions.add("android.permission.CAMERA");
        androidPermissions.add("android.permission.CAPTURE_AUDIO_OUTPUT");
        androidPermissions.add("android.permission.CAPTURE_SECURE_VIDEO_OUTPUT");
        androidPermissions.add("android.permission.CAPTURE_VIDEO_OUTPUT");
        androidPermissions.add("android.permission.CHANGE_COMPONENT_ENABLED_STATE");
        androidPermissions.add("android.permission.CHANGE_CONFIGURATION");
        androidPermissions.add("android.permission.CHANGE_NETWORK_STATE");
        androidPermissions.add("android.permission.CHANGE_WIFI_MULTICAST_STATE");
        androidPermissions.add("android.permission.CHANGE_WIFI_STATE");
        androidPermissions.add("android.permission.CLEAR_APP_CACHE");
        androidPermissions.add("android.permission.CONTROL_LOCATION_UPDATES");
        androidPermissions.add("android.permission.DELETE_CACHE_FILES");
        androidPermissions.add("android.permission.DELETE_PACKAGES");
        androidPermissions.add("android.permission.DIAGNOSTIC");
        androidPermissions.add("android.permission.DISABLE_KEYGUARD");
        androidPermissions.add("android.permission.DUMP");
        androidPermissions.add("android.permission.EXPAND_STATUS_BAR");
        androidPermissions.add("android.permission.FACTORY_TEST");
        androidPermissions.add("android.permission.GET_ACCOUNTS");
        androidPermissions.add("android.permission.GET_ACCOUNTS_PRIVILEGED");
        androidPermissions.add("android.permission.GET_PACKAGE_SIZE");
        androidPermissions.add("android.permission.GET_TASKS");
        androidPermissions.add("android.permission.GLOBAL_SEARCH");
        androidPermissions.add("android.permission.INSTALL_LOCATION_PROVIDER");
        androidPermissions.add("android.permission.INSTALL_PACKAGES");
        androidPermissions.add("android.permission.INTERNET");
        androidPermissions.add("android.permission.KILL_BACKGROUND_PROCESSES");
        androidPermissions.add("android.permission.LOCATION_HARDWARE");
        androidPermissions.add("android.permission.MANAGE_DOCUMENTS");
        androidPermissions.add("android.permission.MASTER_CLEAR");
        androidPermissions.add("android.permission.MEDIA_CONTENT_CONTROL");
        androidPermissions.add("android.permission.MODIFY_AUDIO_SETTINGS");
        androidPermissions.add("android.permission.MODIFY_PHONE_STATE");
        androidPermissions.add("android.permission.MOUNT_FORMAT_FILESYSTEMS");
        androidPermissions.add("android.permission.MOUNT_UNMOUNT_FILESYSTEMS");
        androidPermissions.add("android.permission.NFC");
        androidPermissions.add("android.permission.PACKAGE_USAGE_STATS");
        androidPermissions.add("android.permission.PERSISTENT_ACTIVITY");
        androidPermissions.add("android.permission.PROCESS_OUTGOING_CALLS");
        androidPermissions.add("android.permission.READ_CALENDAR");
        androidPermissions.add("android.permission.READ_CALL_LOG");
        androidPermissions.add("android.permission.READ_CONTACTS");
        androidPermissions.add("android.permission.READ_EXTERNAL_STORAGE");
        androidPermissions.add("android.permission.READ_FRAME_BUFFER");
        androidPermissions.add("android.permission.READ_INPUT_STATE");
        androidPermissions.add("android.permission.READ_LOGS");
        androidPermissions.add("android.permission.READ_PHONE_STATE");
        androidPermissions.add("android.permission.READ_SMS");
        androidPermissions.add("android.permission.READ_SYNC_SETTINGS");
        androidPermissions.add("android.permission.READ_SYNC_STATS");
        androidPermissions.add("android.permission.REBOOT");
        androidPermissions.add("android.permission.RECEIVE_BOOT_COMPLETED");
        androidPermissions.add("android.permission.RECEIVE_MMS");
        androidPermissions.add("android.permission.RECEIVE_SMS");
        androidPermissions.add("android.permission.RECEIVE_WAP_PUSH");
        androidPermissions.add("android.permission.RECORD_AUDIO");
        androidPermissions.add("android.permission.REORDER_TASKS");
        androidPermissions.add("android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
        androidPermissions.add("android.permission.REQUEST_INSTALL_PACKAGES");
        androidPermissions.add("android.permission.RESTART_PACKAGES");
        androidPermissions.add("android.permission.SEND_RESPOND_VIA_MESSAGE");
        androidPermissions.add("android.permission.SEND_SMS");
        androidPermissions.add("android.permission.SET_ALWAYS_FINISH");
        androidPermissions.add("android.permission.SET_ANIMATION_SCALE");
        androidPermissions.add("android.permission.SET_DEBUG_APP");
        androidPermissions.add("android.permission.SET_PREFERRED_APPLICATIONS");
        androidPermissions.add("android.permission.SET_PROCESS_LIMIT");
        androidPermissions.add("android.permission.SET_TIME");
        androidPermissions.add("android.permission.SET_TIME_ZONE");
        androidPermissions.add("android.permission.SET_WALLPAPER");
        androidPermissions.add("android.permission.SET_WALLPAPER_HINTS");
        androidPermissions.add("android.permission.SIGNAL_PERSISTENT_PROCESSES");
        androidPermissions.add("android.permission.STATUS_BAR");
        androidPermissions.add("android.permission.SYSTEM_ALERT_WINDOW");
        androidPermissions.add("android.permission.TRANSMIT_IR");
        androidPermissions.add("android.permission.UPDATE_DEVICE_STATS");
        androidPermissions.add("android.permission.USE_FINGERPRINT");
        androidPermissions.add("android.permission.USE_SIP");
        androidPermissions.add("android.permission.VIBRATE");
        androidPermissions.add("android.permission.WAKE_LOCK");
        androidPermissions.add("android.permission.WRITE_APN_SETTINGS");
        androidPermissions.add("android.permission.WRITE_CALENDAR");
        androidPermissions.add("android.permission.WRITE_CALL_LOG");
        androidPermissions.add("android.permission.WRITE_CONTACTS");
        androidPermissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
        androidPermissions.add("android.permission.WRITE_GSERVICES");
        androidPermissions.add("android.permission.WRITE_SECURE_SETTINGS");
        androidPermissions.add("android.permission.WRITE_SETTINGS");
        androidPermissions.add("android.permission.WRITE_SYNC_SETTINGS");
    }
}
