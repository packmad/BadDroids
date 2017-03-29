package it.unige.dibris.baddroids.domain;

import java.util.HashSet;
import java.util.Set;


public class Permission {
    private static final HashSet<Permission> androidPermissions;

    private String permissionName;

    public Permission(String permissionName) {
        this.permissionName = permissionName;
    }


    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() {
        return permissionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return permissionName.equals(that.permissionName);
    }

    @Override
    public int hashCode() {
        return permissionName.hashCode();
    }


    public static HashSet<Permission> getAndroidPermissions() {
        return androidPermissions;
    }

    static {
        androidPermissions = new HashSet<>(138);
        androidPermissions.add(new Permission("com.android.voicemail.permission.ADD_VOICEMAIL"));
        androidPermissions.add(new Permission("com.android.launcher.permission.INSTALL_SHORTCUT"));
        androidPermissions.add(new Permission("com.android.voicemail.permission.READ_VOICEMAIL"));
        androidPermissions.add(new Permission("com.android.alarm.permission.SET_ALARM"));
        androidPermissions.add(new Permission("com.android.launcher.permission.UNINSTALL_SHORTCUT"));
        androidPermissions.add(new Permission("com.android.voicemail.permission.WRITE_VOICEMAIL"));
        androidPermissions.add(new Permission("android.permission.ACCESS_CHECKIN_PROPERTIES"));
        androidPermissions.add(new Permission("android.permission.ACCESS_COARSE_LOCATION"));
        androidPermissions.add(new Permission("android.permission.ACCESS_FINE_LOCATION"));
        androidPermissions.add(new Permission("android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"));
        androidPermissions.add(new Permission("android.permission.ACCESS_NETWORK_STATE"));
        androidPermissions.add(new Permission("android.permission.ACCESS_NOTIFICATION_POLICY"));
        androidPermissions.add(new Permission("android.permission.ACCESS_WIFI_STATE"));
        androidPermissions.add(new Permission("android.permission.ACCOUNT_MANAGER"));
        androidPermissions.add(new Permission("android.permission.BATTERY_STATS"));
        androidPermissions.add(new Permission("android.permission.BIND_ACCESSIBILITY_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_APPWIDGET"));
        androidPermissions.add(new Permission("android.permission.BIND_CARRIER_MESSAGING_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_CARRIER_SERVICES"));
        androidPermissions.add(new Permission("android.permission.BIND_CHOOSER_TARGET_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_CONDITION_PROVIDER_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_DEVICE_ADMIN"));
        androidPermissions.add(new Permission("android.permission.BIND_DREAM_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_INCALL_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_INPUT_METHOD"));
        androidPermissions.add(new Permission("android.permission.BIND_MIDI_DEVICE_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_NFC_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_NOTIFICATION_LISTENER_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_PRINT_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_QUICK_SETTINGS_TILE"));
        androidPermissions.add(new Permission("android.permission.BIND_REMOTEVIEWS"));
        androidPermissions.add(new Permission("android.permission.BIND_SCREENING_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_TELECOM_CONNECTION_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_TEXT_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_TV_INPUT"));
        androidPermissions.add(new Permission("android.permission.BIND_VOICE_INTERACTION"));
        androidPermissions.add(new Permission("android.permission.BIND_VPN_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_VR_LISTENER_SERVICE"));
        androidPermissions.add(new Permission("android.permission.BIND_WALLPAPER"));
        androidPermissions.add(new Permission("android.permission.BLUETOOTH"));
        androidPermissions.add(new Permission("android.permission.BLUETOOTH_ADMIN"));
        androidPermissions.add(new Permission("android.permission.BLUETOOTH_PRIVILEGED"));
        androidPermissions.add(new Permission("android.permission.BODY_SENSORS"));
        androidPermissions.add(new Permission("android.permission.BROADCAST_PACKAGE_REMOVED"));
        androidPermissions.add(new Permission("android.permission.BROADCAST_SMS"));
        androidPermissions.add(new Permission("android.permission.BROADCAST_STICKY"));
        androidPermissions.add(new Permission("android.permission.BROADCAST_WAP_PUSH"));
        androidPermissions.add(new Permission("android.permission.CALL_PHONE"));
        androidPermissions.add(new Permission("android.permission.CALL_PRIVILEGED"));
        androidPermissions.add(new Permission("android.permission.CAMERA"));
        androidPermissions.add(new Permission("android.permission.CAPTURE_AUDIO_OUTPUT"));
        androidPermissions.add(new Permission("android.permission.CAPTURE_SECURE_VIDEO_OUTPUT"));
        androidPermissions.add(new Permission("android.permission.CAPTURE_VIDEO_OUTPUT"));
        androidPermissions.add(new Permission("android.permission.CHANGE_COMPONENT_ENABLED_STATE"));
        androidPermissions.add(new Permission("android.permission.CHANGE_CONFIGURATION"));
        androidPermissions.add(new Permission("android.permission.CHANGE_NETWORK_STATE"));
        androidPermissions.add(new Permission("android.permission.CHANGE_WIFI_MULTICAST_STATE"));
        androidPermissions.add(new Permission("android.permission.CHANGE_WIFI_STATE"));
        androidPermissions.add(new Permission("android.permission.CLEAR_APP_CACHE"));
        androidPermissions.add(new Permission("android.permission.CONTROL_LOCATION_UPDATES"));
        androidPermissions.add(new Permission("android.permission.DELETE_CACHE_FILES"));
        androidPermissions.add(new Permission("android.permission.DELETE_PACKAGES"));
        androidPermissions.add(new Permission("android.permission.DIAGNOSTIC"));
        androidPermissions.add(new Permission("android.permission.DISABLE_KEYGUARD"));
        androidPermissions.add(new Permission("android.permission.DUMP"));
        androidPermissions.add(new Permission("android.permission.EXPAND_STATUS_BAR"));
        androidPermissions.add(new Permission("android.permission.FACTORY_TEST"));
        androidPermissions.add(new Permission("android.permission.GET_ACCOUNTS"));
        androidPermissions.add(new Permission("android.permission.GET_ACCOUNTS_PRIVILEGED"));
        androidPermissions.add(new Permission("android.permission.GET_PACKAGE_SIZE"));
        androidPermissions.add(new Permission("android.permission.GET_TASKS"));
        androidPermissions.add(new Permission("android.permission.GLOBAL_SEARCH"));
        androidPermissions.add(new Permission("android.permission.INSTALL_LOCATION_PROVIDER"));
        androidPermissions.add(new Permission("android.permission.INSTALL_PACKAGES"));
        androidPermissions.add(new Permission("android.permission.INTERNET"));
        androidPermissions.add(new Permission("android.permission.KILL_BACKGROUND_PROCESSES"));
        androidPermissions.add(new Permission("android.permission.LOCATION_HARDWARE"));
        androidPermissions.add(new Permission("android.permission.MANAGE_DOCUMENTS"));
        androidPermissions.add(new Permission("android.permission.MASTER_CLEAR"));
        androidPermissions.add(new Permission("android.permission.MEDIA_CONTENT_CONTROL"));
        androidPermissions.add(new Permission("android.permission.MODIFY_AUDIO_SETTINGS"));
        androidPermissions.add(new Permission("android.permission.MODIFY_PHONE_STATE"));
        androidPermissions.add(new Permission("android.permission.MOUNT_FORMAT_FILESYSTEMS"));
        androidPermissions.add(new Permission("android.permission.MOUNT_UNMOUNT_FILESYSTEMS"));
        androidPermissions.add(new Permission("android.permission.NFC"));
        androidPermissions.add(new Permission("android.permission.PACKAGE_USAGE_STATS"));
        androidPermissions.add(new Permission("android.permission.PERSISTENT_ACTIVITY"));
        androidPermissions.add(new Permission("android.permission.PROCESS_OUTGOING_CALLS"));
        androidPermissions.add(new Permission("android.permission.READ_CALENDAR"));
        androidPermissions.add(new Permission("android.permission.READ_CALL_LOG"));
        androidPermissions.add(new Permission("android.permission.READ_CONTACTS"));
        androidPermissions.add(new Permission("android.permission.READ_EXTERNAL_STORAGE"));
        androidPermissions.add(new Permission("android.permission.READ_FRAME_BUFFER"));
        androidPermissions.add(new Permission("android.permission.READ_INPUT_STATE"));
        androidPermissions.add(new Permission("android.permission.READ_LOGS"));
        androidPermissions.add(new Permission("android.permission.READ_PHONE_STATE"));
        androidPermissions.add(new Permission("android.permission.READ_SMS"));
        androidPermissions.add(new Permission("android.permission.READ_SYNC_SETTINGS"));
        androidPermissions.add(new Permission("android.permission.READ_SYNC_STATS"));
        androidPermissions.add(new Permission("android.permission.REBOOT"));
        androidPermissions.add(new Permission("android.permission.RECEIVE_BOOT_COMPLETED"));
        androidPermissions.add(new Permission("android.permission.RECEIVE_MMS"));
        androidPermissions.add(new Permission("android.permission.RECEIVE_SMS"));
        androidPermissions.add(new Permission("android.permission.RECEIVE_WAP_PUSH"));
        androidPermissions.add(new Permission("android.permission.RECORD_AUDIO"));
        androidPermissions.add(new Permission("android.permission.REORDER_TASKS"));
        androidPermissions.add(new Permission("android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"));
        androidPermissions.add(new Permission("android.permission.REQUEST_INSTALL_PACKAGES"));
        androidPermissions.add(new Permission("android.permission.RESTART_PACKAGES"));
        androidPermissions.add(new Permission("android.permission.SEND_RESPOND_VIA_MESSAGE"));
        androidPermissions.add(new Permission("android.permission.SEND_SMS"));
        androidPermissions.add(new Permission("android.permission.SET_ALWAYS_FINISH"));
        androidPermissions.add(new Permission("android.permission.SET_ANIMATION_SCALE"));
        androidPermissions.add(new Permission("android.permission.SET_DEBUG_APP"));
        androidPermissions.add(new Permission("android.permission.SET_PREFERRED_APPLICATIONS"));
        androidPermissions.add(new Permission("android.permission.SET_PROCESS_LIMIT"));
        androidPermissions.add(new Permission("android.permission.SET_TIME"));
        androidPermissions.add(new Permission("android.permission.SET_TIME_ZONE"));
        androidPermissions.add(new Permission("android.permission.SET_WALLPAPER"));
        androidPermissions.add(new Permission("android.permission.SET_WALLPAPER_HINTS"));
        androidPermissions.add(new Permission("android.permission.SIGNAL_PERSISTENT_PROCESSES"));
        androidPermissions.add(new Permission("android.permission.STATUS_BAR"));
        androidPermissions.add(new Permission("android.permission.SYSTEM_ALERT_WINDOW"));
        androidPermissions.add(new Permission("android.permission.TRANSMIT_IR"));
        androidPermissions.add(new Permission("android.permission.UPDATE_DEVICE_STATS"));
        androidPermissions.add(new Permission("android.permission.USE_FINGERPRINT"));
        androidPermissions.add(new Permission("android.permission.USE_SIP"));
        androidPermissions.add(new Permission("android.permission.VIBRATE"));
        androidPermissions.add(new Permission("android.permission.WAKE_LOCK"));
        androidPermissions.add(new Permission("android.permission.WRITE_APN_SETTINGS"));
        androidPermissions.add(new Permission("android.permission.WRITE_CALENDAR"));
        androidPermissions.add(new Permission("android.permission.WRITE_CALL_LOG"));
        androidPermissions.add(new Permission("android.permission.WRITE_CONTACTS"));
        androidPermissions.add(new Permission("android.permission.WRITE_EXTERNAL_STORAGE"));
        androidPermissions.add(new Permission("android.permission.WRITE_GSERVICES"));
        androidPermissions.add(new Permission("android.permission.WRITE_SECURE_SETTINGS"));
        androidPermissions.add(new Permission("android.permission.WRITE_SETTINGS"));
        androidPermissions.add(new Permission("android.permission.WRITE_SYNC_SETTINGS"));
    }
}
