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

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAddBandVacancyFormBinding;
import com.example.findyourband.services.BandVacancyDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
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
                if (instrumentCheckedChip.isEmpty()) {
                    Toast.makeText(getContext(), "Выберите хотя бы один инструмент", Toast.LENGTH_SHORT).show();
                } else if (isAllContactFieldsEmpty()) {
                    Toast.makeText(getContext(), "Укажите хотя бы один контакт", Toast.LENGTH_SHORT).show();
                } else if (!isAddedAnyMusic()) {
                    Toast.makeText(getContext(), "Добавьте хотя бы один трек", Toast.LENGTH_SHORT).show();
                } else {

                    DatabaseReference musVacancyRef = FirebaseDatabase.getInstance().getReference("vacancies").child("from_bands");

                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String instrument = getSelectedInstrument();

                    List<String> tracks = new ArrayList<>(Arrays.asList("Трек 1", "Трек 2", "Трек 3"));
                    // TODO: добавление треков в массив

                    String Id = musVacancyRef.push().getKey();


                    List<String> contacts = new ArrayList<>();
                    contacts.add(binding.contact1EditText.getText().toString());
                    contacts.add(binding.contact2EditText.getText().toString());
                    contacts.add(binding.contact3EditText.getText().toString());

                    String description = binding.descriptionEditText.getText().toString();

                    BandVacancyDataClass bandVacancy = new BandVacancyDataClass(Id,instrument, description, tracks, contacts);
                    musVacancyRef.child(userID).setValue(bandVacancy).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Объявление успешно создано!", Toast.LENGTH_SHORT).show();
                            AppActivity.navController.navigate(R.id.action_addNewVacancyFragment_to_navigation_vacancy);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Ошибка при создании объявления: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        return binding.getRoot();
    }

    private String getSelectedInstrument() {
        Integer selectedInstrumentID = binding.instrumentChipGroup.getCheckedChipIds().get(0);

        return ((Chip) binding.instrumentChipGroup.findViewById(selectedInstrumentID)).getText().toString();
    }



    private boolean isAddedAnyMusic() {
        // TODO: Проверка наличия хотя бы одного трека
        return true;
    }

    private boolean isAllContactFieldsEmpty() {
        return binding.contact1EditText.getText().toString().isEmpty() && binding.contact2EditText.getText().toString().isEmpty() && binding.contact3EditText.getText().toString().isEmpty();
    }
}