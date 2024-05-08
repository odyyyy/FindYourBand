package com.example.findyourband;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findyourband.RSSItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findyourband.databinding.FragmentMainPageBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainPageFragment extends Fragment {
    private FragmentMainPageBinding binding;

    private RecyclerView newsRecyclerView;

    private NewsAdapter adapter;

    private ArrayList<RSSItem> newsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMainPageBinding.inflate(inflater, container, false);

        View view = binding.getRoot();


        newsRecyclerView = binding.newsRecyclerView;
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        new ProcessInBackground().execute();

        return view;
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {
        private Exception exception;
        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Загрузка");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try {

                // https://rockcult.ru/news/feed/
                // https://samesound.ru/feed

                URL url = new URL("https://samesound.ru/feed");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                Log.d("MainPageTest", url.getHost() + " " + url.getPath() + " " + url.getQuery() + " " + url.getRef());
                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;
                int eventType = xpp.getEventType();
                RSSItem item = null;

                int news_count = 0;


                // TODO: Поставить ограничение на количество новостей

                while (eventType != XmlPullParser.END_DOCUMENT && news_count < 10) {

                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;

                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                item = new RSSItem();

                                item.title = xpp.nextText();

                            }
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem) {
                                item.pubDate = xpp.nextText();
                            }
                        } else if (xpp.getName().equalsIgnoreCase("enclosure")) {
                            String imageUrl = null;

                            if (insideItem) {
                                try {
                                    imageUrl = xpp.getAttributeValue(null, "url");
                                } catch (Exception e) {
                                }

                                item.img = imageUrl;
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        newsList.add(item);
                        news_count++;
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }

            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            adapter = new NewsAdapter(getContext(), newsList);
            newsRecyclerView.setAdapter(adapter);

            progressDialog.dismiss();
        }
    }

}

class RSSItem {
    String title;
    String link;
    String pubDate;
    String img;

    public RSSItem() {
    }

}