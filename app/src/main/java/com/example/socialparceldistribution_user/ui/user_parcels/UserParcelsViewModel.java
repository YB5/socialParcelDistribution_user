package com.example.socialparceldistribution_user.ui.user_parcels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialparceldistribution_user.Data.ParcelRepository;
import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public class UserParcelsViewModel extends AndroidViewModel {


    private List<Parcel> myParcels;
    private ParcelRepository parcelRepository;

    public UserParcelsViewModel(@NonNull Application application) {
        super(application);
       parcelRepository= ParcelRepository.getInstance(application);
    }

    public LiveData<List<Parcel>> getMyParcels() {
        LiveData<List<Parcel>> listLiveData=  parcelRepository.getMyParcels();
        myParcels = listLiveData.getValue();
        return listLiveData;
    }

    public void updateParcels(Parcel parcel) {
        parcelRepository.updateParcel(parcel);
    }
    public void arrivedParcel(Parcel parcel) {
        parcelRepository.arrivedParcel(parcel);
    }
}