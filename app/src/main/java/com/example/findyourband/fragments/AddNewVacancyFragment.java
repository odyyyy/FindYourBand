package com.example.findyourband.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAddNewVacancyBinding;
import com.google.android.material.chip.ChipGroup;

import java.util.List;


enum GENRE {
    Rock(2131362090),
    Electronic(2131361913),
    Jazz(2131362025),
    Metal(2131362333),
    Classical(2131362451),
    Blues(2131362472),
    Punk(2131362473),
    Country(2131361984),
    Indie(2131362216),
    Другое(2131362284),
    ;

    GENRE(int i) {
    }
}

enum INSTRUMENT {
    Электрогитара(2131362326),
    Бас(2131362033),
    Ударные(2131362132),
    Саксофон(2131362181),
    Труба(2131361958),
    Виолончель(2131361918),
    Скрипка(2131362309),
    Контрабас(2131361988),
    Мультиинструменталист(2131362120),
    Другое(2131362283);

    INSTRUMENT(int i) {
    }
}


public class AddNewVacancyFragment extends Fragment {
    FragmentAddNewVacancyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddNewVacancyBinding.inflate(inflater, container, false);
        binding.addVacancyChipGroup.setOnCheckedStateChangeListener(new ChipGroupListener());

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                VacancyPageFragment fragment = new VacancyPageFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "VacancyPageFragment").addToBackStack(null).commit();
            }
        });
        return binding.getRoot();
    }

    private class ChipGroupListener implements ChipGroup.OnCheckedStateChangeListener {
        Fragment fragment;

        @Override
        public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (checkedIds.get(0) == 2131361951) {
                fragment = new AddMusicianVacancyFormFragment();

            } else {
                fragment = new AddBandVacancyFormFragment();
            }
            fragmentTransaction.replace(R.id.addVacancy_fragment_container, fragment).commit();


        }
    }
}