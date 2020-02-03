package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialparceldistribution_user.Entities.Parcel;
import com.example.socialparceldistribution_user.Entities.UserLocation;
import com.example.socialparceldistribution_user.R;
import com.example.socialparceldistribution_user.ui.user_parcels.UserRecyclerViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestedParcelsFragment extends Fragment {

    private List<Parcel> parcelList = new ArrayList<>();
    private EditText maxDistanceFromMyLocation;
    private EditText maxDistanceFromDestination;
    private EditText destinationEt;
    private Geocoder geocoder;
    private UserLocation destinationLocation;
    private UserLocation myLocation;
    private LocationManager locationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SuggestedParcelsViewModel viewModel =
                ViewModelProviders.of(this).get(SuggestedParcelsViewModel.class);

        View root = inflater.inflate(R.layout.parcels_to_deliver, container, false);
        geocoder = new Geocoder(getContext());
        destinationEt = root.findViewById(R.id.dest_address_et);
        Button findRelevant = root.findViewById(R.id.findParcels_bt);
        maxDistanceFromDestination =root.findViewById(R.id.maxFromDestination);
        maxDistanceFromMyLocation =root.findViewById(R.id.maxFromCurrentLocation);
        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

        findRelevant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destinationEt.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"please enter destination name",Toast.LENGTH_LONG).show();
                    return;}
                try {
                    List<Address> list = geocoder.getFromLocationName(destinationEt.getText().toString(), 1);
                    destinationLocation = new UserLocation(list.get(0).getLatitude(), list.get(0).getLongitude());
                } catch (IOException e) {
                    destinationLocation = null;
                    Toast.makeText(getContext(), "Cannot calculate the destination. Please turn on the gps", Toast.LENGTH_LONG).show();
                }

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MessengerRecyclerViewAdapter messengerRecyclerViewAdapter = new MessengerRecyclerViewAdapter(myFilter(parcelList));
                recyclerView.setAdapter(messengerRecyclerViewAdapter);

            }
        });
//
//        try {
//            List<Address> list = geocoder.getFromLocationName(destinationEt.getText().toString(), 1);
//            destinationLocation = new UserLocation(list.get(0).getLatitude(), list.get(0).getLongitude());
//        } catch (IOException e) {
//            destinationLocation = null;
//            Toast.makeText(getContext(), "Cannot calculate the destination. Please turn on the gps", Toast.LENGTH_LONG).show();
//        }
//
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        MessengerRecyclerViewAdapter messengerRecyclerViewAdapter = new MessengerRecyclerViewAdapter(parcelList);
//        recyclerView.setAdapter(messengerRecyclerViewAdapter);

        viewModel.getParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                parcelList = myFilter(parcels);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                UserRecyclerViewAdapter historyParcelsAdapter = new UserRecyclerViewAdapter(parcelList);
                recyclerView.setAdapter(historyParcelsAdapter);
            }
        });

        return root;
    }

    private List<Parcel> myFilter(List<Parcel> parcels) {
        List<Parcel> filteredList = new ArrayList<>();
        //if no filter determined:
        if(maxDistanceFromDestination.getText().toString().isEmpty()|| maxDistanceFromDestination.getText()==null)
            return parcels;
        double maxDistFromLocation = Double.parseDouble(maxDistanceFromMyLocation.getText().toString());
        double maxDistFromDestination = Double.parseDouble(maxDistanceFromDestination.getText().toString());

        getLocation();
        if (myLocation == null) {
            Toast.makeText(getContext(), "your local location cannot be calculated", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();//empty list
        } else
            for (Parcel p : parcels)
                if (p.getWarehouseUserLocation().airDistanceInMeters_to(myLocation) < maxDistFromLocation &&
                        p.getRecipientUserLocation().airDistanceInMeters_to(destinationLocation) < maxDistFromDestination)
                    filteredList.add(p);
        return filteredList;
    }


    private void getLocation() {
        final int Location_PERMISSION = 1;
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_PERMISSION);
        } else
            myLocation = UserLocation.convertFromLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                if (Build.VERSION.SDK_INT >= 23 && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    myLocation = UserLocation.convertFromLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                } else {
                    myLocation = null;
                    Toast.makeText(getContext(), "Until you grant the permission, we cannot display the location", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}