package com.example.socialparceldistribution_user.ui.user_parcels;

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
import java.util.List;

public class UserParcelsFragment extends Fragment {

    private List<Parcel> parcelList = new ArrayList<>();
    private UserParcelsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(UserParcelsViewModel.class);
        View root = inflater.inflate(R.layout.history_parcels, container, false);

        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        UserRecyclerViewAdapter historyParcelsAdapter = new UserRecyclerViewAdapter(parcelList);
        recyclerView.setAdapter(historyParcelsAdapter);

        UserParcelsViewModel viewModel = ViewModelProviders.of(this).get(UserParcelsViewModel.class);
        viewModel.getParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                parcelList = parcels;
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                UserRecyclerViewAdapter historyParcelsAdapter = new UserRecyclerViewAdapter(parcelList);
                recyclerView.setAdapter(historyParcelsAdapter);
            }
        });

        return root;
    }
}
