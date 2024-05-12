package com.example.findyourband;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentSearchBandMembersBinding;
import com.example.findyourband.fragments.CreateBandFragment;
import com.example.findyourband.fragments.VacancyPageFragment;
import com.example.findyourband.services.MemberDataClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchBandMembersFragment extends Fragment {
    FragmentSearchBandMembersBinding binding;
    BandMembersAdapter searchMembersAdapter;

    List<MemberDataClass> userSearchList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBandMembersBinding.inflate(inflater, container, false);

        binding.bandMembersSearchView.clearFocus();

        userSearchList = new ArrayList<>();


        searchMembersAdapter =  new BandMembersAdapter(getContext(), userSearchList);

        searchMembersAdapter.searchDataList(userSearchList);

        binding.membersListRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.membersListRecycleView.setAdapter(searchMembersAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (userSearchList.size() > 0) {
                    userSearchList.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {

                    MemberDataClass user = new MemberDataClass(ds.child("login").getValue().toString());
                    userSearchList.add(user);
                }
                searchMembersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.bandMembersSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateBandFragment fragment = new CreateBandFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "VacancyPageFragment").addToBackStack(null).commit();
            }
        });

        return binding.getRoot();

    }

    public void searchList(String text) {
        ArrayList<MemberDataClass> searchList = new ArrayList<>();

        for (MemberDataClass user : userSearchList) {
            if (user.getNickname().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(user);
            }
        }
        searchMembersAdapter.searchDataList(searchList);


    }

    public void getAllUsersFromDB() {

    }

}