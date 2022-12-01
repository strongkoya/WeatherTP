package com.example.weathertp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    String ville, url;
    TextView textView;
    String temperature = "";
    String wind = "";
    String description = "";
    // Array of strings...
    ListView listView;
    String listeGouv[] = {"Ariana", "Beja", "Benarous", "Bizerte", "Gabas", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidibouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"};

    private static String urlInit = "https://goweather.herokuapp.com/weather/";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.simpleListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview, R.id.textView, listeGouv);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                textView = (TextView) view.findViewById(R.id.textView);
                ville = textView.getText().toString();
                url = urlInit + ville;
                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();

                new getTemp().execute();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        // .setView(android.R.layout.activity_list_item)
//set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                        .setTitle("Temperature à " + ville + " : " + temperature)
//set message
                        .setMessage("Vitesse du vent :" + wind + '\n' + description)
//set positive button
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                finish();
                            }
                        })
//set negative button
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });


    }


    private class getTemp extends AsyncTask<Void, Void, Void> {
        // String tempera;

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressBar(MainActivity.this);
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected Void doInBackground(Void... voids) {
            Handler handler = new Handler();
            String jsonString = handler.httpServiceCall(url);
            if (jsonString != null) {

                JSONObject jsonObject = null;
                try {
                    Log.d("Hello", jsonString);
                    jsonObject = new JSONObject(jsonString);
                    Log.d("Hello 1", "ffhfhfhfh 123");


                    temperature = jsonObject.getString("temperature");

                    wind = jsonObject.getString("wind");
                    description = jsonObject.getString("description");

                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), jsonString, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Json parsing Error", Toast.LENGTH_LONG).show();

                        }
                    });
                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                    }
                });

            }
            return null;
        }
    }

}