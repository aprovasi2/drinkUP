package com.example.drinkup.repositories;

import com.example.drinkup.services.DrinksService;
import com.example.drinkup.services.IngredientService;
import com.example.drinkup.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    public DrinksService getDrinksServiceWithRetrofit(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DRINKS_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(DrinksService.class);
    }

    public IngredientService getIngredientServiceWithRetrofit(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DRINKS_API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(IngredientService.class);
    }


}
