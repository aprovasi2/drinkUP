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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Response;
import com.example.drinkup.repositories.DrinkRepository;
import com.example.drinkup.repositories.IDrinkRepository;
import com.example.drinkup.repositories.ResponseCallback;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ActivityDrink extends AppCompatActivity implements View.OnClickListener, ResponseCallback {

    private static final String TAG ="ActivityDrink" ;

    private IDrinkRepository drinkRepository;


    private List<Drink> drinksWithDrinksApi;

    private Button button_Search;
    private EditText drinkDaCercare;
    private TextView nomeDrink;
    private ImageView imageViewDownload;

    private TextView textView_Alchool_Drink;
    private TextView textView_Ingredienti_Drink;
    private TextView textView_Preparazione_Drink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        textView_Alchool_Drink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        textView_Ingredienti_Drink = (TextView) findViewById(R.id.textView_Ingredienti_Drink);
        textView_Preparazione_Drink = (TextView) findViewById(R.id.textView_Preparazione_Drink);

        imageViewDownload = (ImageView) findViewById(R.id.imageView_Drink);

        button_Search = (Button) findViewById(R.id.button_Search);
        button_Search.setOnClickListener(this);

        nomeDrink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        drinkDaCercare = (EditText) findViewById(R.id.editTextText_DrinkSearch);

        drinkRepository = new DrinkRepository(this, this.getApplication());
        drinksWithDrinksApi=new ArrayList<>();

    }


    @Override
    public void onClick(View v) {

        String ricerca = drinkDaCercare.getText().toString();
        Toast toast = Toast.makeText(this, "ho provato a ricercare "+ricerca, Toast.LENGTH_LONG);
        toast.show();

        List<Drink> drinkListWithGson = getDrinksWithGson();
        drinkRepository.fetchDrinks(ricerca);
/*
        for (Drink drink : drinkListWithGson) {
            Log.d(TAG, drink.toString());
            Toast toast2 = Toast.makeText(this, "drink1"+drink.getStrDrink(), Toast.LENGTH_LONG);
            toast2.show();
        }
*/
       //imgGlide(drinkListWithGson.get(1).getStrDrinkThumb());



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


    @Override
    public void onResponse(List<Drink> drinkList) {

        drinksWithDrinksApi.addAll(drinkList);
        Toast toast3 = Toast.makeText(this, "Drinklistapi è lungo "+drinksWithDrinksApi.size(), Toast.LENGTH_LONG);
        toast3.show();
        for (Drink drink : drinksWithDrinksApi) {
            Log.d(TAG, drink.toString());
            //Toast toast2 = Toast.makeText(this, "drink1"+drink.getStrDrink(), Toast.LENGTH_LONG);
            //toast2.show();
            textView_Alchool_Drink.setText(drink.getStrAlcoholic());
            textView_Ingredienti_Drink.setText(drink.getStrIngredient1());
            textView_Preparazione_Drink.setText(drink.getStrInstructionsIT());
        }
        drinksWithDrinksApi.clear();
    }

    @Override
    public void onFailure(String msg) {
        Toast toast3 = Toast.makeText(this, "ERRORE!!", Toast.LENGTH_LONG);
        toast3.show();
    }

    public void imgGlide(String urlPassata){

        String url = urlPassata;
        String newUrl = null;

        if (url != null) {
            // This action is a possible alternative to manage HTTP addresses that don't work
            // in the apps that target API level 28 or higher.
            // If it doesn't work, the other option is this one:
            // https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
            newUrl = url.replace("http://", "https://").trim();

            // Download the image associated with the article
            Glide.with(ActivityDrink.this)
                    .load(newUrl)
                    .into(imageViewDownload);
        }

    }
}