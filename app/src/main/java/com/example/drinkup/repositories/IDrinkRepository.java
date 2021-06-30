package com.example.drinkup.repositories;

public interface IDrinkRepository {
    void fetchDrinks(String nomeDrink);
    void fetchPreferitiDrinks(String id);

    void fetchRandomDrink();
}
