package com.example.findyourband.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import com.example.findyourband.R;
import com.example.findyourband.services.RSSItem;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private LayoutInflater layoutInflater;
    private List<RSSItem> newsList;

    public NewsAdapter(Context context, List<RSSItem> newsList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.newsList = newsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.component_news_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titleTextView.setText(newsList.get(position).getTitle());
        holder.dateTextView.setText(parseDate(newsList.get(position).getPubDate()));
        setNewsImageView(holder, newsList.get(position).getImg());

    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;

        TextView dateTextView;
        ImageView newsImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.newsTitleTextView);
            newsImage = itemView.findViewById(R.id.newsCardImage);
            dateTextView = itemView.findViewById(R.id.newsDateTextView);

        }
    }

    private String parseDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM yyyy года", new Locale("ru", "RU"));

        Date preparedDate = null;
        try {
            preparedDate = inputFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String parsedDate = outputFormat.format(preparedDate);
        return parsedDate;

    }

    private void setNewsImageView(ViewHolder holder, String imgURL) {

        if (imgURL != null) {
            imgURL = imgURL.replace("150x150", "750x375");
        }
        Picasso.get().load(imgURL).error(R.drawable.test_news_image).into(holder.newsImage);
    }



}
