package com.example.findyourband.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.R;
import com.example.findyourband.adapters.VacanciesAdapter;
import com.example.findyourband.databinding.FragmentVacancyPageBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VacancyPageFragment extends Fragment {
    FragmentVacancyPageBinding binding;

    RecyclerView vacanciesRecyclerView;
    VacanciesAdapter adapter;

    ArrayList<Map<String, ArrayList<String>>> vacanciesList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentVacancyPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        vacanciesList = getVacanciesData();

        vacanciesRecyclerView = binding.vacancyRecyclerView;
        vacanciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VacanciesAdapter(getContext(), vacanciesList);
        vacanciesRecyclerView.setAdapter(adapter);

        binding.filterChip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FilterFragment fragment = new FilterFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "filterFragment").addToBackStack("filterFragment").commit();
            }
        });

        binding.addVacancyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AddNewVacancyFragment fragment = new AddNewVacancyFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "addNewVacancyFragment").addToBackStack("addNewVacancyFragment").commit();
            }
        });

        return view;
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

    private Map<String, ArrayList<String>> createVacancy(String nickname, String city, List<String> instruments, List<String> genres) {
        Map<String, ArrayList<String>> vacancy = new HashMap<>();
        vacancy.put("name", new ArrayList<>(Arrays.asList(nickname)));
        vacancy.put("city", new ArrayList<>(Arrays.asList(city)));
        vacancy.put("instruments", new ArrayList<>(instruments));
        vacancy.put("genres", new ArrayList<>(genres));
        return vacancy;
    }


}