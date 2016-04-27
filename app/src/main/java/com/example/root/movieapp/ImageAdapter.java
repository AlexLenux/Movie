package com.example.root.movieapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by root on 3/25/16.
 */
public class ImageAdapter extends BaseAdapter {

    public ArrayList<Movies> moviesArrayList;
    Context context;

    public ImageAdapter(Context context){

        this.context = context;
    }

    public void setMoviesArrayList(ArrayList<Movies> moviesArrayList) {
        this.moviesArrayList = moviesArrayList;
    }

    public void appandArrayList(ArrayList<Movies> moviesArrayList1){
        moviesArrayList.addAll(moviesArrayList1);
    }

    @Override
    public int getCount() {
        return moviesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.image_item,parent,false);
                    }
        if(moviesArrayList.get(position).getPoster() == null) {
             Picasso.with(context).load(moviesArrayList.get(position).getUrl()).into((ImageView) convertView);
             Log.d("PICASSO", moviesArrayList.get(position).getUrl());
         }
        else {
            ((ImageView) convertView).setImageBitmap(getImage(moviesArrayList.get(position).getPoster()));
        }

        return convertView;

    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
