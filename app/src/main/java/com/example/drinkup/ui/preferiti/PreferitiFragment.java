package com.example.drinkup.ui.preferiti;

import android.app.FragmentManager;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.drinkup.R;
import com.example.drinkup.ToastCustom;
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

    private static final String TAG = "PreferitiFragment";

    //Dichiarazione variabili
    private PreferitiViewModel preferitiViewModel;
    private IDrinkRepository mDrinkRepository;
    private List<Drink> mDrinksPreferitiWithDrinksApi;
    private List<String> mElencoIdDrink;
    private List<String> drinkRimossi;
    String mResult = "";
    private TextView mTextViewPrefe_Nome_Drink;
    private TextView mTextViewPrefe_Alchool_Drink;
    private TextView mTextViewPrefe_Ingredienti_Drink;
    private TextView mTextViewPrefe_QuantitaIngredienti_Drink;
    private TextView mTextViewPrefe_Preparazione_Drink;
    private ImageView mImageViewPrefe_Drink;
    private Button mButtonPrefe_Precedente_Drink;
    private Button mButtonPrefe_Successivo_Drink;
    private Button mButtonPrefe_Salva_Preferito;
    public static int sPosizionePref = 999;
    private CardView mCardView_InfoDrinkPrefe;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferitiViewModel =
                new ViewModelProvider(this).get(PreferitiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_preferiti, container, false);

        mDrinkRepository = new DrinkRepository(this, requireActivity().getApplication());
        mDrinksPreferitiWithDrinksApi = new ArrayList<>();
        mElencoIdDrink = new ArrayList<>();
        drinkRimossi= new ArrayList<>();

        //Inizializzazione
        mButtonPrefe_Successivo_Drink = root.findViewById(R.id.buttonPrefe_Successivo_Drink);
        mButtonPrefe_Successivo_Drink.setOnClickListener(this);
        mButtonPrefe_Successivo_Drink.setVisibility(View.INVISIBLE);
        mButtonPrefe_Salva_Preferito = root.findViewById(R.id.buttonPrefe_Salva_Preferito);
        mButtonPrefe_Salva_Preferito.setOnClickListener(this);
        mButtonPrefe_Precedente_Drink = root.findViewById(R.id.buttonPrefe_Precedente_Drink);
        mButtonPrefe_Precedente_Drink.setOnClickListener(this);
        mButtonPrefe_Precedente_Drink.setVisibility(View.INVISIBLE);
        mTextViewPrefe_Nome_Drink = root.findViewById(R.id.textViewPrefe_Nome_Drink);
        mTextViewPrefe_Alchool_Drink = root.findViewById(R.id.textViewPrefe_Alchool_Drink);
        mTextViewPrefe_Ingredienti_Drink = root.findViewById(R.id.textViewPrefe_Ingredienti_Drink);
        mTextViewPrefe_QuantitaIngredienti_Drink = root.findViewById(R.id.textViewPrefe_QuantitaIngredienti_Drink);
        mTextViewPrefe_Preparazione_Drink = root.findViewById(R.id.textViewPrefe_Preparazione_Drink);
        mImageViewPrefe_Drink = root.findViewById(R.id.imageViewPrefe_Drink);
        mCardView_InfoDrinkPrefe = root.findViewById(R.id.CardView_InfoDrinkPrefe);
        mCardView_InfoDrinkPrefe.setVisibility(View.INVISIBLE);
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
        mElencoIdDrink = Arrays.asList(mResult.split("\n"));
        sPosizionePref = 0;
        mDrinkRepository.fetchPreferitiDrinks(mElencoIdDrink.get(sPosizionePref));
        setChangesButtonSalva();
        
    }

    //Metodo di risposta dell'API, in cui verranno salavti nella lista tutti i drink recuperati e visualizzato il primo
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResponse(List<Drink> drinkList) {
        mDrinksPreferitiWithDrinksApi.addAll(drinkList);
        visualizzaDrink(sPosizionePref);
        attivaBottoni();
    }

    //Metodo di fallimento chiamata API
    @Override
    public void onFailure(String msg) {
        ToastCustom.makeText(requireActivity().getApplication(),ToastCustom.TYPE_INFO,"La lista preferiti è vuota").show();

    }

    //Metodo che gestisce il click sui vari bottoni
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buttonPrefe_Precedente_Drink) {
            sPosizionePref = sPosizionePref - 1;
            mDrinkRepository.fetchPreferitiDrinks(mElencoIdDrink.get(sPosizionePref));
        }
        if (v.getId() == R.id.buttonPrefe_Successivo_Drink) {
            sPosizionePref = sPosizionePref + 1;
            mDrinkRepository.fetchPreferitiDrinks(mElencoIdDrink.get(sPosizionePref));
        }
        if (v.getId() == R.id.buttonPrefe_Salva_Preferito) {
            try {
                cancellaDrinkdaFile(Integer.parseInt(mElencoIdDrink.get(sPosizionePref)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ToastCustom.makeText(requireActivity().getApplication(),ToastCustom.TYPE_REMOVE,"Il drink è stato rimosso dalla lista preferiti").show();
            Reload();

        }
    }

    // Metodo per ricaricare il fragment
    public void Reload(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(PreferitiFragment.this.getId(), new PreferitiFragment()).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void visualizzaDrink(int posizione) {

        //Inizializzazione delle varie textbox con gli elementi associati al drink che vogliamo visualizzare.
        mTextViewPrefe_Ingredienti_Drink.setText(recuperaIngredienti(posizione));
        mTextViewPrefe_QuantitaIngredienti_Drink.setText(recuperaQuantitaIngredienti(posizione));
        mTextViewPrefe_Nome_Drink.setText(mDrinksPreferitiWithDrinksApi.get(posizione).getStrDrink());
        mTextViewPrefe_Alchool_Drink.setText(mDrinksPreferitiWithDrinksApi.get(posizione).getStrAlcoholic());
        mTextViewPrefe_Preparazione_Drink.setText(mDrinksPreferitiWithDrinksApi.get(posizione).getStrInstructionsIT());
        imgGlide(mDrinksPreferitiWithDrinksApi.get(posizione).getStrDrinkThumb());
        mCardView_InfoDrinkPrefe.setVisibility(View.VISIBLE);
        attivaBottoni();
    }

    //Metodo che attiva o disattiva i bottoni a seconda delle esigenze
    public void attivaBottoni() {

        if (sPosizionePref == 0 && mElencoIdDrink.size() == 1) {
            mButtonPrefe_Successivo_Drink.setVisibility(View.INVISIBLE);
            mButtonPrefe_Precedente_Drink.setVisibility(View.INVISIBLE);

        } else if (sPosizionePref == 0 && mElencoIdDrink.size() != 1) {
            mButtonPrefe_Successivo_Drink.setVisibility(View.VISIBLE);
            mButtonPrefe_Precedente_Drink.setVisibility(View.INVISIBLE);

        } else if (sPosizionePref == mElencoIdDrink.size() - 1) {
            mButtonPrefe_Successivo_Drink.setVisibility(View.INVISIBLE);
            mButtonPrefe_Precedente_Drink.setVisibility(View.VISIBLE);

        } else {
            mButtonPrefe_Precedente_Drink.setVisibility(View.VISIBLE);
            mButtonPrefe_Precedente_Drink.setVisibility(View.VISIBLE);
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
                    .into(mImageViewPrefe_Drink);
        }

    }

    //Recupera la lista degli ingredienti associata ad un determinato drink passato per posizione
    private String recuperaIngredienti(int posizione) {
        List<String> listaIngredienti = new ArrayList<>();
        String ingredienti = "";
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient1() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient1());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient2() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient2());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient3() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient3());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient4() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient4());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient5() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient5());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient6() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient6());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient7() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient7());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient8() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient8());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient9() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient9());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient10() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient10());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient11() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient11());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient12() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient12());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient13() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient13());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient14() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient14());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient15() != null) {
            listaIngredienti.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrIngredient15());
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
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure1() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure1());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure2() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure2());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure3() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure3());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure4() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure4());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure5() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure5());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure6() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure6());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure7() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure7());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure8() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure8());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure9() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure9());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure10() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure10());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure11() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure11());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure12() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure12());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure13() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure13());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure14() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure14());
        }
        if (mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure15() != null) {
            listaQuantita.add(mDrinksPreferitiWithDrinksApi.get(posizione).getStrMeasure15());
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
        drinksPreferitiClone.addAll(mElencoIdDrink);
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
        mElencoIdDrink = new ArrayList<>();
        mElencoIdDrink.addAll(drinksPreferitiClone);
    }

    //metodo che permette cambiamenti grafici una volta premuto il bottone "Salva Preferito"
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChangesButtonSalva() {
        mButtonPrefe_Salva_Preferito.setBackgroundColor(0xFFDAA520);
        Drawable drawable = getResources().getDrawable(android.R.drawable.btn_star_big_on);
        mButtonPrefe_Salva_Preferito.setForeground(drawable);
        mButtonPrefe_Salva_Preferito.setForegroundGravity(View.TEXT_ALIGNMENT_GRAVITY);
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