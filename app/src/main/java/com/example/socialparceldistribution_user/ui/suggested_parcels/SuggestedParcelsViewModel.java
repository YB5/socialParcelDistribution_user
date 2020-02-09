package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
    private MutableLiveData<List<Parcel>> filteredParcelList;
    private IParcelRepository parcelRepository;
    private Geocoder geocoder;
    private LocationManager locationManager;
    private String maxDistFromLocation;
    private String maxDistFromDestination;
    private Location myLocation;
    private String destinationAddress;

    public SuggestedParcelsViewModel(@NonNull Application application) {
        super(application);
        parcelRepository = ParcelRepository.getInstance(application);
        geocoder = new Geocoder(application);
        locationManager = (LocationManager) getApplication().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        filteredParcelList = new MutableLiveData<>();
    }

    public LiveData<List<Parcel>> getParcels() {
        parcels = parcelRepository.getParcels();
        return parcels;
    }

    public MutableLiveData<List<Parcel>> getFilteredParcelList() { return filteredParcelList; }

    public void updateParcels(Parcel parcel) {
        parcelRepository.updateParcel(parcel);
    }

    public void findRelevantParcels() {
        //if no filter determined:
        if (maxDistFromDestination == null || maxDistFromDestination.isEmpty() &&
                maxDistFromLocation == null || maxDistFromLocation.isEmpty()) {
            filteredParcelList.setValue(parcels.getValue());
            return;
        }
        //if destination address line is empty
        if (destinationAddress == null || destinationAddress.isEmpty()) {
            filteredParcelList.setValue(parcels.getValue());
            return;
        }
        //if my location is null
        if (myLocation == null) {
            filteredParcelList.setValue(null);
            return;
        }
        double maxFromLoc = Double.parseDouble(maxDistFromLocation);
        double maxFromDest = Double.parseDouble(maxDistFromDestination);
        UserLocation myUserLocation = UserLocation.convertFromLocation(myLocation);
        UserLocation destUserLocation;
        try {
            List<Address> addressList = geocoder.getFromLocationName(destinationAddress, 1);
            if (addressList == null || addressList.isEmpty()) {
                filteredParcelList.setValue(null);
                return;
            }
            destUserLocation = new UserLocation(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        } catch (IOException e) {
            filteredParcelList.setValue(null);
            return;
        }
        List<Parcel> temp= new ArrayList<>();
        if (parcels.getValue() != null) {
            for (Parcel p : parcels.getValue())
                if (p.getWarehouseUserLocation().airDistanceInMeters_to(myUserLocation) < maxFromLoc &&
                        p.getRecipientUserLocation().airDistanceInMeters_to(destUserLocation) < maxFromDest)
                    temp.add(p);
        }
        filteredParcelList.setValue(temp);
    }

    void updateVolunteerState(int position, String userName) {
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

    Location getLocation() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getApplication().getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        else return null;
    }

    void setMaxDistFromLocation(String maxDistFromLocation) {
        this.maxDistFromLocation = maxDistFromLocation;
    }

    void setMaxDistFromDestination(String maxDistFromDestination) {
        this.maxDistFromDestination = maxDistFromDestination;
    }

    void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

}