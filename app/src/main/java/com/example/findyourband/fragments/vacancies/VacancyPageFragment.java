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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

        loadVacanciesDataFromDatabase();

        vacanciesRecyclerView = binding.vacancyRecyclerView;
        vacanciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VacanciesAdapter(getContext(), vacanciesList);
        vacanciesRecyclerView.setAdapter(adapter);


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

    private ArrayList<Map<String, ArrayList<String>>> getVacanciesData() {
        ArrayList<Map<String, ArrayList<String>>> vacanciesData = new ArrayList<>();

        vacanciesData.add(createVacancy("User", "Москва", Arrays.asList("Гитара", "Барабаны"), Arrays.asList("Рок", "Поп")));
        vacanciesData.add(createVacancy("User_With_Long_Name_2", "Санкт-Петербург", Arrays.asList("Клавиши", "Бас-гитара"), Arrays.asList("Джаз", "Фанк")));
        vacanciesData.add(createVacancy("Группа BandName", "Новосибирск", Arrays.asList("Скрипка", "Флейта"), Arrays.asList("Классика", "Вокал")));
        vacanciesData.add(createVacancy("MusicUser4", "Екатеринбург", Arrays.asList("Саксофон", "Труба"), Arrays.asList("Блюз", "Электроника")));
        vacanciesData.add(createVacancy("User5", "Казань", Arrays.asList("Ударные", "Синтезатор"), Arrays.asList("Хип-хоп", "Рэгги")));

        return vacanciesData;
    }

    private void loadVacanciesDataFromDatabase() {
        DatabaseReference vacanciesRef = FirebaseDatabase.getInstance().getReference("vacancies");

        vacanciesRef.child("from_musicians").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<Map<String, ArrayList<String>>> vacanciesData = new ArrayList<>();
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

                                Map<String, ArrayList<String>> vacancy = new HashMap<>();
                                vacancy.put("name", new ArrayList<>(Arrays.asList(login)));
                                vacancy.put("city", new ArrayList<>(Arrays.asList(city)));
                                vacancy.put("instruments", new ArrayList<>(instruments));
                                vacancy.put("genres", new ArrayList<>(genres));
                                vacanciesData.add(vacancy);

                                if (login != null) {
                                    vacanciesList.addAll(vacanciesData);
                                    Log.d("vacanciesList", vacanciesList.toString());
                                    adapter.notifyDataSetChanged();
                                }
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
                Log.e("getUserLoginByID", "Error getting user data: " + e.getMessage());
                listener.onLoginLoaded(null);
            }
        });
    }


}