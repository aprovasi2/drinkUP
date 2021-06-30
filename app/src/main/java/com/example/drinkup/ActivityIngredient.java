package com.example.drinkup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Ingredient;
import com.example.drinkup.repositories.IIngredientRepository;
import com.example.drinkup.repositories.IngredientRepository;
import com.example.drinkup.repositories.ResponseCallback;
import java.util.ArrayList;
import java.util.List;

public class ActivityIngredient extends AppCompatActivity implements View.OnClickListener, ResponseCallback {

    private IIngredientRepository ingredientRepository;
    private Button button_Search_Ingredient;
    private EditText ingredientDaCercare;
    private TextView textView_Nome_Ingredient;
    private TextView textView_Tipo_Ingredient;
    private TextView textView_Alcolico_Ingredient;
    private TextView textView_Gradazione_Ingredient;
    private TextView textView_Descrizione_Ingredient;
    private CardView cardView_InfoIngredient;
    private ImageView imageView_Ingredient;
    private String ricercaImmagine = "https://www.thecocktaildb.com/images/ingredients/";
    private String ricerca = "";
    private List<Ingredient> listaIngredienti;

    // metodo contente le istruzioni dell'operazione di creazione dell'activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        textView_Nome_Ingredient = findViewById(R.id.textView_Nome_Ingredient);
        textView_Tipo_Ingredient = findViewById(R.id.textView_Tipo_Ingredient);
        textView_Alcolico_Ingredient = findViewById(R.id.textView_Alcolico_Ingredient);
        textView_Gradazione_Ingredient = findViewById(R.id.textView_Gradazione_Ingredient);
        textView_Descrizione_Ingredient = findViewById(R.id.textView_Descrizione_Ingredient);

        button_Search_Ingredient = findViewById(R.id.button_Search_Ingredient);
        button_Search_Ingredient.setOnClickListener(this);

        ingredientDaCercare = findViewById(R.id.editTextText_IngredientSearch);

        cardView_InfoIngredient = findViewById(R.id.cardView_InfoIngredient);
        cardView_InfoIngredient.setVisibility(View.INVISIBLE);
        listaIngredienti = new ArrayList<>();

        imageView_Ingredient = findViewById(R.id.imageView_Ingredient);
        ingredientRepository = new IngredientRepository(this, this.getApplication());

    }

    // metodo contente le istruzioni dell'operazione di click dei bottoni presenti nell'activity
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
            Toast toastErrore = Toast.makeText(this, "Spiacenti! Inserire il nome dell'ingrediente da cercare", Toast.LENGTH_LONG);
            toastErrore.show();
        }
    }

    // metodo contente la risposta in caso di insuccesso al collegamento con l'API
    @Override
    public void onFailure(String msg) {
        Toast toastErrore = Toast.makeText(this, "ERRORE!", Toast.LENGTH_LONG);
        toastErrore.show();
    }

    // metodo contente la risposta dall'API
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

    @Override
    public void onResponseNome(List<Drink> nomeDrink) {

    }

    // metodo per scaricare l'immagine dall'API
    private void imgGlide(String urlPassata){

        String url = urlPassata;
        String newUrl = null;
        if (url != null) {
            newUrl = url.replace("http://", "https://").trim();
            // Download the image associated with the article
            Glide.with(ActivityIngredient.this)
                    .load(newUrl)
                    .into(imageView_Ingredient);
        }

    }

    // metodo per la visualizzazione dei risulati della ricerca
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
        cardView_InfoIngredient.setVisibility(View.VISIBLE);
        imgGlide(ricercaImmagine+ricerca+".png");

    }

    //DA NON USARE
    @Override
    public void onResponse(List<Drink> ingredientList) {
    }
}
