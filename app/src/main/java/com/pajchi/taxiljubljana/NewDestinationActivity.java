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

public class NewDestinationActivity extends AppCompatActivity {

    private Button chooseDestinationButton;
    private PlaceAutocompleteFragment autocompleteFragment;
    private SettingsHandler settingsHandler;
    private Context thisActivityContextByName;
    private Place destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_destination);
        getReferences();
        setViewListeners();
    }

    private void getReferences() {
        chooseDestinationButton = findViewById(R.id.saveHomeAddressButton);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);
        settingsHandler = new SettingsHandler(this);
        thisActivityContextByName = NewDestinationActivity.this;
    }

    private void setViewListeners() {
        chooseDestinationButton.setOnClickListener(new enterDestinationOnClickListener());
        autocompleteFragment.setOnPlaceSelectedListener(new destinationSelectedListener());
    }

    private class enterDestinationOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            settingsHandler.saveLastEnteredDestination(destination);
            if (settingsHandler.favouriteTaxiCompanyExists()) {
                ActivityNavigator.goToPerformCallActivity(thisActivityContextByName);
            } else {
                ActivityNavigator.goToChooseTaxiCompanyActivity(thisActivityContextByName);
            }
        }
    }

    private class destinationSelectedListener implements PlaceSelectionListener {
        @Override
        public void onPlaceSelected(Place place) {
            destination = place;
        }

        @Override
        public void onError(Status status) {
            Log.i(getResources().getString(R.string.api_places), "An error occurred: " + status);
        }
    }
}
