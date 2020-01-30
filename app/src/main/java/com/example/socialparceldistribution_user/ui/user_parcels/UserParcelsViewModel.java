package com.example.socialparceldistribution_user.ui.user_parcels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialparceldistribution_user.Data.ParcelRepository;
import com.example.socialparceldistribution_user.Entities.Parcel;
import com.example.socialparceldistribution_user.R;
import com.example.socialparceldistribution_user.RecyclerViewAdapter;

import java.util.List;

public class UserParcelsViewModel extends AndroidViewModel {


    private LiveData<List<Parcel>> parcels;
    private ParcelRepository database;

    public UserParcelsViewModel(@NonNull Application application) {
        super(application);
        database=ParcelRepository.getInstance(application);
    }




    LiveData<List<Parcel>> getParcels() {
        return database.getParcels();
    }

}