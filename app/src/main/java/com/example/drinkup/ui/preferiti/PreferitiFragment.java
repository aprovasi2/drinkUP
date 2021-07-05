package com.example.drinkup.ui.preferiti;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.drinkup.R;
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

public class PreferitiFragment extends Fragment implements ResponseCallback, View.OnClickListener {

    //Dichiarazione variabili
    private PreferitiViewModel preferitiViewModel;
    private IDrinkRepository drinkRepository;
    private List<Drink> drinksPreferitiWithDrinksApi;
    private List<String> elencoIdDrink;
    private List<String> drinkRimossi;
    String mResult = "";
    private TextView textViewPrefe_Nome_Drink;
    private TextView textViewPrefe_Alchool_Drink;
    private TextView textViewPrefe_Ingredienti_Drink;
    private TextView textViewPrefe_QuantitaIngredienti_Drink;
    private TextView textViewPrefe_Preparazione_Drink;
    private ImageView imageViewPrefe_Drink;
    private Button buttonPrefe_Precedente_Drink;
    private Button buttonPrefe_Successivo_Drink;
    private Button buttonPrefe_Salva_Preferito;
    public static int posizionePref = 999;
    private CardView cardView_InfoDrinkPrefe;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferitiViewModel =
                new ViewModelProvider(this).get(PreferitiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_preferiti, container, false);

        drinkRepository = new DrinkRepository(this, requireActivity().getApplication());
        drinksPreferitiWithDrinksApi = new ArrayList<>();
        elencoIdDrink = new ArrayList<>();
        drinkRimossi= new ArrayList<>();

