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
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAddBandVacancyFormBinding;

import java.util.List;


public class AddBandVacancyFormFragment extends Fragment {
    FragmentAddBandVacancyFormBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddBandVacancyFormBinding.inflate(inflater, container, false);

        binding.createVacancyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<Integer> instrumentCheckedChip = binding.instrumentChipGroup.getCheckedChipIds();
                if (instrumentCheckedChip.isEmpty() ) {
                    Toast.makeText(getContext(), "Выберите хотя бы один инструмент", Toast.LENGTH_SHORT).show();
                }
                else if (isAllContactFieldsEmpty()) {
                    Toast.makeText(getContext(), "Укажите хотя бы один контакт", Toast.LENGTH_SHORT).show();
                } else if (!isAddedAnyMusic()) {
                    Toast.makeText(getContext(), "Добавьте хотя бы один трек", Toast.LENGTH_SHORT).show();
                } else {

                    // Добавление вакансии в БД
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    VacancyPageFragment fragment = new VacancyPageFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.app_fragment_container, fragment, "VacancyPageFragment").addToBackStack(null).commit();
                    Toast.makeText(getContext(), "Вакансия создана", Toast.LENGTH_SHORT).show();
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