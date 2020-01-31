package com.example.socialparceldistribution_user.ui.user_parcels;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.socialparceldistribution_user.Data.ParcelDataSource;
import com.example.socialparceldistribution_user.Data.RoomDatabaseHelper;
import com.example.socialparceldistribution_user.Entities.Parcel;

import java.util.List;

public class ParcelService extends Service {
    ParcelDataSource parcelDataSource;
    String TAG = "mySer";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
        parcelDataSource = ParcelDataSource.getInstance();
        //databaseHelper= new RoomDatabaseHelper(application.getApplicationContext());
        ParcelDataSource.changedListener changedListener= new ParcelDataSource.changedListener() {
            @Override
            public void change() {
                //todo : Identify the parcel owner
                if(true/*the parcel owner*/) {
                    Intent intent = new Intent("YBandSHU.A_CUSTOM_INTENT");
                    //intent.setAction( new ComponentName("com.example.socialparceldistribution_bcreceiver", "com.example.socialparceldistribution_bcreceiver.parcelReceiver"));
                    sendBroadcast(intent);
                }
            }
        };
        parcelDataSource.setChangedListener(changedListener);




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");

    }
}
