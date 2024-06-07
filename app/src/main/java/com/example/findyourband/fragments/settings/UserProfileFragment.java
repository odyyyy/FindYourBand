package com.example.findyourband.fragments.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.findyourband.databinding.FragmentUserProfileBinding;
import com.example.findyourband.services.INSTRUMENT;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

public class UserProfileFragment extends Fragment {
    private ProgressBar progressBar;
    FragmentUserProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        progressBar = binding.profileLoadingSpinner;

        String UserID;
        Bundle userData = getArguments();
        if (userData == null)
            UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        else
            UserID = userData.getString("id");
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(UserID);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // TODO: Установка аватарки
                String login = dataSnapshot.child("login").getValue().toString();

                binding.userImgAndName.nicknameTextView.setText(login);

                // Retrieve instruments
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> instruments = dataSnapshot.child("instruments").getValue(t);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                layoutParams.setMarginStart(10);

                if (instruments != null) {
                    for (String instrument : instruments) {
                        ImageView instrumentImageView = new ImageView(getContext());
                        instrumentImageView.setLayoutParams(layoutParams);
                        instrumentImageView.setImageResource(INSTRUMENT.valueOf(instrument).getImageId());
                        binding.instrumentsVacancyPageLayout.addView(instrumentImageView);
                    }
                }

                binding.experienceValueTextView.setText(dataSnapshot.child("experience").getValue().toString());

                retrieveBandName(login, new BandNameCallback() {
                    @Override
                    public void onBandNameRetrieved(String bandName) {
                        if (bandName.equals("")) {
                            binding.groupTextView.setText("Не состоит в группе");
                        } else {
                            binding.groupTextView.setText("Участник группы " + bandName);
                        }

                    }
                });

                binding.experienceMusicianVacancyLayout.setVisibility(View.VISIBLE);
                binding.groupTextView.setVisibility(View.VISIBLE);
                binding.instrumentsTextView.setVisibility(View.VISIBLE);
                binding.instrumentsVacancyPageLayout.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
            }

        });

        return binding.getRoot();
    }

    private void retrieveBandName(String login, BandNameCallback callback) {
        DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");

        bandsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String bandName = "";
                for (DataSnapshot bandSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> members = (ArrayList<String>) bandSnapshot.child("memberUserLogins").getValue();
                    if (members != null && members.contains(login)) {
                        bandName = bandSnapshot.child("name").getValue(String.class);
                        break;
                    }
                }
                callback.onBandNameRetrieved(bandName);
            }
        });
    }

    private interface BandNameCallback {
        void onBandNameRetrieved(String bandName);
    }
}
