package com.example.root.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

public class Favorite_Activity extends AppCompatActivity {

    Movies moviesSelected = new Movies();
    boolean twoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.favorite_fragment2) != null)
            twoPane = true;

    }

    public void setMoviesSelected(Movies moviesSelected) {
        this.moviesSelected = moviesSelected;
    }

    public void startTwoPane() {
        if (twoPane == true) {

            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setMovies(moviesSelected);
            Log.e("MOVIE DATA", moviesSelected.getTitle());
            detailsFragment.setPhone_flag(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.favorite_fragment2, detailsFragment).commit();
        } else {
            Log.e("one pane data", moviesSelected.getTitle());
            Intent intent = new Intent(this, Details.class)
                    .putExtra("Movies", moviesSelected);
            startActivity(intent);
        }
    }

}
