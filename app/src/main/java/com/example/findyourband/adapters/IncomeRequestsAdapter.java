package com.example.findyourband.adapters;



import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.findyourband.R;
import com.example.findyourband.services.RequestDataClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        holder.denyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDataClass currentRequest = requestsList.get(position);

                DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
                requestsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        boolean requestFound = false;
                        for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                            RequestDataClass req = requestSnapshot.getValue(RequestDataClass.class);

                            Log.d("REQUESTLogging", req.getFrom() + " " + req.getTo() + " " + req.getStatus());
                            Log.d("REQUESTLogging", currentRequest.getFrom() + " " + currentRequest.getTo() + " " + currentRequest.getStatus());
                            if (req != null && currentRequest.getFrom().equals(req.getFrom()) && currentRequest.getTo().equals(req.getTo()) && currentRequest.getStatus().equals(req.getStatus()) && currentRequest.isType() == req.isType()) {
                                Log.d("REQUESTLogging", req.getFrom() + " " + req.getTo() + " " + req.getStatus());
                                requestSnapshot.getRef().child("status").setValue("deny").addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("REQUESTLogging", "Status updated successfully");
                                    } else {
                                        Log.e("REQUESTLogging", "Error updating status", task.getException());
                                    }
                                });
                                requestFound = true;
                                break;
                            }
                        }
                        if (requestFound) {
                            requestsList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            Toast.makeText(layoutInflater.getContext(), "Заявка отклонена", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(layoutInflater.getContext(), "Ошибка: заявка не найдена", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e("REQUESTLogging", "Error getting data", e);
                    Toast.makeText(layoutInflater.getContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show();
                });
            }
        });


        holder.acceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: изменение статуса заявки в бд

                // TODO: изменение фона
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestToNicknameText = itemView.findViewById(R.id.text_nickname);
            denyImageView = itemView.findViewById(R.id.requestStatusImageView);
            acceptImageView = itemView.findViewById(R.id.requestStatusImageView2);
            requestToImageText = itemView.findViewById(R.id.request_to_image_user_avatar);
        }
    }





}
