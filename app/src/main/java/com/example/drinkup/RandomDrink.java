package com.example.drinkup;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Ingredient;
import com.example.drinkup.repositories.DrinkRepository;
import com.example.drinkup.repositories.IDrinkRepository;
import com.example.drinkup.repositories.ResponseCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomDrink extends AppCompatActivity implements View.OnClickListener, ResponseCallback {

    private IDrinkRepository mDrinkRepository;
    private List<Drink> mDrinkRandomWithDrinksApi;
    private List<String> temp;
    private List<String> mDrinksPreferiti;
    String result = "";
    private TextView mTextView_Nome_Drink;
    private TextView mTextView_Alchool_Drink;
    private TextView mTextView_Ingredienti_Drink;
    private TextView mTextView_QuantitaIngredienti_Drink;
    private TextView mTextView_Preparazione_Drink;
    private ImageView mImageView_Drink;
    private ImageButton mButton_Salva_Preferito;
    public static int sPosizionePref = 999;
    private CardView mCardView_InfoDrink;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        mDrinkRepository = new DrinkRepository(this, this.getApplication());
        mDrinkRandomWithDrinksApi = new ArrayList<>();
        mDrinksPreferiti = new ArrayList<>();
        temp = new ArrayList<>();
        //Inizializzazione
        mButton_Salva_Preferito = (ImageButton) findViewById(R.id.buttonR_Salva_Preferito);
        mButton_Salva_Preferito.setOnClickListener(this);
        mTextView_Nome_Drink = (TextView) findViewById(R.id.textViewR_Nome_Drink);
        mTextView_Alchool_Drink = (TextView) findViewById(R.id.textViewR_Alchool_Drink);
        mTextView_Ingredienti_Drink = (TextView) findViewById(R.id.textViewR_Ingredienti_Drink);
        mTextView_QuantitaIngredienti_Drink = (TextView) findViewById(R.id.textViewR_QuantitaIngredienti_Drink);
        mTextView_Preparazione_Drink = (TextView) findViewById(R.id.textViewR_Preparazione_Drink);
        mImageView_Drink = (ImageView) findViewById(R.id.imageViewR_Drink);
        mCardView_InfoDrink = (CardView) findViewById(R.id.CardView_InfoDrinkR);
        mCardView_InfoDrink.setVisibility(View.INVISIBLE);

        try {
            RecuperaDrinkPreferiti();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDrinkRepository.fetchRandomDrink();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int idDrink = mDrinkRandomWithDrinksApi.get(0).getIdDrink();
        boolean trovato = false;
        for (int i = 0; i < mDrinksPreferiti.size(); i++) {
            if (Integer.parseInt(mDrinksPreferiti.get(i)) == idDrink) {
                trovato = true;
            }
        }
        if(trovato==true){
            try {
                cancellaDrinkdaFile(idDrink);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ToastCustom.makeText(getApplicationContext(),ToastCustom.TYPE_REMOVE,"Il drink è stato rimosso dalla lista preferiti").show();
        }
        else{
            try {
                ToastCustom.makeText(getApplicationContext(),ToastCustom.TYPE_SUCCESS,"Il drink è stato inserito nella lista preferiti").show();
                salvaIdDrink(idDrink);
                RecuperaDrinkPreferiti();
                setChangesButtonSalva();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResponse(List<Drink> drinkList) {
        mDrinkRandomWithDrinksApi.addAll(drinkList);
        visualizzaDrink(0);
    }

    @Override
    public void onFailure(String msg) {
        ToastCustom.makeText(getApplicationContext(),ToastCustom.TYPE_ERROR,"Errore").show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void visualizzaDrink(int posizione) {
        
        mCardView_InfoDrink.setVisibility(View.VISIBLE);
        //Inizializzazione delle varie textbox con gli elementi associati al drink che vogliamo visualizzare.
        mTextView_Ingredienti_Drink.setText(recuperaIngredienti(posizione));
        mTextView_QuantitaIngredienti_Drink.setText(recuperaQuantitaIngredienti(posizione));
        mTextView_Nome_Drink.setText(mDrinkRandomWithDrinksApi.get(posizione).getStrDrink());
        mTextView_Alchool_Drink.setText(mDrinkRandomWithDrinksApi.get(posizione).getStrAlcoholic());
        mTextView_Preparazione_Drink.setText(mDrinkRandomWithDrinksApi.get(posizione).getStrInstructionsIT());
        imgGlide(mDrinkRandomWithDrinksApi.get(posizione).getStrDrinkThumb());

    }

    //metodo che permette di scaricare dall'url l'immagine relativa al drink
    private void imgGlide(String urlPassata) {

        String url = urlPassata;
        String newUrl = null;

        if (url != null) {

            newUrl = url.replace("http://", "https://").trim();

            Glide.with(this)
                    .load(newUrl)
                    .into(mImageView_Drink);
        }

    }

    //Recupera la lista degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaIngredienti(int posizione) {
        List<String> listaIngredienti = new ArrayList<>();
        String ingredienti = "";
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient1() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient1());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient2() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient2());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient3() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient3());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient4() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient4());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient5() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient5());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient6() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient6());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient7() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient7());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient8() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient8());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient9() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient9());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient10() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient10());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient11() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient11());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient12() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient12());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient13() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient13());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient14() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient14());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient15() != null) {
            listaIngredienti.add(mDrinkRandomWithDrinksApi.get(posizione).getStrIngredient15());
        }
        if(!listaIngredienti.isEmpty()){
            for (int i = 0; i < (listaIngredienti.size()) - 1; i++) {
                ingredienti += listaIngredienti.get(i) + "\n";
            }
            ingredienti = ingredienti.concat(listaIngredienti.get(listaIngredienti.size() - 1) + "");
        }
        return ingredienti;
    }

    //Recupera la quantità degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaQuantitaIngredienti(int posizione) {
        String quantita = "";
        List<String> listaQuantita = new ArrayList<>();
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure1() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure1());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure2() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure2());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure3() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure3());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure4() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure4());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure5() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure5());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure6() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure6());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure7() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure7());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure8() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure8());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure9() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure9());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure10() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure10());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure11() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure11());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure12() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure12());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure13() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure13());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure14() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure14());
        }
        if (mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure15() != null) {
            listaQuantita.add(mDrinkRandomWithDrinksApi.get(posizione).getStrMeasure15());
        }
        if(!listaQuantita.isEmpty()){
            for (int i = 0; i < (listaQuantita.size()) - 1; i++) {
                quantita += listaQuantita.get(i) + "\n";
            }
            quantita = quantita.concat(listaQuantita.get(listaQuantita.size() - 1) + "");
        }
        return quantita;
    }

    private void salvaIdDrink(int idDrink) throws IOException {
        scriviFile(idDrink);
    }

    private File scriviFile(int data) throws IOException {
        File path = this.getFilesDir(); //==> data/data/com.example.drinkup/files
        String idDrink = ""+data+"\n";

        File file = new File(path, "ElencoPreferiti.txt");
        Log.d("testPath2", file.toString());
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

    //metodo che legge il file e ritorna una stringa contenente i valori letti
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

    //metodo che permette di salvare in una lista l'elenco dei drink preferiti letti dal file, invoca il metodo esterno leggiFile
    private void RecuperaDrinkPreferiti() throws IOException {
        File path = this.getFilesDir();
        File file = new File(path, "ElencoPreferiti.txt");
        String stringElencoPreferiti = leggiFile(file);
        mDrinksPreferiti = Arrays.asList(stringElencoPreferiti.split("\n"));
    }

    //Metodo che permette di cancellare un drink tra i preferiti nel file locale
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancellaDrinkdaFile(int id) throws IOException {
        File path = this.getFilesDir();
        File file = new File(path, "ElencoPreferiti.txt");
        Integer value = new Integer(id);
        String daRimuovere = value.toString();

        //Parte nuova, al posto di lavorare sull'arrayList originale, se ne si fa una copia e si lavora su quella
        List<String> drinksPreferitiClone = new ArrayList<>();
        drinksPreferitiClone.addAll(mDrinksPreferiti);
        for(int i=0;i<drinksPreferitiClone.size();i++){
            String daRemove = drinksPreferitiClone.get(i);
            if(daRemove.equals(id+"")){
                drinksPreferitiClone.remove(daRemove);
                i--;
                //break;
            }
        }
        file.delete();
        for(int i=0;i<drinksPreferitiClone.size();i++){
            scriviFile(Integer.parseInt(drinksPreferitiClone.get(i)));
        }
        //Una volta fatto, riportiamo tutti i valori nell'elenco originale
        //Per farlo prima liberiamo la lista, con il clear crasha quindi si crea nuova
        mDrinksPreferiti = new ArrayList<>();
        mDrinksPreferiti.addAll(drinksPreferitiClone);
        setDefaultButtonSalva();
    }

    //metodo che permette di riportare i valori di default al bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setDefaultButtonSalva(){
        int icona = android.R.drawable.btn_star_big_off;
        mButton_Salva_Preferito.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), icona));
    }

    //metodo che permette cambiamenti grafici una volta premuto il bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChangesButtonSalva(){
        int icona = android.R.drawable.btn_star_big_on;
        mButton_Salva_Preferito.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), icona));
    }

    // Non necessari in questa activity
    @Override
    public void onResponseI(List<Ingredient> ingredientList) {

    }

    // Non necessari in questa activity
    @Override
    public void onResponseNome(List<Drink> nomeDrink) {

    }
}
