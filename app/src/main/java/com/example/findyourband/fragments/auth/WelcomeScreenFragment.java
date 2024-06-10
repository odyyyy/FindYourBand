package com.example.findyourband.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentWelcomeScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeScreenFragment extends Fragment {

    FragmentWelcomeScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment fragment = new RegisterFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.to_right, R.anim.from_right, R.anim.to_left);
                fragmentTransaction.replace(R.id.auth_fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.to_right, R.anim.from_right, R.anim.to_left);
                fragmentTransaction.replace(R.id.auth_fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), AppActivity.class);
            startActivity(intent);
        }
    }
}
