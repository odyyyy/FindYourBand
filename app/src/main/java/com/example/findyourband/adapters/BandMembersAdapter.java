package com.example.findyourband.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.AppActivity;
import com.example.findyourband.R;
import com.example.findyourband.fragments.settings.SearchBandMembersFragment;
import com.example.findyourband.services.FirebaseQueriesServices;
import com.example.findyourband.services.INSTRUMENT;
import com.example.findyourband.services.MemberDataClass;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public void onBindViewHolder(@NonNull BandMembersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MemberDataClass member = dataList.get(position);

        holder.memberNickname.setText(member.getNickname());
        if (member.getImage() != "") {
            // TODO: вставить картинку пользователя glide
            holder.memberImage.setImageResource(R.drawable.logo);
        }
        else
            holder.memberImage.setImageResource(R.drawable.user_default_avatar);


        holder.bandLeaderImageView.setVisibility(View.GONE);
        if (member.isLeader()){
            holder.bandLeaderImageView.setVisibility(View.VISIBLE);
        }

        holder.instrumentsLayout.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
        layoutParams.setMarginStart(20);
        for (String instrument : member.getInstruments()) {
            ImageView instrumentView = new ImageView(context);
            instrumentView.setLayoutParams(layoutParams);
            Drawable drawable = context.getResources().getDrawable(INSTRUMENT.valueOf(instrument).getImageId()).mutate();
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.parseColor("#DFDFDF"));

            instrumentView.setImageDrawable(drawable);


            holder.instrumentsLayout.addView(instrumentView);
        }


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
        else{
            // Добавление открытия профиля музыканта на странице объявления группы при нажатии на участника
            holder.memberCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle userDataBundle = new Bundle();
                    FirebaseQueriesServices.getUserIdByLogin(member.getNickname(), new FirebaseQueriesServices.UserIdGetterCallback() {
                        @Override
                        public void onGetIdCompleted(String ID) {
                            userDataBundle.putString("UserID", ID);
                            AppActivity.navController.navigate(R.id.action_bandPageFragment_to_userProfileFragment, userDataBundle);
                        }
                    });
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

    public void sortDataList(Comparator<MemberDataClass> comparator) {
        Collections.sort(dataList, comparator);
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
        ImageView bandLeaderImageView;
        CardView memberCard;

        LinearLayout instrumentsLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memberImage = itemView.findViewById(R.id.memberImage);
            memberNickname = itemView.findViewById(R.id.memberNickname);
            memberCard = itemView.findViewById(R.id.memberCard);
            instrumentsLayout = itemView.findViewById(R.id.memberCardInstrumentsLayout);
            bandLeaderImageView = itemView.findViewById(R.id.bandLeaderImageView);

        }
    }
}
