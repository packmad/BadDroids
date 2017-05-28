package it.unige.dibris.baddroids;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static it.unige.dibris.baddroids.Constants.DEXSIZE;
import static it.unige.dibris.baddroids.Constants.ISMALWARE;
import static it.unige.dibris.baddroids.Constants.PACKNAME;
import static it.unige.dibris.baddroids.Constants.PROBABILITY;
import static it.unige.dibris.baddroids.Constants.TIME;

public class ScanResultActivity extends Activity {
    final static double THRESHOLD = 80;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            final String packName = bundle.getString(PACKNAME);
            boolean isMalware = bundle.getBoolean(ISMALWARE);
            double probPercent = bundle.getDouble(PROBABILITY) * 100;
            double dexSizeKiB = (double)bundle.getLong(DEXSIZE) * 0.0009765625; // 1/1024
            int time = bundle.getInt(TIME);

            TextView detected = (TextView)findViewById(R.id.textViewDetected);
            TextView packNameResult = (TextView)findViewById(R.id.textViewPackNameResult);
            TextView scanTimeResult = (TextView)findViewById(R.id.textViewScanTimeResult);
            TextView probabilityResult = (TextView)findViewById(R.id.textViewProbabilityResult);
            TextView dexSizeResult = (TextView)findViewById(R.id.textViewDexSizeResult);
            Button buttonUninstall = (Button)findViewById(R.id.buttonUninstall);

            packNameResult.setText(packName);
            probabilityResult.setText(String.format(Locale.ENGLISH, "%.1f %%", probPercent));
            scanTimeResult.setText(String.format(Locale.ENGLISH, "%d ms", time));
            dexSizeResult.setText(String.format(Locale.ENGLISH, "%.1f KiB", dexSizeKiB));

            ImageView imageView = (ImageView)findViewById(R.id.imgDroidResult);
            if (isMalware && !isWhitelisted(packName)) {
                imageView.setImageResource(R.drawable.red_droid);
                detected.setText("MALWARE!");
                detected.setTextColor(Color.RED);
                buttonUninstall.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            PackageInfo packInfo = getPackageManager().getPackageInfo(packName, 0);
                            Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", packInfo.packageName, null));
                            startActivity(intent);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Cannot find apk for uninstall", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
            else {
                imageView.setImageResource(R.drawable.green_droid);
                detected.setText("CLEAN");
                detected.setTextColor(ContextCompat.getColor(context, R.color.holo_green_dark));
                buttonUninstall.setVisibility(View.INVISIBLE);
            }

            if (probPercent <= THRESHOLD) {
                imageView.setImageResource(R.drawable.orange_droid);
                detected.setTextColor(ContextCompat.getColor(context, R.color.DarkOrange));
                probabilityResult.setTextColor(ContextCompat.getColor(context, R.color.DarkOrange));
            }
        }

    }

    private boolean isWhitelisted(String packName) {
        Set<String> whitelist = new HashSet<>(6);
        whitelist.add("com.whatsapp");
        whitelist.add("com.facebook.orca");
        whitelist.add("com.instagram.android");
        whitelist.add("com.facebook.katana");
        whitelist.add("com.skype.raider");
        return whitelist.contains(packName);
    }
}
