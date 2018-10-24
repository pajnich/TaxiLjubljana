package com.pajchi.taxiljubljana;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.pajchi.taxiljubljana.MainActivity.priceStringToDouble;
import static java.lang.Thread.sleep;

public class ChooseTaxiCompanyActivity extends AppCompatActivity implements LocationListener {

    private CardView cardCammeo;
    private CardView cardIntereks;
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
    private String previousActivity;
    private Location currentLocation;
    private Place destination;
    private String distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_taxi_company);
        setReferences();
        setValues();
        setListeners();
    }

    private void setReferences() {
        cardCammeo = findViewById(R.id.cardViewCammeo);
        cardIntereks = findViewById(R.id.cardViewIntereks);
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

    private void setValues() {
        setPreviousActivity();
        setCurrentLocation();
        currentLocation = null;
    }

    private void setListeners() {
        cardCammeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("1 777 1212");
                Caller.callFavouriteTaxiCompany(ChooseTaxiCompanyActivity.this);
            }
        });
        cardIntereks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("1 546 4066");
                Caller.callFavouriteTaxiCompany(ChooseTaxiCompanyActivity.this);
            }
        });
        cardIntertours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 311 311");
                Caller.callFavouriteTaxiCompany(ChooseTaxiCompanyActivity.this);
            }
        });
        cardLaguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 12 33");
                Caller.callFavouriteTaxiCompany(ChooseTaxiCompanyActivity.this);
            }
        });
        cardLjubljana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("1 234 9000");
                Caller.callFavouriteTaxiCompany(ChooseTaxiCompanyActivity.this);
            }
        });
        cardMetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 11 90");
                Caller.callFavouriteTaxiCompany(ChooseTaxiCompanyActivity.this);
            }
        });
        cardRondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsHandler.saveFavouriteTaxiCompanyPhoneNumber("080 900 900");
                Caller.callFavouriteTaxiCompany(ChooseTaxiCompanyActivity.this);
            }
        });
    }

    private void setPreviousActivity() {
        Intent intent = getIntent();
        previousActivity = intent.getStringExtra("previousActivity");
    }

    private void setCurrentLocation() {
        new Thread(new Runnable() {
            public void run() {
                while (currentLocation == null) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ChooseTaxiCompanyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDistanceToDestination();
                    }
                });
            }
        }).start();
    }

    private void setDistanceToDestination() {
        destination = null;
        if (previousActivity.equals("EnterHomeAddressActivity")) {
            destination = settingsHandler.getHome();
        } else if (previousActivity.equals("ChooseDestinationActivity")) {
            destination = settingsHandler.getLastEnteredDestination();
        } else if (previousActivity.equals("MenuActivity")) {
            destination = null;
        }
        calculateDistance();
    }

    private void calculateDistance() {

        final String url = createSearchUrl();

        System.out.println("START OF REQUEST");

        // create a request for a JsonArray of results
        // add the request to the requestQueue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                createResponseListener(),
                createResponseErrorListener()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private String createSearchUrl() {

        String latitude = Double.toString(currentLocation.getLatitude());
        String longitude = Double.toString(currentLocation.getLongitude());
        String destinationCoords;
        if (destination == null) {
            destinationCoords = "0.0,0.0";
        } else {
            destinationCoords =
                    Double.toString(destination.getLatLng().latitude)
                            + ","
                            + Double.toString(destination.getLatLng().longitude);
        }


        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="
                + latitude + "," + longitude
                + "&destinations="
                + destinationCoords
                + "&key="
                + getResources().getString(R.string.google_maps_api_key);
    }

    private Response.Listener<JSONObject> createResponseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    distance = response.getJSONArray("rows").getJSONObject(0).
                            getJSONArray("elements").getJSONObject(0).getJSONObject("distance").
                            get("value").toString();
                    System.out.println("RESPONSE: distance=" + distance + "m.");

                    showRidePrices(priceStringToDouble(distance));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void showRidePrices(double distance) {
        try {
            cenaCammeo.setText(PriceCalculator.getEstimatedPriceToDestinationByTaxiCompany(distance, "Cammeo"));
            cenaIntereks.setText(PriceCalculator.getEstimatedPriceToDestinationByTaxiCompany(distance, "Intereks"));
            cenaIntertours.setText(PriceCalculator.getEstimatedPriceToDestinationByTaxiCompany(distance, "Intertours"));
            cenaLaguna.setText(PriceCalculator.getEstimatedPriceToDestinationByTaxiCompany(distance, "Laguna"));
            cenaLjubljana.setText(PriceCalculator.getEstimatedPriceToDestinationByTaxiCompany(distance, "Ljubljana"));
            cenaMetro.setText(PriceCalculator.getEstimatedPriceToDestinationByTaxiCompany(distance, "Metro"));
            cenaRondo.setText(PriceCalculator.getEstimatedPriceToDestinationByTaxiCompany(distance, "Rondo"));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Oops, something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private Response.ErrorListener createResponseErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HTTP REQUEST ERROR", error.toString());
            }
        };
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }
}

