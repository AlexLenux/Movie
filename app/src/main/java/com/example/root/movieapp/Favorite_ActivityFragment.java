package com.example.root.movieapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class Favorite_ActivityFragment extends Fragment {


    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gridView = (GridView)inflater.inflate(R.layout.fragment_favorite_, container, false);



                return gridView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayList<Movies> movies = new ArrayList<Movies>();
        FavoriteDB mDBHelper = new FavoriteDB(getActivity());
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        int id_url;
        int id_name;
        int id_overView;
        int id_movieId;
        int id_Rate;
        int id_backDrop;
        int id_poster;
        int id_backDrop_url;
        int id_trailer;
        int id_review;
        int id_author;
        int date;

        String[] projection = {
                "movie_name",
                "movie_poster_url",
                "movie_poster",
                "movie_id",
                "movie_overView",
                "movie_rate",
                "movie_backDrop_url",
                "movie_backDrop",
                "movie_trailer",
                "movie_review",
                "author",
                "date"
        };

        Cursor c = db.query(
                "FAVORITE",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        c.moveToFirst();
       if(c.getCount() > 0)
        do {
            Movies movie = new Movies();

            id_url = c.getColumnIndex("movie_poster_url");
            id_poster = c.getColumnIndex("movie_poster");
            id_name = c.getColumnIndex("movie_name");
            id_overView = c.getColumnIndex("movie_overView");
            id_movieId = c.getColumnIndex("movie_id");
            id_Rate = c.getColumnIndex("movie_rate");
            id_backDrop_url = c.getColumnIndex("movie_backDrop_url");
            id_backDrop = c.getColumnIndex("movie_backDrop");
            id_trailer = c.getColumnIndex("movie_trailer");
            id_review = c.getColumnIndex("movie_review");
            id_author = c.getColumnIndex("author");
            date = c.getColumnIndex("date");

            movie.setUrl(c.getString(id_url));
            movie.setPoster(c.getBlob(id_poster));
            movie.setTitle(c.getString(id_name));
            movie.setDate(c.getString(date));
            movie.setOverView(c.getString(id_overView));
            movie.setId(c.getString(id_movieId));
            movie.setVote_average(c.getString(id_Rate));
            movie.setBackdrop_url(c.getString(id_backDrop_url));
            movie.setBackDrop(c.getBlob(id_backDrop));
            String[] trailer = {c.getString(id_trailer)};
            movie.setTrailer(trailer);
            String[] review = {c.getString(id_review)};
            movie.setReviews(review);
            String[] author = {c.getString(id_author)};
            movie.setAuthor(author);

            Log.d("the url value of C", movie.getUrl());
            movies.add(movie);
        } while (c.moveToNext());

        if(movies.size() != 0) {
            final ImageAdapter imageAdapter = new ImageAdapter(getActivity());
            imageAdapter.setMoviesArrayList(movies);

            gridView.setAdapter(imageAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movies onemovie = (Movies) imageAdapter.getItem(position);

                    ((Favorite_Activity) getActivity()).setMoviesSelected(onemovie);
                    ((Favorite_Activity) getActivity()).startTwoPane();
                }
            });
        }else
            Toast.makeText(getActivity(),"Favorite is empty",Toast.LENGTH_SHORT).show();
    }

}
