package com.example.findyourband.fragments.vacancies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentAddMusicianVacancyFormBinding;
import com.example.findyourband.services.MusicianVacancyDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddMusicianVacancyFormFragment extends Fragment {

    FragmentAddMusicianVacancyFormBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddMusicianVacancyFormBinding.inflate(inflater, container, false);

        binding.cityAutoComplete.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.cities)));

        binding.createVacancyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<Integer> genreCheckedChipIds = binding.genreChipGroup.getCheckedChipIds();


                if (binding.cityAutoComplete.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Укажите город", Toast.LENGTH_SHORT).show();
                } else if (genreCheckedChipIds.isEmpty() || genreCheckedChipIds.size() > 3) {
                    Toast.makeText(getContext(), "Выберите от 1 до 3 жанров", Toast.LENGTH_SHORT).show();
                } else if (!isAddedAnyMusic()) { // Возможно сделать поле опциональным
                    Toast.makeText(getContext(), "Добавьте хотя бы один трек", Toast.LENGTH_SHORT).show();
                } else {
                    // Добавление вакансии в БД

                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userID);

                    userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            String experience = dataSnapshot.child("experience").getValue(String.class);
                            List<String> instruments = new ArrayList<>();
                            if (dataSnapshot.hasChild("instruments")) {
                                for (DataSnapshot instrumentSnapshot : dataSnapshot.child("instruments").getChildren()) {
                                    instruments.add(instrumentSnapshot.getValue(String.class));
                                }
                            }
                            List<String> contacts = new ArrayList<>();
                            if (dataSnapshot.hasChild("contacts")) {
                                for (DataSnapshot contactSnapshot : dataSnapshot.child("contacts").getChildren()) {
                                    contacts.add(contactSnapshot.getValue(String.class));
                                }
                            }

                            createVacancy(userID, experience, instruments, contacts);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Ошибка при получении данных пользователя: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        return binding.getRoot();
    }


    private boolean isAddedAnyMusic() {
        // TODO: Проверка наличия хотя бы одного трека
        return true;
    }

    private void createVacancy(String userID, String experience, List<String> instruments, List<String> contacts) {
        DatabaseReference musVacancyRef = FirebaseDatabase.getInstance().getReference("vacancies").child("from_musicians");
        String ID = musVacancyRef.push().getKey();
        String city = binding.cityAutoComplete.getText().toString();
        List<String> genres = getGenreNamesArray(binding.genreChipGroup.getCheckedChipIds());
        String description = binding.descriptionEditText.getText().toString();

        // TODO: добавление треков в массив
        List<String> tracks = new ArrayList<>(Arrays.asList("Трек 1", "Трек 2", "Трек 3"));

        MusicianVacancyDataClass musVacancy = new MusicianVacancyDataClass(ID, city, experience, genres, instruments, description, tracks, contacts);

        musVacancyRef.child(userID).setValue(musVacancy).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    private List<String> getGenreNamesArray(List<Integer> genreCheckedChipIds) {

        List<String> genreNames = new ArrayList<>();
        for (int genreId : genreCheckedChipIds) {
            genreNames.add(((Chip) binding.genreChipGroup.findViewById(genreId)).getText().toString());
        }
        return genreNames;
    }





}