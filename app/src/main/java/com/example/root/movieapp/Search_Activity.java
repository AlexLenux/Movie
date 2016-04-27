package com.example.root.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class Search_Activity extends AppCompatActivity {

    Movies selectiedMovies = new Movies();
    boolean twoPane = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(findViewById(R.id.search_fragment2) != null)
            twoPane = true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_, menu);

        return true;
    }

    public void setSelectiedMovies(Movies selectiedMovies) {
        this.selectiedMovies = selectiedMovies;
    }

    public void startTwoPane(){
        if(twoPane == true){

            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setMovies(selectiedMovies);
            Log.e("MOVIE DATA", selectiedMovies.getTitle());
            detailsFragment.setPhone_flag(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment2, detailsFragment).commit();
        }else {
            Log.e("one pane data",selectiedMovies.getTitle());
            Intent intent = new Intent(this, Details.class)
                    .putExtra("Movies", selectiedMovies);
            startActivity(intent);
        }
    }
}
