package repositories;

import com.example.drinkup.Drink;

import java.util.List;


public interface ResponseCallback {

    void onResponse(List<Drink> drinkList, long lastUpdate);
    void onFailure(String msg);
}