        //Inizializzazione
        buttonPrefe_Successivo_Drink = root.findViewById(R.id.buttonPrefe_Successivo_Drink);
        buttonPrefe_Successivo_Drink.setOnClickListener(this);
        buttonPrefe_Successivo_Drink.setVisibility(View.INVISIBLE);
        buttonPrefe_Salva_Preferito = root.findViewById(R.id.buttonPrefe_Salva_Preferito);
        buttonPrefe_Salva_Preferito.setOnClickListener(this);
        buttonPrefe_Precedente_Drink = root.findViewById(R.id.buttonPrefe_Precedente_Drink);
        buttonPrefe_Precedente_Drink.setOnClickListener(this);
        buttonPrefe_Precedente_Drink.setVisibility(View.INVISIBLE);
        textViewPrefe_Nome_Drink = root.findViewById(R.id.textViewPrefe_Nome_Drink);
        textViewPrefe_Alchool_Drink = root.findViewById(R.id.textViewPrefe_Alchool_Drink);
        textViewPrefe_Ingredienti_Drink = root.findViewById(R.id.textViewPrefe_Ingredienti_Drink);
        textViewPrefe_QuantitaIngredienti_Drink = root.findViewById(R.id.textViewPrefe_QuantitaIngredienti_Drink);
        textViewPrefe_Preparazione_Drink = root.findViewById(R.id.textViewPrefe_Preparazione_Drink);
        imageViewPrefe_Drink = root.findViewById(R.id.imageViewPrefe_Drink);
        cardView_InfoDrinkPrefe = root.findViewById(R.id.CardView_InfoDrinkPrefe);
        cardView_InfoDrinkPrefe.setVisibility(View.INVISIBLE);
        return root;
    }

    //Metodi da eseguire una volta che l'activity è stata creata
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Recupero da file l'elenco degli id di tutti i drink salvati nei preferiti
        File path = requireActivity().getApplication().getFilesDir();
        File file = new File(path, "ElencoPreferiti.txt");

        try {
            mResult = leggiFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Salvo nella lista tutti gli id dei drink preferiti opportunamente splittati
        elencoIdDrink = Arrays.asList(mResult.split("\n"));
        posizionePref = 0;
        drinkRepository.fetchPreferitiDrinks(elencoIdDrink.get(posizionePref));
        setChangesButtonSalva();
        
    }

    //Metodo di risposta dell'API, in cui verranno salavti nella lista tutti i drink recuperati e visualizzato il primo
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResponse(List<Drink> drinkList) {
        drinksPreferitiWithDrinksApi.addAll(drinkList);
        visualizzaDrink(posizionePref);
        attivaBottoni();
    }

    //Metodo di fallimento chiamata API
    @Override
    public void onFailure(String msg) {
        Toast toastFailure= Toast.makeText(requireActivity().getApplication(), "NON HAI DRINK PREFERITI", Toast.LENGTH_LONG);
        toastFailure.show();
    }

    //Metodo che gestisce il click sui vari bottoni
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buttonPrefe_Precedente_Drink) {
           // attivaBottoni();
            posizionePref = posizionePref - 1;
            drinkRepository.fetchPreferitiDrinks(elencoIdDrink.get(posizionePref));
            attivaBottoni();
        }
        if (v.getId() == R.id.buttonPrefe_Successivo_Drink) {
          //  attivaBottoni();
            posizionePref = posizionePref + 1;
            drinkRepository.fetchPreferitiDrinks(elencoIdDrink.get(posizionePref));
            attivaBottoni();
        }
        if (v.getId() == R.id.buttonPrefe_Salva_Preferito) {
            try {
                cancellaDrinkdaFile(Integer.parseInt(elencoIdDrink.get(posizionePref)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setDefaultButtonSalva();
            Toast toastRimosso= Toast.makeText(requireActivity().getApplication(), "Il drink" + elencoIdDrink.get(posizionePref)+" è stato rimosso dalla tua lista preferiti", Toast.LENGTH_LONG);
            toastRimosso.show();
            elencoIdDrink.remove(posizionePref);
            drinksPreferitiWithDrinksApi.remove(posizionePref);
            posizionePref=0;
            visualizzaDrink(posizionePref);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void visualizzaDrink(int posizione) {

        //Inizializzazione delle varie textbox con gli elementi associati al drink che vogliamo visualizzare.
        textViewPrefe_Ingredienti_Drink.setText(recuperaIngredienti(posizione));
        textViewPrefe_QuantitaIngredienti_Drink.setText(recuperaQuantitaIngredienti(posizione));
        textViewPrefe_Nome_Drink.setText(drinksPreferitiWithDrinksApi.get(posizione).getStrDrink());
        textViewPrefe_Alchool_Drink.setText(drinksPreferitiWithDrinksApi.get(posizione).getStrAlcoholic());
        textViewPrefe_Preparazione_Drink.setText(drinksPreferitiWithDrinksApi.get(posizione).getStrInstructionsIT());
        imgGlide(drinksPreferitiWithDrinksApi.get(posizione).getStrDrinkThumb());
        cardView_InfoDrinkPrefe.setVisibility(View.VISIBLE);
        attivaBottoni();

    }

    //Metodo che attiva o disattiva i bottoni a seconda delle esigenze
    public void attivaBottoni() {

        if (posizionePref == 0 && elencoIdDrink.size() == 0) {
            buttonPrefe_Successivo_Drink.setVisibility(View.INVISIBLE);
            buttonPrefe_Precedente_Drink.setVisibility(View.INVISIBLE);

        } else if (posizionePref == 0 && elencoIdDrink.size() != 0) {
            buttonPrefe_Successivo_Drink.setVisibility(View.VISIBLE);
            buttonPrefe_Precedente_Drink.setVisibility(View.INVISIBLE);

        } else if (posizionePref == elencoIdDrink.size() - 1) {
            buttonPrefe_Successivo_Drink.setVisibility(View.INVISIBLE);
            buttonPrefe_Precedente_Drink.setVisibility(View.VISIBLE);

        } else {
            buttonPrefe_Precedente_Drink.setVisibility(View.VISIBLE);
            buttonPrefe_Precedente_Drink.setVisibility(View.VISIBLE);
        }

    }

    //Metodo che permette di scaricare l'immagine associata all'url passata
    private void imgGlide(String urlPassata) {

        String url = urlPassata;
        String newUrl = null;

        if (url != null) {

            newUrl = url.replace("http://", "https://").trim();

            Glide.with(this)
                    .load(newUrl)
                    .into(imageViewPrefe_Drink);
        }

    }

    //Recupera la lista degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaIngredienti(int posizione) {
        List<String> listaIngredienti = new ArrayList<>();
        String ingredienti = "";
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient1() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient1());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient2() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient2());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient3() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient3());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient4() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient4());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient5() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient5());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient6() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient6());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient7() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient7());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient8() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient8());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient9() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient9());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient10() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient10());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient11() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient11());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient12() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient12());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient13() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient13());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient14() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient14());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient15() != null) {
            listaIngredienti.add(drinksPreferitiWithDrinksApi.get(posizione).getStrIngredient15());
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
    private String recuperaQuantitaIngredienti(int posizione) {
        String quantita = "";
        List<String> listaQuantita = new ArrayList<>();
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure1() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure1());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure2() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure2());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure3() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure3());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure4() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure4());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure5() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure5());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure6() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure6());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure7() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure7());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure8() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure8());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure9() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure9());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure10() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure10());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure11() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure11());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure12() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure12());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure13() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure13());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure14() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure14());
        }
        if (drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure15() != null) {
            listaQuantita.add(drinksPreferitiWithDrinksApi.get(posizione).getStrMeasure15());
        }
        if(!listaQuantita.isEmpty()){
            for(int i = 0; i<(listaQuantita.size()-1);i++){
                quantita += listaQuantita.get(i)+"\n";
            }
            quantita = quantita.concat(listaQuantita.get(listaQuantita.size()-1)+"");
        }
        return quantita;
    }

    //Metodo che si occupa della scrittura su file
    private File scriviFile(int data) throws IOException {
        File path = requireActivity().getApplication().getFilesDir();
        String idDrink = "" + data + "\n";

        File file = new File(path, "ElencoPreferiti.txt");

        if (!file.exists()) {
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(idDrink.getBytes());
            } finally {
                stream.close();
            }

        } else {
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

    //Metodo che permette di leggere il file passato come argomento e ritorna una stringa composta del contenuto
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

    //Metodo che cancella da file l'id del drink passato come argomento
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cancellaDrinkdaFile(int id) throws IOException {
        File path = requireActivity().getApplication().getFilesDir();
        File file = new File(path, "ElencoPreferiti.txt");
        Integer value = new Integer(id);
        String daRimuovere = value.toString();


        //Parte nuova, al posto di lavorare sull'arrayList originale, se ne si fa una copia e si lavora su quella
        List<String> drinksPreferitiClone = new ArrayList<>();
        drinksPreferitiClone.addAll(elencoIdDrink);
        for (int i = 0; i < drinksPreferitiClone.size(); i++) {
            String daRemove = drinksPreferitiClone.get(i);
            if (daRemove.equals(id + "")) {
                drinksPreferitiClone.remove(daRemove);
                i--;
                //break;
            }
        }
        file.delete();
        for (int i = 0; i < drinksPreferitiClone.size(); i++) {
            scriviFile(Integer.parseInt(drinksPreferitiClone.get(i)));
        }
        //Una volta fatto, riportiamo tutti i valori nell'elenco originale
        //Per farlo prima liberiamo la lista, con il clear crasha quindi si crea nuova
        elencoIdDrink = new ArrayList<>();
        elencoIdDrink.addAll(drinksPreferitiClone);
        setDefaultButtonSalva();
    }

    //metodo che permette cambiamenti grafici una volta premuto il bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChangesButtonSalva() {
        buttonPrefe_Salva_Preferito.setBackgroundColor(0xFFDAA520);
        Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_on);
        buttonPrefe_Salva_Preferito.setForeground(drawable);
        buttonPrefe_Salva_Preferito.setForegroundGravity(View.TEXT_ALIGNMENT_GRAVITY);
    }

    //metodo che permette di riportare i valori di default al bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setDefaultButtonSalva() {
        buttonPrefe_Salva_Preferito.setBackgroundColor(0xFFCA4700);
        buttonPrefe_Salva_Preferito.setForeground(null);
    }

    //DA NON USARE
    @Override
    public void onResponseI(List<Ingredient> ingredientList) {
    }

    // DA NON USARE
    @Override
    public void onResponseNome(List<Drink> nomeDrink) {
    }

}