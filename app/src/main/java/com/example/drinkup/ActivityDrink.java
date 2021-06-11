package com.example.drinkup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.content.Context;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.example.drinkup.GestioneFile.*;

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
    private CardView cardView_InfoDrink;

    private TextView textView_Nome_Drink;
    private TextView textView_Alchool_Drink;
    private TextView textView_Ingredienti_Drink;
    private TextView textView_Preparazione_Drink;

    private Button button_Successivo_Drink;
    private Button button_Precedente_Drink;
    private Button button_Salva_Preferito;

    public static int posizione = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        textView_Alchool_Drink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        text_nome = (TextView) findViewById(R.id.textView);
        cardView_InfoDrink=(CardView)findViewById(R.id.CardView_InfoDrink);
        text_gradazione = (TextView) findViewById(R.id.text_Gradazione);
        text_ingredienti= (TextView) findViewById(R.id.text_Ingredienti);
        text_preparazione= (TextView) findViewById(R.id.text_Preparazione);
        text_nome.setVisibility(View.INVISIBLE);
        text_gradazione.setVisibility(View.INVISIBLE);
        text_ingredienti.setVisibility(View.INVISIBLE);
        text_preparazione.setVisibility(View.INVISIBLE);
        cardView_InfoDrink.setVisibility(View.INVISIBLE);


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

        button_Salva_Preferito = (Button) findViewById(R.id.button_Salva_Preferito);
        button_Salva_Preferito.setOnClickListener(this);

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
            String ricerca = drinkDaCercare.getText().toString();
             if(ricerca.equals("")) {
                 Toast toastErroreRicerca = Toast.makeText(this, "Spiacenti! Inserire il nome del drink da cercare", Toast.LENGTH_LONG);
                 toastErroreRicerca.show();
                }
             else{
                 posizione = 0;
                 //drinksWithDrinksApi.add(null);
                 attivaBottoni();
                 drinksWithDrinksApi.clear();
                 drinkDaCercare.setText("");
                 List<Drink> drinkListWithGson = getDrinksWithGson();
                 drinkRepository.fetchDrinks(ricerca);
             }
        } else if(v.getId() == R.id.button_Salva_Preferito){
            int idDrink = drinksWithDrinksApi.get(posizione).getIdDrink();
            //Toast toastId = Toast.makeText(this, ""+idDrink, Toast.LENGTH_LONG);
            //toastId.show();
            try {
                salvaIdDrink(idDrink);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        String ingredienti = "";
        if(drinksWithDrinksApi.get(posizione).getStrIngredient1() != null){
            ingredienti = drinksWithDrinksApi.get(posizione).getStrIngredient1()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient2() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient2()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient3() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient3()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient4() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient4()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient5() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient5()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient6() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient6()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient7() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient7()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient8() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient8()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient9() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient9()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient10() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient10()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient11() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient11()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient12() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient12()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient13() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient13()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient14() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient14()+"\n";
        }
        if(drinksWithDrinksApi.get(posizione).getStrIngredient15() != null){
            ingredienti += drinksWithDrinksApi.get(posizione).getStrIngredient15()+"\n";
        }

        textView_Ingredienti_Drink.setText(ingredienti);

            textView_Nome_Drink.setText(drinksWithDrinksApi.get(posizione).getStrDrink());
            textView_Alchool_Drink.setText(drinksWithDrinksApi.get(posizione).getStrAlcoholic());
            //textView_Ingredienti_Drink.setText(drinksWithDrinksApi.get(posizione).getStrIngredient1()+ "\n");
            //textView_Ingredienti_Drink.append(drinksWithDrinksApi.get(posizione).getStrIngredient2()+ "\n");
            textView_Preparazione_Drink.setText(drinksWithDrinksApi.get(posizione).getStrInstructionsIT());
            imgGlide(drinksWithDrinksApi.get(posizione).getStrDrinkThumb());
            cardView_InfoDrink.setVisibility(View.VISIBLE);



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

    private void salvaIdDrink(int idDrink) throws IOException {
        LibFileExt.writeFile("ElencoIdDrink", ""+idDrink);

        String contenuto = leggiFile(scriviFile(idDrink));

        Toast toastControllo = Toast.makeText(this, contenuto, Toast.LENGTH_LONG);
        toastControllo.show();
    }

    private File scriviFile(int data) throws IOException {
        File path = this.getFilesDir(); //==> data/data/com.example.drinkup/files
        String idDrink = ""+data+"\n";

        File file = new File(path, "ElencoPreferiti.txt");
        if(!file.exists()){
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(idDrink.getBytes());
            } finally {
                stream.close();
            }

        } else{
            FileOutputStream stream = new FileOutputStream(file, true);
            OutputStreamWriter outWriter = new OutputStreamWriter(stream);
            try {
                outWriter.append(idDrink);
            } finally {
                outWriter.close();
                stream.close();
            }
        }

        return file;

    }
    private String leggiFile(File file) throws IOException {
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
        } finally {
            in.close();
        }
        String contents = new String(bytes);

        return contents;
    }

    /*
    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("ElencoIdDrink.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
     */
}