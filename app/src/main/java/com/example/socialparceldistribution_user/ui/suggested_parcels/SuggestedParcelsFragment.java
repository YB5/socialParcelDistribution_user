package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
import com.example.socialparceldistribution_user.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
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
    private String maxDistFromLocation;
    private String maxDistFromDestination;
    private SuggestedParcelsViewModel viewModel;
    final int LOCATION_PERMISSION = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(SuggestedParcelsViewModel.class);
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
                //check for permissions and call getLocation at view model
                getLocation();
                if (myLocation==null)
                    Toast.makeText(getContext(),"please turn on the gps",Toast.LENGTH_LONG).show();

                parcelList = viewModel.findRelevantParcels(maxDistFromLocation, maxDistFromDestination, myLocation, destinationAddress);
                if (parcelList.isEmpty())
                    Toast.makeText(getContext(),"no parcels appropriate",Toast.LENGTH_LONG).show();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MessengerRecyclerViewAdapter suggestedParcelsAdapter = new MessengerRecyclerViewAdapter(parcelList, userName);
                suggestedParcelsAdapter.setListener(new MessengerRecyclerViewAdapter.SuggestedParcelsListener() {
                    @Override
                    public void onButtonClicked(int position, View view) {
                        viewModel.updateVolunteerState(position, userName);
                    }
                });
                recyclerView.setAdapter(suggestedParcelsAdapter);
            }
        });

        viewModel.getParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(final List<Parcel> parcels) {
                parcelList = viewModel.findRelevantParcels(maxDistFromLocation, maxDistFromDestination, myLocation, destinationAddress);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MessengerRecyclerViewAdapter suggestedParcelsAdapter = new MessengerRecyclerViewAdapter(parcelList, userName);
                suggestedParcelsAdapter.setListener(new MessengerRecyclerViewAdapter.SuggestedParcelsListener() {
                    @Override
                    public void onButtonClicked(int position, View view) {
                        if(view.getId()==R.id.bt_volunteer)
                            viewModel.updateVolunteerState(position, userName);
                        else if (view.getId() == R.id.bt_sendSMS) {
                            String phone=parcels.get(position).getRecipientPhone();
                            if (phone.isEmpty()) {
                                Toast.makeText(getContext(), "no phone number exist", Toast.LENGTH_LONG).show();
                            }
                            else {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)));
                            }
                        }
                        else if (view.getId() == R.id.bt_sendMail) {
                            String mail=parcels.get(position).getRecipientEmail();

                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto",mail, null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        }

                    }
                });
                recyclerView.setAdapter(suggestedParcelsAdapter);
            }
        });
        return root;
    }


    private void getLocation() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        } else
            myLocation = viewModel.getLocation();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                if (Build.VERSION.SDK_INT >= 23 && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    myLocation = viewModel.getLocation();
                } else {
                    myLocation = null;
                    Toast.makeText(getContext(), "Until you grant the permission, we cannot display the location", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}