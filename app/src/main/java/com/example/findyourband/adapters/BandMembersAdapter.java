package com.example.findyourband.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.R;
import com.example.findyourband.SearchBandMembersFragment;
import com.example.findyourband.services.MemberDataClass;


import java.util.List;

public class BandMembersAdapter extends RecyclerView.Adapter<BandMembersAdapter.ViewHolder> {

    private Context context;
    private List<MemberDataClass> dataList;

    private SearchBandMembersFragment fragment;

    private int selectedCount = 0;

    public BandMembersAdapter(Context ctx, List<MemberDataClass> bandMembersList, SearchBandMembersFragment fragment) {
        this.context = ctx;
        this.dataList = bandMembersList;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public BandMembersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_band_member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BandMembersAdapter.ViewHolder holder, int position) {
        MemberDataClass member = dataList.get(position);
        holder.memberNickname.setText(member.getNickname());

        holder.memberCard.setOnClickListener(new View.OnClickListener() {
            boolean isHighlighted = false;

            @Override
            public void onClick(View v) {


                // Изменение фона при клике
                if (!isHighlighted) {
                    if (getSelectedCount() >= 4) {
                        Toast.makeText(context, "Вы не можете добавить больше четырех участников", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    v.findViewById(R.id.groupMemberLayout).setBackgroundColor(context.getResources().getColor(R.color.card_selected_color));
                    isHighlighted = true;
                    selectedCount++;
                } else {
                    // Возврат к обычному фону при повторном клике
                    v.findViewById(R.id.groupMemberLayout).setBackgroundColor(context.getResources().getColor(R.color.card_member_not_selected));
                    isHighlighted = false;
                    selectedCount--;
                }
                fragment.updateSelectedCountText();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(List<MemberDataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();

    }

    public int getSelectedCount() {
        return selectedCount;
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
