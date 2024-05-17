package com.example.findyourband.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.MainActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentRegisterChooseInstrumentsBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RegisterChooseInstrumentsFragment extends Fragment {

    FragmentRegisterChooseInstrumentsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterChooseInstrumentsBinding.inflate(inflater, container, false);


        binding.registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<Integer> instrumentCheckedChipIds = binding.instrumentRegisterChipGroup.getCheckedChipIds();

                if (instrumentCheckedChipIds.isEmpty() || instrumentCheckedChipIds.size() > 3) {

                    Toast.makeText(getContext(), "Выберите от 1 до 3 инструментов", Toast.LENGTH_SHORT).show();
                }
                else{

                    Bundle registrationData = getArguments();

                    if (registrationData != null){
                        String email = registrationData.getString("email");
                        String login = registrationData.getString("login");
                        String password =registrationData.getString("password");
                        ArrayList<String> instruments = getInstrumentNamesArray(instrumentCheckedChipIds);

                        createUser(email, login, password, instruments);

                    }
                    else{
                        Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }

                }
            }
        });

        return binding.getRoot();
    }

    private void createUser(String email, String login, String password, ArrayList<String> instruments) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        userRef.child("email").setValue(email);
                        userRef.child("login").setValue(login);
                        userRef.child("image").setValue("");
                        userRef.child("instruments").setValue(instruments);

                        Intent intent = new Intent(getActivity(), AppActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                });
    }

    private ArrayList<String> getInstrumentNamesArray(List<Integer> instrumentIds) {

        ArrayList<String> instrumentNames = new ArrayList<>();

        for (int instrumentId : instrumentIds) {
            instrumentNames.add(((Chip) binding.instrumentRegisterChipGroup.findViewById(instrumentId)).getText().toString());
        }

        return instrumentNames;
    }

}