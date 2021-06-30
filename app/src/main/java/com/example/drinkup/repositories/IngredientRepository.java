package com.example.drinkup.repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.drinkup.models.Ingredient;
import com.example.drinkup.models.Response;
import com.example.drinkup.services.IngredientService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class IngredientRepository implements IIngredientRepository{

    private final IngredientService ingredientService;
    private final ResponseCallback responseCallback;

    public IngredientRepository(ResponseCallback responseCallback, Application application){
        this.ingredientService = ServiceLocator.getInstance().getIngredientServiceWithRetrofit();
        this.responseCallback = responseCallback;
    }

    public void fetchIngredient(String nomeIngredient) {
        Call<Response> call = ingredientService.getIngredient(nomeIngredient);

        call.enqueue(new Callback<Response>() {

            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull  retrofit2.Response<Response> response) {

                if (response.body() != null && response.isSuccessful() ) {
                    List<Ingredient> ingredientList = response.body().getIngredients();
                    responseCallback.onResponseI(ingredientList);
                }

            }

            @Override
            public void onFailure(@NonNull Call<Response> call,@NonNull  Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }
}
