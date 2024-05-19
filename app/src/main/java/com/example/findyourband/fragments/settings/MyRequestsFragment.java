package com.example.findyourband.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentMyRequestsBinding;


public class MyRequestsFragment extends Fragment {

    FragmentMyRequestsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRequestsBinding.inflate(inflater, container, false);

        setUserNicknameInUpperBar();
        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppActivity.navController.navigate(R.id.action_myRequestsFragment_to_navigation_my_account_settings);

            }
        });

        return binding.getRoot();
    }

    private void setUserNicknameInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        String nickname = preferences.getString("login", "");
        binding.nicknameText.setText(nickname);
    }
}