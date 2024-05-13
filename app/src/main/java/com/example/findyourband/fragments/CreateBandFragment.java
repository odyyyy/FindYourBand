package com.example.findyourband.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.SearchBandMembersFragment;
import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentCreateBandBinding;
import com.example.findyourband.services.BandDataClass;
import com.example.findyourband.services.MemberDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class CreateBandFragment extends Fragment {
    FragmentCreateBandBinding binding;

    BandMembersAdapter searchMembersAdapter;

    ArrayList<MemberDataClass> selectedMembersArray;
    Bundle selectedMembers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCreateBandBinding.inflate(inflater, container, false);
        setSelectedBandMembers();

        //selectedMembers.getParcelableArrayList("selectedMembers");


        // TODO: Нужно сохранять состояние, чтобы оно оставалось прежним
        //  при возврате с страницы поиска учатсника
        binding.createBandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.bandnameEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Введите название группы", Toast.LENGTH_SHORT).show();
                    return;
                } else if (selectedMembersArray != null && selectedMembersArray.size() < 1) {
                    Toast.makeText(getContext(), "Добавьтя хотя бы одного участника", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    // сохранение названия группы, ID user лидера группы и участников группы в бд
                    DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");
                    String bandId = bandsRef.push().getKey();

                    String bandName = binding.bandnameEditText.getText().toString();
                    String leaderID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    List<String> bandMembersLogins = new ArrayList<>();
                    for (MemberDataClass member : selectedMembersArray) {
                        bandMembersLogins.add(member.getNickname());
                    }

                    // TODO: Проверка картинки. Если она загружена, то добавляем её в объект через Glide (сеттер)
                    BandDataClass band = new BandDataClass(bandName, leaderID, bandMembersLogins);

                    bandsRef.child(bandId).setValue(band)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Группа успешно создана!", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!binding.bandnameEditText.getText().toString().equals("")) {
            outState.putString("bandName", binding.bandnameEditText.getText().toString());
        }
        // TODO: Сохранение картинки Glide
        if (binding.avatarImageView.getDrawable() != null) {
            outState.putString("image", binding.avatarImageView.getDrawable().toString());
        }
        if (binding.bandMemberListView.getAdapter() != null) {
            outState.putParcelableArrayList("selectedMembers", searchMembersAdapter.getSelectedMembers());
        }
        Log.d("CreateBandFragment", binding.bandnameEditText.getText().toString());
        Log.d("CreateBandFragment", outState.toString());

    }
}