package com.example.findyourband.fragments.vacancies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.findyourband.R;
import com.example.findyourband.adapters.BandMembersAdapter;
import com.example.findyourband.databinding.FragmentBandPageBinding;
import com.example.findyourband.services.BandDataClass;
import com.example.findyourband.services.INSTRUMENT;
import com.example.findyourband.services.MemberDataClass;
import com.google.android.gms.tasks.OnSuccessListener;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBandPageBinding.inflate(inflater, container, false);

        Bundle bandData = this.getArguments();

        if (bandData != null) {
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



            List<MemberDataClass> members = getBandMembersData(bandData.getStringArrayList("members"));

            BandMembersAdapter searchMembersAdapter = new BandMembersAdapter(getContext(), members, this);

            binding.bandMemberVacancyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.bandMemberVacancyRecycleView.setAdapter(searchMembersAdapter);




            String description = bandData.getString("description");
            TextView descriptionTextView = binding.musicianDescriptionValueLayout.findViewById(R.id.aboutMeContentTextView);
            descriptionTextView.setText(description);

            List<String> tracks = bandData.getStringArrayList("tracks");
            // TODO: загрузка треков

            ArrayList<String> contacts = bandData.getStringArrayList("contacts");

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

        return binding.getRoot();

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



}