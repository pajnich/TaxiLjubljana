package com.pajchi.taxiljubljana;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class Caller {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public static void callFavouriteTaxiCompany(Context context) {
        SettingsHandler settingsHandler = new SettingsHandler((Activity) context);
        String favouriteTaxiCompanyPhoneNumber = settingsHandler.getFavouriteTaxiCompanyPhoneNumber();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + favouriteTaxiCompanyPhoneNumber));
        context.startActivity(intent);
    }
}
