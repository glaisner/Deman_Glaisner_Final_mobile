package com.example.deman_glaisner.deman_glaisner_mobile;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {

    private DownloadFragment dfrag = null;

    Hashtable<String, double[]> ht = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        */

        Button searchButton = (Button) findViewById(R.id.button);
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text_spinner1 = spinner1.getSelectedItem().toString();
                System.out.println(text_spinner1);
                Toast.makeText(getApplicationContext(), "Liste pour: " + text_spinner1, Toast.LENGTH_SHORT).show();

                dfrag = new DownloadFragment();
                dfrag.show(getFragmentManager(),"test");
                System.out.println("niveau 1");
                Handler handler = new Handler();
                System.out.println("niveau 2");
                handler.postDelayed(new Runnable() {
                    public void run(){
                        System.out.println("niveau 3");
                        Intent i = new Intent(getApplicationContext(), RestaurantsList.class);
                        i.putExtra("ville_choisie", text_spinner1);
                        System.out.println("niveau 4");
                        startActivity(i);
                    }
                }, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dfrag != null)
            dfrag.dismiss();
    }
}
