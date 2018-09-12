package com.pajchi.taxiljubljana;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    public static String distance = "0";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    private Place mDestination = null;
    private TaxiAdapter globalTaxiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set view params
        setViewParams();

        // create place search fragment
        createPlaceSearchFragment();

        // start fetching GPS location periodically
        startFetchingLocation();

        // show existing taxi data
        showExistingTaxiData();

        // update taxi data
        try {
            new getAllTaxiLinksFromPageTask().execute(new URL("http://www.mojtaksi" +
                    ".si/taxi-prevozniki/taxi-ljubljana/#category_id_22"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(mCurrentLocation == null){
            System.out.println("mCurrentLocation is null");
        }

    }

    private void createPlaceSearchFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                System.out.println("PLACE_FRAGMENT: " + "Place: " + place.getName());

                mDestination = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.println("PLACE_FRAGMENT: " + "An error occurred: " + status);
            }
        });

    }

    private void startFetchingLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = new LocationRequest();
        // High accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Update interval in milliseconds
        mLocationRequest.setInterval(2000);
    /* Explicitly set the fastest interval for location updates, in milliseconds.
       This controls the fastest rate at which your application will receive location
       updates, which might be faster than setInterval(long) in some situations (for
       example, if other applications are triggering location updates).*/
        mLocationRequest.setFastestInterval(1000);
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

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    private Response.ErrorListener createResponseErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HTTP REQUEST ERROR", error.toString());
            }
        };
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    private String createSearchUrl() {

        String latitude = Double.toString(mCurrentLocation.getLatitude());
        String longitude = Double.toString(mCurrentLocation.getLongitude());
        String destinationCoords;
        if(mDestination == null){
            destinationCoords = "0.0,0.0";
        }
        else{
            destinationCoords =
                    Double.toString(mDestination.getLatLng().latitude)
                    + ","
                    + Double.toString(mDestination.getLatLng().longitude);
        }


        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="
                +  latitude + "," + longitude
                + "&destinations="
                + destinationCoords
                + "&key=AIzaSyBdbuZVZ_owjA87F9q0MAxce5VUgYS27M0";
    }


    private void setViewParams() {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbLoading);
        progressBar.setScaleX(0.6f);
        progressBar.setScaleY(0.6f);

    }

    private void showExistingTaxiData() {

        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
        final TextView tvStartFee = (TextView) findViewById(R.id.tvStartFee);
        final TextView tvWaitingHour = (TextView) findViewById(R.id.tvWaitingHour);
        final TextView tvRandomKm = (TextView) findViewById(R.id.tvRandomKm);
        final LinearLayout llDetails = (LinearLayout) findViewById(R.id.llDetails);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        databaseHelper.insertNewTaxi(new Taxi("SMURFTOWN", "17SMURF17", "17", "17", "17", "17",
                "17", "17", "17", "17"), db);

        final Cursor cursorAllTaxis = databaseHelper.getAllTaxis(db);

        // Now, we can use the CursorAdapter in the Activity to display an array of items into
        // the ListView:

        // Find ListView to populate
        final ListView lvTaxis = (ListView) findViewById(R.id.lvTaxis);
        // Setup cursor globalTaxiAdapter using cursor from last step
        TaxisCursorAdapter taxisCursorAdapter = new TaxisCursorAdapter(this, cursorAllTaxis);
        // Attach cursor globalTaxiAdapter to the ListView
        lvTaxis.setAdapter(taxisCursorAdapter);
        lvTaxis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < lvTaxis.getChildCount(); i++) {
                    View listItem = lvTaxis.getChildAt(i);
                    listItem.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.GREEN);

                // show details of selected taxi
                for (int i = 0; i < lvTaxis.getChildCount(); i++) {
                    int color = Color.TRANSPARENT;
                    Drawable background = lvTaxis.getChildAt(i).getBackground();
                    if (background instanceof ColorDrawable)
                        color = ((ColorDrawable) background).getColor();
                    if (color == Color.GREEN) {

                        cursorAllTaxis.moveToFirst();
                        while (cursorAllTaxis.getPosition() != i) {
                            cursorAllTaxis.moveToNext();
                            if (cursorAllTaxis.getPosition() > -1) {
                                System.out.println("ZDEJ SMO NA: " + cursorAllTaxis.getString(1));
                            }
                        }

                        tvName.setText(cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_NAME)));
                        tvPhone.setText(cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_PHONE)));
                        tvStartFee.setText(cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_START_FEE)));
                        tvWaitingHour.setText(cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_WAITING_HOUR)));
                        tvRandomKm.setText(cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_RANDOM_KM)));

                        llDetails.setVisibility(View.VISIBLE);

                        System.out.println("SHOWING DETAILS OF: "
                                + cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_NAME)));

                    }
                }

            }
        });

        // set button on click listener
        Button bCall = (Button) findViewById(R.id.bCall);
        bCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lvTaxis.getChildCount(); i++) {
                    int color = Color.TRANSPARENT;
                    Drawable background = lvTaxis.getChildAt(i).getBackground();
                    if (background instanceof ColorDrawable)
                        color = ((ColorDrawable) background).getColor();
                    if (color == Color.GREEN) {

                        cursorAllTaxis.moveToFirst();
                        while (cursorAllTaxis.getPosition() != i) {
                            cursorAllTaxis.moveToNext();
                            if (cursorAllTaxis.getPosition() > -1) {
                                System.out.println("ZDEJ SMO NA: " + cursorAllTaxis.getString(1));
                            }
                        }

                        System.out.println("CALLING: "
                                + cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_NAME)));

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"
                                + cursorAllTaxis.getString(
                                cursorAllTaxis.getColumnIndex(
                                        DatabaseContract.Taxis.COLUMN_NAME_PHONE))));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest
                                .permission
                                .CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST_CALL_PHONE);


                            return;
                        }


                        startActivity(callIntent);

                    }
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("PERMISSION_CALL", "granted");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION_CALL", "denied");
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("PERMISSION_COARSE", "granted");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION_COARSE", "denied");
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("PERMISSION_FINE", "granted");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION_FINE", "denied");
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        TextView tvLoading = (TextView) findViewById(R.id.tvLoading);
        tvLoading.setText("LAT: " + mCurrentLocation.getLatitude()
                + ", LON: " + mCurrentLocation.getLongitude());
        System.out.println("LAT: " + mCurrentLocation.getLatitude()
                + " | LON: " + mCurrentLocation.getLongitude());

        // if globalTaxiAdapter has been created, notify it that a new destination is ready for
        // the price to be estimated
        if(globalTaxiAdapter != null){
            globalTaxiAdapter.notifyDataSetChanged();
        }


        // calculate distance from current position to default destination
        calculateDistance();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }


    private class getAllTaxiLinksFromPageTask extends AsyncTask<URL, Integer, ArrayList> {

        protected ArrayList<Taxi> doInBackground(URL... urls) {

            ArrayList<Taxi> taxis = new ArrayList<>();

            for (URL url : urls) {

                BufferedReader in = null;
                try {
                    in = new BufferedReader(
                            new InputStreamReader(
                                    url.openStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String inputLine;

                try {
                    Taxi taxi;
                    if (in != null) {
                        while ((inputLine = in.readLine()) != null) {

                            if (inputLine.contains("portfolio-view")) {
                                String taxiURL = inputLine.substring(inputLine.indexOf("http"),
                                        inputLine.indexOf("\">"));

                                taxi = getTaxiFromLink(taxiURL);

                                if (taxi.getCity().equals("Ljubljana")) {
                                    taxis.add(taxi);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Escape early if cancel() is called
                if (isCancelled()) break;
            }

            if(taxis.size() == 0){
                taxis = manuallyCreateTaxis();
            }

            // sort taxis by price
            Collections.sort(taxis, new Comparator<Taxi>() {
                @Override public int compare(Taxi t1, Taxi t2) {
                    return Double.compare(
                            priceStringToDouble(t1.getRandom10Km()),
                            priceStringToDouble(t2.getRandom10Km()));
                    //
                    // Ascending
                }

            });

            return taxis;
        }

        private ArrayList<Taxi> manuallyCreateTaxis() {
            ArrayList<Taxi> taxis = new ArrayList<>();
            taxis.add(new Taxi("Ljubljana", "Metro", "080 11 90", "0,95", "0,89", "0,89", "0,89", "13,90", "8,9", "8,9"));
            taxis.add(new Taxi("Ljubljana", "Cammeo", "01 777 12 12", "0,85", "0,85", "0,85", "0,85", "20,00", "8,5", "8,5"));
            taxis.add(new Taxi("Ljubljana", "Intertours", "080 311 311", "1,00", "0,77", "0,77", "0,77", "14,80", "0,77", "7,7"));
            return taxis;
        }

        private Taxi getTaxiFromLink(String taxiURL) throws IOException {

            System.out.println();
            System.out.println("Handling link: " + taxiURL);

            String city = "NN", name = null, phone = null, startFee = null, regularKm = null,
                    contractKm = null, randomKm = null, waitingHour = null, regular10Km = null,
                    random10Km = null;

            BufferedReader in;
            in = new BufferedReader(
                    new InputStreamReader(
                            new URL(taxiURL).openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("Taksi v Ljubljani, ")) {

                    city = "Ljubljana";
                    System.out.println("CITY: " + city);

                    name = inputLine.substring(inputLine.indexOf(", ") + 2, inputLine.indexOf("."));
                    System.out.println("NAME: " + name);

                    inputLine = in.readLine();
                    phone = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine.indexOf
                            ("</"));
                    System.out.println("PHONE: " + phone);

                    in.readLine();
                    inputLine = in.readLine();
                    startFee = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine.indexOf
                            ("</"));
                    System.out.println("START_FEE: " + startFee);

                    inputLine = in.readLine();
                    regularKm = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine
                            .indexOf("</"));
                    System.out.println("REGULAR_KM: " + regularKm);

                    inputLine = in.readLine();
                    contractKm = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine
                            .indexOf("</"));
                    System.out.println("CONTRACT_KM: " + contractKm);

                    inputLine = in.readLine();
                    randomKm = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine
                            .indexOf("</"));
                    System.out.println("RANDOM_KM: " + randomKm);

                    inputLine = in.readLine();
                    waitingHour = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine
                            .indexOf("</"));
                    System.out.println("WAITING_HOUR: " + waitingHour);

                    in.readLine();
                    inputLine = in.readLine();
                    regular10Km = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine
                            .indexOf("</"));
                    System.out.println("REGULAR_10_KM: " + regular10Km);

                    inputLine = in.readLine();
                    random10Km = inputLine.substring(inputLine.indexOf(": ") + 2, inputLine
                            .indexOf("</"));
                    System.out.println("RANDOM_10_KM: " + random10Km);
                }
            }
            in.close();


            return new Taxi(city, name, phone, startFee, regularKm, contractKm, randomKm,
                    waitingHour, regular10Km, random10Km);
        }

        /**
         * Receives {@link ArrayList} of {@link Taxi} objects and creates a list in main activity
         * from it.
         *
         * @param taxis    {@link ArrayList} of {@link Taxi} objects received from the AsyncTask.
         */
        protected void onPostExecute(ArrayList taxis) {

            final TextView tvName = (TextView) findViewById(R.id.tvName);
            final TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
            final TextView tvStartFee = (TextView) findViewById(R.id.tvStartFee);
            final TextView tvWaitingHour = (TextView) findViewById(R.id.tvWaitingHour);
            final TextView tvRandomKm = (TextView) findViewById(R.id.tvRandomKm);
            final LinearLayout llDetails = (LinearLayout) findViewById(R.id.llDetails);

            if (taxis != null) {

                llDetails.setVisibility(View.INVISIBLE);

                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                // delete existing taxi data
                databaseHelper.deleteAllRows(DatabaseContract.Taxis.TABLE_NAME, db);

                // insert new taxi data
                for(int i = 0; i < taxis.size(); i++){

                    databaseHelper.insertNewTaxi((Taxi) taxis.get(i), db);
                }

                // Create the globalTaxiAdapter to convert the ArrayList to views
                globalTaxiAdapter = new TaxiAdapter(MainActivity.this, taxis);

                // Attach the globalTaxiAdapter to a ListView to update ListView taxi data
                final ListView listView = (ListView) findViewById(R.id.lvTaxis);
                listView.setAdapter(globalTaxiAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            View listItem = listView.getChildAt(i);
                            listItem.setBackgroundColor(Color.TRANSPARENT);
                        }
                        view.setBackgroundColor(Color.GREEN);

                        for (int i = 0; i < listView.getChildCount(); i++) {
                            int color = Color.TRANSPARENT;
                            Drawable background = listView.getChildAt(i).getBackground();
                            if (background instanceof ColorDrawable)
                                color = ((ColorDrawable) background).getColor();
                            if (color == Color.GREEN) {

                                tvName.setText(globalTaxiAdapter.getTaxis().get(i).getName());
                                tvPhone.setText(globalTaxiAdapter.getTaxis().get(i).getPhone());
                                tvStartFee.setText(globalTaxiAdapter.getTaxis().get(i).getStartFee());
                                tvWaitingHour.setText(globalTaxiAdapter.getTaxis().get(i).getWaitingHour());
                                tvRandomKm.setText(globalTaxiAdapter.getTaxis().get(i).getRandomKm());

                                llDetails.setVisibility(View.VISIBLE);

                                System.out.println("SHOWING DETAILS FOR: " + globalTaxiAdapter.getTaxis()
                                        .get(i).getName());
                            }
                        }

                    }
                });

                hideLoadingAnimation();

                // set button on click listener
                Button bCall = (Button) findViewById(R.id.bCall);
                bCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < listView.getChildCount(); i++) {
                            int color = Color.TRANSPARENT;
                            Drawable background = listView.getChildAt(i).getBackground();
                            if (background instanceof ColorDrawable)
                                color = ((ColorDrawable) background).getColor();
                            if (color == Color.GREEN) {
                                System.out.println("CALLING: " + globalTaxiAdapter.getTaxis().get(i)
                                        .getName());

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + globalTaxiAdapter.getTaxis().get(i)
                                        .getPhone()));
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest
                                        .permission
                                        .CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.CALL_PHONE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    return;
                                }
                                startActivity(callIntent);

                            }
                        }
                    }
                });

            }
        }
    }

    public static double priceStringToDouble(String random10Km) {
        random10Km = random10Km.replace(" €","");
        random10Km = random10Km.replace(",",".");
        return Double.parseDouble(random10Km);
    }

    private void hideLoadingAnimation() {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pbLoading);
        TextView textView = (TextView) findViewById(R.id.tvLoading);

        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

    }


}