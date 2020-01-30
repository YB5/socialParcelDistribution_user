package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.socialparceldistribution_user.R;

public class SuggestedParcelsFragment extends Fragment {

    private SuggestedParcelsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(SuggestedParcelsViewModel.class);
        View root = inflater.inflate(R.layout.parcels_to_deliver, container, false);
        final TextView textView = root.findViewById(R.id.parcels_to_deliver_tv);
        viewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}