package com.example.findyourband.fragments.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAboutAuthorBinding;


public class AboutAuthorFragment extends Fragment {

    FragmentAboutAuthorBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAboutAuthorBinding.inflate(inflater, container, false);

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_aboutAuthorFragment_to_navigation_my_account_settings);
            }
        });

        binding.githubLayout.setOnClickListener(v -> {
            String githubUrl = "https://github.com/odyyyy";

            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse(githubUrl));

            startActivity(intent);
        });
        return binding.getRoot();
    }
}