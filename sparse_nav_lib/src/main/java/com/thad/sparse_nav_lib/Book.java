package com.thad.sparse_nav_lib;

import android.util.Log;

import com.thad.sparse_nav_lib.Utils.Vec;

/**
 * Created by theo on 4/9/18.
 */

public class Book {
    private static final String TAG = "|Book|";

    private String author, locationTag, title;
    private int row, col;
//    private Vec loc;

    public Book(Book book){
        this.author = book.author;
        this.locationTag = book.locationTag;
        this.title = book.title;
        this.row = book.row;
        this.col = book.col;

    }

    public Book() {

    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }

    public String getLocationTag() {
        return locationTag;
    }

    public void setCell(int row, int col){
        //Log.d(TAG, "Row/Col : "+row+"/"+col);
        this.row = row;
        this.col = col;
    }

    public int[] getCell(){return new int[]{row,col};}



    //ADD Mutator funcs
}
