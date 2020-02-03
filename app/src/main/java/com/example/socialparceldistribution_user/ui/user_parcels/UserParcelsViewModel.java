package com.example.socialparceldistribution_user.ui.user_parcels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialparceldistribution_user.Data.ParcelRepository;
import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public class UserParcelsViewModel extends AndroidViewModel {


    private LiveData<List<Parcel>> parcels;
    private ParcelRepository database;

    public UserParcelsViewModel(@NonNull Application application) {
        super(application);
        database=ParcelRepository.getInstance(application);
    }

    LiveData<List<Parcel>> getParcels() {
        parcels= database.getParcels();
        return parcels;
    }

}