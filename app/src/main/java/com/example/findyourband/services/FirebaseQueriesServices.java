package com.example.findyourband.services;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseQueriesServices {



    public interface BandCheckCallback {
        void onCheckCompleted(boolean isInBand);
    }

    public interface UserIdGetterCallback {
        void onGetIdCompleted(String ID);
    }

    public interface UserContactsGetterCallback {
        void onGetContactsCompleted(ArrayList<String> contacts);
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

    public static void getUserIdByLogin(String login, UserIdGetterCallback callback){

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child("login").getValue(String.class).equals(login)) {
                        callback.onGetIdCompleted(userSnapshot.getKey());
                        return;
                    }
                }
                callback.onGetIdCompleted(null);
            }
        }).addOnFailureListener(e -> {
            callback.onGetIdCompleted(null);
        });


    }

    public static void getUserContactsByLogin(String login, UserContactsGetterCallback callback){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.child("login").getValue(String.class).equals(login)) {
                        ArrayList<String> contacts = new ArrayList<>();
                        for (DataSnapshot contactSnapshot : userSnapshot.child("contacts").getChildren()) {
                            contacts.add(contactSnapshot.getValue(String.class));
                        }
                        callback.onGetContactsCompleted(contacts);
                        return;
                    }
                }
                callback.onGetContactsCompleted(null);
            }
        }).addOnFailureListener(e -> {
            callback.onGetContactsCompleted(null);
        });
    }


}
