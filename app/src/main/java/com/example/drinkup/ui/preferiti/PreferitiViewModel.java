package com.example.drinkup.ui.preferiti;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PreferitiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PreferitiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is preferiti fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}