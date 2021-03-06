package org.meruvian.workshop.content;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.meruvian.workshop.content.database.NewsDatabase;

/**
 * Created by Enrico_Didan on 24/12/2016.
 */

public class NewsContentProvider extends android.content.ContentProvider
{
    private NewsDatabase dbHelper;

    public static final String TABLES[] =
            {
                NewsDatabase.NEWS_TABLE,
            };

    public static final String AUTHORITY = NewsContentProvider.class.getName().toLowerCase();
    public static final String CONTENT_PATH = "content://" + AUTHORITY +"/";
    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH)
    {
        {
            int i = 0;
            for(String table : TABLES)
            {
                addURI(AUTHORITY, table, i);
            }
        }
    };

    @Override
    public boolean onCreate() {
        dbHelper = new NewsDatabase(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        builder.setTables(TABLES[matcher.match(uri)]);
        Cursor cursor = builder.query(database,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(TABLES[matcher.match(uri)], null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TABLES[matcher.match(uri)] + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int deleted = database.delete(TABLES[matcher.match(uri)], selection,
                selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int updated = database.update(TABLES[matcher.match(uri)], values,
                selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updated;

    }
}
