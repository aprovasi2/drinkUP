package com.example.drinkup;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.drinkup.R;
import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Ingredient;
import com.example.drinkup.repositories.DrinkRepository;
import com.example.drinkup.repositories.IDrinkRepository;
import com.example.drinkup.repositories.ResponseCallback;
import com.example.drinkup.ui.preferiti.PreferitiViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomDrink extends AppCompatActivity implements View.OnClickListener, ResponseCallback {

    private IDrinkRepository drinkRepository;
    private List<Drink> drinkRandomWithDrinksApi;
    private List<String> temp;
    private List<String> drinksPreferiti;
    String result = "";
    private TextView textView_Nome_Drink;
    private TextView textView_Alchool_Drink;
    private TextView textView_Ingredienti_Drink;
    private TextView textView_QuantitaIngredienti_Drink;
    private TextView textView_Preparazione_Drink;
    private ImageView imageView_Drink;
    private Button button_Salva_Preferito;
    public static int posizionePref = 999;
    private CardView CardView_InfoDrink;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        drinkRepository = new DrinkRepository(this, this.getApplication());
        drinkRandomWithDrinksApi = new ArrayList<>();
        temp = new ArrayList<>();

        //Inizializzazione
        button_Salva_Preferito = (Button) findViewById(R.id.buttonR_Salva_Preferito);
        button_Salva_Preferito.setOnClickListener(this);
        textView_Nome_Drink = (TextView) findViewById(R.id.textViewR_Nome_Drink);
        textView_Alchool_Drink = (TextView) findViewById(R.id.textViewR_Alchool_Drink);
        textView_Ingredienti_Drink = (TextView) findViewById(R.id.textViewR_Ingredienti_Drink);
        textView_QuantitaIngredienti_Drink = (TextView) findViewById(R.id.textViewR_QuantitaIngredienti_Drink);
        textView_Preparazione_Drink = (TextView) findViewById(R.id.textViewR_Preparazione_Drink);
        imageView_Drink = (ImageView) findViewById(R.id.imageViewR_Drink);
        CardView_InfoDrink = (CardView) findViewById(R.id.CardView_InfoDrinkR);

        try {
            RecuperaDrinkPreferiti();
        } catch (IOException e) {
            e.printStackTrace();
        }
        drinkRepository.fetchRandomDrink();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int idDrink = drinkRandomWithDrinksApi.get(0).getIdDrink();
        boolean trovato=false;
        for(int i=0; i<drinksPreferiti.size();i++)
        {
            if(Integer.parseInt(drinksPreferiti.get(i))==idDrink){
                trovato=true;
            }
        }
        if(trovato==true){
            try {
                cancellaDrinkdaFile(idDrink);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast toastRimozione= Toast.makeText(this, "Il drink è stato rimosso dalla Lista Preferiti", Toast.LENGTH_LONG);
            toastRimozione.show();
        }
        else{
            try {
                Toast toastSalvataggio= Toast.makeText(this, "Il drink selezionato è ora nella tua lista preferiti", Toast.LENGTH_LONG);
                toastSalvataggio.show();
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
        drinkRandomWithDrinksApi.addAll(drinkList);
        visualizzaDrink(0);
    }

    @Override
    public void onFailure(String msg) {
        Toast toastFailure= Toast.makeText(this.getApplication(), "Errore", Toast.LENGTH_LONG);
        toastFailure.show();
    }

    // DA NON USARE
    @Override
    public void onResponseI(List<Ingredient> ingredientList) {

    }

    @Override
    public void onResponseNome(List<Drink> nomeDrink) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void visualizzaDrink(int posizione) {

        //Inizializzazione delle varie textbox con gli elementi associati al drink che vogliamo visualizzare.
        textView_Ingredienti_Drink.setText(recuperaIngredienti(posizione));
        textView_QuantitaIngredienti_Drink.setText(recuperaQuantitaIngredienti(posizione));
        textView_Nome_Drink.setText(drinkRandomWithDrinksApi.get(posizione).getStrDrink());
        textView_Alchool_Drink.setText(drinkRandomWithDrinksApi.get(posizione).getStrAlcoholic());
        textView_Preparazione_Drink.setText(drinkRandomWithDrinksApi.get(posizione).getStrInstructionsIT());
        imgGlide(drinkRandomWithDrinksApi.get(posizione).getStrDrinkThumb());
        //setChangesButtonSalva();
    }

    private void imgGlide(String urlPassata) {

        String url = urlPassata;
        String newUrl = null;

        if (url != null) {
            // This action is a possible alternative to manage HTTP addresses that don't work
            // in the apps that target API level 28 or higher.
            // If it doesn't work, the other option is this one:
            // https://developer.android.com/guide/topics/manifest/application-element#usesCleartextTraffic
            newUrl = url.replace("http://", "https://").trim();

            // Download the image associated with the article
            Glide.with(this)
                    .load(newUrl)
                    .into(imageView_Drink);
        }

    }

    //Recupera la lista degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaIngredienti(int posizione) {
        List<String> listaIngredienti = new ArrayList<>();
        String ingredienti = "";
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient1() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient1());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient2() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient2());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient3() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient3());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient4() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient4());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient5() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient5());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient6() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient6());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient7() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient7());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient8() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient8());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient9() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient9());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient10() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient10());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient11() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient11());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient12() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient12());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient13() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient13());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient14() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient14());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrIngredient15() != null) {
            listaIngredienti.add(drinkRandomWithDrinksApi.get(posizione).getStrIngredient15());
        }
        for (int i = 0; i < (listaIngredienti.size()) - 1; i++) {
            ingredienti += listaIngredienti.get(i) + "\n";
        }
        ingredienti = ingredienti.concat(listaIngredienti.get(listaIngredienti.size() - 1) + "");
        return ingredienti;
    }

    //Recupera la quantità degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaQuantitaIngredienti(int posizione) {
        String quantita = "";
        List<String> listaQuantita = new ArrayList<>();
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure1() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure1());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure2() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure2());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure3() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure3());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure4() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure4());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure5() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure5());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure6() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure6());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure7() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure7());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure8() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure8());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure9() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure9());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure10() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure10());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure11() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure11());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure12() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure12());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure13() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure13());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure14() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure14());
        }
        if (drinkRandomWithDrinksApi.get(posizione).getStrMeasure15() != null) {
            listaQuantita.add(drinkRandomWithDrinksApi.get(posizione).getStrMeasure15());
        }
        for (int i = 0; i < (listaQuantita.size()) - 1; i++) {
            quantita += listaQuantita.get(i) + "\n";
        }
        quantita = quantita.concat(listaQuantita.get(listaQuantita.size() - 1) + "");
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
        File path = this.getFilesDir(); //==> data/data/com.example.drinkup/files
        File file = new File(path, "ElencoPreferiti.txt");
        String stringElencoPreferiti = leggiFile(file);
        drinksPreferiti= Arrays.asList(stringElencoPreferiti.split("\n"));
    }

    //Metodo che permette di cancellare un drink tra i preferiti nel file locale
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancellaDrinkdaFile(int id) throws IOException {
        File path = this.getFilesDir(); //==> data/data/com.example.drinkup/files
        File file = new File(path, "ElencoPreferiti.txt");
        Integer value = new Integer(id);
        String daRimuovere = value.toString();
        //Toast.makeText(this, daRimuovere, Toast.LENGTH_LONG).show();

        //Parte nuova, al posto di lavorare sull'arrayList originale, se ne si fa una copia e si lavora su quella
        List<String> drinksPreferitiClone = new ArrayList<>();
        drinksPreferitiClone.addAll(drinksPreferiti);
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
        drinksPreferiti = new ArrayList<>();
        drinksPreferiti.addAll(drinksPreferitiClone);
        setDefaultButtonSalva();
    }

    //metodo che permette di riportare i valori di default al bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setDefaultButtonSalva(){
        button_Salva_Preferito.setBackgroundColor(0xFFCA4700);
        button_Salva_Preferito.setForeground(null);
    }

    //metodo che permette cambiamenti grafici una volta premuto il bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChangesButtonSalva(){
        button_Salva_Preferito.setBackgroundColor(0xFFDAA520);
        Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_on);
        button_Salva_Preferito.setForeground(drawable);
        button_Salva_Preferito.setForegroundGravity(View.TEXT_ALIGNMENT_GRAVITY);
    }
}
