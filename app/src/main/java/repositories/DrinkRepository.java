package repositories;

import com.example.drinkup.Drink;
import com.example.drinkup.ServiceLocator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.DrinkService;

public class DrinkRepository implements IDrinkRepository {
    private DrinkService drinkService;
    private ResponseCallback responseCallback;

    @Override
    public void fetchDrink() {
        Call<Response> call = drinkService.getTopHeadlines("Margarita");
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, Response<Response> response) {
                if (response.body() != null && response.isSuccessful())
                {
                    List<Drink> drinkList = response.body().get ;


                }

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });


    }


    public DrinkRepository(ResponseCallback responseCallback){
    this.drinkService = ServiceLocator.getInstance().getDrinkServiceWithRetrofit();
    this.responseCallback = responseCallback;

    }


}
