package com.example.findyourband.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.adapters.VacanciesAdapter;
import com.example.findyourband.databinding.FragmentFavoritesBinding;

import java.util.ArrayList;
import java.util.Map;


public class FavoritesFragment extends Fragment {

    FragmentFavoritesBinding binding;
    ArrayList<Map<String, ArrayList<String>>> favoritesVacanciesList;

    RecyclerView favoritesRecyclerView;
    VacanciesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }
}