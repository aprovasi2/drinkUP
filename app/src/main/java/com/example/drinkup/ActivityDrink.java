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
    private TextView text_nome;
    private TextView text_gradazione;
    private TextView text_ingredienti;
    private TextView text_preparazione;
    private ImageView imageViewDownload;

    private TextView textView_Nome_Drink;
    private TextView textView_Alchool_Drink;
    private TextView textView_Ingredienti_Drink;
    private TextView textView_Preparazione_Drink;

    private Button button_Successivo_Drink;
    private Button button_Precedente_Drink;

    public static int posizione = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        textView_Alchool_Drink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        text_nome = (TextView) findViewById(R.id.textView);
        text_gradazione = (TextView) findViewById(R.id.text_Gradazione);
        text_ingredienti= (TextView) findViewById(R.id.text_Ingredienti);
        text_preparazione= (TextView) findViewById(R.id.text_Preparazione);
        text_nome.setVisibility(View.INVISIBLE);
        text_gradazione.setVisibility(View.INVISIBLE);
        text_ingredienti.setVisibility(View.INVISIBLE);
        text_preparazione.setVisibility(View.INVISIBLE);


        textView_Ingredienti_Drink = (TextView) findViewById(R.id.textView_Ingredienti_Drink);
        textView_Preparazione_Drink = (TextView) findViewById(R.id.textView_Preparazione_Drink);
        textView_Nome_Drink = (TextView) findViewById(R.id.textView_Nome_Drink);

        imageViewDownload = (ImageView) findViewById(R.id.imageView_Drink);
        imageViewDownload.setVisibility(View.INVISIBLE);
        button_Search = (Button) findViewById(R.id.button_Search);
        button_Search.setOnClickListener(this);

        button_Successivo_Drink = (Button) findViewById(R.id.button_Successivo_Drink);
        button_Successivo_Drink.setOnClickListener(this);

        button_Precedente_Drink = (Button) findViewById(R.id.button_Precedente_Drink);
        button_Precedente_Drink.setOnClickListener(this);

        nomeDrink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        drinkDaCercare = (EditText) findViewById(R.id.editTextText_DrinkSearch);

        drinkRepository = new DrinkRepository(this, this.getApplication());
        drinksWithDrinksApi=new ArrayList<>();

        button_Precedente_Drink.setClickable(false);
        button_Precedente_Drink.setEnabled(false);
        button_Successivo_Drink.setClickable(false);
        button_Successivo_Drink.setEnabled(false);

    }


    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.button_Precedente_Drink){
            posizione = posizione-1;
            visualizzaDrink(posizione);
            attivaBottoni();
        } else if(v.getId() == R.id.button_Successivo_Drink){
            posizione++;
            visualizzaDrink(posizione);
            attivaBottoni();
        } else if(v.getId() == R.id.button_Search) {

            posizione = 0;
            //drinksWithDrinksApi.add(null);
            attivaBottoni();
            drinksWithDrinksApi.clear();
            String ricerca = drinkDaCercare.getText().toString();
            drinkDaCercare.setText("");
            List<Drink> drinkListWithGson = getDrinksWithGson();
            drinkRepository.fetchDrinks(ricerca);


        }

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


            if(drinkList != null) {
                drinksWithDrinksApi.addAll(drinkList);

                if(drinksWithDrinksApi.size()==1)
                {
                    button_Precedente_Drink.setClickable(false);
                    button_Precedente_Drink.setEnabled(false);
                    button_Successivo_Drink.setClickable(false);
                    button_Successivo_Drink.setEnabled(false);
                }
                else{
                    attivaBottoni();
                }
                visualizzaDrink(posizione);
            }else{
                posizione = 9999999;
                 Toast toastErrore = Toast.makeText(this, "Spiacenti! Il drink ricercato non è disponibile", Toast.LENGTH_LONG);
                 toastErrore.show();

            }


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

    public void visualizzaDrink(int posizione){

            textView_Nome_Drink.setText(drinksWithDrinksApi.get(posizione).getStrDrink());
            textView_Alchool_Drink.setText(drinksWithDrinksApi.get(posizione).getStrAlcoholic());
            textView_Ingredienti_Drink.setText(drinksWithDrinksApi.get(posizione).getStrIngredient1()+ "\n");
            textView_Ingredienti_Drink.append(drinksWithDrinksApi.get(posizione).getStrIngredient2()+ "\n");
            textView_Preparazione_Drink.setText(drinksWithDrinksApi.get(posizione).getStrInstructionsIT());
            imgGlide(drinksWithDrinksApi.get(posizione).getStrDrinkThumb());

        text_nome.setVisibility(View.VISIBLE);
        text_gradazione.setVisibility(View.VISIBLE);
        text_ingredienti.setVisibility(View.VISIBLE);
        text_preparazione.setVisibility(View.VISIBLE);
        imageViewDownload.setVisibility(View.VISIBLE);

    }
    public void attivaBottoni(){


        if (posizione==0 && drinksWithDrinksApi.size()==0)
        {
            button_Precedente_Drink.setClickable(false);
            button_Precedente_Drink.setEnabled(false);
            button_Successivo_Drink.setClickable(false);
            button_Successivo_Drink.setEnabled(false);

        }
       else if (posizione==0 && drinksWithDrinksApi.size()!=0)
        {
            button_Precedente_Drink.setClickable(false);
            button_Precedente_Drink.setEnabled(false);
            button_Successivo_Drink.setClickable(true);
            button_Successivo_Drink.setEnabled(true);
        }
        else if (posizione == drinksWithDrinksApi.size()-1)
        {
            button_Precedente_Drink.setClickable(true);
            button_Precedente_Drink.setEnabled(true);
            button_Successivo_Drink.setClickable(false);
            button_Successivo_Drink.setEnabled(false);

        }else
        {
            button_Precedente_Drink.setClickable(true);
            button_Precedente_Drink.setEnabled(true);
            button_Successivo_Drink.setClickable(true);
            button_Successivo_Drink.setEnabled(true);

        }

    }
}