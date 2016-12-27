package org.meruvian.workshop.content.database.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import org.meruvian.workshop.content.CatContentProvider;
import org.meruvian.workshop.content.NewsContentProvider;
import org.meruvian.workshop.content.database.model.CatDatabaseModel;
import org.meruvian.workshop.entity.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enrico_Didan on 27/12/2016.
 */

public class CatDatabaseAdapter {
    private Uri dbUriNews = Uri.parse(CatContentProvider.CONTENT_PATH
            + NewsContentProvider.TABLES[0]);
    private Context context;

    public CatDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void save(News news) {
        ContentValues values = new ContentValues();
        if (news.getId() == -1) {
            values.put(CatDatabaseModel.TITLE, news.getTitle());
            values.put(CatDatabaseModel.CONTENT, news.getContent());
            values.put(CatDatabaseModel.STATUS, 1);
            context.getContentResolver().insert(dbUriNews, values);
        } else {
            values.put(CatDatabaseModel.TITLE, news.getTitle());
            values.put(CatDatabaseModel.CONTENT, news.getContent());
            values.put(CatDatabaseModel.STATUS, 1);
            context.getContentResolver().update(dbUriNews, values, CatDatabaseModel.ID + " = ?", new
                    String[]{news.getId() + ""});
        }
    }

    public List<News> findNewsByTitle(String title) {
        String query = CatDatabaseModel.TITLE + " like ? AND " + CatDatabaseModel.STATUS + " = ?";
        String[] parameter = {"%" + title + "%", "1"};
        Cursor cursor = context.getContentResolver().query(dbUriNews, null, query, parameter,
                CatDatabaseModel.ID);
        List<News> categories = new ArrayList<News>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                News news = new News();
                news.setId(cursor.getInt(cursor.getColumnIndex(CatDatabaseModel.ID)));
                news.setTitle(cursor.getString(cursor.getColumnIndex(CatDatabaseModel.TITLE)));
                news.setContent(cursor.getString(cursor.getColumnIndex(CatDatabaseModel.CONTENT)));
                news.setStatus(cursor.getInt(cursor.getColumnIndex(CatDatabaseModel.STATUS)));
                categories.add(news);
            }
        }
        cursor.close();
        return categories;
    }

    public List<News> findNewsAll() {
        String query = CatDatabaseModel.STATUS + " = ?";
        String[] parameter = {"1"};
        Cursor cursor = context.getContentResolver().query(dbUriNews, null, query, parameter,
                CatDatabaseModel.ID);
        List<News> categories = new ArrayList<News>();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    News news = new News();
                    news.setId(cursor.getInt(cursor.getColumnIndex(CatDatabaseModel.ID)));
                    news.setTitle(cursor.getString(cursor.getColumnIndex(CatDatabaseModel.TITLE)));
                    news.setContent(cursor.getString(cursor.getColumnIndex(CatDatabaseModel.CONTENT)));
                    news.setStatus(cursor.getInt(cursor.getColumnIndex(CatDatabaseModel.STATUS)));
                    categories.add(news);
                }
            }
        }
        cursor.close();
        return categories;
    }

    public News findNewsById(long id) {
        String query = CatDatabaseModel.ID + " = ?";
        String[] parameter = {id + ""};
        Cursor cursor = context.getContentResolver().query(dbUriNews, null, query, parameter, null);
        News news = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                try {
                    cursor.moveToFirst();
                    news = new News();
                    news.setId(cursor.getInt(cursor.getColumnIndex(CatDatabaseModel.ID)));
                    news.setTitle(cursor.getString(cursor.getColumnIndex(CatDatabaseModel.TITLE)));
                    news.setContent(cursor.getString(cursor.getColumnIndex(CatDatabaseModel.CONTENT)));
                    news.setStatus(cursor.getInt(cursor.getColumnIndex(CatDatabaseModel.STATUS)));
                } catch (SQLException e) {
                    e.printStackTrace();
                    news = null;
                }
            }
        }
        cursor.close();
        return news;
    }

    public void delete(News news) {
        ContentValues values = new ContentValues();
        values.put(CatDatabaseModel.STATUS, 0);
        context.getContentResolver().update(dbUriNews, values, CatDatabaseModel.ID + " = ? ", new
                String[]{news.getId() + ""});
    }
}
