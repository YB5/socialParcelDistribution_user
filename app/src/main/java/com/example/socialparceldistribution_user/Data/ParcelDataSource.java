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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.socialparceldistribution_user.Entities.Parcel.ParcelStatus.successfullyArrived;

public class ParcelDataSource implements IParcelDataSource {

    private MutableLiveData<Boolean> isSuccess= new MutableLiveData<>();
    @Override
    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }
    List<Parcel> allParcelsList;
    MutableLiveData<List<Parcel>> myParcels= new MutableLiveData<>();

    @Override
    public MutableLiveData<List<Parcel>> getMyParcels() {
        return myParcels;
    }

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference existingParcels = firebaseDatabase.getReference("ExistingParcels");
    DatabaseReference historyParcels = firebaseDatabase.getReference("HistoryParcels");


    private ParcelDataSource() {
        allParcelsList = new ArrayList<>();
        myParcels.setValue(new ArrayList<Parcel>());

        existingParcels.addValueEventListener(new ValueEventListener() {
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

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                String email = firebaseAuth.getCurrentUser().getEmail();
                existingParcels.child(email.replace(".",",")).addValueEventListener(new ValueEventListener() {
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
            }
        });

    }

    @Override
    public void updateParcel(Parcel parcel) {
        String email= parcel.getRecipientEmail();
        String id= parcel.getParcelId();
        HashMap map= new HashMap();
        map.put(id,parcel);
        existingParcels.child(email.replace(".",",")).updateChildren(map);
    }

    @Override
    public void arrivedParcel(Parcel parcel) {

        parcel.setParcelStatus(successfullyArrived);
        parcel.setArrivalDate(new Date());
        historyParcels.child(parcel.getRecipientEmail().replace(".",",")).child(parcel.getParcelId()).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    isSuccess.setValue(true);
                    isSuccess.setValue(null);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    isSuccess.setValue(false);
                    isSuccess.setValue(null);
                }
            });
        existingParcels.child(parcel.getRecipientEmail().replace(".",",")).child(parcel.getParcelId()).removeValue();

    }

//    public interface parcelsChangedListener {
//        void onParcelsChanged();
//    }
    private ParcelsChangedListener parcelsChangedListener;


    public void setParcelsChangedListener(ParcelsChangedListener l) {
        parcelsChangedListener = l;
    }

//    public interface myParcelsChangedListener {
//        void onMyParcelsChanged();
//    }
    private MyParcelsChangedListener myParcelsChangedListener;
    public void setMyParcelsChangedListener(MyParcelsChangedListener l) {
        myParcelsChangedListener = l;
    }

    @Override
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



