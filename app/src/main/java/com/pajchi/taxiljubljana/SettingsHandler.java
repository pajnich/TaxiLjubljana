package com.pajchi.taxiljubljana;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;

class SettingsHandler {

    private final SharedPreferences sharedPref;
    private Activity activity;

    SettingsHandler(Activity activity) {
        this.activity = activity;
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public boolean savedHomeAddressExists() {
        String savedHomeAddress = sharedPref.getString(activity.getString(R.string.home), "");
        return !savedHomeAddress.equals("");
    }

    public boolean favouriteTaxiCompanyExists() {
        String savedHomeAddress = sharedPref.getString(activity.getString(R.string.favouriteTaxiCompany), "");
        return !savedHomeAddress.equals("");
    }

    public Place getHome() {
        return new Gson().fromJson(sharedPref.getString(activity.getString(R.string.home), ""), Place.class);
    }

    public void saveHome(Place place) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.home), place.toString());
        editor.apply();
    }

    public String getFavouriteTaxiCompanyPhoneNumber() {
        return sharedPref.getString(activity.getString(R.string.favouriteTaxiCompany), "");
    }

    public void saveFavouriteTaxiCompanyPhoneNumber(String taxiCompanyPhoneNumber) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.favouriteTaxiCompany), taxiCompanyPhoneNumber);
        editor.apply();
    }

    public Place getLastEnteredDestination() {
        return new Gson().fromJson(sharedPref.getString(activity.getString(R.string.lastEnteredDestination), ""), Place.class);
    }

    public void saveLastEnteredDestination(Place destination) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.lastEnteredDestination), destination.toString());
        editor.apply();
    }
}
