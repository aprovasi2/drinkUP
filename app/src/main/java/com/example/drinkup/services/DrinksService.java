package com.example.drinkup.services;

import com.example.drinkup.models.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
//import retrofit2.http.Header;

public interface DrinksService {
    @GET("search.php")
    Call<Response> getDrink(@Query("s") String nomeDrink);

    @GET("lookup.php")
    Call<Response> getPrefDrink(@Query("i") String id);

    @GET("random.php")
    Call<Response> getRandomDrink();

    @GET("filter.php")
    Call<Response> getByIngredient(@Query("i") String nomeIngrediente);
}
