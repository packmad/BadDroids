package it.unige.dibris.baddroids;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static it.unige.dibris.baddroids.Constants.PACKNAME;
import static it.unige.dibris.baddroids.Constants.ISMALWARE;
import static it.unige.dibris.baddroids.Constants.TIME;

public class ScanResultActivity extends Activity {
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
            int time = bundle.getInt(TIME);

            TextView detected = (TextView)findViewById(R.id.textViewDetected);
            TextView packNameResult = (TextView)findViewById(R.id.textViewPackNameResult);
            TextView scanTimeResult = (TextView)findViewById(R.id.textViewScanTimeResult);
            Button buttonUninstall = (Button)findViewById(R.id.buttonUninstall);

            packNameResult.setText(packName);
            scanTimeResult.setText(time+"ms");

            View decorView = getWindow().getDecorView();
            if (isMalware) {
                decorView.setBackgroundColor(Color.RED);
                detected.setText("MALWARE DETECTED!");
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
                decorView.setBackgroundColor(Color.GREEN);
                detected.setText("CLEAN");
                buttonUninstall.setVisibility(View.INVISIBLE);
            }
        }

    }
}
