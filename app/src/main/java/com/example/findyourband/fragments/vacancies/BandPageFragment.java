package com.example.findyourband.fragments.vacancies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentBandPageBinding;


public class BandPageFragment extends Fragment {

    FragmentBandPageBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBandPageBinding.inflate(inflater, container, false);

        binding.arrowBackComponent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VacancyPageFragment fragment = new VacancyPageFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "VacancyPageFragment").addToBackStack(null).commit();
            }
        });

        return binding.getRoot();

    }
}