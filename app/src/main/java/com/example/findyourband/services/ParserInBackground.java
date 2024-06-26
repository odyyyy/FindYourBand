package com.example.findyourband.services;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParserInBackground extends AsyncTask<Integer, Void, Exception> {
    private Exception exception;
    ProgressDialog progressDialog;
    Context ctx;

    private ArrayList<RSSItem> newsList;

    private URL url;
    private XmlPullParser xpp;


    public ParserInBackground(Context context) {
        ctx = context;
        newsList = new ArrayList<>();
    }

    private InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Exception doInBackground(Integer... integers) {

        try {
            parseAllNews();
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        }




        return null;
    }

    private void prepareParser() throws XmlPullParserException, MalformedURLException {
        url = new URL("https://samesound.ru/feed");
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        xpp = factory.newPullParser();

        InputStream inputStream = getInputStream(url);
        if (inputStream == null) {
            throw new XmlPullParserException("Failed to get input stream from URL");
        }

        xpp.setInput(inputStream, "UTF_8");
    }


    private void parseAllNews() throws XmlPullParserException {

        try {

            // https://rockcult.ru/news/feed/

            prepareParser();
            boolean insideItem = false;
            int eventType = xpp.getEventType();
            RSSItem item = null;

            int news_count = 0;
            final int NEWS_LIMIT = 10; // Ограничение на количество новостей


            while (eventType != XmlPullParser.END_DOCUMENT && news_count < NEWS_LIMIT) {

                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;

                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem) {
                            item = new RSSItem();
                            item.setTitle(xpp.nextText());

                        }
                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                        if (insideItem) {
                            item.setPubDate(xpp.nextText());
                        }
                    } else if (xpp.getName().equalsIgnoreCase("enclosure")) {
                        String imageUrl = null;

                        if (insideItem) {
                            try {
                                imageUrl = xpp.getAttributeValue(null, "url");
                            } catch (Exception e) {
                            }
                            item.setImg(imageUrl);
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    newsList.add(item);
                    news_count++;
                    insideItem = false;
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException | IOException e) {
            exception = e;
        }

    }


    @Override
    protected void onPostExecute(Exception s) {
        super.onPostExecute(s);
        writeNewsListToJson(newsList);
    }

    private void writeNewsListToJson(ArrayList<RSSItem> newsList) {
        Gson gson = new Gson();
        Type newsListType = new TypeToken<ArrayList<RSSItem>>() {}.getType();
        String json = gson.toJson(newsList, newsListType);
        File cacheFile = new File(ctx.getFilesDir(), "cached_news.json");

        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void updateCachedNewsJSON() {
        Gson gson = new Gson();
        File cacheFile = new File(ctx.getFilesDir(), "cached_news.json");

        if (!cacheFile.exists()) {
            try {
                cacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Type newsListType = new TypeToken<ArrayList<RSSItem>>() {
        }.getType();
        ArrayList<RSSItem> cachedNewsList = new ArrayList<>();
        if (cacheFile.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cacheFile))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                cachedNewsList = gson.fromJson(jsonContent.toString(), newsListType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (RSSItem newItem : newsList) {
            boolean alreadyExists = false;
            for (RSSItem cachedItem : cachedNewsList) {
                if (newItem.getTitle().equals(cachedItem.getTitle())) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                cachedNewsList.add(newItem);
            }
        }

        try (FileWriter writer = new FileWriter(cacheFile)) {
            gson.toJson(cachedNewsList, newsListType, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isAnyLatestNews() {
        try {

            prepareParser();


            boolean insideItem = false;
            int eventType = xpp.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;

                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem) {
                            String title = xpp.nextText();
                            return !title.equals(getLatestCachedNews());
                        }
                    }
                }
                eventType = xpp.next();
            }
        } catch (IOException | XmlPullParserException ignored) {
        }
        return true;

    }

    private String getLatestCachedNews() {
        Gson gson = new Gson();
        File cacheFile = new File(ctx.getFilesDir(), "cached_news.json");

        if (!cacheFile.exists()) {
            return null;
        }

        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(cacheFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Type newsListType = new TypeToken<ArrayList<RSSItem>>() {
        }.getType();
        List<RSSItem> cachedNewsList = gson.fromJson(jsonContent.toString(), newsListType);

        if (!cachedNewsList.isEmpty()) {
            Collections.sort(cachedNewsList, new Comparator<RSSItem>() {
                @Override
                public int compare(RSSItem item1, RSSItem item2) {
                    return item2.getPubDate().compareTo(item1.getPubDate());
                }
            });
            return cachedNewsList.get(0).getTitle();
        } else {
            return null;
        }
    }
}