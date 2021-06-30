package com.example.drinkup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Ingredient;
import com.example.drinkup.repositories.DrinkRepository;
import com.example.drinkup.repositories.IDrinkRepository;
import com.example.drinkup.repositories.IIngredientRepository;
import com.example.drinkup.repositories.IngredientRepository;
import com.example.drinkup.repositories.ResponseCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.drinkup.GestioneFile.*;

public class ActivityIngredient extends AppCompatActivity implements View.OnClickListener, ResponseCallback {
    private static final String TAG ="ActivityIngredient" ;

    private IIngredientRepository ingredientRepository;

    private Button button_Search_Ingredient;
    private EditText ingredientDaCercare;
    private TextView textView_Nome_Ingredient;
    private TextView text_Tipo_Ingredient;
    private TextView text_Alcolico_Ingredient;
    private TextView text_Gradazione_Ingredient;
    private TextView text_Descrizione_Ingredient;
    private TextView textView_Tipo_Ingredient;
    private TextView textView_Alcolico_Ingredient;
    private TextView textView_Gradazione_Ingredient;
    private TextView textView_Descrizione_Ingredient;
    private LinearLayout linearLayout_Ingredient;
    private ImageView imageView_Ingredient;
    private String ricercaImmagine = "https://www.thecocktaildb.com/images/ingredients/";
    private String ricerca = "";
    private List<Ingredient> listaIngredienti;

    public ActivityIngredient() throws IOException {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        textView_Nome_Ingredient = (TextView) findViewById(R.id.textView_Nome_Ingredient);
        text_Tipo_Ingredient = (TextView) findViewById(R.id.text_Tipo_Ingredient);
        text_Alcolico_Ingredient = (TextView) findViewById(R.id.text_Alcolico_Ingredient);
        text_Gradazione_Ingredient = (TextView) findViewById(R.id.text_Gradazione_Ingredient);
        text_Descrizione_Ingredient = (TextView) findViewById(R.id.text_Descrizione_Ingredient);
        textView_Tipo_Ingredient = (TextView) findViewById(R.id.textView_Tipo_Ingredient);
        textView_Alcolico_Ingredient = (TextView) findViewById(R.id.textView_Alcolico_Ingredient);
        textView_Gradazione_Ingredient = (TextView) findViewById(R.id.textView_Gradazione_Ingredient);
        textView_Descrizione_Ingredient = (TextView) findViewById(R.id.textView_Descrizione_Ingredient);

        button_Search_Ingredient = (Button) findViewById(R.id.button_Search_Ingredient);
        button_Search_Ingredient.setOnClickListener(this);

        ingredientDaCercare = (EditText) findViewById(R.id.editTextText_IngredientSearch);

        linearLayout_Ingredient = (LinearLayout) findViewById(R.id.linearLayout_Ingredient);
        linearLayout_Ingredient.setVisibility(View.INVISIBLE);
        listaIngredienti = new ArrayList<>();

        imageView_Ingredient = (ImageView) findViewById(R.id.imageView_Ingredient);
        ingredientRepository = new IngredientRepository(this, this.getApplication());

    }

    @Override
    public void onClick(View v) {
        ricerca="";
        listaIngredienti.clear();
        ricerca = ingredientDaCercare.getText().toString();
        if(!ricerca.equals("")){
            ingredientRepository.fetchIngredient(ricerca);
            ingredientDaCercare.setText("");
        }
        else{
            Toast toastErrore = Toast.makeText(this, "Spiacenti! L'ingrediente cercato non è disponibile", Toast.LENGTH_LONG);
            toastErrore.show();
        }
    }

    //DA NON USARE
    @Override
    public void onResponse(List<Drink> ingredientList) {
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onResponseI(List<Ingredient> ingredientList) {
        if(ingredientList != null){
            listaIngredienti.addAll(ingredientList);
            visualizzaIngredient(0);
        }
        else{
            Toast toastErrore = Toast.makeText(this, "Spiacenti! L'ingrediente cercato non è disponibile", Toast.LENGTH_LONG);
            toastErrore.show();
        }
    }

    private void imgGlide(String urlPassata){

        String url = urlPassata;
        String newUrl = null;

        if (url != null) {
            // This action is a possible alternative to manage HTTP addresses that don't work
            // in the apps that target API level 28 or higher.
            // If it doesn't work, the other option is this one:
            // https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
            newUrl = url.replace("http://", "https://").trim();

            // Download the image associated with the article
            Glide.with(ActivityIngredient.this)
                    .load(newUrl)
                    .into(imageView_Ingredient);
        }

    }

    public void visualizzaIngredient(int pos){

        if(listaIngredienti.get(pos).getStrType() == null){
            textView_Tipo_Ingredient.setText("-");
        }
        else{
            textView_Tipo_Ingredient.setText(listaIngredienti.get(pos).getStrType());
        }
        if(listaIngredienti.get(pos).getStrAlcohol() == null){
            textView_Alcolico_Ingredient.setText("No");
        }
        else{
            textView_Alcolico_Ingredient.setText(listaIngredienti.get(pos).getStrAlcohol());
        }
        if(listaIngredienti.get(pos).getStrABV() == null){
            textView_Gradazione_Ingredient.setText("-");
        }
        else{
            textView_Gradazione_Ingredient.setText(listaIngredienti.get(pos).getStrABV());
        }
        if(listaIngredienti.get(pos).getStrDescription() == null){
            textView_Descrizione_Ingredient.setText("-");
        }
        else{
            textView_Descrizione_Ingredient.setText(listaIngredienti.get(pos).getStrDescription());
        }
        textView_Nome_Ingredient.setText(listaIngredienti.get(pos).getStrIngredient());
        //textView_Tipo_Ingredient.setText(listaIngredienti.get(pos).getStrType());
        //textView_Alcolico_Ingredient.setText(listaIngredienti.get(pos).getStrAlcohol());
        //textView_Gradazione_Ingredient.setText(listaIngredienti.get(pos).getStrABV());
        //textView_Descrizione_Ingredient.setText(listaIngredienti.get(pos).getStrDescription());
        linearLayout_Ingredient.setVisibility(View.VISIBLE);

        imgGlide(ricercaImmagine+ricerca+".png");

    }

}
