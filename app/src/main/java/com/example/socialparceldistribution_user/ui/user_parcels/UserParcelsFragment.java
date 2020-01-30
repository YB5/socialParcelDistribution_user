package com.example.socialparceldistribution_user.ui.user_parcels;

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

public class UserParcelsFragment extends Fragment {

    private UserParcelsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(UserParcelsViewModel.class);
        View root = inflater.inflate(R.layout.user_parcels, container, false);
        final TextView textView = root.findViewById(R.id.user_parcels_tv);
        viewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}