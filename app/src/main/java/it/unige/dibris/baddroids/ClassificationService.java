package it.unige.dibris.baddroids;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import it.unige.dibris.baddroids.db.PermInvokeDbHelper;
import it.unige.dibris.baddroids.engine.Extractor;
import it.unige.dibris.baddroids.engine.MappingGenerator;


public class ClassificationService extends IntentService {
    private static final String BASE_APK = "BASE_APK";
    private static final String CLASS_NAME = "ClassificationService";


    public ClassificationService() {
        super(CLASS_NAME);
    }


    public static void startActionBaz(Context context, String baseApk) {
        Intent intent = new Intent(context, ClassificationService.class);
        Bundle bundle = new Bundle();
        bundle.putString(BASE_APK, baseApk);
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
                //dbHelper.try2Fix();
                    /*
                    Long test;
                    test = dbHelper.getIdFromPermission("android.permission.SEND_SMS");
                    test = dbHelper.getIdFromPermission("nothing");
                    test = dbHelper.getIdFromInvoke("java.lang.String-><init>");
                    test = dbHelper.getIdFromInvoke("nothing2");
                    */
                 Log.d(CLASS_NAME, "-- extraction finished");
                MappingGenerator mappingGenerator = new MappingGenerator(extractor, dbHelper);
                mappingGenerator.generate();
                long end_time = System.nanoTime();
                int delta_time = (int) ((end_time - start_time) / 1000000);
                Log.d(CLASS_NAME, "Time needed: " + delta_time + " ms");
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
