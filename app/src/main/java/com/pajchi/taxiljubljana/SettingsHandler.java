package com.pajchi.taxiljubljana;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.location.places.Place;

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

    public void saveHome(Place place) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(place.toString(), "");
        editor.apply();
    }

    public void saveFavouriteTaxiCompanyPhoneNumber(String taxiCompanyPhoneNumber) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(taxiCompanyPhoneNumber, "");
        editor.apply();
    }
}
