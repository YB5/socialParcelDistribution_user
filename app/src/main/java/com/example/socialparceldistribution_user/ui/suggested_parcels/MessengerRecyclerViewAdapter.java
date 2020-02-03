package com.example.socialparceldistribution_user.ui.suggested_parcels;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessengerRecyclerViewAdapter extends RecyclerView.Adapter<MessengerRecyclerViewAdapter.SuggestedParcelsViewHolder> {

    private List<Parcel> parcels;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private SuggestedParcelsListener listener;
    private String currentUser;

    MessengerRecyclerViewAdapter(List<Parcel> parcels, String currentUSer) {
        this.parcels = parcels;
        this.currentUser=currentUSer;
    }

    interface SuggestedParcelsListener {
        void onVolunteerButtonClicked(int position, View view);
    }

    void setListener(SuggestedParcelsListener listener){
        this.listener=listener;
    }

    class SuggestedParcelsViewHolder extends RecyclerView.ViewHolder {


        TextView status;
        TextView recipientName;
        TextView parcelType;
        TextView recipientAddress;
        TextView warehouseAddress;
        TextView date;
        TextView messengerName;
        final Button volunteer_bt;


        private SuggestedParcelsViewHolder(View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status_tv);
            recipientName = itemView.findViewById(R.id.recipient_name_tv);
            parcelType = itemView.findViewById(R.id.type_tv);
            recipientAddress = itemView.findViewById(R.id.recipientAddressTv);
            warehouseAddress = itemView.findViewById(R.id.warehouseAddressTv);
            date = itemView.findViewById(R.id.date_tv);
            messengerName = itemView.findViewById(R.id.messenger_name_tv);
            volunteer_bt=itemView.findViewById(R.id.bt_volunteer);
            volunteer_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null)
                        listener.onVolunteerButtonClicked(getAdapterPosition(),view);
                    //volunteer_bt.setText(R.string.cancellation);
                }
            });
        }
    }

    @NonNull
    @Override
    public SuggestedParcelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messenger_parcel_cell, parent, false);
        return new SuggestedParcelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedParcelsViewHolder holder, int position) {
        Parcel parcel = parcels.get(position);
        //holder.messengerName.setText(parcel.getMessengers().isEmpty()?"no messenger": parcel.getMessengerName()+"");
        holder.date.setText(parcel.getDeliveryDate() == null ? "no date" : format.format(parcel.getDeliveryDate()));
        holder.recipientAddress.setText(parcel.getRecipientAddress().isEmpty() ? "no address" : parcel.getRecipientAddress());
        holder.warehouseAddress.setText(parcel.getWarehouseAddress().isEmpty() ? "no warehouse address" : parcel.getWarehouseAddress());
        holder.recipientName.setText(parcel.getRecipientName().isEmpty() ? "no recipient name" : parcel.getRecipientName());
        holder.parcelType.setText(parcel.getParcelType() == null ? "no type" : parcel.getParcelType().toString());
        holder.status.setText(parcel.getParcelStatus() == null ? "no status" : parcel.getParcelStatus().toString());
        holder.volunteer_bt.setText(parcel.getMessengers()!=null&&parcel.getMessengers().containsKey(currentUser)?R.string.cancellation:R.string.suggestOption);
        holder.volunteer_bt.setBackgroundColor(parcel.getMessengers()!=null&&parcel.getMessengers().containsKey(currentUser)? Color.RED :Color.GREEN);

    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }
}
