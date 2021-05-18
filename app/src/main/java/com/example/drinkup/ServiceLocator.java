package com.example.drinkup;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import services.DrinkService;

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

    public DrinkService getDrinkServiceWithRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(" qualcosa con l'url ---- Constants.NEWS_API_BASE_URL").
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(DrinkService.class);
    }

    public ArticleRoomDatabase getArticleDao(Application application) {
        return ArticleRoomDatabase.getDatabase(application);
    }


}
