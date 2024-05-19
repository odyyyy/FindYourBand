package com.example.findyourband.fragments.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentCreateBandBinding;
import com.example.findyourband.services.BandDataClass;
import com.example.findyourband.services.MemberDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CreateBandFragment extends Fragment {
    FragmentCreateBandBinding binding;

    BandMembersAdapter searchMembersAdapter;


    ArrayList<MemberDataClass> selectedMembersArray;
    Bundle selectedMembers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateBandBinding.inflate(inflater, container, false);
        loadState();
        setSelectedBandMembers();

        binding.cityAutoComplete.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.cities)));

        binding.createBandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> genreCheckedChipIds = binding.genreChipGroup.getCheckedChipIds();
                if (binding.bandnameEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Введите название группы", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (binding.cityAutoComplete.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Выберите город", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (selectedMembersArray != null && selectedMembersArray.size() < 1) {
                    Toast.makeText(getContext(), "Добавьтя хотя бы одного участника", Toast.LENGTH_SHORT).show();
                    return;
                } else if (genreCheckedChipIds.isEmpty() || genreCheckedChipIds.size() > 3) {
                    Toast.makeText(getContext(), "Выберите от 1 до 3 жанров", Toast.LENGTH_SHORT).show();
                } else {

                    DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");

                    String bandName = binding.bandnameEditText.getText().toString();
                    String leaderID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    String city = binding.cityAutoComplete.getText().toString();

                    List<String> genres = getGenreNamesArray(binding.genreChipGroup.getCheckedChipIds());

                    // TODO: Проверка картинки. Если она загружена, то добавляем её в объект через Glide (сеттер)
                    String image = "";


                    List<String> bandMembersLogins = new ArrayList<>();
                    for (MemberDataClass member : selectedMembersArray) {
                        bandMembersLogins.add(member.getNickname());
                    }
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                    Task<DataSnapshot> loginTask = userRef.child(leaderID).child("login").get();

                    loginTask.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            String login = dataSnapshot.getValue(String.class);
                            bandMembersLogins.add(login);

                            BandDataClass band = new BandDataClass(bandName, city, genres, image, bandMembersLogins);

                            bandsRef.child(leaderID).setValue(band)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Группа успешно создана!", Toast.LENGTH_SHORT).show();
                                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean("bandLeader", true);
                                            editor.apply();

                                            NavController navController = NavHostFragment.findNavController(CreateBandFragment.this);
                                            NavOptions navOptions = new NavOptions.Builder()
                                                    .setPopUpTo(R.id.navigation_my_account_settings, true)
                                                    .build();
                                            navController.navigate(R.id.action_createBandFragment_to_navigation_my_account_settings, null, navOptions);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Ошибка при создании группы: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Не удалось получить логин: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_createBandFragment_to_navigation_my_account_settings);
            }
        });

        binding.addBandMembersBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveState();
                AppActivity.navController.navigate(R.id.action_createBandFragment_to_searchBandMembersFragment);
            }
        });

        return binding.getRoot();
    }

    private void saveState() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CreateBandState_" + uid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bandName", binding.bandnameEditText.getText().toString());
        editor.putString("city", binding.cityAutoComplete.getText().toString());

        Set<String> chipIds = new HashSet<>();
        for (int id : binding.genreChipGroup.getCheckedChipIds()) {
            chipIds.add(String.valueOf(id));
        }
        editor.putStringSet("selectedChips", chipIds);

        //editor.putString("imagePath", getImagePath());

        editor.apply();
    }

    private void loadState() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CreateBandState_" + uid, Context.MODE_PRIVATE);
        String bandName = sharedPreferences.getString("bandName", "");
        String city = sharedPreferences.getString("city", "");

        binding.bandnameEditText.setText(bandName);
        binding.cityAutoComplete.setText(city);

        Set<String> chipIds = sharedPreferences.getStringSet("selectedChips", new HashSet<>());
        for (String id : chipIds) {
            int chipId = Integer.parseInt(id);
            Chip chip = binding.genreChipGroup.findViewById(chipId);
            if (chip != null) {
                chip.setChecked(true);
            }
        }

//        String imagePath = sharedPreferences.getString("imagePath", "");
//        if (!imagePath.isEmpty()) {
//            Glide.with(this).load(imagePath).into(binding.bandImageView);
//        }
    }

    private String getImagePath() {
        return "";
    }


    void setSelectedBandMembers() {
        selectedMembers = getArguments();
        if (selectedMembers != null) {
            selectedMembersArray = selectedMembers.getParcelableArrayList("selectedMembers");
            searchMembersAdapter = new BandMembersAdapter(getContext(), selectedMembersArray, this);

            binding.bandMemberListView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.bandMemberListView.setAdapter(searchMembersAdapter);
        }
    }

    private List<String> getGenreNamesArray(List<Integer> genreCheckedChipIds) {

        List<String> genreNames = new ArrayList<>();
        for (int genreId : genreCheckedChipIds) {
            genreNames.add(((Chip) binding.genreChipGroup.findViewById(genreId)).getText().toString());
        }
        return genreNames;
    }


}