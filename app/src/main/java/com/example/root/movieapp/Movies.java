package com.example.root.movieapp;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by root on 3/25/16.
 */
public class Movies implements Serializable{

    private String url;
    private String title;
    private String overView;
    private String vote_average;
    private String backdrop_url;
    private String id;
    private String[] trailer;
    private String[] reviews;
    private byte[] poster;
    private byte[] backDrop;
    private String[] author;
    private String date;

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setBackdrop_url(String backdrop) {
        this.backdrop_url = backdrop;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTrailer(String[] trailer) {
        this.trailer = trailer;
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public void setBackDrop(byte[] backDrop) {
        this.backDrop = backDrop;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverView() {
        return overView;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public String getId() {
        return id;
    }

    public String[] getTrailer() {
        return trailer;
    }

    public String[] getReviews() {
        return reviews;
    }

    public byte[] getPoster() {
        return poster;
    }

    public byte[] getBackDrop() {
        return backDrop;
    }

    public String[] getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }
}
