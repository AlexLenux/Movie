package com.example.root.movieapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    LinearLayout linearLayout_trailer;
    LinearLayout linearLayout_reviews;
    ImageButton imageButton;
    Movies movies;
    long newRowId;
    Bitmap backDropImage;
    Bitmap posterImage;
    boolean favorite_flag = false;
    public static boolean phone_flag = true;
    String id = null ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

          if(phone_flag == true) {
              Intent intent = getActivity().getIntent();
              movies = (Movies) intent.getSerializableExtra("Movies");
          }

        final ImageView imageView =(ImageView) view.findViewById(R.id.imageView);
        ImageView share = (ImageView) view.findViewById(R.id.share);
        TextView textView_title = (TextView) view.findViewById(R.id.textView_title);
        TextView textView_overview = (TextView) view.findViewById(R.id.textview_overview);
        TextView textView_vote_average = (TextView) view.findViewById(R.id.textView_vote_average);
        TextView textView_date = (TextView) view.findViewById(R.id.textView_Date);
        linearLayout_trailer = (LinearLayout) view.findViewById(R.id.linearLayout_trailer);
        linearLayout_reviews = (LinearLayout) view.findViewById(R.id.linearLayout_reviews);
        imageButton = (ImageButton) view.findViewById(R.id.star);

        textView_title.setText(movies.getTitle());
        textView_overview.setText(movies.getOverView());
        textView_vote_average.setText(movies.getVote_average());
        textView_date.setText(movies.getDate());



        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
            Picasso.with(getActivity()).load(movies.getUrl()).into(new Target() {
               @Override
               public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                   posterImage = bitmap;
               }
               @Override
               public void onBitmapFailed(Drawable errorDrawable) {
               }
               @Override
               public void onPrepareLoad(Drawable placeHolderDrawable) {
               }
           });
           Picasso.with(getActivity()).load(movies.getBackdrop_url()).into(new Target() {
               @Override
               public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                   backDropImage = bitmap;
                   if(backDropImage != null)
                       imageView.setImageBitmap(bitmap);
                   else
                       imageView.setImageBitmap(posterImage);
               }
               @Override
               public void onBitmapFailed(Drawable errorDrawable) {
               }
               @Override
               public void onPrepareLoad(Drawable placeHolderDrawable) {
               }
           });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String[] shareList = movies.getTrailer();
                    if (shareList.length != 0) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, shareList[0]);
                        startActivity(share);
                    } else
                        Toast.makeText(getActivity(), "no trailer to share", Toast.LENGTH_SHORT).show();
                }
            });

            //trailer task
            trailerTask ttask = new trailerTask();
            ttask.execute();

            //reviews task
            reviewsTask rtask = new reviewsTask();
            rtask.execute();
       }else
       {
           if(movies.getBackDrop() != null)
                imageView.setImageBitmap(getImage(movies.getBackDrop()));
           else
                 imageView.setImageBitmap(getImage(movies.getPoster()));

           share.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   String[] shareList = movies.getTrailer();
                   if(shareList[0] != null) {
                       Intent share = new Intent(Intent.ACTION_SEND);
                       share.setType("text/plain");
                       share.putExtra(Intent.EXTRA_TEXT, shareList[0]);
                       startActivity(share);
                   }else
                       Toast.makeText(getActivity(),"no trailer to share",Toast.LENGTH_SHORT).show();
               }
           });
           final String[] trailer =movies.getTrailer();
           if(trailer[0] != null){
               for(int i = 0; i < trailer.length ; i++) {
                   TextView textView = new TextView(getActivity());
                   textView.setLayoutParams(new ViewGroup.LayoutParams(
                           ViewGroup.LayoutParams.WRAP_CONTENT,
                           ViewGroup.LayoutParams.WRAP_CONTENT));
                   linearLayout_trailer.addView(textView);
                   textView.setTextColor(Color.BLACK);
                   textView.setPadding(20, 20, 20, 20);
                   textView.setBottom(5);
                   textView.setText("Trailer " + (i+1));
                   final int  x = i;
                   textView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           Intent intent = new Intent(Intent.ACTION_VIEW);
                           intent.setData(Uri.parse(trailer[x]));
                           startActivity(intent);
                       }
                   });
               }
           }

            String[] review = movies.getReviews();
           if(review != null) {
               final String[] reviews = movies.getReviews();
               final String[] author = movies.getAuthor();

               for (int i = 0; i < review.length; i++) {

                   TextView textView = new TextView(getActivity());
                   textView.setLayoutParams(new ViewGroup.LayoutParams(
                           ViewGroup.LayoutParams.WRAP_CONTENT,
                           ViewGroup.LayoutParams.WRAP_CONTENT));
                   linearLayout_reviews.addView(textView);
                   textView.setTextColor(Color.BLACK);
                   textView.setPadding(20, 20, 20, 20);
                   textView.setBottom(5);
                   textView.setText(author[i]);
                   final int x = i;
                   textView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           Intent intent = new Intent(getActivity(), Reviews.class);
                           intent.putExtra("REVIEW", reviews[x]);
                           startActivity(intent);
                       }
                   });
               }
           }

       }



        //FAVORITE TASK

        final Drawable value1 = getResources().getDrawable(R.drawable.ic_favorite_24dp);
        Drawable value2 = getResources().getDrawable(R.drawable.ic_favorite_outline_24dp);




        FavoriteDB mDBHelper1 = new FavoriteDB(getActivity());
        SQLiteDatabase db1 = mDBHelper1.getReadableDatabase();

        String Query = "Select * from FAVORITE where movie_id = " + movies.getId();
        Cursor cursor = db1.rawQuery(Query, null);
        if(cursor.getCount() > 0){
            favorite_flag = true;
            imageButton.setImageDrawable(value1);
        } else
            imageButton.setImageDrawable(value2);

        imageButton.setOnClickListener(new View.OnClickListener() {

            Drawable replace = getResources().getDrawable(R.drawable.ic_favorite_outline_24dp);
            Drawable replace2 = getResources().getDrawable(R.drawable.ic_favorite_24dp);

            @Override
            public void onClick(View v) {

                FavoriteDB mDBHelper = new FavoriteDB(getContext());
                SQLiteDatabase db = mDBHelper.getWritableDatabase();


                if (favorite_flag == true) {
                    ((ImageButton) v).setImageDrawable(replace);

                    String[] args = {movies.getTitle()};
                    db.delete("FAVORITE", "movie_name=?", args);
                    favorite_flag = false;
                } else {
                    ((ImageButton) v).setImageDrawable(replace2);


                    ContentValues values = new ContentValues();
                    values.put("movie_name", movies.getTitle());
                    values.put("movie_poster_url", movies.getUrl());
                    if (posterImage != null)
                        values.put("movie_poster", getBytes(posterImage));
                    values.put("movie_id", movies.getId());
                    values.put("movie_overView", movies.getOverView());
                    values.put("movie_rate", movies.getVote_average());
                    values.put("date",movies.getDate());
                    values.put("movie_backDrop_url", movies.getBackdrop_url());
                    if (backDropImage != null) {
                        values.put("movie_backDrop", getBytes(backDropImage));
                    }
                    if (movies.getTrailer().length != 0) {
                        values.put("movie_trailer", movies.getTrailer()[0]);
                    }
                    if(movies.getAuthor() !=null) {
                        values.put("movie_review", movies.getReviews()[0]);
                        values.put("author", movies.getAuthor()[0]);
                    }
                     newRowId=db.insert("FAVORITE",null,values);
                    favorite_flag = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //favorite button

    }


    public class trailerTask extends AsyncTask<Void,Void,String[]>

    {

        BufferedReader reader = null;
        HttpsURLConnection urlConnection = null;
        String trailerStringJson = null;


        @Override
        protected String[] doInBackground(Void... params) {

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + movies.getId() + "/videos" + "?api_key=" + BuildConfig.MOVIE_API_KEY);

                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    Log.d("inputStream", "ERROR");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Log.d("buffer", "Proplem");
                    return null;
                }

                trailerStringJson = buffer.toString();
                Log.d("TRAILER JSON STRIN", trailerStringJson);
            } catch (IOException e) {
                Log.d("IOException", "there exist problem !!", e);
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("IOExcepion of reader", "Error closing Stream", e);
                    }
                }
            }
              try {

                  return trailerParsing();
              }catch (final JSONException e){
                  e.printStackTrace();
              }
            return null;
        }

        @Override
        protected void onPostExecute(final String[] strings) {
            super.onPostExecute(strings);

            if(strings != null){
                for(int i = 0; i < strings.length ; i++) {

                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    linearLayout_trailer.addView(textView);
                    textView.setTextColor(Color.BLACK);
                    textView.setPadding(20, 20, 20, 20);
                    textView.setBottom(5);
                    textView.setText("Trailer " + (i+1));
                    final int  x = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(strings[x]));
                            startActivity(intent);
                        }
                    });
                }

            }
        }

        public String[] trailerParsing()
                throws JSONException {
            JSONObject parsing = new JSONObject(trailerStringJson);
            JSONArray trailerArray = parsing.getJSONArray("results");
            String basicURL = "https://www.youtube.com/watch?v=";
            String[] trailer = new String[trailerArray.length()];

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject oneTrailer = trailerArray.getJSONObject(i);
                trailer[i] = basicURL + oneTrailer.getString("key");
            }

            movies.setTrailer(trailer);
         return trailer;
        }
    }


    public class reviewsTask extends AsyncTask<Void,Void,String[]>

    {

        BufferedReader reader = null;
        HttpsURLConnection urlConnection = null;
        String reviewsStringJson = null;


        @Override
        protected String[] doInBackground(Void... params) {

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + movies.getId() + "/reviews" + "?api_key=" + BuildConfig.MOVIE_API_KEY);

                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    Log.d("inputStream", "ERROR");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Log.d("buffer", "Proplem");
                    return null;
                }

                reviewsStringJson = buffer.toString();
                Log.d("REVIEWS JSON STRIN", reviewsStringJson);
            } catch (IOException e) {
                Log.d("IOException", "there exist problem !!", e);
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("IOExcepion of reader", "Error closing Stream", e);
                    }
                }
            }
            try {

                return reviewsParsing();
            }catch (final JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String[] strings) {
            super.onPostExecute(strings);

            if(strings != null) {
                final String[] reviews = movies.getReviews();
                for (int i = 0; i < strings.length; i++) {

                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    linearLayout_reviews.addView(textView);
                    textView.setTextColor(Color.BLACK);
                    textView.setPadding(20, 20, 20, 20);
                    textView.setBottom(5);
                    textView.setText(strings[i]);
                    final int x = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getActivity(), Reviews.class);
                            intent.putExtra("REVIEW", reviews[x]);
                            startActivity(intent);
                        }
                    });
                }
            }

        }

        public String[] reviewsParsing()
            throws JSONException{

            JSONObject parsing = new JSONObject(reviewsStringJson);
            JSONArray reviewsArray = parsing.getJSONArray("results");
            String[] reviews = new String[reviewsArray.length()];
            String[] author = new String[reviewsArray.length()];
            if(reviewsArray.length() != 0) {

                for (int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject oneTrailer = reviewsArray.getJSONObject(i);
                    reviews[i] = oneTrailer.getString("content");
                    author[i] = oneTrailer.getString("author");
                }
                Log.d("REVIEWS", reviews[0]);
                movies.setReviews(reviews);
                movies.setAuthor(author);

                return author;
            }
            return null;
        }
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }

    public void setPhone_flag(boolean phone_flag) {
        this.phone_flag = phone_flag;
    }
}

