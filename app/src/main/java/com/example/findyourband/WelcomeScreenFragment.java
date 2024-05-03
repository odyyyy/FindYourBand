package com.example.findyourband;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.databinding.FragmentWelcomeScreenBinding;
import com.example.findyourband.ui.login.LoginFragment;


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
                RegisterFirstStep fragment = new RegisterFirstStep();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.auth_fragment_container, fragment).addToBackStack(null).commit();
                ;

            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LoginFragment fragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.auth_fragment_container, fragment).addToBackStack(null).commit();
            }
        });


        return view;
    }


}