package com.example.drinkup.repositories;

import com.example.drinkup.models.Drink;
import com.example.drinkup.models.Ingredient;

import java.util.List;

public interface ResponseCallback {
    void onResponse(List<Drink> drinkList);
    void onFailure(String msg);
    void onResponseI(List<Ingredient> ingredientList);
    void onResponseNome(List<String> nomeDrink);
}
