package com.example.drinkup.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Response;
import com.example.drinkup.services.DrinksService;
import com.example.drinkup.utils.Constants;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DrinkRepository implements IDrinkRepository{

    private final DrinksService drinksService;
    private final ResponseCallback responseCallback;
    private long lastUpdate = 0;

    public DrinkRepository(ResponseCallback responseCallback, Application application){
        this.drinksService=ServiceLocator.getInstance().getDrinksServiceWithRetrofit();
        this.responseCallback=responseCallback;
    }

    @Override
    public void fetchDrinks(String nomeDrink) {
        Call<Response> call = drinksService.getDrink(nomeDrink);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull  retrofit2.Response<Response> response) {

                if (response.body() != null && response.isSuccessful()) {
                    List<Drink> drinkList = response.body().getDrinks();
                    
                    for (Drink drink : drinkList) {
                        Log.d("bodyDrink", drink.getStrDrink());

                    }
                    responseCallback.onResponse(drinkList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Response> call,@NonNull  Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }
}
