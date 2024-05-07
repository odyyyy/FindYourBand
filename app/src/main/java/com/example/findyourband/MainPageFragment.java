package com.example.findyourband;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.findyourband.databinding.FragmentMainPageBinding;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class MainPageFragment extends Fragment {
    FragmentMainPageBinding binding;

    RecyclerView newsRecyclerView;

    NewsAdapter adapter;

    ArrayList<String> newsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMainPageBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        newsList = get_news_data();

        newsRecyclerView = binding.newsRecyclerView;
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter(getContext(), newsList);
        newsRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mainPageScrollView.scrollTo(0, 0);
    }

//    private ArrayList<String> get_news_data() {
//        ArrayList<String> newsData = new ArrayList<>();
//        newsData.add("Новость 1");
//        newsData.add("Новость 2");
//        newsData.add("Новостной заголовок 3");
//        newsData.add("Новостей заголовок более длинный 4");
//        newsData.add("Новостей заголовок более длинный и еще длиннее 5");
//
//        return newsData;
//    }

    private ArrayList<RSSItem> get_news_data() {

        RSSParser parser = new RSSParser();
        ArrayList<RSSItem> items = null;
        try {
            InputStream inputStream = new URL("http://www.darkside.ru/rss/").openStream();
            items = (ArrayList<RSSItem>) parser.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }



}