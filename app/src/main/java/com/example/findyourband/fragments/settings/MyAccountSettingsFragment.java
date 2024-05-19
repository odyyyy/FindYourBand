package com.example.findyourband.fragments.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.MainActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentMyAccountSettingsBinding;
import com.example.findyourband.services.AlreadyBandMemberChecker;
import com.google.firebase.auth.FirebaseAuth;

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
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_manageBandFragment);
            }
        });

        binding.createBandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String login = preferences.getString("login", "Пользователь");

                AlreadyBandMemberChecker.isUserAlreadyInBand(login, new AlreadyBandMemberChecker.BandCheckCallback() {

                    @Override
                    public void onCheckCompleted(boolean isInBand) {
                        if (isInBand) {
                            Toast.makeText(getContext(), "Вы уже состоите в группе! Сначала покиньте её!", Toast.LENGTH_LONG).show();
                        } else {
                            AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_createBandFragment);
                        }
                    }
                });
            }
        });


//        binding.myPageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        binding.myVacanciesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_myVacanciesFragment);

            }
        });

        binding.myRequestsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_myRequestsFragment);
            }
        });

//        binding.favoritesButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
        binding.settingsButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_settingsFragment);

            }
        });

        binding.aboutAppButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_aboutAppFragment);
            }
        });

        binding.aboutAuthorButtonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_my_account_settings_to_aboutAuthorFragment);
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
        } else {
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