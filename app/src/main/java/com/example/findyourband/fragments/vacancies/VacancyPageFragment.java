package com.example.findyourband.fragments.vacancies;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.adapters.VacanciesAdapter;
import com.example.findyourband.databinding.FragmentVacancyPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

interface OnLoginLoadedListener {
    void onLoginLoaded(String login);
}


public class VacancyPageFragment extends Fragment  {
    FragmentVacancyPageBinding binding;

    RecyclerView vacanciesRecyclerView;
    VacanciesAdapter adapter;


    ArrayList<Map<String, ArrayList<String>>> vacanciesList = new ArrayList<Map<String, ArrayList<String>>>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentVacancyPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setUserLoginInUpperBar();


        vacanciesRecyclerView = binding.vacancyRecyclerView;
        vacanciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VacanciesAdapter(getContext(), vacanciesList);
        vacanciesRecyclerView.setAdapter(adapter);
        loadVacanciesDataFromDatabase();



        binding.filterChip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FilterFragment fragment = new FilterFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "filterFragment").addToBackStack("filterFragment").commit();
            }
        });

        binding.addVacancyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AddNewVacancyFragment fragment = new AddNewVacancyFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "addNewVacancyFragment").addToBackStack("addNewVacancyFragment").commit();
            }
        });

        return view;
    }



    private void setUserLoginInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        String login = preferences.getString("login", "пользователь!");
        binding.profileLayout.welcomeTextView.setText("Добро пожаловать,\n" + login);
    }


    private void loadVacanciesDataFromDatabase() {
        DatabaseReference vacanciesRef = FirebaseDatabase.getInstance().getReference("vacancies");

        // Запрос на получение объявлений от музыкантов
        vacanciesRef.child("from_musicians").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    vacanciesList.clear();
                    for (DataSnapshot vacancySnapshot : dataSnapshot.getChildren()) {
                        getUserLoginByID(vacancySnapshot.getKey(), new OnLoginLoadedListener() {
                            @Override
                            public void onLoginLoaded(String login) {
                                String city = vacancySnapshot.child("city").getValue(String.class);
                                List<String> instruments = new ArrayList<>();
                                List<String> genres = new ArrayList<>();
                                for (DataSnapshot instrumentSnapshot : vacancySnapshot.child("instruments").getChildren()) {
                                    instruments.add(instrumentSnapshot.getValue(String.class));
                                }
                                for (DataSnapshot genreSnapshot : vacancySnapshot.child("genres").getChildren()) {
                                    genres.add(genreSnapshot.getValue(String.class));
                                }

                                Map<String, ArrayList<String>> vacancy = createVacancy(login, city, instruments, genres);
                                if (!vacanciesList.contains(vacancy)) {
                                    vacanciesList.add(vacancy);
                                }
                                binding.amongOfVacanciesTextView.setText("Найдено " + vacanciesList.size() + " объявлений");

                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Ошибка загрузки объявлений", Toast.LENGTH_SHORT).show();
            }
        });

        // Запрос на получение объявлений от групп
        vacanciesRef.child("from_bands").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    vacanciesList.clear();
                    for (DataSnapshot bandSnapshot : dataSnapshot.getChildren()) {
                        String bandId = bandSnapshot.getKey();
                        DatabaseReference bandRef = FirebaseDatabase.getInstance().getReference("bands").child(bandId);
                        ArrayList<String> instruments = new ArrayList<>(Collections.singletonList(bandSnapshot.child("instrument").getValue(String.class)));
                        bandRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot bandDataSnapshot) {
                                if (bandDataSnapshot.exists()) {
                                    String city = bandDataSnapshot.child("city").getValue(String.class);
                                    String name = "Группа " + bandDataSnapshot.child("name").getValue(String.class);

                                    ArrayList<String> genres = new ArrayList<>();
                                    for (DataSnapshot genreSnapshot : bandDataSnapshot.child("genres").getChildren()) {
                                        genres.add(genreSnapshot.getValue(String.class));
                                    }

                                    Map<String, ArrayList<String>> band = createVacancy(name, city, instruments, genres);
                                    if (!vacanciesList.contains(band)) {
                                        vacanciesList.add(band);
                                    }
                                    binding.amongOfVacanciesTextView.setText("Найдено " + vacanciesList.size() + " объявлений");

                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Ошибка загрузки данных о группе", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Ошибка загрузки объявлений", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private Map<String, ArrayList<String>> createVacancy(String nickname, String city, List<String> instruments, List<String> genres) {
        Map<String, ArrayList<String>> vacancy = new HashMap<>();
        vacancy.put("name", new ArrayList<>(Arrays.asList(nickname)));
        vacancy.put("city", new ArrayList<>(Arrays.asList(city)));
        vacancy.put("instruments", new ArrayList<>(instruments));
        vacancy.put("genres", new ArrayList<>(genres));
        return vacancy;
    }

    private void getUserLoginByID(String userID, OnLoginLoadedListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("login");

        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String login = dataSnapshot.getValue(String.class);
                    listener.onLoginLoaded(login);
                } else {
                    listener.onLoginLoaded(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onLoginLoaded(null);
            }
        });
    }


}