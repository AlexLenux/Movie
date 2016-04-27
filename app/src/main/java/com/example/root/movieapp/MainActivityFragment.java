package com.example.root.movieapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ImageAdapter mImageAdapter ;
    GridView gridView;
    int i = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gridView = (GridView) inflater.inflate(R.layout.fragment_main, container, false);


        return gridView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String sharedValue = pref.getString("sort_option", "popular_value");


            ArrayList<Movies> moviesArrayList = new ArrayList<Movies>();
            mImageAdapter = new ImageAdapter(getActivity());
            mImageAdapter.setMoviesArrayList(moviesArrayList);
                mAsyncTask task = new mAsyncTask();
            task.setSecond(false);
            task.setAdapter(mImageAdapter);
            task.setSharedValue(sharedValue);
            task.execute(1);

            gridView.setAdapter(mImageAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Movies movies = (Movies) mImageAdapter.getItem(position);

                    ((MainActivity) getActivity()).setSelectiedMovies(movies);
                    ((MainActivity) getActivity()).startTwoPane();
                }
            });

            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

                private int currentVisibleItemCount;
                private int currentScrollState;
                private int currentFirstVisibleItem;
                private int totalItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    this.currentScrollState = scrollState;
                    this.isScrollCompleted();
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    this.currentFirstVisibleItem = firstVisibleItem;
                    this.currentVisibleItemCount = visibleItemCount;
                    this.totalItem = totalItemCount;
                }

                private void isScrollCompleted() {
                    if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                            && this.currentScrollState == SCROLL_STATE_IDLE) {
                        /** To do code here*/

                        mAsyncTask task = new mAsyncTask();
                        task.setSecond(true);
                        task.setAdapter(mImageAdapter);
                        task.setSharedValue(sharedValue);
                        task.execute(i);
                        i++;
                    }
                }
            });


            }else {
            Toast.makeText(getContext(), "check your connection!", Toast.LENGTH_SHORT).show();

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
                movie.setDate(c.getString(date));
                Log.d("the url value of C", movie.getUrl());
                movies.add(movie);
            }while (c.moveToNext());

            if(movies.size() != 0) {
                final ImageAdapter imageAdapter = new ImageAdapter(getActivity());
                imageAdapter.setMoviesArrayList(movies);

                gridView.setAdapter(imageAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movies onemovie = (Movies) imageAdapter.getItem(position);

                        ((MainActivity) getActivity()).setSelectiedMovies(onemovie);
                        ((MainActivity) getActivity()).startTwoPane();
                    }
                });
            }else
                Toast.makeText(getActivity(),"Favorite is empty",Toast.LENGTH_SHORT).show();
        }
    }
}
