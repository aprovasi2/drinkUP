package com.example.drinkup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    private IIngredientRepository mIngredientRepository;
    private Button mButton_Search_Ingredient;
    private EditText mIngredientDaCercare;
    private TextView mTextView_Nome_Ingredient;
    private TextView mTextView_Tipo_Ingredient;
    private TextView mTextView_Alcolico_Ingredient;
    private TextView mTextView_Gradazione_Ingredient;
    private TextView mTextView_Descrizione_Ingredient;
    private CardView mCardView_InfoIngredient;
    private ImageView mImageView_Ingredient;
    private String mRicercaImmagine = "https://www.thecocktaildb.com/images/ingredients/";
    private String mRicerca = "";
    private List<Ingredient> mListaIngredienti;

    // metodo contente le istruzioni dell'operazione di creazione dell'activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        mTextView_Nome_Ingredient = findViewById(R.id.textView_Nome_Ingredient);
        mTextView_Tipo_Ingredient = findViewById(R.id.textView_Tipo_Ingredient);
        mTextView_Alcolico_Ingredient = findViewById(R.id.textView_Alcolico_Ingredient);
        mTextView_Gradazione_Ingredient = findViewById(R.id.textView_Gradazione_Ingredient);
        mTextView_Descrizione_Ingredient = findViewById(R.id.textView_Descrizione_Ingredient);

        mButton_Search_Ingredient = findViewById(R.id.button_Search_Ingredient);
        mButton_Search_Ingredient.setOnClickListener(this);

        mIngredientDaCercare = findViewById(R.id.editTextText_IngredientSearch);

        mCardView_InfoIngredient = findViewById(R.id.cardView_InfoIngredient);
        mCardView_InfoIngredient.setVisibility(View.INVISIBLE);
        mListaIngredienti = new ArrayList<>();

        mImageView_Ingredient = findViewById(R.id.imageView_Ingredient);
        mIngredientRepository = new IngredientRepository(this, this.getApplication());

    }

    // metodo contente le istruzioni dell'operazione di click dei bottoni presenti nell'activity
    @Override
    public void onClick(View v) {
        mRicerca ="";
        mListaIngredienti.clear();
        mRicerca = mIngredientDaCercare.getText().toString();
        if(!mRicerca.equals("")){
            mIngredientRepository.fetchIngredient(mRicerca);
            mIngredientDaCercare.setText("");
        }
        else{
            ToastCustom.makeText(this,ToastCustom.TYPE_WARN,"Inserire il nome dell'ingrediente da cercare").show();
        }
    }

    // metodo contente la risposta in caso di insuccesso al collegamento con l'API
    @Override
    public void onFailure(String msg) {
        ToastCustom.makeText(this,ToastCustom.TYPE_ERROR,"Errore inserimento").show();
    }

    // metodo contente la risposta dall'API
    @Override
    public void onResponseI(List<Ingredient> ingredientList) {
        if(ingredientList != null){
            mListaIngredienti.addAll(ingredientList);
            visualizzaIngredient(0);
        }
        else{
            ToastCustom.makeText(this,ToastCustom.TYPE_INFO,"L'ingrediente cercato non è stato trovato").show();
        }
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
                    .into(mImageView_Ingredient);
        }

    }

    // metodo per la visualizzazione dei risulati della ricerca
    public void visualizzaIngredient(int pos){

        if(mListaIngredienti.get(pos).getStrType() == null){
            mTextView_Tipo_Ingredient.setText("-");
        }
        else{
            mTextView_Tipo_Ingredient.setText(mListaIngredienti.get(pos).getStrType());
        }
        if(mListaIngredienti.get(pos).getStrAlcohol() == null){
            mTextView_Alcolico_Ingredient.setText("No");
        }
        else{
            mTextView_Alcolico_Ingredient.setText(mListaIngredienti.get(pos).getStrAlcohol());
        }
        if(mListaIngredienti.get(pos).getStrABV() == null){
            mTextView_Gradazione_Ingredient.setText("-");
        }
        else{
            mTextView_Gradazione_Ingredient.setText(mListaIngredienti.get(pos).getStrABV());
        }
        if(mListaIngredienti.get(pos).getStrDescription() == null){
            mTextView_Descrizione_Ingredient.setText("-");
        }
        else{
            mTextView_Descrizione_Ingredient.setText(mListaIngredienti.get(pos).getStrDescription());
        }
        mTextView_Nome_Ingredient.setText(mListaIngredienti.get(pos).getStrIngredient());
        mCardView_InfoIngredient.setVisibility(View.VISIBLE);
        imgGlide(mRicercaImmagine + mRicerca +".png");

    }

    //DA NON USARE
    @Override
    public void onResponse(List<Drink> ingredientList) {
    }

    // DA NON USARE
    @Override
    public void onResponseNome(List<Drink> nomeDrink) {
    }
}
