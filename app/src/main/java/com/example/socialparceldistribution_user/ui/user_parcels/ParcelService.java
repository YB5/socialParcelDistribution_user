package com.example.socialparceldistribution_user.ui.user_parcels;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialparceldistribution_user.Data.ParcelDataSource;
import com.example.socialparceldistribution_user.Entities.Parcel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class ParcelService extends Service {
    String TAG = "mySer";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myParcelsRef = firebaseDatabase.getReference("ExistingParcels").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","));

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        myParcelsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Parcel parcel = dataSnapshot.getValue(Parcel.class);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND,-10);
                Date date = calendar.getTime();
                if (parcel.getDeliveryDate().after(date)){
                    Intent intent=  new Intent("YBandSHU.A_CUSTOM_INTENT");
                    //intent.setComponent( new ComponentName("com.example.socialparceldistribution_bcreceiver", "com.example.socialparceldistribution_bcreceiver.parcelReceiver"));
                    sendBroadcast(intent);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        parcelDataSource = ParcelDataSource.getInstance();
//        //databaseHelper= new RoomDatabaseHelper(application.getApplicationContext());
//        ParcelDataSource.parcelsChangedListener parcelsChangedListener = new ParcelDataSource.parcelsChangedListener() {
//            @Override
//            public void onParcelsChanged() {
//                if(true/*the parcel owner*/) {
//                    Intent intent = new Intent("YBandSHU.A_CUSTOM_INTENT");
//                    //intent.setAction( new ComponentName("com.example.socialparceldistribution_bcreceiver", "com.example.socialparceldistribution_bcreceiver.parcelReceiver"));
//                    sendBroadcast(intent);
//                }
//            }
//        };
//        parcelDataSource.setParcelsChangedListener(parcelsChangedListener);
//
//        ParcelDataSource.get



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

    }
}
