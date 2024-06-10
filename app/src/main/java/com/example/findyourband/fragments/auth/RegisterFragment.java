package com.example.findyourband.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentRegisterBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isAnyEmptyFields()) {
                    Toast.makeText(getContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show();
                } else if (!binding.registerPassword1.getText().toString().equals(binding.registerPassword2.getText().toString())) {
                    Toast.makeText(getContext(), "Введенные пароли должны совпадать", Toast.LENGTH_SHORT).show();
                }
//                else if (binding.registerPassword1.getText().toString().length() < 8) {
//                    Toast.makeText(getContext(), "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show();
//                }
                else if (binding.registerLogin.getText().toString().length() < 3) {
                    Toast.makeText(getContext(), "Логин должен содержать не менее 3 символов", Toast.LENGTH_SHORT).show();
                } else if (isNotValidEmail(binding.registerEmail.getText().toString())) {
                    Toast.makeText(getContext(), "Неверный формат почты", Toast.LENGTH_SHORT).show();
                } else if (binding.registerLogin.getText().toString().contains(" ")) {
                    Toast.makeText(getContext(), "Логин не должен содержать пробелов", Toast.LENGTH_SHORT).show();
                } else if (binding.registerPassword1.getText().toString().contains(" ") || binding.registerPassword2.getText().toString().contains(" ")) {
                    Toast.makeText(getContext(), "Почта не должна содержать пробелов", Toast.LENGTH_SHORT).show();
                } else {

                    checkIsUserLoginAlreadyExists();
                }
            }
        });

        return view;
    }


    private boolean isNotValidEmail(CharSequence target) {
        return TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void checkIsUserLoginAlreadyExists() {
        /* Проверка что не существует пользователя с таким никнеймом */
        String login = binding.registerLogin.getText().toString();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.orderByChild("login").equalTo(login).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getContext(), "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show();
                } else {
                    processNextRegistrationStep(login);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Ошибка при проверке логина", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void processNextRegistrationStep(String login) {
        String email = binding.registerEmail.getText().toString();
        String password = binding.registerPassword1.getText().toString();

        Bundle registrationData = new Bundle();
        registrationData.putString("email", email);
        registrationData.putString("password", password);
        registrationData.putString("login", login);

        RegisterChooseInstrumentsFragment registerChooseInstrumentsFragment = new RegisterChooseInstrumentsFragment();
        registerChooseInstrumentsFragment.setArguments(registrationData);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.auth_fragment_container, registerChooseInstrumentsFragment);
        fragmentTransaction.addToBackStack(null).commit();

    }



    private boolean isAnyEmptyFields() {
        return binding.registerEmail.getText().toString().isEmpty() || binding.registerPassword1.getText().toString().isEmpty()
                || binding.registerPassword2.getText().toString().isEmpty() || binding.registerLogin.getText().toString().isEmpty();
    }



}