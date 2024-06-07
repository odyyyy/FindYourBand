package com.example.findyourband.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.MainActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentRegisterInputContactsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RegisterInputContactsFragment extends Fragment {
    FragmentRegisterInputContactsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterInputContactsBinding.inflate(inflater, container, false);

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllContactFieldsEmpty()) {
                    Toast.makeText(getContext(), "Укажите хотя бы один контакт", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle registrationData = getArguments();
                    if (registrationData != null) {
                        String email = registrationData.getString("email");
                        String login = registrationData.getString("login");
                        String password = registrationData.getString("password");
                        ArrayList<String> instruments = registrationData.getStringArrayList("instruments");
                        String experience = registrationData.getString("experience");
                        ArrayList<String> contacts = new ArrayList<>();

                        contacts.add(binding.contact1EditText.getText().toString());
                        contacts.add(binding.contact2EditText.getText().toString());
                        contacts.add(binding.contact3EditText.getText().toString());

                        createUser(email, login, password, instruments, experience, contacts);
                    }
                    else {
                        Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return binding.getRoot();

    }


    private void createUser(String email, String login, String password, ArrayList<String> instruments, String experience, ArrayList<String> contacts) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        userRef.child("email").setValue(email);
                        userRef.child("login").setValue(login);
                        userRef.child("image").setValue("");
                        userRef.child("instruments").setValue(instruments);
                        userRef.child("experience").setValue(experience);
                        userRef.child("contacts").setValue(contacts);

                        Intent intent = new Intent(getActivity(), AppActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                });
    }

    private boolean isAllContactFieldsEmpty() {
        return binding.contact1EditText.getText().toString().isEmpty() && binding.contact2EditText.getText().toString().isEmpty() && binding.contact3EditText.getText().toString().isEmpty();
    }
}