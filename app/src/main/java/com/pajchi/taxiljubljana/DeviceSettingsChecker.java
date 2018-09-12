package com.pajchi.taxiljubljana;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;

class DeviceSettingsChecker {
    public void turnOnLocation(MainActivity mainActivity) {
        statusCheck(mainActivity);
    }

    private void statusCheck(MainActivity mainActivity) {
        final LocationManager manager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(mainActivity);
        }
    }

    private void buildAlertMessageNoGps(final MainActivity mainActivity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        mainActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
