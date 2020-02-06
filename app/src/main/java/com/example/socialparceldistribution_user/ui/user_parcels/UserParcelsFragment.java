package com.example.socialparceldistribution_user.ui.user_parcels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialparceldistribution_user.Entities.Parcel;
import com.example.socialparceldistribution_user.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserParcelsFragment extends Fragment {

    private List<Parcel> myParcelsList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final UserParcelsViewModel viewModel = ViewModelProviders.of(this).get(UserParcelsViewModel.class);
        View root = inflater.inflate(R.layout.history_parcels, container, false);

        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final UserRecyclerViewAdapter myParcelsAdapter = new UserRecyclerViewAdapter(myParcelsList);
        myParcelsAdapter.setListener(new UserRecyclerViewAdapter.MyParcelsListener() {
            @Override
            public void onVolunteerButtonClicked(final int position, View view) {
                HashMap<String, Boolean> map = myParcelsList.get(position).getMessengers();
                final Object[] keysAsObjectArray=map.keySet().toArray();
                final String[] keys = Arrays.copyOf(keysAsObjectArray,keysAsObjectArray.length,String[].class);
                boolean[] approvalKeys = new boolean[keys.length];
                for (int i = 0; i < map.size(); i++) {
                    if (map.get(keys[i]) == true)
                        approvalKeys[i] = true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("choose which messenger to approve").setMultiChoiceItems(keys, approvalKeys, new DialogInterface.OnMultiChoiceClickListener() {
                     @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Parcel parcel = myParcelsList.get(position);
                        parcel.getMessengers().put(keys[which], isChecked);
                        viewModel.updateParcels(parcel);
                    }
                }).setPositiveButton("finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });

        recyclerView.setAdapter(myParcelsAdapter);


        viewModel.getMyParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                myParcelsList = parcels;
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                final UserRecyclerViewAdapter myParcelsAdapter = new UserRecyclerViewAdapter(myParcelsList);
                myParcelsAdapter.setListener(new UserRecyclerViewAdapter.MyParcelsListener() {
                    @Override
                    public void onVolunteerButtonClicked(final int position, View view) {
                        HashMap<String, Boolean> map = myParcelsList.get(position).getMessengers();
                        final Object[] keysAsObjectArray=map.keySet().toArray();
                        final String[] keys = Arrays.copyOf(keysAsObjectArray,keysAsObjectArray.length,String[].class);
                        boolean[] approvalKeys = new boolean[keys.length];
                        for (int i = 0; i < map.size(); i++) {
                            if (map.get(keys[i]) == true)
                                approvalKeys[i] = true;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("choose which messenger to approve").setMultiChoiceItems(keys, approvalKeys, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                Parcel parcel = myParcelsList.get(position);
                                parcel.getMessengers().put(keys[which], isChecked);
                                viewModel.updateParcels(parcel);
                            }
                        }).setPositiveButton("finish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                    }
                });


                recyclerView.setAdapter(myParcelsAdapter);
            }
        });


        return root;
    }
}
