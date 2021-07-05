package com.example.drinkup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

public class DrinkByIngredient extends AppCompatActivity implements View.OnClickListener, ResponseCallback {
    //Dichiarazioni variabili
    private IDrinkRepository mDrinkRepository;
    private List<Drink> mDrinksWithDrinksApi;
    private List<String> mDrinksPreferiti;
    private Button mButton_Search;
    private EditText mDrinkDaCercare;
    private ImageView mImageViewDownload;
    private CardView mCardView_InfoDrink;
    private TextView mTextView_Nome_Drink;
    private TextView mTextView_Alchool_Drink;
    private TextView mTextView_Ingredienti_Drink;
    private TextView mTextView_QuantitaIngredienti_Drink;
    private TextView mTextView_Preparazione_Drink;
    private Button mButton_Successivo_Drink;
    private Button mButton_Precedente_Drink;
    private Button mButton_Salva_Preferito;

    private List<String> mNomiDrink;
    public static int sPOSIZIONE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inizializzazione variabili al momento della creazione dell'Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        mTextView_Alchool_Drink = findViewById(R.id.textView_Alchool_Drink);
        mCardView_InfoDrink = findViewById(R.id.CardView_InfoDrink);
        mTextView_Ingredienti_Drink = findViewById(R.id.textView_Ingredienti_Drink);
        mTextView_QuantitaIngredienti_Drink = findViewById(R.id.textView_QuantitaIngredienti_Drink);
        mTextView_Preparazione_Drink = findViewById(R.id.textView_Preparazione_Drink);
        mTextView_Nome_Drink = findViewById(R.id.textView_Nome_Drink);
        mImageViewDownload = findViewById(R.id.imageView_Drink);
        mButton_Search = findViewById(R.id.button_Search);
        mButton_Search.setOnClickListener(this);
        mButton_Successivo_Drink = findViewById(R.id.button_Successivo_Drink);
        mButton_Successivo_Drink.setOnClickListener(this);
        mButton_Successivo_Drink.setVisibility(View.INVISIBLE);
        mButton_Precedente_Drink = findViewById(R.id.button_Precedente_Drink);
        mButton_Precedente_Drink.setOnClickListener(this);
        mButton_Precedente_Drink.setVisibility(View.INVISIBLE);
        mButton_Salva_Preferito = findViewById(R.id.button_Salva_Preferito);
        mButton_Salva_Preferito.setOnClickListener(this);
        mDrinkDaCercare = findViewById(R.id.editTextText_DrinkSearch);
        mDrinkRepository = new DrinkRepository(this, this.getApplication());
        mDrinksWithDrinksApi = new ArrayList<>();
        mDrinksPreferiti = new ArrayList<>();
        mNomiDrink = new ArrayList<>();
        mCardView_InfoDrink.setVisibility(View.INVISIBLE);
        mDrinkDaCercare.setHint("Inserisci l'ingrediente da cercare");


