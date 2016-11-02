package com.example.deman_glaisner.deman_glaisner_mobile;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deman_glaisner.deman_glaisner_mobile.db.FavoriContentProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by julo on 31/10/16.
 */


public class RestaurantDescription extends AppCompatActivity {
    String nameString, longitudeString, cuisineString, costString, ratingString, votesString, locationString, latitudeString;
    String webContent2 = null;
    TextView name, location, cuisine, cost, rating, votes, dist, dist2, dist3;
    CheckBox checkBox;
    Double latrest, longrest;
    int IDrecupere;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_description);

        DownloadTask dlTask2 = new DownloadTask();


        name = (TextView) findViewById(R.id.name);
        location = (TextView) findViewById(R.id.location);
        cuisine = (TextView) findViewById(R.id.cuisine);
        cost = (TextView) findViewById(R.id.cost);
        rating = (TextView) findViewById(R.id.note);
        votes = (TextView) findViewById(R.id.vote);
        dist = (TextView) findViewById(R.id.dist);
        dist2 = (TextView) findViewById(R.id.dist2);
        dist3 = (TextView) findViewById(R.id.dist3);
        checkBox= (CheckBox) findViewById(R.id.checkBox);


        Intent intent = getIntent();
        IDrecupere = intent.getExtras().getInt("restaurantID");
        String IDurl = String.valueOf(IDrecupere);
        Toast.makeText(getApplicationContext(), "ID du resto: " + IDrecupere, Toast.LENGTH_SHORT).show();

        getFavorite(IDrecupere);
        try {

            dlTask2.execute("GET", "https://developers.zomato.com/api/v2.1/restaurant?res_id=" + IDurl, "JSON", "2");
            webContent2 = dlTask2.get();

        } catch (InterruptedException | ExecutionException e) {
        }

        System.out.println("webcontent vaut: " + webContent2);
        System.out.println("webcontent taille: " + webContent2.length());

        Toast.makeText(getApplicationContext(), "ID premier: ", Toast.LENGTH_SHORT).show();

        // JSON PARSING POUR NAME ET RESTAURANT_ID

        try {

            JSONObject parentObject = new JSONObject(webContent2);

            nameString = parentObject.getString("name");
            System.out.println("Nom restaurant recupere: " + nameString);
            locationString = parentObject.getJSONObject("location").getString("address");
            cuisineString = parentObject.getString("cuisines");
            System.out.println("type de cuisine" + cuisineString);
            costString = parentObject.getString("average_cost_for_two");
            ratingString = parentObject.getJSONObject("user_rating").getString("aggregate_rating");
            votesString = parentObject.getJSONObject("user_rating").getString("aggregate_rating");
            System.out.println("Nb votes:" + votesString);
            latitudeString = parentObject.getJSONObject("location").getString("latitude");
            longitudeString = parentObject.getJSONObject("location").getString("longitude");
            latrest = Double.parseDouble(latitudeString);
            longrest = Double.parseDouble(longitudeString);

            name.setText(nameString);

            cuisine.setText(cuisineString);
            votes.setText(votesString);
            cost.setText(costString);
            rating.setText(ratingString);
            location.setText(locationString);

            Toast.makeText(getApplicationContext(), "212121212121", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ArrayList<LocationProvider> providers = new ArrayList<>();
        List<String> names = locationManager.getProviders(true);

        for (String name : names)
            providers.add(locationManager.getProvider(name));

        Criteria criter = new Criteria();
        criter.setAccuracy(Criteria.ACCURACY_FINE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 150, new LocationListener() {


            @Override

            public void onStatusChanged(String provider, int status, Bundle extras) {


            }


            @Override

            public void onProviderEnabled(String provider) {


            }


            @Override

            public void onProviderDisabled(String provider) {


            }


            @Override

            public void onLocationChanged(Location location) {

                Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
                dist.setText("Latitude" + location.getLatitude());
                dist2.setText("Longitude" + location.getLongitude());
                System.out.println(latrest);
                System.out.println(longrest);
                Location loc1 = new Location("");
                loc1.getLatitude();
                loc1.getLongitude();
                Location loc2 = new Location("");
                loc2.setLatitude(latrest);
                loc2.setLongitude(longrest);
                double distanceInMeters = loc1.distanceTo(loc2);
                double distanceInMetres2 = distanceInMeters/100;
                dist3.setText("Distance du restaurant" + distanceInMetres2);


            }


        });






    }




    public void addToFavorite(View view){
        if(checkBox.isChecked()) {
            try {
                Log.d("add bdd", "uri fonction");
                ContentValues values = new ContentValues();
                values.put(FavoriContentProvider.KEY_ID, IDrecupere);
                values.put(FavoriContentProvider.KEY_TITLE, nameString);
                getContentResolver().insert(FavoriContentProvider.CONTENT_URI, values);
                //Log.d("add bdd", uri.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            Log.d("Supp bdd", "uri fonction");
            Uri uri = FavoriContentProvider.CONTENT_URI;
            getContentResolver().delete(uri, FavoriContentProvider.KEY_ID+ "=?", new String[]{String.valueOf(IDrecupere)});




        }
    }

    private void getFavorite(int ID){
        Uri uri= FavoriContentProvider.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, FavoriContentProvider.KEY_ID + "=?", new String[]{String.valueOf(ID)}, null);

        //System.out.println("CURSEUR ====>" + cursor.getPosition());

        if(cursor.moveToFirst()) {
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }

    }


}
