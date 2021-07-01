package com.example.drinkup.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Ingredient;
import com.example.drinkup.models.Response;
import com.example.drinkup.services.DrinksService;
import com.example.drinkup.utils.Constants;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

// classe per la ricerca di drink dall'API
public class DrinkRepository implements IDrinkRepository{

    // dichiarazioni variabili
    private final DrinksService drinksService;
    private final ResponseCallback responseCallback;
    private long lastUpdate = 0;

    // costruttuore della classe
    public DrinkRepository(ResponseCallback responseCallback, Application application){
        this.drinksService=ServiceLocator.getInstance().getDrinksServiceWithRetrofit();
        this.responseCallback=responseCallback;
    }

    // metodo per il recupero delle informazioni dall'API: ricerca drink per nome
    @Override
    public void fetchDrinks(String nomeDrink) {
        Call<Response> call = drinksService.getDrink(nomeDrink);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull  retrofit2.Response<Response> response) {
                if (response.body() != null && response.isSuccessful() ) {
                    List<Drink> drinkList = response.body().getDrinks();
                    responseCallback.onResponse(drinkList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Response> call,@NonNull  Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }

    // metodo per il recupero delle informazioni dall'API: ricerca drink per id
    public void fetchPreferitiDrinks(String id) {
        Call<Response> call = drinksService.getPrefDrink(id);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull  retrofit2.Response<Response> response) {
                if (response.body() != null && response.isSuccessful() ) {
                    List<Drink> drinkList = response.body().getDrinks();
                    responseCallback.onResponse(drinkList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Response> call,@NonNull  Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }

    // metodo per il recupero delle informazioni dall'API: ricerca drink random
    public void fetchRandomDrink() {
        Call<Response> call = drinksService.getRandomDrink();
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull  retrofit2.Response<Response> response) {
                if (response.body() != null && response.isSuccessful() ) {
                    List<Drink> drinkList = response.body().getDrinks();
                    responseCallback.onResponse(drinkList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Response> call,@NonNull  Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }

    // metodo per il recupero delle informazioni dall'API: ricerca drink per ingrediente
    public void fetchByIngredient(String nomeIngrediente) {
        Call<Response> call = drinksService.getByIngredient(nomeIngrediente);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull  retrofit2.Response<Response> response) {
                if (response.body() != null && response.isSuccessful() ) {
                    List<Drink> drinkList = response.body().getDrinks();
                    responseCallback.onResponseNome(drinkList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Response> call,@NonNull  Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }

}
