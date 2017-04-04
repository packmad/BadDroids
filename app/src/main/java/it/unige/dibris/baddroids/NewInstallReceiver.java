package it.unige.dibris.baddroids;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NewInstallReceiver extends BroadcastReceiver {
    private static final String TAG = NewInstallReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getEncodedSchemeSpecificPart();
        Log.d(TAG, "New app installed: " + packageName);
        ClassificationService.startClassification(context, ""); //TODO
    }
}
