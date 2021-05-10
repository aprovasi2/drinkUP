package com.example.drinkup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

public class ActivityDrink extends AppCompatActivity implements View.OnClickListener {

    private Button button_Search;
    private EditText drinkDaCercare;
    private TextView nomeDrink;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        button_Search = (Button) findViewById(R.id.button_Search);
        button_Search.setOnClickListener(this);

        nomeDrink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        drinkDaCercare = (EditText) findViewById(R.id.editTextText_DrinkSearch);


    }


    @Override
    public void onClick(View v) {

        String ricerca = drinkDaCercare.getText().toString();
        Toast toast = Toast.makeText(this, "ho provato a ricercare "+ricerca, Toast.LENGTH_LONG);
        toast.show();


        /*
        InputStream risposta= null ;

        try {

            // Creao l'oggetto URL che rappresenta l'indirizzo della pagina da richiamare
            URL paginaURL = new URL("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + ricerca);
            Toast toast2 = Toast.makeText(this, "la url è  "+paginaURL, Toast.LENGTH_LONG);
            toast2.show();

            // creo l'oggetto HttpURLConnection e apro la connessione al server
            HttpURLConnection client = (HttpURLConnection) paginaURL.openConnection();

            // Recupero le informazioni inviate dal server
             risposta = new BufferedInputStream(client.getInputStream());

        } catch (Exception e) {
            Toast toast3 = Toast.makeText(this, "sono in eccezione ", Toast.LENGTH_LONG);
            toast3.show();

            e.printStackTrace();
        }


       String datiLetti = mostroDati(risposta);
        Toast toast3 = Toast.makeText(this, "i dati letti sono "+datiLetti, Toast.LENGTH_LONG);
        toast3.show();*/

    }




    private static String mostroDati(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


}