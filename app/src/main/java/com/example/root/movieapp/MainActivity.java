package com.example.root.movieapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Movies selectiedMovies = new Movies();
    boolean twoPane = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(findViewById(R.id.fragment2) != null) {
            if (savedInstanceState == null) {

                twoPane = true;
            }
        }else {
            twoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable())
            getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatemen
          if(id == R.id.action_settings){
              Intent intent = new Intent(this, Setting.class);
              startActivity(intent);
              return true;
          } else if(id == R.id.action_favorie){

                  Intent intent = new Intent(this, Favorite_Activity.class);
                  startActivity(intent);

          }else{
              Intent intent = new Intent(this, Search_Activity.class);
              startActivity(intent);
          }

        return super.onOptionsItemSelected(item);
    }

    public void setSelectiedMovies(Movies selectiedMovies) {
        this.selectiedMovies = selectiedMovies;
        Log.d("MOVIE DATA",selectiedMovies.getTitle());
    }

    public void startTwoPane(){
        if(twoPane == true){

            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setMovies(selectiedMovies);
            Log.e("MOVIE DATA", selectiedMovies.getTitle());
            detailsFragment.setPhone_flag(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, detailsFragment).commit();
        }else {
            Log.e("one pane data",selectiedMovies.getTitle());
            Intent intent = new Intent(this, Details.class)
                    .putExtra("Movies", selectiedMovies);
            startActivity(intent);
        }
    }
}
