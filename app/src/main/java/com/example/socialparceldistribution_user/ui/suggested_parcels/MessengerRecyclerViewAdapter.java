package com.example.socialparceldistribution_user.ui.suggested_parcels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialparceldistribution_user.Entities.Parcel;
import com.example.socialparceldistribution_user.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessengerRecyclerViewAdapter extends RecyclerView.Adapter<MessengerRecyclerViewAdapter.HistoryParcelViewHolder> {

    private List<Parcel> parcels;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public MessengerRecyclerViewAdapter(List<Parcel> parcels) {
        this.parcels = parcels;
    }

    class HistoryParcelViewHolder extends RecyclerView.ViewHolder {

        TextView status;
        TextView recipientName;
        TextView parcelType;
        TextView recipientAddress;
        TextView warehouseAddress;
        TextView date;
        TextView messengerName;

        private HistoryParcelViewHolder(View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status_tv);
            recipientName = itemView.findViewById(R.id.recipient_name_tv);
            parcelType = itemView.findViewById(R.id.type_tv);
            recipientAddress = itemView.findViewById(R.id.recipientAddressTv);
            warehouseAddress = itemView.findViewById(R.id.warehouseAddressTv);
            date = itemView.findViewById(R.id.date_tv);
            messengerName = itemView.findViewById(R.id.messenger_name_tv);
        }
    }

    @NonNull
    @Override
    public HistoryParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messenger_parcel_cell, parent, false);
        return new HistoryParcelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryParcelViewHolder holder, int position) {
        Parcel parcel = parcels.get(position);
        //holder.messengerName.setText(parcel.getMessengers().isEmpty()?"no messenger": parcel.getMessengerName()+"");
        holder.date.setText(parcel.getDeliveryDate() == null ? "no date" : format.format(parcel.getDeliveryDate()));
        holder.recipientAddress.setText(parcel.getRecipientAddress().isEmpty() ? "no address" : parcel.getRecipientAddress());
        holder.warehouseAddress.setText(parcel.getWarehouseAddress().isEmpty() ? "no warehouse address" : parcel.getWarehouseAddress());
        holder.recipientName.setText(parcel.getRecipientName().isEmpty() ? "no recipient name" : parcel.getRecipientName());
        holder.parcelType.setText(parcel.getParcelType() == null ? "no type" : parcel.getParcelType().toString());
        holder.status.setText(parcel.getParcelStatus() == null ? "no status" : parcel.getParcelStatus().toString());
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }
}
