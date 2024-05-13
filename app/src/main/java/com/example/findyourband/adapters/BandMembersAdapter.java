package com.example.findyourband.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.R;
import com.example.findyourband.SearchBandMembersFragment;
import com.example.findyourband.fragments.CreateBandFragment;
import com.example.findyourband.services.MemberDataClass;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BandMembersAdapter extends RecyclerView.Adapter<BandMembersAdapter.ViewHolder> {

    private Context context;
    private List<MemberDataClass> dataList;

    private Fragment fragment;

    private int selectedCount = 0;

    public BandMembersAdapter(Context ctx, List<MemberDataClass> bandMembersList, Fragment fragment) {
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

        // Обработчик нажатия отрабатывает только на странице поиска
        if (fragment instanceof SearchBandMembersFragment) {

            // Устанавливаем фон в соответствии с состоянием isSelected
            if (member.isSelected()) {
                holder.memberCard.findViewById(R.id.groupMemberLayout).setBackgroundColor(context.getResources().getColor(R.color.card_selected_color));
            } else {
                holder.memberCard.findViewById(R.id.groupMemberLayout).setBackgroundColor(context.getResources().getColor(R.color.card_member_not_selected));
            }
            holder.memberCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!member.isSelected()) {
                        if (getSelectedCount() >= 4) {
                            Toast.makeText(context, "Вы не можете добавить больше четырех участников", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    member.setSelected(!member.isSelected());
                    notifyItemChanged(position);

                    if (member.isSelected()) {

                        selectedCount++;
                    } else {
                        selectedCount--;
                    }
                    ((SearchBandMembersFragment) fragment).updateSelectedCountText();
                }
            });
        }
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

    public ArrayList<? extends Parcelable> getSelectedMembers() {
        List<MemberDataClass> selectedMembers = new ArrayList<>();
        for (MemberDataClass member : dataList) {
            if (member.isSelected()) {
                selectedMembers.add(member);
            }
        }

        return (ArrayList<? extends Parcelable>) selectedMembers;
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
