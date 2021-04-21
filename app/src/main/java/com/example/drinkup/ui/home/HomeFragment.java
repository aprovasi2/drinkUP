package com.example.drinkup.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.drinkup.ActivityDrink;
import com.example.drinkup.R;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel view;
    private ImageButton imagebuttonebeer;
    private ImageButton imagebuttonedrink;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {


        view = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        imagebuttonebeer = (ImageButton) root.findViewById(R.id.imageButtonBeer);
        imagebuttonebeer.setOnClickListener(this);

        imagebuttonedrink = (ImageButton) root.findViewById(R.id.imageButtonDrink);
        imagebuttonedrink.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageButtonDrink:
                //Toast.makeText(this.getContext(), "cliccatodrink", Toast.LENGTH_LONG).show();
                //fare riferimento a questo
                //https://stackoverflow.com/questions/15478105/start-an-activity-from-a-fragment
                Intent intent = new Intent(getActivity(), ActivityDrink.class);
                startActivity(intent);


                break;
            case R.id.imageButtonBeer:
                Toast.makeText(this.getContext(), "cliccatobeer", Toast.LENGTH_LONG).show();
                break;

        }


    }
}