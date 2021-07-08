package com.example.drinkup.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drinkup.ActivityDrink;
import com.example.drinkup.ActivityIngredient;
import com.example.drinkup.DrinkByIngredient;
import com.example.drinkup.R;
import com.example.drinkup.RandomDrink;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel view;
    private Button mButtondrinkingredient;
    private Button mButtondrink;
    private Button mbuttonIngredient;
    private Button mButtonRandom;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {


        view = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mButtondrinkingredient = (Button) root.findViewById(R.id.Button_DrinkPerIngredient);
        mButtondrinkingredient.setOnClickListener(this);

        mButtondrink = (Button) root.findViewById(R.id.ButtonDrink);
        mButtondrink.setOnClickListener(this);

        mbuttonIngredient = (Button) root.findViewById(R.id.button_Ingredient);
        mbuttonIngredient.setOnClickListener(this);

        mButtonRandom = (Button) root.findViewById(R.id.button_randomDrink);
        mButtonRandom.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ButtonDrink:
               //azione da compiere dopo aver cliccato bottone drink
                Intent intentNomeDrink = new Intent(getActivity(), ActivityDrink.class);
                startActivity(intentNomeDrink);
                break;

            case R.id.Button_DrinkPerIngredient:
                //azione da compiere dopo aver cliccato bottone beer
                Intent intentByIngredient = new Intent(getActivity(), DrinkByIngredient.class);
                startActivity(intentByIngredient);
                break;

            case R.id.button_Ingredient:
                //azione da compiere dopo aver cliccato bottone drink
                Intent intentIngredient = new Intent(getActivity(), ActivityIngredient.class);
                startActivity(intentIngredient);
                break;

            case R.id.button_randomDrink:
                //azione da compiere dopo aver cliccato bottone beer
                Intent intentRandom = new Intent(getActivity(), RandomDrink.class);
                startActivity(intentRandom);
                break;
        }

    }
}