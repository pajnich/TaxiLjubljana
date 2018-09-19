package com.pajchi.taxiljubljana;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

class SettingsHandler {

    private final SharedPreferences sharedPref;
    private Activity activity;

    SettingsHandler(Activity activity) {
        this.activity = activity;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public boolean savedHomeAddressExists() {
        String savedHomeAddress = sharedPref.getString(activity.getString(R.string.homeAddress), "");
        return !savedHomeAddress.equals("");
    }

    public boolean favouriteTaxiCompanyExists() {
        String savedHomeAddress = sharedPref.getString(activity.getString(R.string.favouriteTaxiCompany), "");
        return !savedHomeAddress.equals("");
    }
}
