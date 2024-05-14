package com.example.findyourband.fragments.vacancies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAddNewVacancyBinding;
import com.google.android.material.chip.Chip;


enum GENRE {
    Rock(0),
    Electronic(1),
    Jazz(2),
    Metal(3),
    Classical(4),
    Blues(5),
    Punk(6),
    Country(7),
    Indie(8),
    Другое(9),
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
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddNewVacancyBinding.inflate(inflater, container, false);


        binding.chipFindMusicBands.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new AddMusicianVacancyFormFragment();
                fragmentTransaction.replace(R.id.addVacancy_fragment_container, fragment).commit();
            }
        });

        binding.chipFindMusician.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (userIsBandLeader()) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragment = new AddBandVacancyFormFragment();
                    fragmentTransaction.replace(R.id.addVacancy_fragment_container, fragment).commit();
                } else {

                    v.setClickable(false);
                    Toast.makeText(getContext(), "Только лидер группы может разместить такое объявление. Создайте группу!", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                VacancyPageFragment fragment = new VacancyPageFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "VacancyPageFragment").addToBackStack(null).commit();
            }
        });
        return binding.getRoot();
    }

    private boolean userIsBandLeader() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("bandLeader", false);
    }


}