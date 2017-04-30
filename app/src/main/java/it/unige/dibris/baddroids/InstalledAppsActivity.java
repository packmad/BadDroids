package it.unige.dibris.baddroids;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InstalledAppsActivity extends Activity {

    private InstalledAppsActivity self = this;
    private PackageManager packageManager;
    private ListView apps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installedapps);

        packageManager = getPackageManager();
        List<PackageInfo> apkMetaList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA); // all apps in the phone
        final List<PackageInfo> installedPackages = new ArrayList<>();

        apps = (ListView) findViewById(R.id.listView1);
        apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(self);
                alertDialogBuilder.setMessage("The analysis will continue in background");

                alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            PackageInfo pi = installedPackages.get(arg2);
                            String baseApk = pi.applicationInfo.publicSourceDir;
                            String packName = pi.applicationInfo.processName;
                            ClassificationService.startClassification(self, packName, baseApk);
                        }
                    });

                alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        try {
            for (PackageInfo packInfo : apkMetaList) {
                if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { //check weather it is system app or user installed app
                    try {
                        installedPackages.add(packInfo); // add in 2nd list if it is user installed app
                        Collections.sort(installedPackages,new Comparator<PackageInfo >()
                                // this will sort App list on the basis of app name
                        {
                            public int compare(PackageInfo o1, PackageInfo o2) {
                                return o1.applicationInfo.loadLabel(getPackageManager()).toString()
                                        .compareToIgnoreCase(o2.applicationInfo.loadLabel(getPackageManager())
                                                .toString());
                            }
                        });

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ListAdapter Adapter = new InstalledAppsAdapter(this, installedPackages, packageManager);
        apps.setAdapter(Adapter);
    }
}