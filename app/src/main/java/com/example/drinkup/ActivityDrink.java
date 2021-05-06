package com.example.drinkup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        Toast toast = Toast.makeText(this, ricerca, Toast.LENGTH_LONG);
        //toast.show();

        // Creao l'oggetto URL che rappresenta l'indirizzo della pagina da richiamare
        URL paginaURL = null;
        try {
            paginaURL = new URL("www.thecocktaildb.com/api/json/v1/1/search.php?s=" + ricerca);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast toast1 = Toast.makeText(this, "try1", Toast.LENGTH_LONG);
            toast1.show();
        }

        // creo l'oggetto HttpURLConnection e apro la connessione al server
        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) paginaURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            Toast toast2 = Toast.makeText(this, "try2", Toast.LENGTH_LONG);
            toast2.show();
        }

        // Recupero le informazioni inviate dal server
        try {
            InputStream risposta = new BufferedInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Toast toast3 = Toast.makeText(this, "try3", Toast.LENGTH_LONG);
            toast3.show();
        }

        //nomeDrink.setText(mostroDati(risposta));
        //Toast toast = Toast.makeText(this, "ciao", Toast.LENGTH_LONG);
        //toast.show();

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