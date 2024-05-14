package com.example.findyourband.fragments.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentMyRequestsBinding;


public class MyRequestsFragment extends Fragment {

    FragmentMyRequestsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRequestsBinding.inflate(inflater, container, false);
        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyAccountSettingsFragment fragment = new MyAccountSettingsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "MyAccountSettingsFragment").addToBackStack(null).commit();
            }
        });

        return binding.getRoot();
    }
}