package com.example.socialparceldistribution_user.Data;

import androidx.lifecycle.LiveData;

import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public interface IParcelRepository {
    void updateParcel(Parcel parcel);
    void arrivedParcel(Parcel parcel);
    LiveData<List<Parcel>> getMyParcels();
    LiveData<Boolean> getIsSuccess();
    LiveData<List<Parcel>> getParcels();
}
