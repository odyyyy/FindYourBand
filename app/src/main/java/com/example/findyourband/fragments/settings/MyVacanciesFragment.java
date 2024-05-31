package com.example.findyourband.fragments.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.adapters.VacanciesAdapter;
import com.example.findyourband.databinding.FragmentMyVacanciesBinding;
import com.example.findyourband.services.OnLoginLoadedListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyVacanciesFragment extends Fragment {

    FragmentMyVacanciesBinding binding;
    VacanciesAdapter adapter;
    private List<Map<String, ArrayList<String>>> vacanciesList = new ArrayList<Map<String, ArrayList<String>>>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMyVacanciesBinding.inflate(inflater, container, false);


        binding.myVacanciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VacanciesAdapter(getContext(), vacanciesList, true);

        binding.myVacanciesRecyclerView.setAdapter(adapter);

        loadVacanciesDataFromDatabase();

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_myVacanciesFragment_to_navigation_my_account_settings);
            }
        });

        return binding.getRoot();
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
                        String UserID = vacancySnapshot.getKey();

                        if (UserID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {


                            getUserLoginAndImgByID(UserID, new OnLoginLoadedListener() {
                                @Override
                                public void onLoginAndImgLoaded(String login, String img) {
                                    String city = vacancySnapshot.child("city").getValue(String.class);
                                    List<String> instruments = new ArrayList<>();
                                    List<String> genres = new ArrayList<>();
                                    for (DataSnapshot instrumentSnapshot : vacancySnapshot.child("instruments").getChildren()) {
                                        instruments.add(instrumentSnapshot.getValue(String.class));
                                    }
                                    for (DataSnapshot genreSnapshot : vacancySnapshot.child("genres").getChildren()) {
                                        genres.add(genreSnapshot.getValue(String.class));
                                    }
                                    String description = vacancySnapshot.child("description").getValue(String.class);
                                    String experience = vacancySnapshot.child("experience").getValue(String.class);

                                    List<String> tracks = new ArrayList<>();
                                    if (vacancySnapshot.child("tracks").exists()) {
                                        for (DataSnapshot trackSnapshot : vacancySnapshot.child("tracks").getChildren()) {
                                            tracks.add(trackSnapshot.getValue(String.class));
                                        }
                                    }
                                    String id = vacancySnapshot.child("id").getValue(String.class);


                                    List<String> contacts = new ArrayList<>();
                                    for (DataSnapshot contactSnapshot : vacancySnapshot.child("contacts").getChildren()) {
                                        contacts.add(contactSnapshot.getValue(String.class));
                                    }


                                    Map<String, ArrayList<String>> vacancy = createVacancy(id, login, img, city, instruments, genres, description,
                                            tracks, contacts, experience, null);
                                    if (!vacanciesList.contains(vacancy)) {
                                        vacanciesList.add(vacancy);
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
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
                        if (!bandId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            continue;
                        }

                        DatabaseReference bandRef = FirebaseDatabase.getInstance().getReference("bands").child(bandId);
                        ArrayList<String> instruments = new ArrayList<>(Collections.singletonList(bandSnapshot.child("instrument").getValue(String.class)));

                        String description = bandSnapshot.child("description").getValue(String.class);

                        List<String> tracks = new ArrayList<>();
                        if (dataSnapshot.child("tracks").exists()) {
                            for (DataSnapshot trackSnapshot : bandSnapshot.child("tracks").getChildren()) {
                                tracks.add(trackSnapshot.getValue(String.class));
                            }
                        }
                        List<String> contacts = new ArrayList<>();

                        for (DataSnapshot contactSnapshot : bandSnapshot.child("contacts").getChildren()) {
                            contacts.add(contactSnapshot.getValue(String.class));
                        }
                        String vacancyID = bandSnapshot.child("id").getValue(String.class);


                        bandRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot bandDataSnapshot) {
                                if (bandDataSnapshot.exists()) {
                                    String name = "Группа " + bandDataSnapshot.child("name").getValue(String.class);
                                    String img = bandDataSnapshot.child("bandImage").getValue(String.class);

                                    String city = bandDataSnapshot.child("city").getValue(String.class);

                                    ArrayList<String> genres = new ArrayList<>();
                                    for (DataSnapshot genreSnapshot : bandDataSnapshot.child("genres").getChildren()) {
                                        genres.add(genreSnapshot.getValue(String.class));
                                    }
                                    List<String> members = new ArrayList<>();
                                    for (DataSnapshot memberSnapshot : bandDataSnapshot.child("memberUserLogins").getChildren()) {
                                        members.add(memberSnapshot.getValue(String.class));
                                    }


                                    Map<String, ArrayList<String>> band = createVacancy(vacancyID, name, img, city, instruments, genres, description, tracks, contacts, null, members);
                                    if (!vacanciesList.contains(band)) {
                                        vacanciesList.add(band);
                                    }

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

    private Map<String, ArrayList<String>> createVacancy(String id, String nickname, String img, String city, List<String> instruments,
                                                         List<String> genres, String description, List<String> tracks,
                                                         List<String> contacts, String experience, List<String> members) {
        Map<String, ArrayList<String>> vacancy = new HashMap<>();
        vacancy.put("id", new ArrayList<>(Collections.singletonList(id)));
        vacancy.put("name", new ArrayList<>(Collections.singletonList(nickname)));
        vacancy.put("image", new ArrayList<>(Collections.singletonList(img)));
        vacancy.put("city", new ArrayList<>(Collections.singletonList(city)));
        vacancy.put("instruments", new ArrayList<>(instruments));
        vacancy.put("genres", new ArrayList<>(genres));
        vacancy.put("description", new ArrayList<>(Collections.singletonList(description)));
        vacancy.put("tracks", new ArrayList<>(tracks));
        vacancy.put("contacts", new ArrayList<>(contacts));


        if (experience != null) {
            vacancy.put("experience", new ArrayList<>(Collections.singletonList(experience)));
        }
        if (members != null) {
            vacancy.put("members", new ArrayList<>(members));
        }
        return vacancy;
    }

    private void getUserLoginAndImgByID(String userID, OnLoginLoadedListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userID);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String login = dataSnapshot.child("login").getValue(String.class);
                    String img = dataSnapshot.child("image").getValue(String.class);
                    listener.onLoginAndImgLoaded(login, img);
                } else {
                    listener.onLoginAndImgLoaded(null, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onLoginAndImgLoaded(null, null);
            }
        });
    }
}