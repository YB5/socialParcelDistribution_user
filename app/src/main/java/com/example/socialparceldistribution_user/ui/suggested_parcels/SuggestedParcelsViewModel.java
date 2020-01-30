package com.example.socialparceldistribution_user.ui.suggested_parcels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SuggestedParcelsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SuggestedParcelsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is suggested parcels fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}