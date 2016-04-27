package com.example.root.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Reviews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = this.getIntent();
        String data = intent.getStringExtra("REVIEW");
        TextView textView = (TextView) findViewById(R.id.textview_reviews);
        textView.setText(data);
    }
}
