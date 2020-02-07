package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.socialparceldistribution_user.Data.IParcelRepository;
import com.example.socialparceldistribution_user.Data.ParcelRepository;
import com.example.socialparceldistribution_user.Entities.Parcel;
import com.example.socialparceldistribution_user.Entities.UserLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuggestedParcelsViewModel extends AndroidViewModel {

    private LiveData<List<Parcel>> parcels;
    private IParcelRepository parcelRepository;
    private Geocoder geocoder;

    public SuggestedParcelsViewModel(@NonNull Application application) {
        super(application);
        parcelRepository = ParcelRepository.getInstance(application);
        geocoder = new Geocoder(application);
    }

    public LiveData<List<Parcel>> getParcels() {
        parcels = parcelRepository.getParcels();
        return parcels;
    }

    public void updateParcels(Parcel parcel) {
        parcelRepository.updateParcel(parcel);
    }

    public List<Parcel> findRelevantParcels(String maxDistFromLocation, String maxDistFromDestination, Location myLocation, String destinationAddress) {
        List<Parcel> filteredList = new ArrayList<>();
        //if no filter determined:
        if (maxDistFromDestination==null||maxDistFromDestination.isEmpty() &&
                maxDistFromLocation==null||maxDistFromLocation.isEmpty())
            return parcels.getValue();
        if (destinationAddress==null||destinationAddress.isEmpty())
            return parcels.getValue();
        if(myLocation==null)
            return new ArrayList<>();

        double maxFromLoc = Double.parseDouble(maxDistFromLocation);
        double maxFromDest = Double.parseDouble(maxDistFromDestination);

        UserLocation myUserLocation = UserLocation.convertFromLocation(myLocation);
        UserLocation destUserLocation;
        try {
            List<Address> addressList = geocoder.getFromLocationName(destinationAddress, 1);
            if (addressList == null || addressList.isEmpty())
                return new ArrayList<>();
            destUserLocation = new UserLocation(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        } catch (IOException e) {
            return new ArrayList<>();
        }

        if (parcels.getValue() != null) {
            for (Parcel p : parcels.getValue())
                if (p.getWarehouseUserLocation().airDistanceInMeters_to(myUserLocation) < maxFromLoc &&
                        p.getRecipientUserLocation().airDistanceInMeters_to(destUserLocation) < maxFromDest)
                    filteredList.add(p);
        }
        return filteredList;
    }

    public void updateVolunteerState(int position, String userName) {
        Parcel parcel = parcels.getValue().get(position);
        if (parcel.getMessengers() == null) {
            HashMap hashMap = new HashMap();
            hashMap.put(userName, false);
            parcel.setMessengers(hashMap);
        } else {
            //if key is already exist, cancel the registration
            if (parcel.getMessengers().containsKey(userName))
                parcel.getMessengers().remove(userName);
            else
                parcel.getMessengers().put(userName, false);
        }
        updateParcels(parcel);
    }
}