package com.example.findyourband.fragments.vacancies;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.databinding.FragmentMusicianPageBinding;
import com.example.findyourband.services.INSTRUMENT;
import com.example.findyourband.services.RequestDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class MusicianPageFragment extends Fragment {

    FragmentMusicianPageBinding binding;

    String currentUserLogin;
    String musicianLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMusicianPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUserNicknameInUpperBar();



        Bundle musicianData = getArguments();


        if (musicianData != null) {
            String id = musicianData.getString("id");
            updateBtnStateIfRequestExists(id);


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


        }

        hideButtonIfUsersVacancy();

        binding.arrowBackComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_musicianPageFragment_to_navigation_vacancy);
            }
        });

        binding.sendRequestVacancyPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");

                if (binding.sendRequestVacancyPageBtn.getText().toString().equals("Заявка отправлена")) {
                    Toast.makeText(getContext(), "Вы уже отправляли заявку!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bandsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(currentUserID).exists()) {
                            sendRequest();

                        } else {
                            Toast.makeText(getContext(), "Только лидер группы может отправлять запросы музыкантам!", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Ошибка проверки статуса группы: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void sendRequest() {
                String requestID = musicianData.getString("id");

                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
                RequestDataClass requestDataClass = new RequestDataClass(requestID, currentUserLogin, musicianLogin, "send",  false);

                requestsRef.child(requestsRef.push().getKey() + currentUserLogin ).setValue(requestDataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Заявка успешно отправлена!", Toast.LENGTH_SHORT).show();
                        changeBtnStateAfterSendRequest();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Ошибка при создании заявки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;


    }

    private void  updateBtnStateIfRequestExists(String id) {

        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");

        requestsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for (DataSnapshot request : dataSnapshot.getChildren()) {
                    if (request.getKey().equals(id) && request.child("from").getValue(String.class).equals(currentUserLogin)) {
                        changeBtnStateAfterSendRequest();
                        return;
                    }
                }

            }
        });

    }

    private void hideButtonIfUsersVacancy() {
        musicianLogin = binding.musicianAvatarAndNickname.nicknameTextView.getText().toString();
        if (currentUserLogin.equals(musicianLogin)) {
            binding.sendRequestVacancyPageBtn.setVisibility(View.GONE);
        }
    }

    private void changeBtnStateAfterSendRequest() {
        binding.sendRequestVacancyPageBtn.setBackgroundColor(getContext().getResources().getColor(R.color.orange));
        binding.sendRequestVacancyPageBtn.setText("Заявка отправлена");
    }

    private void setUserNicknameInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        currentUserLogin = preferences.getString("login", "пользователь!");
        binding.welcomeTextView.setText(currentUserLogin);
    }

}