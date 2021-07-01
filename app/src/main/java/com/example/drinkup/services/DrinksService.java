package com.example.drinkup.services;

import com.example.drinkup.models.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
//import retrofit2.http.Header;

public interface DrinksService {
    // ricerca drink per nome
    @GET("search.php")
    Call<Response> getDrink(@Query("s") String nomeDrink);

    // ricerca drink per id
    @GET("lookup.php")
    Call<Response> getPrefDrink(@Query("i") String id);

    // ricerca drink random
    @GET("random.php")
    Call<Response> getRandomDrink();

    // ricerca drink per ingrediente
    @GET("filter.php")
    Call<Response> getByIngredient(@Query("i") String nomeIngrediente);
}
