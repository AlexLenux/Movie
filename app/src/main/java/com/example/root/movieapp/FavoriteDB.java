package com.example.root.movieapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 4/16/16.
 */
public class FavoriteDB extends SQLiteOpenHelper {

    static final String DB_NAME = "favorite.db";
    private static final int DB_VERSION = 1 ;

    public FavoriteDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE FAVORITE (id INTEGER PRIMARY KEY, movie_name TEXT, movie_poster_url TEXT,movie_poster BLOB , movie_id TEXT, movie_overView TEXT , movie_rate TEXT , movie_backDrop_url TEXT , movie_backDrop BLOB , movie_trailer TEXT , movie_review TEXT, author TEXT, date TEXT);";


        db.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + "FAVORITE");

        onCreate(db);
    }
}
