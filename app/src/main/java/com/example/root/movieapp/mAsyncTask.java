package com.example.root.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * Created by root on 3/25/16.
 */
public class mAsyncTask extends AsyncTask<Integer , Void, ArrayList<Movies>>{

    final static String TAG = mAsyncTask.class.getSimpleName();
    HttpsURLConnection urlConnection = null;
    BufferedReader reader = null;
    String movieStringJSON = null;
    Movies movies ;
    ArrayList<Movies> moviesList ;
    ImageAdapter adapter;
    String sharedValue ;
    boolean second;

    public mAsyncTask(){
        moviesList = new ArrayList<Movies>();
    }

    public void setAdapter(ImageAdapter adapter) {
        this.adapter = adapter;
    }

    public void setSharedValue(String sharedValue) {
        this.sharedValue = sharedValue;
    }

    public void setSecond(boolean second) {
        this.second = second;
    }

    @Override
    protected ArrayList<Movies> doInBackground(Integer... params) {

        try {
            URL url;

            if(sharedValue.equals("Now_Playing"))
                url = new URL("https://api.themoviedb.org/3/movie/now_playing?page="+params[0]+"&api_key=" + BuildConfig.MOVIE_API_KEY);
            else if(sharedValue.equals("Top_Rate"))
                url = new URL("https://api.themoviedb.org/3/movie/top_rated?page="+params[0]+"&api_key=" + BuildConfig.MOVIE_API_KEY);
            else if(sharedValue.equals("Upcoming"))
                url = new URL("https://api.themoviedb.org/3/movie/upcoming?page="+params[0]+"&api_key=" + BuildConfig.MOVIE_API_KEY);
            else
                url = new URL("https://api.themoviedb.org/3/movie/popular?page="+params[0]+"&api_key=" + BuildConfig.MOVIE_API_KEY);

            urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null) {
               Log.e(TAG,"Error");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line ;

            while((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
              Log.d(TAG,"error buffer == 0");
                return null;
            }
            movieStringJSON = buffer.toString();
            Log.v(TAG,"Movie URL String" + movieStringJSON);

        }catch (IOException e){
            Log.d(TAG, "there exist problem !!", e);
            e.printStackTrace();
            return null;
        }finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing Stream", e);
                }
            }
        }
          try{
              return urlFromJSON(movieStringJSON);
          }catch (final JSONException e){
              Log.d(TAG,e.getMessage(),e);
              e.printStackTrace();
          }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movies> movies) {
        super.onPostExecute(movies);
       if(second == false)
           adapter.setMoviesArrayList(movies);
        if(second == true)
           adapter.appandArrayList(movies);

        adapter.notifyDataSetChanged();

        }

    private ArrayList<Movies> urlFromJSON(String movieStrinJSON)
             throws JSONException {

        JSONObject movieJSON = new JSONObject(movieStringJSON);
        JSONArray movieArray = movieJSON.getJSONArray("results");
        String baseImageUrl = "http://image.tmdb.org/t/p/w185";
        for(int i = 0; i < movieArray.length(); i++){
            JSONObject oneMovie = movieArray.getJSONObject(i);

            movies =new Movies();

            movies.setUrl(baseImageUrl + oneMovie.getString("poster_path"));
            movies.setTitle(oneMovie.getString("title"));
            movies.setOverView(oneMovie.getString("overview"));
            movies.setVote_average(oneMovie.getString("vote_average"));
            movies.setBackdrop_url(baseImageUrl + oneMovie.getString("backdrop_path"));
            movies.setId(oneMovie.getString("id"));
            movies.setDate(oneMovie.getString("release_date"));
            Log.d(TAG, "the path " + movies.getUrl());
            moviesList.add(movies);
        }
       return moviesList;
    }
}

