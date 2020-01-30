package com.example.socialparceldistribution_user.ui.user_parcels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserParcelsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserParcelsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user parcels fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}