package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.socialparceldistribution_user.Data.ParcelRepository;
import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public class SuggestedParcelsViewModel extends AndroidViewModel {

    private LiveData<List<Parcel>> parcels;
    private ParcelRepository parcelRepository;


    public SuggestedParcelsViewModel(@NonNull Application application) {
        super(application);
        parcelRepository= ParcelRepository.getInstance(application);

    }

    public LiveData<List<Parcel>> getParcels() {
        parcels= parcelRepository.getParcels();
        return parcels;
    }
}