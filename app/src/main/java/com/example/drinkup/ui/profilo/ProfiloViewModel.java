package com.example.drinkup.ui.profilo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfiloViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfiloViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profilo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
