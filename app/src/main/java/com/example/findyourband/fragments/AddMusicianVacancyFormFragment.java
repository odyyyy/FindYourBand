package com.example.findyourband.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAddMusicianVacancyFormBinding;
import com.example.findyourband.fragments.VacancyPageFragment;

import java.util.List;


public class AddMusicianVacancyFormFragment extends Fragment {

    FragmentAddMusicianVacancyFormBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddMusicianVacancyFormBinding.inflate(inflater, container, false);

        binding.cityAutoComplete.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.cities)));
        binding.experienceDropDown.setAdapter(new ArrayAdapter<>(getContext(), R.layout.component_dropdown_item, getResources().getStringArray(R.array.experience)));

        binding.createVacancyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<Integer> instrumentCheckedChip = binding.instrumentChipGroup.getCheckedChipIds();
                List<Integer> genreCheckedChip = binding.genreChipGroup.getCheckedChipIds();
                Log.d("TAG", instrumentCheckedChip.toString());
                Log.d("TAG", genreCheckedChip.toString());


                if (binding.cityAutoComplete.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Укажите город", Toast.LENGTH_SHORT).show();
                } else if (binding.experienceDropDown.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Укажите опыт", Toast.LENGTH_SHORT).show();
                } else if (genreCheckedChip.isEmpty() || genreCheckedChip.size() > 3) {
                    Toast.makeText(getContext(), "Выберите от 1 до 3 жанров", Toast.LENGTH_SHORT).show();
                } else if (instrumentCheckedChip.isEmpty() || instrumentCheckedChip.size() > 3) {
                    Toast.makeText(getContext(), "Выберите от 1 до 3 инструментов", Toast.LENGTH_SHORT).show();
                } else if (isAllContactFieldsEmpty()) {
                    Toast.makeText(getContext(), "Укажите хотя бы один контакт", Toast.LENGTH_SHORT).show();
                } else if (!isAddedAnyMusic()) {
                    Toast.makeText(getContext(), "Добавьте хотя бы один трек", Toast.LENGTH_SHORT).show();
                } else {

                    // Добавление вакансии в БД

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    VacancyPageFragment fragment = new VacancyPageFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "VacancyPageFragment").addToBackStack(null).commit();
                }

            }
        });

        return binding.getRoot();
    }

    private boolean isAddedAnyMusic() {
        // TODO: Проверка наличия хотя бы одного трека
        return true;
    }

    private boolean isAllContactFieldsEmpty() {
        return binding.contact1EditText.getText().toString().isEmpty() && binding.contact2EditText.getText().toString().isEmpty() && binding.contact3EditText.getText().toString().isEmpty();
    }
}