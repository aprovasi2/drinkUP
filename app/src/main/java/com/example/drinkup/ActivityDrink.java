package com.example.drinkup;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ActivityDrink extends AppCompatActivity implements View.OnClickListener {

    private Button button_Search;
    private EditText drinkDaCercare;
    private TextView nomeDrink;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        button_Search = (Button) findViewById(R.id.button_Search);
        button_Search.setOnClickListener(this);

        nomeDrink = (TextView) findViewById(R.id.textView_Alchool_Drink);
        drinkDaCercare = (EditText) findViewById(R.id.editTextText_DrinkSearch);




    }



    @Override
    public void onClick(View v) {

        String ricerca = drinkDaCercare.getText().toString();
        Toast toast = Toast.makeText(this, "ho provato a ricercare "+ricerca, Toast.LENGTH_LONG);
        toast.show();



    }

    private List<Drink> getArticlesWithGson() {
        InputStream fileInputStream = null;
        JsonReader jsonReader = null;
        try {
            fileInputStream = this.open("com/example/drinkup/ui/search.php.json");
            jsonReader = new JsonReader(new InputStreamReader(fileInputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        Response response = new Gson().fromJson(bufferedReader, Response.class);

        return response.getDrinksList();
    }

}