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
import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentSearchBandMembersBinding;
import com.example.findyourband.services.FirebaseQueriesServices;
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

        searchMembersAdapter = new BandMembersAdapter(getContext(), userSearchList, this);

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

                DataSnapshot currentUserSnapshot = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                String currentUserLogin = currentUserSnapshot.child("login").getValue(String.class);

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String login = ds.child("login").getValue(String.class);
                    String image = ds.child("image").getValue(String.class);
                    ArrayList<String> instruments = (ArrayList<String>) ds.child("instruments").getValue();

                    if (login != null && image != null && instruments != null && !login.equals(currentUserLogin)) {
                        MemberDataClass user = new MemberDataClass(login, image, instruments);

                        FirebaseQueriesServices.isUserAlreadyInBand(login, new FirebaseQueriesServices.BandCheckCallback() {
                            @Override
                            public void onCheckCompleted(boolean isInBand) {
                                if (!isInBand) {
                                    userSearchList.add(user);
                                    searchMembersAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
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

                AppActivity.navController.navigate(R.id.action_searchBandMembersFragment_to_createBandFragment, selectedMembers);
            }
        });

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_searchBandMembersFragment_to_createBandFragment);
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

    public void updateSelectedCountText() {
        int selectedCount = searchMembersAdapter.getSelectedCount();
        binding.selectedMembersText.setText(String.valueOf(selectedCount));
    }
}