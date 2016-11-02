package com.example.deman_glaisner.deman_glaisner_mobile;

/**
 * Created by julo on 29/10/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class RestaurantsList extends AppCompatActivity {


    private List<String> nameList = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private ListView listView;
    Hashtable<String, double[]> ht = new Hashtable<>();
    DownloadFragment dfrag2 = null;

    double[] bostonCord = new double[]{42.360083,-71.05888};
    double[] nyCord = {40.712784,-74.005941};
    double[] laCord= {34.052234,-118.243685};
    double[] chicagoCord= {41.878114,-87.629798};
    double[] seattleCord= {47.60621,-122.332071};
    double[] denverCord= {47.60621,-122.332071};
    double[] lasvegasCord= {36.169941,-115.13983};
    double[] miamiCord = {25.76168,-80.19179};
    double[] dallasCord = {32.776664,-96.796988};
    double[] sfCord ={37.77493,-122.419416};

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);
        listView = (ListView) findViewById(R.id.restaurant);
        String webContent = null;
        DownloadTask dlTask = new DownloadTask();

        ht.put("Boston", bostonCord);
        ht.put("New York", nyCord);
        ht.put("Los Angeles", laCord);
        ht.put("Chicago", chicagoCord);
        ht.put("Seattle", seattleCord);
        ht.put("Denver", denverCord);
        ht.put("Las Vegas", lasvegasCord);
        ht.put("Miami", miamiCord);
        ht.put("Dallas", dallasCord);
        ht.put("San Francisco", sfCord);

        Intent intent = getIntent();
        String text_spinner1 = intent.getExtras().getString("ville_choisie");

        double[] latlong = ht.get(text_spinner1);

        try {

            dlTask.execute("GET", "https://developers.zomato.com/api/v2.1/search?lat=" + String.valueOf(latlong[0]) +"&lon="+ String.valueOf(latlong[1]) +"&radius=1200", "JSON", "1");
            webContent = dlTask.get();

        } catch (InterruptedException | ExecutionException e) {
        }

        System.out.println("webcontent vaut: " + webContent);
        System.out.println("webcontent taille: " + webContent.length());

        Toast.makeText(getApplicationContext(), "ID premier: ", Toast.LENGTH_SHORT).show();

        // JSON PARSING POUR NAME ET RESTAURANT_ID

        try {

            JSONObject parentObject = new JSONObject(webContent);
            JSONArray parentArray = parentObject.getJSONArray("restaurants");


            //JSON MAGIC HERE JSON JSON JSON JSON JSON JSON JSON JSON jSON
            for (int i = 0; i < parentArray.length(); i++) {
                System.out.println("RECUPERATION DES VALEURS JSON");
                JSONObject finalObject = parentArray.getJSONObject(i);
                String restaurantName = finalObject.getJSONObject("restaurant").getString("name");
                System.out.println("Nom restaurant recupere: " + restaurantName);
                int id = finalObject.getJSONObject("restaurant").getJSONObject("R").getInt("res_id");
                System.out.println("restaurad ID recupere" + id);
                System.out.println("Ajout d'une valeur dans nameList");
                nameList.add(restaurantName);
                System.out.println("AJOUT d'une valeur dans idList");
                idList.add(id);

            }

            for(int i = 0; i < nameList.size(); i++) {
                System.out.println("VALEURS de nameList: " + nameList.get(i));
            }


            for(int i = 0; i < idList.size(); i++) {
                System.out.println("VALEURS DE idList: " + idList.get(i));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.restaurant);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                nameList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView adapterView, View view, final int position, long id) {

                Toast.makeText(getApplicationContext(), "infos: " + nameList.get(position), Toast.LENGTH_SHORT).show();

                dfrag2 = new DownloadFragment();
                dfrag2.show(getFragmentManager(),"test");
                System.out.println("niveau 1");
                Handler handler = new Handler();
                System.out.println("niveau 2");
                handler.postDelayed(new Runnable() {
                    public void run(){
                        System.out.println("niveau 3");
                        Intent i = new Intent(getApplicationContext(), RestaurantDescription.class);
                        i.putExtra("restaurantID", idList.get(position));
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
        if(dfrag2 != null)
            dfrag2.dismiss();
    }
}