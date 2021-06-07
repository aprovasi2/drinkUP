package com.example.drinkup.models;

import java.util.List;

public class Response {

    private List<Drink> drinks;

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinkList(List<Drink> drinks) {
        this.drinks = drinks;
    }
}
