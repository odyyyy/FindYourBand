package com.example.findyourband.fragments.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findyourband.R;
import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentSearchBandMembersBinding;
import com.example.findyourband.fragments.settings.CreateBandFragment;
import com.example.findyourband.services.MemberDataClass;
import com.google.firebase.auth.FirebaseAuth;
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


        searchMembersAdapter =  new BandMembersAdapter(getContext(), userSearchList, this);

        searchMembersAdapter.searchDataList(userSearchList);

        binding.membersListRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.membersListRecycleView.setAdapter(searchMembersAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // получение данных при изменении бд
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (userSearchList.size() > 0) {
                    userSearchList.clear();
                }
                String currentUserLogin = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("login").getValue().toString();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    MemberDataClass user = new MemberDataClass(ds.child("login").getValue().toString());
                    if (!user.getNickname().equals(currentUserLogin))
                        userSearchList.add(user);
                }

                searchMembersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // реализация автокомплита
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
        binding.applyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle selectedMembers = new Bundle();
                selectedMembers.putParcelableArrayList("selectedMembers", searchMembersAdapter.getSelectedMembers());

                CreateBandFragment fragment = new CreateBandFragment();
                fragment.setArguments(selectedMembers);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment, "CreateBandFragment").addToBackStack(null).commit();
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void searchList(String text) {
        ArrayList<MemberDataClass> searchList = new ArrayList<>();

        for (MemberDataClass user : userSearchList) {
            if (user.getNickname().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(user);
            }
        }
        // TODO: подумать над сохранением состояния selected в списке при поиске оно сбрасывается
        searchMembersAdapter.searchDataList(searchList);
    }

    public void updateSelectedCountText() {
        int selectedCount = searchMembersAdapter.getSelectedCount();
        binding.selectedMembersText.setText(String.valueOf(selectedCount));
    }
}