package com.example.findyourband.fragments.vacancies;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentMusicianPageBinding;
import com.example.findyourband.services.INSTRUMENT;

import java.util.List;


public class MusicianPageFragment extends Fragment {

    FragmentMusicianPageBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMusicianPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUserNicknameInUpperBar();


        Bundle musicianData = getArguments();


        if (musicianData != null) {
            // TODO: Установка картинки
            if (!musicianData.getString("image").equals("")) {
                binding.musicianAvatarAndNickname.avatarImageView.setImageBitmap(musicianData.getParcelable("img"));
            }
            else
                binding.musicianAvatarAndNickname.avatarImageView.setImageResource(R.drawable.user_default_avatar);

            binding.musicianAvatarAndNickname.nicknameTextView.setText(musicianData.getString("name"));
            binding.musicianCityTextView.setText(musicianData.getString("city"));

            ContextThemeWrapper newContext = new ContextThemeWrapper(getContext(), R.style.genreTextViewStyle);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMarginEnd(20);
            for (String genre : musicianData.getStringArrayList("genres")) {
                TextView genreTextView = new TextView(newContext);
                genreTextView.setText(genre);
                genreTextView.setLayoutParams(params);
                binding.genreVacancyPageLayout.addView(genreTextView);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
            layoutParams.setMarginStart(10);

            for (String instrument : musicianData.getStringArrayList("instruments")) {
                ImageView instrumentImageView = new ImageView(getContext());
                instrumentImageView.setLayoutParams(layoutParams);
                instrumentImageView.setImageResource(INSTRUMENT.valueOf(instrument).getImageId());
                binding.instrumentsVacancyPageLayout.addView(instrumentImageView);
            }

            binding.experienceValueTextView.setText(musicianData.getString("experience"));

            String description = musicianData.getString("description");
            TextView descriptionTextView = binding.musicianDescriptionValueLayout.findViewById(R.id.aboutMeContentTextView);
            if (!description.equals("")) {
                binding.musicianDescriptionValueLayout.setVisibility(View.VISIBLE);
                descriptionTextView.setText(description);
            }
            else {
                descriptionTextView.setText("Описание отсутствует");
            }


            // TODO: Установка треков
            List<String> tracks = musicianData.getStringArrayList("tracks");

            List<String> contacts = musicianData.getStringArrayList("contacts");

            if (contacts.get(0).equals("")) {
                binding.musicianContactsValueLayout.removeView(binding.contactPhoneLayout);
            } else {
                binding.musicianPhoneNumberText.setText(contacts.get(0));
            }
            if (contacts.get(1).equals("")) {
                binding.musicianContactsValueLayout.removeView(binding.contactEmailLayout);
            } else {
                binding.musicianEmailText.setText(contacts.get(1));
            }
            if (contacts.get(2).equals("")) {
                binding.musicianContactsValueLayout.removeView(binding.contactSocialMediaLayout);
            } else {
                binding.musicianSocialMediaText.setText(contacts.get(2));
            }


        }


        binding.arrowBackComponent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VacancyPageFragment fragment = new VacancyPageFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "VacancyPageFragment").addToBackStack(null).commit();
            }
        });

        return view;


    }
    private void setUserNicknameInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        String login = preferences.getString("login", "пользователь!");
        binding.welcomeTextView.setText(login);
    }

}