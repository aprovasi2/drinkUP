package com.example.drinkup.services;

import com.example.drinkup.models.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
//import retrofit2.http.Header;

public interface IngredientService {
    @GET("search.php")
    Call<Response> getIngredient(@Query("i") String nomeIngredient);
}
