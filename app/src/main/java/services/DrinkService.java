package services;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DrinkService {
    @GET("top-headlines")
    Call<Response> getTopHeadlines(/*@Query("country") String country,
                                   @Query("pageSize") int pageSize,
                                   @Query("page") int page,
                                   @Header("Authorization") String apiKey*/

                                   @Query("nomeDrink") String nomeDrink

    );

}
