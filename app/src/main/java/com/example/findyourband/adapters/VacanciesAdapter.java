package com.example.findyourband.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.R;
import com.example.findyourband.fragments.vacancies.BandPageFragment;
import com.example.findyourband.fragments.vacancies.MusicianPageFragment;
import com.example.findyourband.services.INSTRUMENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VacanciesAdapter extends RecyclerView.Adapter<VacanciesAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Map<String, ArrayList<String>>> vacanciesList;


    public VacanciesAdapter(Context context, List<Map<String, ArrayList<String>>> vacanciesList) {
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
    public void onBindViewHolder(@NonNull VacanciesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = vacanciesList.get(position).get("name").get(0);
        String image = vacanciesList.get(position).get("image").get(0);
        String city = vacanciesList.get(position).get("city").get(0);

        ArrayList<String> instruments = vacanciesList.get(position).get("instruments");
        ArrayList<String> genres = vacanciesList.get(position).get("genres");

        LinearLayout instrumentsLayout = holder.itemView.findViewById(R.id.instrumentsVacancyLayout);
        LinearLayout genresLayout = holder.itemView.findViewById(R.id.genreVacancyLayout);

        instrumentsLayout.removeAllViews();
        genresLayout.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
        layoutParams.setMarginStart(10);

        for (String instrument : instruments) {
            ImageView instrumentImageView = new ImageView(holder.itemView.getContext());
            instrumentImageView.setLayoutParams(layoutParams);
            instrumentImageView.setImageResource(INSTRUMENT.valueOf(instrument).getImageId());
            instrumentsLayout.addView(instrumentImageView);
        }

        ContextThemeWrapper newContext = new ContextThemeWrapper(holder.itemView.getContext(), R.style.genreTextViewStyle);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginEnd(18);

        for (String genre : genres) {
            TextView genreTextView = new TextView(newContext);
            genreTextView.setText(genre);
            genreTextView.setLayoutParams(params);
            genresLayout.addView(genreTextView);
        }

        holder.name.setText(name);
        holder.city.setText(city);

        // TODO: загрузка картинки image
        holder.image.setImageResource(R.drawable.paul);


        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView vacancyTextView = ((TextView) v.findViewById(R.id.nameVacancyTextView));
                String vacancy_title = vacancyTextView.getText().toString();

                String description = vacanciesList.get(position).get("description").get(0);
                ArrayList<String> tracks = vacanciesList.get(position).get("tracks");
                ArrayList<String> contacts = vacanciesList.get(position).get("contacts");

                Bundle vacancyData = new Bundle();
                vacancyData.putString("name", name);
                vacancyData.putString("image", image);
                vacancyData.putString( "city", city);
                vacancyData.putStringArrayList("instruments", instruments);
                vacancyData.putStringArrayList("genres", genres);
                vacancyData.putString("description", description);
                vacancyData.putStringArrayList("tracks", tracks);
                vacancyData.putStringArrayList("contacts", contacts);


                Fragment fragment;
                if (vacancy_title.contains("Группа")) {
                    fragment = new BandPageFragment();
                    vacancyData.putStringArrayList("members", vacanciesList.get(position).get("members"));
                } else {
                    fragment = new MusicianPageFragment();
                    vacancyData.putString("experience", vacanciesList.get(position).get("experience").get(0));
                }

                fragment.setArguments(vacancyData);
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.app_fragment_container, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vacanciesList.size();
    }


    private Map<String, ArrayList<String>> createVacancy(String nickname, String city, List<String> instruments, List<String> genres) {
        Map<String, ArrayList<String>> vacancy = new HashMap<>();
        vacancy.put("name", new ArrayList<>(Arrays.asList(nickname)));
        vacancy.put("city", new ArrayList<>(Arrays.asList(city)));
        vacancy.put("instruments", new ArrayList<>(instruments));
        vacancy.put("genres", new ArrayList<>(genres));
        return vacancy;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView city;

        ImageView image;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameVacancyTextView);
            image = itemView.findViewById(R.id.vacancyCardImage);
            city = itemView.findViewById(R.id.cityVacancyTextView);

            cardView = itemView.findViewById(R.id.vacancyCardView);
        }
    }

}
