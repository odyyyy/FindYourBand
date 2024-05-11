package com.example.findyourband.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAddBandVacancyFormBinding;


public class AddBandVacancyFormFragment extends Fragment {
    FragmentAddBandVacancyFormBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddBandVacancyFormBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }
}