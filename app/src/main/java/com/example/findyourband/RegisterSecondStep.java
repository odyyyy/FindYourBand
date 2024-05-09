package com.example.findyourband;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.databinding.FragmentRegisterSecondStepBinding;


public class RegisterSecondStep extends Fragment {

    FragmentRegisterSecondStepBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterSecondStepBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Intent intent = new Intent(getActivity(), AppActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
}