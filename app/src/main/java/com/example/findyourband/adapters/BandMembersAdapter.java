package com.example.findyourband.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.R;
import com.example.findyourband.services.MemberDataClass;


import java.util.List;

public class BandMembersAdapter extends RecyclerView.Adapter<BandMembersAdapter.ViewHolder> {

    private Context context;
    private List<MemberDataClass> dataList;

    public BandMembersAdapter(Context ctx, List<MemberDataClass> bandMembersList) {
        this.context = ctx;
        this.dataList = bandMembersList;

    }

    @NonNull
    @Override
    public BandMembersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_band_member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BandMembersAdapter.ViewHolder holder, int position) {
        Log.d("BandMembersAdapter", "onBindViewHolder: " + dataList.get(position).getNickname());


        holder.memberNickname.setText(dataList.get(position).getNickname());
        // TODO: дописать установку картинки из БД
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(List<MemberDataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView memberImage;

        TextView memberNickname;
        CardView memberCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memberImage = itemView.findViewById(R.id.memberImage);
            memberNickname = itemView.findViewById(R.id.memberNickname);
            memberCard = itemView.findViewById(R.id.memberCard);

        }
    }
}
