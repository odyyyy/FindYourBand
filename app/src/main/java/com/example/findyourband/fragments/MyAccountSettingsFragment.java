package com.example.findyourband.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.MainActivity;
import com.example.findyourband.ManageBandFragment;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentMyAccountSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyAccountSettingsFragment extends Fragment {

    FragmentMyAccountSettingsBinding binding;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyAccountSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setUserLoginInUpperBar();

        setButtonsIfUserIsBandLeader();

        binding.manageBandButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ManageBandFragment fragment = new ManageBandFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "ManageBandFragmentTag").addToBackStack(null).commit();

            }
        });

        binding.createBandButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CreateBandFragment fragment = new CreateBandFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "CreateBandFragmentTag").addToBackStack(null).commit();
            }
        });


        binding.myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProfileFragment fragment = new MyProfileFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "MyProfileFragmentTag").addToBackStack(null).commit();
            }
        });

        binding.myVacanciesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MyVacanciesFragment fragment = new MyVacanciesFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "MyAdsFragmentTag").addToBackStack(null).commit();

            }
        });

        binding.myRequestsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MyRequestsFragment fragment = new MyRequestsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "MyRequestsFragmentTag").addToBackStack(null).commit();
            }
        });

        binding.favoritesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FavoritesFragment fragment = new FavoritesFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "FavoritesFragmentTag").addToBackStack(null).commit();

            }
        });



        binding.settingsButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsFragment fragment = new SettingsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "SettingsFragmentTag").addToBackStack(null).commit();

            }
        });

        binding.signOutButtonAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setButtonsIfUserIsBandLeader() {

        if (isUserBandLeader()) {
            binding.manageBandButton.setVisibility(View.VISIBLE);
            binding.createBandButton.setVisibility(View.GONE);
        }
        else {
            binding.manageBandButton.setVisibility(View.GONE);
        }
    }

    private boolean isUserBandLeader() {

        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return preferences.getBoolean("bandLeader", false);

    }

    private void setUserLoginInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        String login = preferences.getString("login", "Пользователь");
        binding.userImgAndName.nicknameTextView.setText(login);
    }

}