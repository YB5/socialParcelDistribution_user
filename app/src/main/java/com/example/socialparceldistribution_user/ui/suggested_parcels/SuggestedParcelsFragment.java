package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.example.socialparceldistribution_user.Entities.Person;
import com.example.socialparceldistribution_user.Entities.UserLocation;
import com.example.socialparceldistribution_user.R;
import com.example.socialparceldistribution_user.ui.user_parcels.UserRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuggestedParcelsFragment extends Fragment {

    private List<Parcel> parcelList = new ArrayList<>();
    private EditText maxDistanceFromMyLocation;
    private EditText maxDistanceFromDestination;
    private EditText destinationEt;
    private String destinationAddress;
    private Location myLocation;
    private LocationManager locationManager;
    private String userName;
    String maxDistFromLocation;
    String maxDistFromDestination;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final SuggestedParcelsViewModel viewModel = ViewModelProviders.of(this).get(SuggestedParcelsViewModel.class);

        View root = inflater.inflate(R.layout.parcels_to_deliver, container, false);
        destinationEt = root.findViewById(R.id.dest_address_et);
        maxDistanceFromDestination = root.findViewById(R.id.maxFromDestination);
        maxDistanceFromMyLocation = root.findViewById(R.id.maxFromCurrentLocation);
        userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        Button findRelevant = root.findViewById(R.id.findParcels_bt);

        findRelevant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationAddress = destinationEt.getText().toString();
                if (destinationAddress.isEmpty()) {
                    Toast.makeText(getContext(), "please enter destination name", Toast.LENGTH_LONG).show();
                    return;
                }
                maxDistFromLocation = maxDistanceFromMyLocation.getText().toString();
                maxDistFromDestination = maxDistanceFromDestination.getText().toString();
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                getLocation();

                parcelList = viewModel.findRelevantParcels(maxDistFromLocation, maxDistFromDestination, myLocation, destinationAddress);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MessengerRecyclerViewAdapter suggestedParcelsAdapter = new MessengerRecyclerViewAdapter(parcelList, userName);
                suggestedParcelsAdapter.setListener(new MessengerRecyclerViewAdapter.SuggestedParcelsListener() {
                    @Override
                    public void onVolunteerButtonClicked(int position, View view) {
                        viewModel.updateVolunteerState(position, userName);
                    }
                });
                recyclerView.setAdapter(suggestedParcelsAdapter);

            }
        });

        viewModel.getParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                parcelList = viewModel.findRelevantParcels(maxDistFromLocation, maxDistFromDestination, myLocation, destinationAddress);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MessengerRecyclerViewAdapter suggestedParcelsAdapter = new MessengerRecyclerViewAdapter(parcelList, userName);
                suggestedParcelsAdapter.setListener(new MessengerRecyclerViewAdapter.SuggestedParcelsListener() {
                    @Override
                    public void onVolunteerButtonClicked(int position, View view) {
                        viewModel.updateVolunteerState(position, userName);
                    }
                });
                recyclerView.setAdapter(suggestedParcelsAdapter);
            }
        });

        return root;
    }


    private void getLocation() {
        final int Location_PERMISSION = 1;
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_PERMISSION);
        } else
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                if (Build.VERSION.SDK_INT >= 23 && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else {
                    myLocation = null;
                    Toast.makeText(getContext(), "Until you grant the permission, we cannot display the location", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}