package com.pajchi.taxiljubljana;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class ChooseTaxiCompanyActivity extends AppCompatActivity {

    private CardView cardCammeo;
    private CardView cardintereks;
    private CardView cardIntertours;
    private CardView cardLaguna;
    private CardView cardLjubljana;
    private CardView cardMetro;
    private CardView cardRondo;
    private SettingsHandler settingsHandler;
    private TextView cenaCammeo;
    private TextView cenaIntereks;
    private TextView cenaIntertours;
    private TextView cenaLaguna;
    private TextView cenaLjubljana;
    private TextView cenaMetro;
    private TextView cenaRondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_taxi_company);
        setReferences();
        setListeners();
    }

    private void setReferences() {
        cardCammeo = findViewById(R.id.cardViewCammeo);
        cardintereks = findViewById(R.id.cardViewIntereks);
        cardIntertours = findViewById(R.id.cardViewIntertours);
        cardLaguna = findViewById(R.id.cardViewLaguna);
        cardLjubljana = findViewById(R.id.cardViewLjubljana);
        cardMetro = findViewById(R.id.cardViewMetro);
        cardRondo = findViewById(R.id.cardViewRondo);

        cenaCammeo = findViewById(R.id.textView4Cammeo);
        cenaIntereks = findViewById(R.id.textView4Intereks);
        cenaIntertours = findViewById(R.id.textView4Intertours);
        cenaLaguna = findViewById(R.id.textView4Laguna);
        cenaLjubljana = findViewById(R.id.textView4Ljubljana);
        cenaMetro = findViewById(R.id.textView4Metro);
        cenaRondo = findViewById(R.id.textView4Rondo);

        settingsHandler = new SettingsHandler(this);
    }

    private void setListeners() {
        cardCammeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("1 777 1212");
                ActivityNavigator.goToPerformCallActivity(ChooseTaxiCompanyActivity.this);
            }
        });
        cardintereks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("1 546 4066");
                ActivityNavigator.goToPerformCallActivity(ChooseTaxiCompanyActivity.this);
            }
        });
        cardIntertours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 311 311");
                ActivityNavigator.goToPerformCallActivity(ChooseTaxiCompanyActivity.this);
            }
        });
        cardLaguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 12 33");
                ActivityNavigator.goToPerformCallActivity(ChooseTaxiCompanyActivity.this);
            }
        });
        cardLjubljana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("1 234 9000");
                ActivityNavigator.goToPerformCallActivity(ChooseTaxiCompanyActivity.this);
            }
        });
        cardMetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 11 90");
                ActivityNavigator.goToPerformCallActivity(ChooseTaxiCompanyActivity.this);
            }
        });
        cardRondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 900 900");
                ActivityNavigator.goToPerformCallActivity(ChooseTaxiCompanyActivity.this);
            }
        });
    }
}