        try {
            RecuperaDrinkPreferiti();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo che gestisce gli eventi di Click sui vari bottoni
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_Precedente_Drink) {
            sPOSIZIONE = sPOSIZIONE - 1;
            mDrinkRepository.fetchDrinks(mNomiDrink.get(sPOSIZIONE));
        }
        if (v.getId() == R.id.button_Successivo_Drink) {
            sPOSIZIONE = sPOSIZIONE + 1;
            mDrinkRepository.fetchDrinks(mNomiDrink.get(sPOSIZIONE));
        }
        if (v.getId() == R.id.button_Salva_Preferito) {
            //Controllo se il drink è nei preferiti
            int idDrink = mDrinksWithDrinksApi.get(sPOSIZIONE).getIdDrink();
            boolean trovato=false;
            for(int i = 0; i< mDrinksPreferiti.size(); i++)
            {
                if(Integer.parseInt(mDrinksPreferiti.get(i))==idDrink){
                    trovato=true;
                }
            } // se presente vuol dire che l'utente lo vuole cancellare
            if(trovato==true){
                try {
                    cancellaDrinkdaFile(idDrink);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast toastRimozione= Toast.makeText(this, "Il drink è stato rimosso dalla Lista Preferiti", Toast.LENGTH_LONG);
                toastRimozione.show();
            } // se non è presente, significa che l'utente lo vuole salvare
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
        if(v.getId() == R.id.button_Search){
            String ricerca = mDrinkDaCercare.getText().toString();
            mDrinksWithDrinksApi.clear();
            mNomiDrink.clear();
            mDrinkDaCercare.setText("");
            if(!ricerca.equals("")){
                //chiamata api per recuperare i nomi di tutti i drink preparati con quell'ingrediente
                mDrinkRepository.fetchByIngredient(ricerca);
                sPOSIZIONE = 0;
            }
            else{
                Toast toastErrore = Toast.makeText(this, "Spiacenti! Inserire il nome dell'ingrediente da cercare", Toast.LENGTH_LONG);
                toastErrore.show();
            }

        }

    }

    //Metodo di ritorno chiamata API dei nomi dei drink
    @Override
    public void onResponseNome(List<Drink> nomeDrink) {
        if(nomeDrink != null){
            for(int i = 0; i < nomeDrink.size(); i++){
                mNomiDrink.add(nomeDrink.get(i).getStrDrink());
            }
            mDrinkRepository.fetchDrinks(mNomiDrink.get(sPOSIZIONE));
        }
        else{
            Toast toastErrore = Toast.makeText(this, "Spiacenti! Non sono presenti drink con questo ingrediente", Toast.LENGTH_LONG);
            toastErrore.show();
        }

    }

    //Metodo di ritorno chiamata API dei drink
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResponse(List<Drink> drinkList) {
        if(drinkList != null){
            mDrinksWithDrinksApi.add(drinkList.get(0));
            visualizzaDrink(sPOSIZIONE);
            attivaBottoni();
        }
        else{
            Toast toastErrore = Toast.makeText(this, "Spiacenti! Non sono presenti drink con questo ingrediente", Toast.LENGTH_LONG);
            toastErrore.show();
        }

    }

    //Metodo che verrà invocato se la chiamata API non andrà a buon fine
    @Override
    public void onFailure(String msg) {
        Toast toastOnFailure = Toast.makeText(this, "ERRORE nella ricerca!", Toast.LENGTH_LONG);
        toastOnFailure.show();
    }

    //metodo che permette di visualizzare il drink
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void visualizzaDrink(int posizione){
        //Inizializzazione delle varie textbox con gli elementi associati al drink che vogliamo visualizzare.
        mTextView_Nome_Drink.setText(mDrinksWithDrinksApi.get(posizione).getStrDrink());
        mTextView_Alchool_Drink.setText(mDrinksWithDrinksApi.get(posizione).getStrAlcoholic());
        mTextView_Preparazione_Drink.setText(mDrinksWithDrinksApi.get(posizione).getStrInstructionsIT());
        imgGlide(mDrinksWithDrinksApi.get(posizione).getStrDrinkThumb());
        mTextView_Ingredienti_Drink.setText(recuperaIngredienti(posizione));
        mTextView_QuantitaIngredienti_Drink.setText(recuperaQuantitaIngredienti(posizione));
        attivaBottoni();
        mCardView_InfoDrink.setVisibility(View.VISIBLE);

        //se stiamo visualizzando un drink che è già presente nel nostro elenco preferiti dovremo modificare il bottone_Salva
        boolean trovato=false;
        for(int i = 0; i< mDrinksPreferiti.size(); i++)
        {
            if(Integer.parseInt(mDrinksPreferiti.get(i))== mDrinksWithDrinksApi.get(posizione).getIdDrink()){
                trovato=true;
            }
        }

        if(trovato){
            setChangesButtonSalva();
        }
        else {setDefaultButtonSalva();}

    }

    //Metodo che permette di scaricare l'immagine associata all'url passata
    private void imgGlide(String urlPassata){

        String url = urlPassata;
        String newUrl = null;

        if (url != null) {
            newUrl = url.replace("http://", "https://").trim();
            // Download dell'immagine associata al drink
            Glide.with(DrinkByIngredient.this)
                    .load(newUrl)
                    .into(mImageViewDownload);
        }

    }

    //Recupera la lista degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaIngredienti(int posizione){
        List<String> listaIngredienti = new ArrayList<>();
        String ingredienti = "";
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient1() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient1());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient2() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient2());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient3() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient3());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient4() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient4());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient5() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient5());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient6() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient6());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient7() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient7());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient8() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient8());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient9() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient9());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient10() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient10());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient11() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient11());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient12() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient12());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient13() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient13());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient14() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient14());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrIngredient15() != null){
            listaIngredienti.add(mDrinksWithDrinksApi.get(posizione).getStrIngredient15());
        }
        if(!listaIngredienti.isEmpty()){
            for(int i = 0; i<(listaIngredienti.size()-1);i++){
                ingredienti += listaIngredienti.get(i)+"\n";
            }
            ingredienti = ingredienti.concat(listaIngredienti.get(listaIngredienti.size()-1)+"");
        }
        return ingredienti;
    }

    //Recupera la quantità degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaQuantitaIngredienti(int posizione){
        String quantita = "";
        List<String> listaQuantita = new ArrayList<>();
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure1() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure1());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure2() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure2());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure3() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure3());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure4() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure4());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure5() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure5());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure6() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure6());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure7() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure7());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure8() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure8());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure9() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure9());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure10() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure10());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure11() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure11());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure12() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure12());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure13() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure13());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure14() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure14());
        }
        if(mDrinksWithDrinksApi.get(posizione).getStrMeasure15() != null){
            listaQuantita.add(mDrinksWithDrinksApi.get(posizione).getStrMeasure15());
        }
        if(!listaQuantita.isEmpty()){
            for(int i = 0; i<(listaQuantita.size()-1);i++){
                quantita += listaQuantita.get(i)+"\n";
            }
            quantita = quantita.concat(listaQuantita.get(listaQuantita.size()-1)+"");
        }
        return quantita;
    }

    //Metodo che attiva o disattiva i bottoni a seconda delle esigenze
    public void attivaBottoni(){

        if (sPOSIZIONE ==0 && mNomiDrink.size()==0)
        {
            mButton_Successivo_Drink.setVisibility(View.INVISIBLE);
            mButton_Precedente_Drink.setVisibility(View.INVISIBLE);

        }
        else if (sPOSIZIONE ==0 && mNomiDrink.size()!=0)
        {
            mButton_Successivo_Drink.setVisibility(View.VISIBLE);
            mButton_Precedente_Drink.setVisibility(View.INVISIBLE);

        }
        else if (sPOSIZIONE == mNomiDrink.size()-1)
        {
            mButton_Successivo_Drink.setVisibility(View.INVISIBLE);
            mButton_Precedente_Drink.setVisibility(View.VISIBLE);

        }else
        {
            mButton_Successivo_Drink.setVisibility(View.VISIBLE);
            mButton_Precedente_Drink.setVisibility(View.VISIBLE);
        }

    }

    //Metodo che salva su file preferiti l'id del drink passato
    private void salvaIdDrink(int idDrink) throws IOException {
        scriviFile(idDrink);
    }

    //Metodo che permette la scrittura su file
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
        mDrinksPreferiti = Arrays.asList(stringElencoPreferiti.split("\n"));
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
        mButton_Salva_Preferito.setBackgroundColor(0xFFCA4700);
        mButton_Salva_Preferito.setForeground(null);
    }

    //metodo che permette cambiamenti grafici una volta premuto il bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChangesButtonSalva(){
        mButton_Salva_Preferito.setBackgroundColor(0xFFDAA520);
        Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_on);
        mButton_Salva_Preferito.setForeground(drawable);
        mButton_Salva_Preferito.setForegroundGravity(View.TEXT_ALIGNMENT_GRAVITY);
    }

    // DA NON USARE
    @Override
    public void onResponseI(List<Ingredient> ingredientList) {
    }

}