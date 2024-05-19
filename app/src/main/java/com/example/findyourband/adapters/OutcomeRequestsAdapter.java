package com.example.findyourband.adapters;


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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RequestDataClass request = requestsList.get(position);
        holder.requestToNicknameText.setText(request.getTo());
        holder.statusImageView.setImageResource(STATUS.valueOf(request.getStatus()).getStatusId());
        holder.requestToImageText.setImageResource(R.drawable.user_default_avatar);



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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestToNicknameText = itemView.findViewById(R.id.text_nickname);
            statusImageView = itemView.findViewById(R.id.requestStatusImageView);
            requestToImageText = itemView.findViewById(R.id.request_to_image_user_avatar);
            requestDecisionImageView = itemView.findViewById(R.id.requestStatusImageView2);

            requestDecisionImageView.setVisibility(View.GONE);

        }
    }





}
