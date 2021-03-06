package com.example.socialparceldistribution_user.Data;

import android.app.Application;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public class ParcelRepository implements IParcelRepository {

    private MutableLiveData<List<Parcel>> mutableLiveData = new MutableLiveData<>();
    private IParcelDataSource parcelDataSource;
    private RoomDatabaseHelper databaseHelper;
    private static ParcelRepository instance;
    public static ParcelRepository getInstance(Application application) {
        if (instance == null)
            instance = new ParcelRepository(application);
        return instance;
    }

    private ParcelRepository(Application application) {
        parcelDataSource = ParcelFirebaseDataSource.getInstance();
        databaseHelper = new RoomDatabaseHelper(application.getApplicationContext());
        IParcelDataSource.ParcelsChangedListener parcelsChangedListener = new IParcelDataSource.ParcelsChangedListener() {
            @Override
            public void onParcelsChanged() {
                List<Parcel> parcelList = parcelDataSource.getAllParcelsList();
                mutableLiveData.setValue(parcelDataSource.getAllParcelsList());
                databaseHelper.clearTable();
                databaseHelper.addParcels(parcelList);

            }
        };
        parcelDataSource.setParcelsChangedListener(parcelsChangedListener);
    }

    @Override
    public LiveData<List<Parcel>> getParcels() {
        return databaseHelper.getParcels();
    }

    @Override
    public LiveData<Boolean> getIsSuccess() {
        return parcelDataSource.getIsSuccess();
    }

    @Override
    public void updateParcel(Parcel parcel) {
        parcelDataSource.updateParcel(parcel);
    }

    @Override
    public void arrivedParcel(Parcel parcel) {
        parcelDataSource.arrivedParcel(parcel);
    }

    @Override
    public LiveData<List<Parcel>> getMyParcels() {
        return parcelDataSource.getMyParcels();
    }
}
