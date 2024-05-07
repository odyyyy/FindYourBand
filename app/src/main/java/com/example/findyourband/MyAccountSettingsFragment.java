package com.example.findyourband;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.databinding.FragmentMyAccountSettingsBinding;


public class MyAccountSettingsFragment extends Fragment {

    FragmentMyAccountSettingsBinding binding;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyAccountSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProfileFragment fragment = new MyProfileFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "MyProfileFragmentTag").addToBackStack(null).commit();
            }
        });

        binding.myVacanciesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MyVacanciesFragment fragment = new MyVacanciesFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "MyAdsFragmentTag").addToBackStack(null).commit();

            }
        });

        binding.myRequestsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MyRequestsFragment fragment = new MyRequestsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "MyRequestsFragmentTag").addToBackStack(null).commit();
            }
        });

        binding.favoritesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FavoritesFragment fragment = new FavoritesFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "FavoritesFragmentTag").addToBackStack(null).commit();

            }
        });



        binding.settingsButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsFragment fragment = new SettingsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "SettingsFragmentTag").addToBackStack(null).commit();

            }
        });

        return view;
    }


}