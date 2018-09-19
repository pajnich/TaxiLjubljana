package com.pajchi.taxiljubljana;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterHomeAddressActivity extends AppCompatActivity {

    private Button saveHomeAddressButton;
    private SettingsHandler settingsHandler;
    private Context thisActivityContextByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_home_address);
        getReferences();
        setViewListeners();
    }

    private void getReferences() {
        saveHomeAddressButton = findViewById(R.id.saveHomeAddressButton);
        settingsHandler = new SettingsHandler(this);
        thisActivityContextByName = EnterHomeAddressActivity.this;
    }

    private void setViewListeners() {
        saveHomeAddressButton.setOnClickListener(new SaveHomeAddressOnClickListener());
    }

    private class SaveHomeAddressOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (settingsHandler.favouriteTaxiCompanyExists()) {
                ActivityNavigator.goToPerformCallActivity(thisActivityContextByName);
            } else {
                ActivityNavigator.goToChooseTaxiCompanyActivity(thisActivityContextByName);
            }
        }
    }
}
