package com.example.findyourband.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.adapters.IncomeRequestsAdapter;
import com.example.findyourband.adapters.OutcomeRequestsAdapter;
import com.example.findyourband.databinding.FragmentMyRequestsBinding;
import com.example.findyourband.services.RequestDataClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyRequestsFragment extends Fragment {

    FragmentMyRequestsBinding binding;
    OutcomeRequestsAdapter outcomeRequestsAdapter;
    IncomeRequestsAdapter incomeRequestsAdapter;
    DatabaseReference requestsRef;

    List<RequestDataClass> outcomeRequestsList = new ArrayList<>();
    List<RequestDataClass> incomeRequestsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRequestsBinding.inflate(inflater, container, false);

        setUserNicknameInUpperBar();
        loadRequestsDataFromDatabase();

        // Для входящих заявок
        binding.incomeRequestsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        incomeRequestsAdapter = new IncomeRequestsAdapter(getContext(), incomeRequestsList);
        binding.incomeRequestsRecycleView.setAdapter(incomeRequestsAdapter);

        // Для исходящих заявок
        binding.outcomeRequestsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        outcomeRequestsAdapter = new OutcomeRequestsAdapter(getContext(), outcomeRequestsList);
        binding.outcomeRequestsRecycleView.setAdapter(outcomeRequestsAdapter);

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.navController.navigate(R.id.action_myRequestsFragment_to_navigation_my_account_settings);
            }
        });

        return binding.getRoot();
    }

    private void loadRequestsDataFromDatabase() {
        outcomeRequestsList.clear();
        incomeRequestsList.clear();
        requestsRef = FirebaseDatabase.getInstance().getReference("requests");
        DatabaseReference bandsRef = FirebaseDatabase.getInstance().getReference("bands");
        String currentUserLogin = binding.nicknameText.getText().toString();

        requestsRef.get().addOnSuccessListener(dataSnapshot -> {
            List<Task<DataSnapshot>> tasks = new ArrayList<>();
            for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                RequestDataClass request = requestSnapshot.getValue(RequestDataClass.class);
                if (request != null) {
                    if (currentUserLogin.equals(request.getTo()) && !"deny".equals(request.getStatus())) {
                        tasks.add(bandsRef.get().continueWithTask(task -> {
                            DataSnapshot dataSnapshot1 = task.getResult();
                            processIncomeRequest(dataSnapshot1, request);
                            return Tasks.forResult(null);
                        }));
                    } else if (currentUserLogin.equals(request.getFrom())) {
                        tasks.add(bandsRef.get().continueWithTask(task -> {
                            DataSnapshot dataSnapshot1 = task.getResult();
                            processOutcomeRequest(dataSnapshot1, request);
                            return Tasks.forResult(null);
                        }));
                    }
                }
            }

            Tasks.whenAll(tasks).addOnSuccessListener(unused -> {
                // Дождались выполнения всех задач
                filterOutcomeRequests();
            });
        });
    }

    private void processIncomeRequest(DataSnapshot dataSnapshot, RequestDataClass request) {
        if (!request.isType()) {
            for (DataSnapshot bandSnapshot : dataSnapshot.getChildren()) {
                List<String> members = new ArrayList<>();
                for (DataSnapshot memberSnapshot : bandSnapshot.child("memberUserLogins").getChildren()) {
                    members.add(memberSnapshot.getValue(String.class));
                }
                if (!members.isEmpty() && members.get(members.size() - 1).equals(request.getFrom())) {
                    request.setFrom("Группа " + bandSnapshot.child("name").getValue(String.class));
                    incomeRequestsList.add(request);
                    incomeRequestsAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else {
            incomeRequestsList.add(request);
            incomeRequestsAdapter.notifyDataSetChanged();
        }
    }

    private void processOutcomeRequest(DataSnapshot dataSnapshot, RequestDataClass request) {
        if (request.isType()) {
            for (DataSnapshot bandSnapshot : dataSnapshot.getChildren()) {
                List<String> members = new ArrayList<>();
                for (DataSnapshot memberSnapshot : bandSnapshot.child("memberUserLogins").getChildren()) {
                    members.add(memberSnapshot.getValue(String.class));
                }
                if (!members.isEmpty() && members.get(members.size() - 1).equals(request.getTo())) {
                    request.setTo("Группа " + bandSnapshot.child("name").getValue(String.class));
                    outcomeRequestsList.add(request);
                    break;
                }
            }
        } else {
            outcomeRequestsList.add(request);
        }
    }

    private void filterOutcomeRequests() {
        List<RequestDataClass> filteredOutcomeRequests = new ArrayList<>();
        for (RequestDataClass request : outcomeRequestsList) {
            if (incomeRequestsList.isEmpty() || (request.getTo() != null && !incomeRequestsList.get(0).getFrom().equals(request.getTo()))) {
                filteredOutcomeRequests.add(request);
            }
        }
        outcomeRequestsList.clear();
        outcomeRequestsList.addAll(filteredOutcomeRequests);
        outcomeRequestsAdapter.notifyDataSetChanged();
    }

    private void setUserNicknameInUpperBar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserData", 0);
        String nickname = preferences.getString("login", "");
        binding.nicknameText.setText(nickname);
    }
}
