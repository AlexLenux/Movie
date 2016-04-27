package com.example.root.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * A placeholder fragment containing a simple view.
 */
public class Search_ActivityFragment extends Fragment {

    String value = "" ;
    ArrayList<Movies> moviesList;
    ImageAdapter adapter;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_search_, container, false);

        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {

            final EditText editText = (EditText) view.findViewById(R.id.editText);
            Button button = (Button) view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value = editText.getText().toString();

                    if (value.length() != 0) {
                        moviesList = new ArrayList<Movies>();
                        adapter = new ImageAdapter(getActivity());
                        adapter.setMoviesArrayList(moviesList);
                        SearchTask searchTask = new SearchTask();
                        searchTask.execute(value);
                        GridView gridView = (GridView) view.findViewById(R.id.search_gridView);

                        gridView.setAdapter(adapter);


                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Movies movies = (Movies) adapter.getItem(position);
                                ((Search_Activity) getActivity()).setSelectiedMovies(movies);
                                ((Search_Activity) getActivity()).startTwoPane();
                            }
                        });
                    }

                }
            });
        }else
          Toast.makeText(getActivity(),"check your connection",Toast.LENGTH_SHORT).show();

        return view;
    }

    public class SearchTask extends AsyncTask<String,Void,ArrayList<Movies>>{

        final  String TAG = SearchTask.class.getSimpleName();
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieStringJSON = null;
        Movies movies ;

        @Override
        protected ArrayList<Movies> doInBackground(String... params) {


            try {
                String urlString = "https://api.themoviedb.org/3/search/movie?query="+params[0]+"&api_key=" + BuildConfig.MOVIE_API_KEY;
                URL url = new URL(urlString.replace(" " ,"%20"));


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

            if(movies.size() != 0) {
                adapter.setMoviesArrayList(movies);
                adapter.notifyDataSetChanged();
            }else
                Toast.makeText(getActivity(),"NO RESALT !!",Toast.LENGTH_SHORT).show();
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

    public void setValue(String value) {
        this.value = value;
    }
}
