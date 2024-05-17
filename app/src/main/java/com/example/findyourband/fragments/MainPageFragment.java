package com.example.findyourband.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.adapters.NewsAdapter;
import com.example.findyourband.databinding.FragmentMainPageBinding;
import com.example.findyourband.services.RSSItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;


public class MainPageFragment extends Fragment {
    private FragmentMainPageBinding binding;
    private RecyclerView newsRecyclerView;
    List<RSSItem> newsList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMainPageBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        setLoggedInUserNickname();

        newsRecyclerView = binding.newsRecyclerView;
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadNewsFromCache();
        newsRecyclerView.setAdapter(new NewsAdapter(getContext(), newsList));



        return view;
    }

    private void setLoggedInUserNickname() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        binding.profileLayout.welcomeTextView.setText("Добро пожаловать,\n" + login + "!");
    }

    private void loadNewsFromCache() {
        Gson gson = new Gson();
        File cacheFile = new File(getContext().getFilesDir(), "cached_news.json");

        if (!cacheFile.exists()) {
            return;
        }

        try {
            FileReader reader = new FileReader(cacheFile);
            Type newsListType = new TypeToken<ArrayList<RSSItem>>(){}.getType();
            ArrayList<RSSItem> cachedNewsList = gson.fromJson(reader, newsListType);
            reader.close();

            if (cachedNewsList != null) {
                newsList.addAll(cachedNewsList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

