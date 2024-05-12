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
import android.widget.ArrayAdapter;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentFilterBinding;

import java.util.HashMap;

public class FilterFragment extends Fragment {
    FragmentFilterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        HashMap<String, ArrayAdapter<String>> adaptersMap = getAdaptersForFilterInputs();


        // Установка адапторов для выпадающих списков и автокомплита
        binding.cityAutoComplete.setAdapter(adaptersMap.get("city"));
        binding.instrumentDropDown.setAdapter(adaptersMap.get("instruments"));
        binding.genreDropDown.setAdapter(adaptersMap.get("genres"));
        binding.experienceDropDown.setAdapter(adaptersMap.get("experience"));


        // Закрытие фильтра
        binding.closeFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                VacancyPageFragment fragment = new VacancyPageFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "VacancyPageFragment").addToBackStack(null).commit();


            }
        });

        // Сброс фильтра

        binding.resetFilterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.filterChipGroup.clearCheck();
                binding.cityAutoComplete.setText("");
                binding.instrumentDropDown.setText("");
                binding.genreDropDown.setText("");
                binding.experienceDropDown.setText("");
            }
        });

        // Применние фильтра

        binding.filterApplyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO: применение фильтра
            }
        });



        return view;

    }

    private HashMap<String, ArrayAdapter<String>> getAdaptersForFilterInputs() {
        HashMap<String, ArrayAdapter<String>> adaptersMap = new HashMap<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.cities));
        ArrayAdapter<String> adapter_instruments = new ArrayAdapter<>(getContext(), R.layout.component_dropdown_item, getResources().getStringArray(R.array.instruments));
        ArrayAdapter<String> adapter_genres = new ArrayAdapter<>(getContext(), R.layout.component_dropdown_item, getResources().getStringArray(R.array.genres));
        ArrayAdapter<String> adapter_experience = new ArrayAdapter<>(getContext(), R.layout.component_dropdown_item, getResources().getStringArray(R.array.experience));

        adaptersMap.put("city", adapter);
        adaptersMap.put("instruments", adapter_instruments);
        adaptersMap.put("genres", adapter_genres);
        adaptersMap.put("experience", adapter_experience);


        return adaptersMap;

    }


}