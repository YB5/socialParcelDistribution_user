package com.example.socialparceldistribution_user.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.socialparceldistribution_user.Entities.Parcel;


@Database(entities = Parcel.class, version = 1, exportSchema = false)
@TypeConverters({Parcel.ParcelStatus.class, Parcel.ParcelType.class, Parcel.DateConverter.class, Parcel.UserLocationConverter.class, Parcel.MessengersConverter.class})
public abstract class RoomDataSource extends RoomDatabase {

    public static final String DATABASE_NAME="database.db";
    private static RoomDataSource database;

    public static RoomDataSource getInstance(Context context){
        if (database==null)
            database= Room.databaseBuilder(context, RoomDataSource.class,DATABASE_NAME).allowMainThreadQueries().build();
        return database;
    }

    public abstract ParcelDao getParcelDao();
}
