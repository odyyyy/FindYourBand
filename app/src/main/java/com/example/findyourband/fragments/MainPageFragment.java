package com.example.findyourband.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
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

        binding.addNewAdBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_home_to_addNewVacancyFragment);
            }
        });

        binding.aboutAppBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_home_to_aboutAppFragment);
            }
        });

        binding.newRequestsBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_navigation_home_to_myRequestsFragment);
            }
        });


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

            if (newsList.isEmpty()) {
                String jsonString = "[{\"img\":\"http://samesound.ru/wp-content/uploads/2024/06/serato-dj-3.2.0-beta-150x150.jpg\",\"pubDate\":\"Fri, 07 Jun 2024 13:25:01 +0000\",\"title\":\"Вышла публичная бета Serato DJ 3.2.0 с переработанной секцией эффектов\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/06/spotlajt-bandlink-2024-150x150.jpg\",\"pubDate\":\"Fri, 07 Jun 2024 13:04:27 +0000\",\"title\":\"Проект СпотЛайт от BandLink пополнился новыми кейсами креативного продвижения релизов\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/06/de-school-het-archief-arhiv-didzhej-setov-za-25-let-150x150.jpg\",\"pubDate\":\"Thu, 06 Jun 2024 14:03:40 +0000\",\"title\":\"Нидерландский клуб De School запустил онлайн-архив с лучшими диджей-сетами за восемь лет существования площадки\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/06/deniel-ek-ceo-spotify-150x150.jpg\",\"pubDate\":\"Tue, 04 Jun 2024 12:13:08 +0000\",\"title\":\"Глава Spotify заявил, что \\\"стоимость создания музыки близка к нулю\\\"\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/05/marshall-zajmyotsya-vypuskom-modeliruyushhih-usilitelej-150x150.jpg\",\"pubDate\":\"Fri, 31 May 2024 14:52:31 +0000\",\"title\":\"Marshall займётся выпуском моделирующих усилителей\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/05/behringer-1273-150x150.jpg\",\"pubDate\":\"Fri, 31 May 2024 12:36:56 +0000\",\"title\":\"Behringer 1273 представляет собой двухканальную копию предусилителя Neve 1073 по доступной цене\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/05/audio-technica-at-umx3-150x150.jpg\",\"pubDate\":\"Thu, 30 May 2024 13:43:55 +0000\",\"title\":\"Audio-Technica представила компактный USB-микшер AT-UMX3\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/05/korg-berlin-prototype-phase-8-decent-sampler-150x150.jpg\",\"pubDate\":\"Thu, 30 May 2024 06:38:07 +0000\",\"title\":\"Прототип синтезатора KORG Acoustic Synthesis phase_8 стал доступен в виде бесплатной библиотеки для Decent Sampler\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/05/apple-music-haptics-ios-18-150x150.jpg\",\"pubDate\":\"Wed, 29 May 2024 20:58:31 +0000\",\"title\":\"Новый способ наслаждаться песнями: функция Music Haptics в iOS 18 научит iPhone вибрировать в такт музыке\"},{\"img\":\"https://samesound.ru/wp-content/uploads/2024/05/kit-richards-1986-150x150.jpg\",\"pubDate\":\"Wed, 29 May 2024 11:27:41 +0000\",\"title\":\"\\\"Музыкантам ещё предстоит понять, что по-настоящему полезно, а что всего лишь игрушка\\\": что думал о развитии технологий в музыке Кит Ричардс в 1986 году\"}]";

                Type listType = new TypeToken<ArrayList<RSSItem>>(){}.getType();
                List<RSSItem> defaultNewsList = gson.fromJson(jsonString, listType);
                newsList.addAll(defaultNewsList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

