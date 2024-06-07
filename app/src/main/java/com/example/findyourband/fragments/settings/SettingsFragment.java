package com.example.findyourband.fragments.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentSettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setCurrentUserDataInInputFields();

        binding.backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_settingsFragment_to_navigation_my_account_settings);
            }
        });

        return view;
    }

    private void setCurrentUserDataInInputFields() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot ds) {
                if (ds.exists()) {
                    binding.emailEditText.setText(ds.child("email").getValue(String.class));
                    binding.nicknameEditText.setText(ds.child("login").getValue(String.class));
                    binding.experienceDropDown.setText(ds.child("experience").getValue(String.class));

                    setCheckedUserInstrumentsChips((List<String>) ds.child("instruments").getValue());


                    List<String> contacts = (List<String>) ds.child("contacts").getValue();

                    if (!contacts.get(0).isEmpty()) {
                        binding.contact1EditText.setText(contacts.get(0));
                    }
                    if (!contacts.get(1).isEmpty()) {
                        binding.contact2EditText.setText(contacts.get(1));
                    }
                    if (!contacts.get(2).isEmpty()) {
                        binding.contact3EditText.setText(contacts.get(2));
                    }
                }
            }
        });

    }

    private void setCheckedUserInstrumentsChips(List<String> instruments) {
        int chipCount = binding.instrumentRegisterChipGroup.getChildCount();
        Map<String, Integer> chipMap = new HashMap<>();
        for (int i = 0; i < chipCount; i++) {
            Chip chip = (Chip) binding.instrumentRegisterChipGroup.getChildAt(i);
            chipMap.put(chip.getText().toString(), chip.getId());
        }

        for (String instrument : instruments) {
            if (chipMap.containsKey(instrument)) {
                ((Chip) binding.instrumentRegisterChipGroup.findViewById(chipMap.get(instrument))).setChecked(true);
            }
        }
    }


}