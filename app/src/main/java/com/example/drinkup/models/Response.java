package com.example.drinkup.models;

import java.util.List;

public class Response {

    private List<Drink> drinks;

    private List<Ingredient> ingredients;
    public List<Drink> getDrinks() {
        return drinks;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void setDrinkList(List<Drink> drinks) {
        this.drinks = drinks;
    }
}
