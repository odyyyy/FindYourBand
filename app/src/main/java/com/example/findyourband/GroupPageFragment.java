package com.example.findyourband;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.databinding.FragmentGroupPageBinding;


public class GroupPageFragment extends Fragment {

    FragmentGroupPageBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupPageBinding.inflate(inflater, container, false);

        binding.arrowBackComponent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VacancyPageFragment fragment = new VacancyPageFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "VacancyPageFragment").addToBackStack(null).commit();
            }
        });

        return binding.getRoot();

    }
}