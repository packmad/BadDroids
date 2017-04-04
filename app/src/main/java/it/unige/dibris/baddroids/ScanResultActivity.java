package it.unige.dibris.baddroids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static it.unige.dibris.baddroids.Constants.APK;
import static it.unige.dibris.baddroids.Constants.ISMALWARE;

public class ScanResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String apkFile = bundle.getString(APK);
            boolean isMalware = bundle.getBoolean(ISMALWARE);

            TextView tv1 = (TextView)findViewById(R.id.textViewScanResult);
            tv1.setText(apkFile + " isMalware? " + isMalware);

            if (isMalware) {
                getWindow().getDecorView().setBackgroundColor(Color.RED);
            }
        }

    }
}
