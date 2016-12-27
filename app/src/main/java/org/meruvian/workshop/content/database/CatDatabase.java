package org.meruvian.workshop.content.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.meruvian.workshop.content.database.model.CatDatabaseModel;

/**
 * Created by Enrico_Didan on 27/12/2016.
 */

public class CatDatabase extends SQLiteOpenHelper {
    public static final String DATABASE = "sample_news";
    public static final String NEWS_TABLE = "tbl_news";
    private static final int VERSION = 1;
    private Context context;

    public CatDatabase(Context context) {
        super(context, DATABASE, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NEWS_TABLE + "("
                + CatDatabaseModel.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + CatDatabaseModel.TITLE + " TEXT, "
                + CatDatabaseModel.CONTENT + " TEXT, "
                + CatDatabaseModel.STATUS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
        }
    }
}
