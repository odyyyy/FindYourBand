package com.example.findyourband;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VacanciesAdapter extends RecyclerView.Adapter<VacanciesAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Map<String, ArrayList<String>>> vacanciesList;

    VacanciesAdapter(Context context, List<Map<String, ArrayList<String>>> vacanciesList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.vacanciesList = vacanciesList;
    }


    @NonNull
    @Override
    public VacanciesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.componet_vacancy_item, parent, false);

        return new VacanciesAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VacanciesAdapter.ViewHolder holder, int position) {
        String name = vacanciesList.get(position).get("name").get(0);
        String city =  vacanciesList.get(position).get("city").get(0);
        ArrayList<String> instruments =  vacanciesList.get(position).get("instruments");
        ArrayList<String> genres =  vacanciesList.get(position).get("genres");

        LinearLayout instrumentsLayout = holder.itemView.findViewById(R.id.instrumentsVacancyLayout);
        LinearLayout genresLayout = holder.itemView.findViewById(R.id.genreVacancyLayout);




        // TODO:дописать установку параметров для инструментов и жанров
        holder.name.setText(name);
        holder.city.setText(city);
        holder.image.setImageResource(R.drawable.paul);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            private Fragment fragment;

            @Override
            public void onClick(View v) {

                String vacancy_title = v.findViewById(R.id.nameVacancyTextView).toString();
                if (vacancy_title.contains("Группа")) {
                    fragment = new GroupPageFragment();
                }
                else{
                    fragment = new MusicianPageFragment();
                }


                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_app, fragment, "musicianPageFragment").addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vacanciesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView city;
        ImageView instruments; // TODO: Инструментов может быть от 1 до 3, позже переделать в список или др.
        TextView genres; // TODO: Аналогично

        ImageView image;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameVacancyTextView);
            image = itemView.findViewById(R.id.vacancyCardImage);
            city = itemView.findViewById(R.id.cityVacancyTextView);
            // TODO: дописать для genre и instrument


            cardView = itemView.findViewById(R.id.vacancyCardView);
        }
    }

}
