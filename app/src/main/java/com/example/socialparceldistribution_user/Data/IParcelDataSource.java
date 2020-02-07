package com.example.socialparceldistribution_user.Data;

import androidx.lifecycle.MutableLiveData;

import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public interface IParcelDataSource {
    MutableLiveData<List<Parcel>> getMyParcels();
    void updateParcel(Parcel parcel);
    void arrivedParcel(Parcel parcel);

}
