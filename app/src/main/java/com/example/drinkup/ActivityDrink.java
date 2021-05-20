package com.example.drinkup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

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
import java.util.List;

public class ActivityDrink extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="ActivityDrink" ;
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

        List<Drink> drinkListWithGson = getDrinksWithGson();


       



        /*for (Drink drink : drinkListWithGson) {
            Log.d(TAG, drink.toString());
            Toast toast2 = Toast.makeText(this, "drink1"+drink.getStrDrink(), Toast.LENGTH_LONG);
            toast2.show();
        }*/


    }



    private List<Drink> getDrinksWithGson() {
        InputStream fileInputStream = null;
        JsonReader jsonReader;
        try {
            fileInputStream = this.getAssets().open("search_margarita.json");
            jsonReader = new JsonReader(new InputStreamReader(fileInputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        Response response = new Gson().fromJson(bufferedReader, Response.class);

        return response.getDrinks();
    }



}