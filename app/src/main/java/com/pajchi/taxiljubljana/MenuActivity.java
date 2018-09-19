package com.pajchi.taxiljubljana;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button goHomeButton;
    private Button newDestinationButton;
    private Button directCallButton;
    private Button settingsButton;
    private SettingsHandler settingsHandler;
    private Context thisActivityContextByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getReferences();
        setViewListeners();
    }

    private void getReferences() {
        goHomeButton = findViewById(R.id.goHomeButton);
        newDestinationButton = findViewById(R.id.newDestinationButton);
        directCallButton = findViewById(R.id.directCallButton);
        settingsButton = findViewById(R.id.settingsButton);
        settingsHandler = new SettingsHandler(this);
        thisActivityContextByName = MenuActivity.this;
    }

    private void setViewListeners() {
        goHomeButton.setOnClickListener(new GoHomeOnClickListener());
        newDestinationButton.setOnClickListener(new NewDestinationOnClickListener());
        directCallButton.setOnClickListener(new DirectCallOnClickListener());
        settingsButton.setOnClickListener(new SettingsOnClickListener());
    }

    private class GoHomeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (settingsHandler.savedHomeAddressExists()) {
                if (settingsHandler.favouriteTaxiCompanyExists()) {
                    ActivityNavigator.goToPerformCallActivity(thisActivityContextByName);
                } else {
                    ActivityNavigator.goToChooseTaxiCompanyActivity(thisActivityContextByName);
                }
            } else {
                ActivityNavigator.goToEnterHomeAddressActivity(thisActivityContextByName);
            }
        }
    }

    private class NewDestinationOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ActivityNavigator.goToNewDestinationActivity(thisActivityContextByName);
        }
    }

    private class DirectCallOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ActivityNavigator.goToPerformCallActivity(thisActivityContextByName);
        }
    }

    private class SettingsOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // https://developer.android.com/guide/topics/ui/settings
        }
    }
}
