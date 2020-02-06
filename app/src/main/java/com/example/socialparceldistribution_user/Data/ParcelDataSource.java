package com.example.socialparceldistribution_user.Data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.socialparceldistribution_user.Entities.Parcel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParcelDataSource implements IParcelDataSource {

    private MutableLiveData<Boolean> isSuccess= new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }
    List<Parcel> allParcelsList;
    MutableLiveData<List<Parcel>> myParcels= new MutableLiveData<>();

    public MutableLiveData<List<Parcel>> getMyParcels() {
        return myParcels;
    }

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference parcels = firebaseDatabase.getReference("ExistingParcels");


    private ParcelDataSource() {
        allParcelsList = new ArrayList<>();
        myParcels.setValue(new ArrayList<Parcel>());

        parcels.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allParcelsList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Parcel parcel = snapshot1.getValue(Parcel.class);
                            allParcelsList.add(parcel);
                        }
                }
                if (parcelsChangedListener != null)
                    parcelsChangedListener.onParcelsChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        parcels.child(email.replace(".",",")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Parcel> temp= new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Parcel parcel = snapshot.getValue(Parcel.class);
                        temp.add(parcel);
                    }
                    myParcels.setValue(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void updateParcel(Parcel parcel) {
        String email= parcel.getRecipientEmail();
        String id= parcel.getParcelId();
        HashMap map= new HashMap();
        map.put(id,parcel);
        parcels.child(email.replace(".",",")).updateChildren(map);
    }

    public interface parcelsChangedListener {
        void onParcelsChanged();
    }
    private parcelsChangedListener parcelsChangedListener;
    public void setParcelsChangedListener(parcelsChangedListener l) {
        parcelsChangedListener = l;
    }

    public interface myParcelsChangedListener {
        void onMyParcelsChanged();
    }
    private myParcelsChangedListener myParcelsChangedListener;
    public void setMyParcelsChangedListener(myParcelsChangedListener l) {
        myParcelsChangedListener = l;
    }

    public List<Parcel> getAllParcelsList() {
        return allParcelsList;
    }

    private static ParcelDataSource instance;

    public static ParcelDataSource getInstance() {
        if (instance == null)
            instance = new ParcelDataSource();
        return instance;
    }


}



