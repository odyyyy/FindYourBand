package com.example.findyourband;

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

import com.example.findyourband.databinding.FragmentRegisterFirstStepBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RegisterFirstStep extends Fragment {

    FragmentRegisterFirstStepBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterFirstStepBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isAnyEmptyFields()) {
                    Toast.makeText(getContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show();
                } else if (!binding.registerPassword1.getText().toString().equals(binding.registerPassword2.getText().toString())) {
                    Toast.makeText(getContext(), "Введенные пароли должны совпадать", Toast.LENGTH_SHORT).show();
                } else if (binding.registerPassword1.getText().toString().length() < 8) {
                    Toast.makeText(getContext(), "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show();
                } else if (binding.registerLogin.getText().toString().length() < 3) {
                    Toast.makeText(getContext(), "Логин должен содержать не менее 3 символов", Toast.LENGTH_SHORT).show();
                } else if (!binding.registerEmail.getText().toString().contains("@") ||
                        !binding.registerEmail.getText().toString().contains(".") ||
                        binding.registerEmail.getText().toString().contains(" ")) {
                    Toast.makeText(getContext(), "Неверный формат почты", Toast.LENGTH_SHORT).show();
                } else if (binding.registerLogin.getText().toString().contains(" ")) {
                    Toast.makeText(getContext(), "Логин не должен содержать пробелов", Toast.LENGTH_SHORT).show();
                } else if (binding.registerPassword1.getText().toString().contains(" ") || binding.registerPassword2.getText().toString().contains(" ")) {
                    Toast.makeText(getContext(), "Почта не должна содержать пробелов", Toast.LENGTH_SHORT).show();
                }

                else {




                    Bundle firstStepRegistrationData = new Bundle();

                    firstStepRegistrationData.putString("email", binding.registerEmail.getText().toString());
                    firstStepRegistrationData.putString("password1", binding.registerPassword1.getText().toString());
                    firstStepRegistrationData.putString("password2", binding.registerPassword2.getText().toString());
                    firstStepRegistrationData.putString("login", binding.registerLogin.getText().toString());

                    RegisterSecondStep fragment = new RegisterSecondStep();
                    fragment.setArguments(firstStepRegistrationData);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.auth_fragment_container, fragment).addToBackStack(null).commit();

                }


            }
        });

        return view;
    }



    private boolean isAnyEmptyFields() {

        return binding.registerEmail.getText().toString().isEmpty() || binding.registerPassword1.getText().toString().isEmpty()
                || binding.registerPassword2.getText().toString().isEmpty() || binding.registerLogin.getText().toString().isEmpty();

    }

}