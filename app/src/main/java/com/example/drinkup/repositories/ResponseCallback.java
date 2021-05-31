package com.example.drinkup.repositories;

import com.example.drinkup.models.Drink;

import java.util.List;

public interface ResponseCallback {
    void onResponse(List<Drink> drinkList);
    void onFailure(String msg);
}
