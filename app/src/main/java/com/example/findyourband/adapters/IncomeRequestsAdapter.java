package com.example.findyourband.adapters;



import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.findyourband.R;
import com.example.findyourband.services.RequestDataClass;

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
                // TODO: изменение статуса заявки в бд

                requestsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });

        holder.acceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: изменение статуса заявки в бд

                requestsList.remove(position);
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
