package com.example.findyourband.adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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


import com.example.findyourband.R;
import com.example.findyourband.services.ContactsDialogController;
import com.example.findyourband.services.FirebaseQueriesServices;
import com.example.findyourband.services.RequestDataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class IncomeRequestsAdapter extends RecyclerView.Adapter<IncomeRequestsAdapter.ViewHolder> {


    private LayoutInflater layoutInflater;
    private List<RequestDataClass> requestsList;

    public IncomeRequestsAdapter(Context context, List<RequestDataClass> requestsList) {
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
        holder.requestToNicknameText.setText(request.getFrom());
        holder.requestToImageText.setImageResource(R.drawable.user_default_avatar);

        holder.denyImageView.setImageResource(R.drawable.ic_deny);
        holder.acceptImageView.setImageResource(R.drawable.ic_accepted);

        if (request.getStatus().equals("accept")) {
            holder.acceptImageView.setVisibility(View.GONE);
            holder.denyImageView.setVisibility(View.GONE);
            holder.frameLayout.setBackgroundResource(R.drawable.request_accept_shape);
        }
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (request.getStatus().equals("accept")) {
                    DatabaseReference vacanciesRef = FirebaseDatabase.getInstance().getReference("vacancies");
                    if (request.getFrom().contains("Группа")) {
                        vacanciesRef.child("from_bands").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("id").getValue(String.class).equals(request.getId())) {
                                        ArrayList<String> contacts = new ArrayList<>();
                                        for (DataSnapshot contactSnapshot : snapshot.child("contacts").getChildren()) {
                                            contacts.add(contactSnapshot.getValue(String.class));
                                        }
                                        ContactsDialogController.showContactsDialog(v.getContext(), contacts);
                                    }

                                }
                            }
                        });
                    } else {
                        FirebaseQueriesServices.getUserContactsByLogin(request.getFrom(), new FirebaseQueriesServices.UserContactsGetterCallback() {
                            @Override
                            public void onGetContactsCompleted(ArrayList<String> contacts) {
                                ContactsDialogController.showContactsDialog(v.getContext(), contacts);
                            }
                        });
                    }
                }
            }
        });


        holder.denyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
                requestsRef.child(request.getId()).child("status").getRef().setValue("deny").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        requestsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                        Toast.makeText(v.getContext(), "Заявка успешно отклонена!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Ошибка отклонения заявки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.acceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
                requestsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for (DataSnapshot req : dataSnapshot.getChildren()) {
                            if (request.getId() != null && req.child("id").getValue(String.class).equals(request.getId())) {
                                req.child("status").getRef().setValue("accept");
                                holder.acceptImageView.setVisibility(View.GONE);
                                holder.denyImageView.setVisibility(View.GONE);
                                holder.frameLayout.setBackgroundResource(R.drawable.request_accept_shape);
                                request.setStatus("accept");
                                Toast.makeText(v.getContext(), "Заявка успешно принята! Нажмите чтобы посмотреть контакты.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });

            }
        });


    }


    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView requestToNicknameText;

        ImageView denyImageView;
        ImageView acceptImageView;
        ImageView requestToImageText;
        FrameLayout frameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestToNicknameText = itemView.findViewById(R.id.text_nickname);
            denyImageView = itemView.findViewById(R.id.requestStatusImageView);
            acceptImageView = itemView.findViewById(R.id.requestStatusImageView2);
            requestToImageText = itemView.findViewById(R.id.request_to_image_user_avatar);
            frameLayout = itemView.findViewById(R.id.container_request);
        }
    }


}
