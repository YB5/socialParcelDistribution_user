package com.example.socialparceldistribution_user.Data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public class RoomDatabaseHelper {
    private ParcelDao parcelDao;

    public RoomDatabaseHelper(Context context){
        RoomDataSource database= RoomDataSource.getInstance(context);
        parcelDao =database.getParcelDao();
        parcelDao.clear();
    }

    public LiveData<List<Parcel>> getParcels(){
        return parcelDao.getAll();
    }

    public LiveData<Parcel> getParcel(String id){
        return parcelDao.get(id);
    }

    public void addParcel(Parcel p) {
        parcelDao.insert(p);
    }

    public void addParcels(List<Parcel> parcelList) {
        parcelDao.insert(parcelList);
    }

    public void editParcel(Parcel p) {
        parcelDao.update(p);
    }

    public void deleteParcel(Parcel p){
        parcelDao.delete(p);
    }

    public void clearTable(){parcelDao.clear();}
}
