package com.pajchi.taxiljubljana;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class EnterHomeAddressActivity extends AppCompatActivity {

    private Button saveHomeAddressButton;
    private PlaceAutocompleteFragment autocompleteFragment;
    private SettingsHandler settingsHandler;
    private Context thisActivityContextByName;
    private Place home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_home_address);
        getReferences();
        setViewListeners();
    }

    private void getReferences() {
        saveHomeAddressButton = findViewById(R.id.saveHomeAddressButton);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        settingsHandler = new SettingsHandler(this);
        thisActivityContextByName = EnterHomeAddressActivity.this;
    }

    private void setViewListeners() {
        saveHomeAddressButton.setOnClickListener(new SaveHomeAddressOnClickListener());
        autocompleteFragment.setOnPlaceSelectedListener(new HomeAddressSelectedListener());
    }

    private class SaveHomeAddressOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            settingsHandler.saveHome(home);
            if (settingsHandler.favouriteTaxiCompanyExists()) {
                Caller.callFavouriteTaxiCompany(thisActivityContextByName);
            } else {
                ActivityNavigator.goToChooseTaxiCompanyActivity(thisActivityContextByName);
            }
        }
    }

    private class HomeAddressSelectedListener implements PlaceSelectionListener {
        @Override
        public void onPlaceSelected(Place place) {
            home = place;
        }

        @Override
        public void onError(Status status) {
            Log.i(getResources().getString(R.string.api_places), "An error occurred: " + status);
        }
    }
}
