package com.example.findyourband.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentCreateBandBinding;


public class CreateBandFragment extends Fragment {
    FragmentCreateBandBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateBandBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}