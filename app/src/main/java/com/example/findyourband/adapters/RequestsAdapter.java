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

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {


    private LayoutInflater layoutInflater;
    private List<RequestDataClass> requestsList;

    public RequestsAdapter(Context context, List<RequestDataClass> requestsList) {
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



    }


    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView requestToNicknameText;

        ImageView statusImageView;
        ImageView requestToImageText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }





}
