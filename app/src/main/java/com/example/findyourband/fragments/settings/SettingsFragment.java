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


public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.navController.navigate(R.id.action_settingsFragment_to_navigation_my_account_settings);
            }
        });

        return view;
    }


}