package com.example.findyourband.fragments.vacancies;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentBandPageBinding;

import com.example.findyourband.services.AlreadyBandMemberChecker;
import com.example.findyourband.services.INSTRUMENT;
import com.example.findyourband.services.MemberDataClass;
import com.example.findyourband.services.RequestDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class BandPageFragment extends Fragment {

    FragmentBandPageBinding binding;
    List<MemberDataClass> members;
    String currentUserLogin;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBandPageBinding.inflate(inflater, container, false);

        setUserNicknameInUpperBar();

        Bundle bandData = this.getArguments();

        if (bandData != null) {
            updateBtnStateIfRequestExists(bandData.getString("id"));
            String bandName = bandData.getString("name");
            binding.bandnameTextView.setText(bandName);

            String img = bandData.getString("image");

            if (!img.equals("")) {
                binding.bandImageView.setImageResource(R.drawable.user_default_avatar);
            }
            else
                binding.bandImageView.setImageResource(R.drawable.user_default_avatar);

            String city = bandData.getString("city");
            binding.bandCityTextView.setText(city);


            List<String> instruments = bandData.getStringArrayList("instruments");
            binding.wantFindInstrumentImage.setImageResource(INSTRUMENT.valueOf(instruments.get(0)).getImageId());

            List<String> genres = bandData.getStringArrayList("genres");
            ContextThemeWrapper newContext = new ContextThemeWrapper(getContext(), R.style.genreTextViewStyle);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMarginEnd(20);
            for (String genre : genres) {
                TextView genreTextView = new TextView(newContext);
                genreTextView.setText(genre);
                genreTextView.setLayoutParams(params);
                binding.genreVacancyPageLayout.addView(genreTextView);
            }



            members = getBandMembersData(bandData.getStringArrayList("members"));

            BandMembersAdapter searchMembersAdapter = new BandMembersAdapter(getContext(), members, this);

            binding.bandMemberVacancyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.bandMemberVacancyRecycleView.setAdapter(searchMembersAdapter);




            String description = bandData.getString("description");
            TextView descriptionTextView = binding.musicianDescriptionValueLayout.findViewById(R.id.aboutMeContentTextView);
            descriptionTextView.setText(description);

            List<String> tracks = bandData.getStringArrayList("tracks");
            // TODO: загрузка треков

            ArrayList<String> contacts = bandData.getStringArrayList("contacts");


        }
        hideButtonIfBandsVacancy();


        binding.arrowBackComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_bandPageFragment_to_navigation_vacancy);
            }
        });

        binding.sendRequestVacancyPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");

                if (binding.sendRequestVacancyPageBtn.getText().toString().equals("Заявка отправлена")) {
                    Toast.makeText(getContext(), "Вы уже отправляли заявку!", Toast.LENGTH_SHORT).show();
                    return;
                }

                bandsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        AlreadyBandMemberChecker.isUserAlreadyInBand(currentUserLogin, new AlreadyBandMemberChecker.BandCheckCallback() {

                            @Override
                            public void onCheckCompleted(boolean isInBand) {
                                if (isInBand) {
                                    Toast.makeText(getContext(), "Участники групп не могут отправлять заявки музыкантам!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                sendRequest();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Ошибка проверки статуса группы: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            private void sendRequest() {

                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
                String bandLeaderLogin = getBandLeaderLogin();
                String requestID = bandData.getString("id");

                RequestDataClass requestDataClass = new RequestDataClass(requestID,currentUserLogin,
                        bandLeaderLogin != null ? bandLeaderLogin : binding.bandnameTextView.getText().toString(),
                        "send",
                        true);

                requestsRef.child(requestsRef.push().getKey() + currentUserLogin).setValue(requestDataClass).addOnSuccessListener(new OnSuccessListener<Void>() {
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

            private String getBandLeaderLogin() {
                for (MemberDataClass member : members) {
                    if (member.isLeader()) {
                        binding.sendRequestVacancyPageBtn.setVisibility(View.GONE);
                        return member.getNickname();
                    }
                }
                return null;
            }
        });

        return binding.getRoot();

    }



    private void setUserNicknameInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        currentUserLogin = preferences.getString("login", "пользователь!");
        binding.welcomeTextView.setText( currentUserLogin );
    }

    private List<MemberDataClass> getBandMembersData(ArrayList<String> membersNames) {
        List<MemberDataClass> members = new ArrayList<>();
        DatabaseReference userBandMembersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");

        userBandMembersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                    String name = memberSnapshot.child("login").getValue(String.class);
                    if (membersNames.contains(name)) {
                        String img = memberSnapshot.child("image").getValue(String.class);

                        GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> instruments = memberSnapshot.child("instruments").getValue(genericTypeIndicator);
                        MemberDataClass memberData = new MemberDataClass(name, img, instruments);

                        String memberKey = memberSnapshot.getKey();

                        bandsRef.child(memberKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    memberData.setLeader(true);
                                }
                                members.add(memberData);
                                if (binding.bandMemberVacancyRecycleView.getAdapter() != null) {
                                    members.sort(Comparator.comparing(MemberDataClass::isLeader).reversed());

                                    binding.bandMemberVacancyRecycleView.getAdapter().notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("FirebaseError", "Отмена: " + databaseError.getMessage());
                            }
                        });
                    }
                }
            }
        });

        return members;
    }

    private void hideButtonIfBandsVacancy() {
        for (MemberDataClass member : members) {
            if (member.getNickname().equals(currentUserLogin)) {
                binding.sendRequestVacancyPageBtn.setVisibility(View.GONE);
                break;
            }
        }
    }
    private void changeBtnStateAfterSendRequest() {
        binding.sendRequestVacancyPageBtn.setBackgroundColor(getContext().getResources().getColor(R.color.orange));
        binding.sendRequestVacancyPageBtn.setText("Заявка отправлена");
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
}