package com.example.findyourband.services;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AlreadyBandMemberChecker {



    public interface BandCheckCallback {
        void onCheckCompleted(boolean isInBand);
    }

    public static void isUserAlreadyInBand(String login, BandCheckCallback callback) {
        DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");

        bandsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot bandSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> members = (ArrayList<String>) bandSnapshot.child("memberUserLogins").getValue();
                    if (members != null && members.contains(login)) {
                        callback.onCheckCompleted(true);
                        return;
                    }
                }
                callback.onCheckCompleted(false);
            }
        }).addOnFailureListener(e -> {
            callback.onCheckCompleted(false);
        });
    }


}
