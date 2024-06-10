package com.example.findyourband.adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.services.ContactsDialogController;
import com.example.findyourband.services.FirebaseQueriesServices;
import com.example.findyourband.services.RequestDataClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

enum STATUS {
    accept(R.drawable.ic_accepted),
    deny(R.drawable.ic_deny),
    send(R.drawable.ic_clock);
    private final int statusID;

    STATUS(int i) {
        statusID = i;
    }

    public int getStatusId() {
        return statusID;
    }
}

public class OutcomeRequestsAdapter extends RecyclerView.Adapter<OutcomeRequestsAdapter.ViewHolder> {


    private LayoutInflater layoutInflater;
    private List<RequestDataClass> requestsList;

    public OutcomeRequestsAdapter(Context context, List<RequestDataClass> requestsList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.requestsList = requestsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.component_request_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        RequestDataClass request = requestsList.get(position);
        holder.requestToNicknameText.setText(request.getTo());
        holder.statusImageView.setImageResource(STATUS.valueOf(request.getStatus()).getStatusId());
        holder.requestToImageText.setImageResource(R.drawable.user_default_avatar);

        if (request.getStatus().equals("send")) {

            holder.frameLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (request.getTo().contains("Группа")) {
                        //Bundle bandDataBundle = new Bundle();
                        // TODO: получаем данные группы и открываем страницу их объявлений

                        // AppActivity.navController.navigate(R.id.action_myRequestsFragment_to_bandPageFragment, bandDataBundle);
                    }

                    else{
                        Bundle UserDataBundle = new Bundle();
                        FirebaseQueriesServices.getUserIdByLogin(request.getTo(), new FirebaseQueriesServices.UserIdGetterCallback() {
                            @Override
                            public void onGetIdCompleted(String ID) {
                                UserDataBundle.putString("UserID", ID);
                                AppActivity.navController.navigate(R.id.action_myRequestsFragment_to_userProfileFragment,UserDataBundle);
                            }
                        });
                    }
                }
            });

        }

        else if (request.getStatus().equals("deny")) {
            holder.frameLayout.setBackgroundResource(R.drawable.request_deny_shape);
            DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
            holder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestsRef.child(request.getId()).removeValue();

                    requestsList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            });
        } else if (request.getStatus().equals("accept")) {
            holder.frameLayout.setBackgroundResource(R.drawable.request_accept_shape);
            holder.frameLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatabaseReference vacanciesRef = FirebaseDatabase.getInstance().getReference("vacancies");
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

                    if (request.getTo().contains("Группа")) {
                        vacanciesRef.child("from_bands").get().addOnSuccessListener(dataSnapshot -> {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d("TAGLogging", "onDataChange: " + snapshot.child("id").getValue(String.class) + " " + request.getId());
                                if (snapshot.child("id").getValue(String.class).equals(request.getId())) {
                                    ArrayList<String> contacts = new ArrayList<>();
                                    for (DataSnapshot contactSnapshot : snapshot.child("contacts").getChildren()) {
                                        contacts.add(contactSnapshot.getValue(String.class));
                                    }
                                    ContactsDialogController.showContactsDialog(v.getContext(), contacts);
                                    break;

                                }
                            }
                        });
                    } else {

                        FirebaseQueriesServices.getUserContactsByLogin(request.getTo(), new FirebaseQueriesServices.UserContactsGetterCallback() {
                            @Override
                            public void onGetContactsCompleted(ArrayList<String> contacts) {
                                ContactsDialogController.showContactsDialog(v.getContext(), contacts);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView requestToNicknameText;

        ImageView statusImageView;
        ImageView requestToImageText;
        ImageView requestDecisionImageView;
        FrameLayout frameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestToNicknameText = itemView.findViewById(R.id.text_nickname);
            statusImageView = itemView.findViewById(R.id.requestStatusImageView);
            requestToImageText = itemView.findViewById(R.id.request_to_image_user_avatar);
            requestDecisionImageView = itemView.findViewById(R.id.requestStatusImageView2);
            frameLayout = itemView.findViewById(R.id.container_request);
            requestDecisionImageView.setVisibility(View.GONE);

        }
    }


}
