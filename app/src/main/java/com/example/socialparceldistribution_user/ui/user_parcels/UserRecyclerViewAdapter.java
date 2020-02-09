package com.example.socialparceldistribution_user.ui.user_parcels;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialparceldistribution_user.Entities.Parcel;
import com.example.socialparceldistribution_user.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.HistoryParcelViewHolder> {

    private List<Parcel> parcels;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public UserRecyclerViewAdapter(List<Parcel> parcels) {
        this.parcels = parcels;
    }

    private MyParcelsListener listener;

    interface MyParcelsListener {
        void onButtonClicked(int position, View view);
    }

    void setListener(MyParcelsListener listener) {
        this.listener = listener;
    }


    class HistoryParcelViewHolder extends RecyclerView.ViewHolder {

        TextView status;
        TextView recipientName;
        TextView parcelType;
        TextView recipientAddress;
        TextView warehouseAddress;
        TextView date;
        TextView messengerName;
        Button seeSuggestions;
        Button setArrivedParcel;

        private HistoryParcelViewHolder(View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status_tv);
            recipientName = itemView.findViewById(R.id.recipient_name_tv);
            parcelType = itemView.findViewById(R.id.type_tv);
            recipientAddress = itemView.findViewById(R.id.recipientAddressTv);
            warehouseAddress = itemView.findViewById(R.id.warehouseAddressTv);
            date = itemView.findViewById(R.id.date_tv);
            seeSuggestions = itemView.findViewById(R.id.bt_see_suggestions);
            seeSuggestions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null)
                        listener.onButtonClicked(getAdapterPosition(), view);
                }
            });
            setArrivedParcel = itemView.findViewById(R.id.bt_arrivedParcel);
            setArrivedParcel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onButtonClicked(getAdapterPosition(), view);
                }
            });

        }
    }

    @NonNull
    @Override
    public HistoryParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcel_cell, parent, false);
        return new HistoryParcelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryParcelViewHolder holder, int position) {
        Parcel parcel = parcels.get(position);
        holder.date.setText(parcel.getDeliveryDate() == null ? "no date" : format.format(parcel.getDeliveryDate()));
        holder.recipientAddress.setText(parcel.getRecipientAddress().isEmpty() ? "no recipient address" : parcel.getRecipientAddress());
        holder.warehouseAddress.setText(parcel.getWarehouseAddress().isEmpty() ? "no warehouse address" : parcel.getRecipientAddress());
        holder.recipientName.setText(parcel.getRecipientName().isEmpty() ? "no recipient name" : parcel.getRecipientName());
        holder.parcelType.setText(parcel.getParcelType() == null ? "no type" : parcel.getParcelType().toString());
        holder.status.setText(parcel.getParcelStatus() == null ? "no status" : parcel.getParcelStatus().toString());
        holder.seeSuggestions.setEnabled(parcel.getMessengers() != null ? true : false);
        holder.seeSuggestions.setText(parcel.getMessengers() != null ? parcel.getMessengers().size() + " suggestions" : "no suggestions");
        holder.seeSuggestions.setBackgroundColor(parcel.getMessengers() != null && parcel.getMessengers().size() > 0 ? Color.GREEN : Color.GRAY);
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }
}
