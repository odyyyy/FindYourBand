package com.example.findyourband.fragments.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentCreateBandBinding;
import com.example.findyourband.services.BandDataClass;
import com.example.findyourband.services.MemberDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class CreateBandFragment extends Fragment {
    FragmentCreateBandBinding binding;

    BandMembersAdapter searchMembersAdapter;

    String bandNameSavedState = "";

    String TAG = "CreateBandFragment";

    ArrayList<MemberDataClass> selectedMembersArray;
    Bundle selectedMembers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateBandBinding.inflate(inflater, container, false);
        setSelectedBandMembers();


        binding.cityAutoComplete.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.cities)));

        binding.bandnameEditText.setText(bandNameSavedState);



        // TODO: Нужно сохранять состояние, чтобы оно оставалось прежним
        //  при возврате с страницы поиска учатсника
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


                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    MyAccountSettingsFragment fragment = new MyAccountSettingsFragment();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.app_fragment_container, fragment, "MyAccountSettingsFragment").addToBackStack(null).commit();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Ошибка при создании группы: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                MyAccountSettingsFragment fragment = new MyAccountSettingsFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "MyAccountSettingsFragment").addToBackStack(null).commit();
            }
        });

        binding.addBandMembersBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                SearchBandMembersFragment fragment = new SearchBandMembersFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "SearchBandMembersFragment").addToBackStack(null).commit();

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO: Восстановление состояния
        super.onCreate(savedInstanceState);


//        if (savedInstanceState != null) {
//            String bandName = savedInstanceState.getString("bandName");
//            String image = savedInstanceState.getString("image");
//            binding.bandnameEditText.setText(bandName);
//
//            if (image != null) {
//                Log.d("CreateBandFragment", image);
//            }
//        }
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



    @Override
    public void onPause() {
        super.onPause();
        bandNameSavedState = binding.bandnameEditText.getText().toString();
    }


}