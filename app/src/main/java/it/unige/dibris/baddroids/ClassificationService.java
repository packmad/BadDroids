package it.unige.dibris.baddroids;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import it.unige.dibris.baddroids.db.PermInvokeDbHelper;
import it.unige.dibris.baddroids.engine.Extractor;
import it.unige.dibris.baddroids.engine.Classifier;

import static it.unige.dibris.baddroids.Constants.PACKNAME;
import static it.unige.dibris.baddroids.Constants.ISMALWARE;
import static it.unige.dibris.baddroids.Constants.PROBABILITY;
import static it.unige.dibris.baddroids.Constants.TIME;


public class ClassificationService extends IntentService {
    private static final String BASE_APK = "BASE_APK";
    private static final String PACK_NAME = "PACK_NAME";
    private static final String CLASS_NAME = "ClassificationService";
    private static final int notificationId = 0;

    public ClassificationService() {
        super(CLASS_NAME);
    }


    public static void startClassification(Context context, String packname, String baseApk) {
        Intent intent = new Intent(context, ClassificationService.class);
        Bundle bundle = new Bundle();
        bundle.putString(BASE_APK, baseApk);
        bundle.putString(PACK_NAME, packname);
        intent.putExtras(bundle);
        context.startService(intent);

        // back to home screen
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String baseApk = intent.getStringExtra(BASE_APK);
            String packName = intent.getStringExtra(PACK_NAME);

            NotificationCompat.Builder startNotification =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("BaDDroids analysis")
                            .setContentText(packName)
                            .setAutoCancel(true)
                            .setNumber(1);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationId, startNotification.build());

            // Classification
            File apk = new File(baseApk);
            if (!apk.exists()) {
                Log.e(CLASS_NAME, String.format("%s doesn't exists!", baseApk));
                return;
            }

            Log.d(CLASS_NAME, String.format(">>> Start extracting from=%s", baseApk));
            PermInvokeDbHelper dbHelper = null;
            long start_time = System.nanoTime();
            Extractor extractor = new Extractor(apk);
            try {
                extractor.extract();
                dbHelper = PermInvokeDbHelper.getInstance(getApplicationContext());
                Log.d(CLASS_NAME, "--- extraction finished");

                Classifier classifier = new Classifier(extractor, dbHelper);
                Classifier.ClassifierResult classifierResult = classifier.getResult();
                long end_time = System.nanoTime();
                int delta_time = (int) ((end_time - start_time) / 1000000);

                boolean isMalware = classifierResult.isMalware();
                double probability = classifierResult.getProbability();

                String msg = String.format(Locale.ENGLISH, "apk=%s isMalware=%s prob=%f time=%dms", baseApk, isMalware, probability, delta_time);
                Log.d(CLASS_NAME, msg);

                Intent scanResultIntent = new Intent(this, ScanResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(PACKNAME, packName);
                bundle.putBoolean(ISMALWARE, isMalware);
                bundle.putDouble(PROBABILITY, probability);
                bundle.putInt(TIME, delta_time);
                scanResultIntent.putExtras(bundle);
                scanResultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(scanResultIntent);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dbHelper != null)
                    dbHelper.close();
            }
            Log.d(CLASS_NAME, String.format("<<< End extracting from=%s", baseApk));
        }
    }

}
