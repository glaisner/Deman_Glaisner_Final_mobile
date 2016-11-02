package com.example.deman_glaisner.deman_glaisner_mobile;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class DownloadTask extends AsyncTask<String, Void, String> {

    private String webMethod, myURL, postContent, fileType, downloadType;

    @Override
    protected String doInBackground(String... args) {
        try {

            webMethod = args[0];
            myURL = args[1];
            fileType = args[2];

            System.out.println("args ===> webMethod: " +webMethod);
            System.out.println("args ===> myurl: " +myURL);
            System.out.println("args ===> filetype: " +fileType);

            downloadType = args[3];
            System.out.println("DOWNLOADTYPE: " + downloadType);

            if(args.length == 5)
                postContent = args[4];

            System.out.println("avant downloadUrl");
            return downloadUrl();

        } catch (IOException e) {
            return "Error while downloading";
        }

    }

    private String downloadUrl() throws IOException {

        System.out.println("On est dans downloadUrl");
        String result1 = "Erreur";
        String result2 = "Erreur";
        String API_KEY = "92bf152a1c366bc032ffce163a0f5d44";
        System.out.println("On y est toujours");
        ArrayList<Integer> idlist = null;




        if (downloadType == "1") {

            System.out.println("Debut du try");
            URL url = new URL(myURL);
            System.out.println("URL passée: " + myURL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            System.out.println("instanciation de la connection");

            conn.setRequestMethod(webMethod);

            if (fileType.equals("JSON")) {
                System.out.println("Filetype est bien equal JSON");
                conn.setRequestProperty("Content-Type", "application/json");
                System.out.println("avant envoi de user key");
                conn.setRequestProperty("user-key", " " + API_KEY);
            }
            conn.connect();

            System.out.println("CONNEXION CONNEXION CONNEXION CONNEXION !!!!");

            InputStream is = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder buffer = new StringBuilder();
            String line = "";

            int checker = 0;


            while ((line = reader.readLine()) != null) {
                checker++;
                System.out.println("line length = " + line.length() + " and is equal to : " + line);
                System.out.println("bufferisation checker: " + checker + " fois");
                buffer.append(line);
            }

            System.out.println("la boucle de buffurisation a tourné " + checker + " fois");
            result1 = buffer.toString();
            //reader.reset();
            //is.reset();
            is.close();
            conn.disconnect();
            System.out.println("result vaut: " + buffer);
            System.out.println("result taille: " + result1.length());
            boolean bol = isJSONValid(result1);
            System.out.println("VALID JSON = " + bol);
            System.out.println("buffer las char = " + buffer.substring(buffer.length() - 1));


            System.out.println("retour de downloadUrl: " + result1);

            System.out.println("Retour buffer 11111111111");

            return result1;
        }

        else{

            System.out.println("Debut du try");
            URL url2 = new URL(myURL);
            System.out.println("URL passée: " + myURL);
            HttpsURLConnection conn2 = (HttpsURLConnection) url2.openConnection();
            System.out.println("instanciation de la connection");

            conn2.setRequestMethod(webMethod);

            if (fileType.equals("JSON")) {
                System.out.println("Filetype est bien equal JSON");
                conn2.setRequestProperty("Content-Type", "application/json");
                System.out.println("avant envoi de user key");
                conn2.setRequestProperty("user-key", " " + API_KEY);
            }
            conn2.connect();

            System.out.println("CONNEXION 22222 CONNEXION 22222 CONNEXION 222222 CONNEXION 222222 !!!!");

            InputStream is2 = conn2.getInputStream();

            BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2));

            StringBuilder buffer2 = new StringBuilder();
            String line2 = "";

            //int checker = 0;


            while ((line2 = reader2.readLine()) != null) {
                //checker++;
                System.out.println("line length = " + line2.length() + " and is equal to : " + line2);
                //System.out.println("bufferisation checker: " + checker + " fois");
                buffer2.append(line2);
            }

            //System.out.println("la boucle de buffurisation a tourné " + checker + " fois");
            result2 = buffer2.toString();
            //reader.reset();
            //is.reset();
            is2.close();
            conn2.disconnect();
            System.out.println("result vaut: " + buffer2);
            System.out.println("result taille: " + result2.length());
            boolean bol = isJSONValid(result2);
            System.out.println("VALID JSON = " + bol);
            System.out.println("buffer las char = " + buffer2.substring(buffer2.length() - 1));


            System.out.println("retour de downloadUrl: " + result2);

            System.out.println("retour buffer 222222222222222");

            return result2;
        }
    }

    //verifie que le JSON soit bien valide
    private boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
