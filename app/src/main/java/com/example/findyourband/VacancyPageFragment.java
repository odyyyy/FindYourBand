package com.example.findyourband;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.databinding.FragmentVacancyPageBinding;


public class VacancyPageFragment extends Fragment {
    FragmentVacancyPageBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentVacancyPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.filterChip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FilterFragment fragment = new FilterFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}